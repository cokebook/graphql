package org.cokebook.graphql.spring;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.*;
import org.cokebook.graphql.GraphQLAdapter;
import org.cokebook.graphql.ScalarRegister;
import org.cokebook.graphql.TypeWiringKeeper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

public class GraphQlFactoryBean implements FactoryBean<GraphQLAdapter>, ApplicationContextAware {

    public static final String SCHEMA_LOCATION = "classpath:schema.graphql";

    /**
     * schema file location
     */
    private String location = SCHEMA_LOCATION;
    private TypeWiringKeeper typeWiringKeeper;
    private ApplicationContext applicationContext;

    public GraphQlFactoryBean(TypeWiringKeeper typeWiringKeeper) {
        this(null, typeWiringKeeper);
    }

    public GraphQlFactoryBean(String location, TypeWiringKeeper typeWiringKeeper) {
        this.location = location;
        this.typeWiringKeeper = typeWiringKeeper;
    }

    @Override
    public GraphQLAdapter getObject() throws Exception {
        GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(getTypeDefinitionRegistry(), getRuntimeWiring());
        return new SimpleGraphQLAdapter(GraphQL.newGraphQL(schema).build());
    }

    @Override
    public Class<?> getObjectType() {
        return GraphQLAdapter.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private TypeDefinitionRegistry getTypeDefinitionRegistry() throws IOException {
        final String targetLocation = this.location == null ? SCHEMA_LOCATION : this.location;
        PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        Resource resource = resourcePatternResolver.getResource(targetLocation);
        final String sdl = resourceToString(resource.getURL());
        return new SchemaParser().parse(sdl);
    }

    private RuntimeWiring getRuntimeWiring() {
        List<TypeRuntimeWiring.Builder> builders = typeWiringKeeper.dataFetchers().stream()
                .map(fetcher -> TypeRuntimeWiring.newTypeWiring(fetcher.getType())
                        .dataFetcher(fetcher.getField(), fetcher)
                ).collect(Collectors.toList());
        final RuntimeWiring.Builder runtimeWiringBuilder = RuntimeWiring.newRuntimeWiring();
        for (TypeRuntimeWiring.Builder builder : builders) {
            runtimeWiringBuilder.type(builder);
        }
        for (ScalarRegister register : findAllRegister()) {
            register.register(runtimeWiringBuilder);
        }

        return runtimeWiringBuilder.build();

    }

    private List<ScalarRegister> findAllRegister() {
        ServiceLoader<ScalarRegister> serviceLoader = ServiceLoader.load(ScalarRegister.class);
        final List<ScalarRegister> registers = new ArrayList<>();
        for (ScalarRegister register : serviceLoader) {
            registers.add(register);
        }
        return registers;
    }

    private static String resourceToString(URL resource) throws IOException {
        return Resources.toString(resource, Charsets.UTF_8);
    }

    public static class SimpleGraphQLAdapter implements GraphQLAdapter {

        private GraphQL graphQL;

        public SimpleGraphQLAdapter(GraphQL graphQL) {
            this.graphQL = graphQL;
        }

        @Override
        public ExecutionResult execute(String query) {
            return graphQL.execute(query);
        }

        @Override
        public ExecutionResult execute(String query, Map<String, Object> variables) {
            return graphQL.execute(ExecutionInput.newExecutionInput()
                    .variables(variables)
                    .query(query)
                    .build());
        }
    }


}
