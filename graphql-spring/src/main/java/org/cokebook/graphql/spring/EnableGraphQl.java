package org.cokebook.graphql.spring;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * EnableGraphQl : a spring boot annotation to quickly import graphQl-spring extension.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(GraphQlAutoConfiguration.class)
public @interface EnableGraphQl {
}
