package org.cokebook.graphql.spring;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.*;
import org.cokebook.graphql.TypeWiringKeeper;
import org.cokebook.graphql.util.MethodParamHelper;
import org.cokebook.graphql.TypeWiring;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

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
    public GraphQlBeanPostProcess graphQlBeanPostProcess() {
        return new GraphQlBeanPostProcess();
    }


    @Bean
    @Lazy
    @ConfigurationProperties("spring.graphql")
    public GraphQlFactoryBean graphQlFactoryBean(TypeWiringKeeper typeWiringKeeper) {
        return new GraphQlFactoryBean(typeWiringKeeper);
    }

}
