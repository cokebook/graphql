package org.cokebook.graphql.spring;

import graphql.ExecutionResult;
import graphql.GraphQL;
import org.cokebook.graphql.GraphQLAdapter;
import org.cokebook.graphql.TypeWiringKeeper;
import org.cokebook.graphql.register.GraphQlControllerRegister;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import java.util.Map;

/**
 * GraphQl Configuration for spring boot
 *
 * @date 2019/11/28 16:56
 */
@Configuration
@ConditionalOnClass(GraphQL.class)
@Import(GraphQlControllerRegister.GraphQlController.class)
public class GraphQlAutoConfiguration {

    private static final String SCHEMA_FILE_PATH_VAR = "${spring.graphql.location:classpath:schema.graphql}";

    ApplicationContext applicationContext;

    public GraphQlAutoConfiguration(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    @Primary
    @ConfigurationProperties("spring.graphql")
    @ConditionalOnResource(resources = SCHEMA_FILE_PATH_VAR)
    public GraphQlFactoryBean graphQlFactoryBean(TypeWiringKeeper typeWiringKeeper) {
        return new GraphQlFactoryBean(typeWiringKeeper);
    }

    @Bean
    @ConditionalOnMissingBean(GraphQLAdapter.class)
    public GraphQLAdapter defaultGraphQLAdapter() {
        return new GraphQLAdapter() {
            @Override
            public ExecutionResult execute(String query) {
                String location = applicationContext.getEnvironment().resolvePlaceholders(SCHEMA_FILE_PATH_VAR);
                throw new GraphQLSchemaFileNotFoundedException(location);
            }

            @Override
            public ExecutionResult execute(String query, Map<String, Object> variables) {
                String location = applicationContext.getEnvironment().resolvePlaceholders(SCHEMA_FILE_PATH_VAR);
                throw new GraphQLSchemaFileNotFoundedException(location);
            }
        };
    }

    /**
     * GraphQLSchemaFileNotFoundedException
     */
    public static class GraphQLSchemaFileNotFoundedException extends RuntimeException {

        private String location;

        public GraphQLSchemaFileNotFoundedException(String location) {
            super("there is no graphql schema file found  @location = '" + location + "'");
            this.location = location;
        }
    }

}
