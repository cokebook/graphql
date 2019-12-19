package org.cokebook.graphql.spring;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.*;
import org.cokebook.graphql.*;
import org.cokebook.graphql.common.ArgumentResolvers;
import org.cokebook.graphql.common.MethodParameter;
import org.cokebook.graphql.common.MethodParameterHelper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

public class GraphQlFactoryBean implements FactoryBean<GraphQL>, ApplicationContextAware {

    public static final String SCHEMA_LOCATION = "graphql.schema";

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
    public GraphQL getObject() throws Exception {
        GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(getTypeDefinitionRegistry(), getRuntimeWiring());
        return GraphQL.newGraphQL(schema).build();
    }

    @Override
    public Class<?> getObjectType() {
        return GraphQL.class;
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
        final String sdl = resourceToString(Resources.getResource(targetLocation));
        return new SchemaParser().parse(sdl);
    }

    private RuntimeWiring getRuntimeWiring() {
        List<TypeRuntimeWiring.Builder> builders = typeWiringKeeper.typeWiringMethods().entrySet().stream()
                .map(entry -> {
                    Method method = entry.getKey();
                    String beanName = entry.getValue();
                    TypeWiring typeWiring = method.getAnnotation(TypeWiring.class);
                    return TypeRuntimeWiring.newTypeWiring(typeWiring.type())
                            .dataFetcher(typeWiring.field(), new DataFetcher() {
                                @Override
                                public Object get(DataFetchingEnvironment environment) throws Exception {
                                    Object bean = applicationContext.getBean(beanName);
                                    if (TypeWiring.DEFAULT_TYPE.equals(typeWiring.type())) {
                                        final List<MethodParameter> parameters = MethodParameterHelper.getParams(method);
                                        final List<Object> pValues = parameters.stream().map(parameter -> {
                                            return ArgumentResolvers.parse(environment.getArguments(), parameter);
                                        }).collect(Collectors.toList());
                                        return method.invoke(bean, pValues.toArray(new Object[pValues.size()]));
                                    }
                                    return method.invoke(bean, (Object) environment.getSource());
                                }
                            });
                }).collect(Collectors.toList());

        final RuntimeWiring.Builder runtimeWiringBuilder = RuntimeWiring.newRuntimeWiring();
        for (TypeRuntimeWiring.Builder builder : builders) {
            runtimeWiringBuilder.type(builder);
        }

        return runtimeWiringBuilder.build();

    }

    private static String resourceToString(URL resource) throws IOException {
        return Resources.toString(resource, Charsets.UTF_8);
    }


}
