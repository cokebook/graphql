package org.cokebook.graphql.spring;

import graphql.GraphQL;
import org.cokebook.graphql.TypeWiringKeeper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * GraphQl Configuration for spring boot
 *
 * @date 2019/11/28 16:56
 */
@Configuration
@ConditionalOnClass(GraphQL.class)
public class GraphQlAutoConfiguration {

    ApplicationContext applicationContext;

    public GraphQlAutoConfiguration(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    @Lazy
    @ConfigurationProperties("spring.graphql")
    public GraphQlFactoryBean graphQlFactoryBean(TypeWiringKeeper typeWiringKeeper) {
        return new GraphQlFactoryBean(typeWiringKeeper);
    }

}
