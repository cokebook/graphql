package org.cokebook.graphql;

import java.lang.annotation.*;

/**
 * TypeWiring Annotation
 *
 * @date 2019/11/29 14:11
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
public @interface TypeWiring {

    /**
     * 内置 Query 类型
     */
    String INNER_TYPE_QUERY = "Query";

    /**
     * 内置 Mutation 类型
     */
    String INNER_TYPE_MUTATION = "Mutation";

    /**
     * 默认值 {@link #INNER_TYPE_QUERY}
     *
     * @return
     */
    String type() default INNER_TYPE_QUERY;

    String field();
}
