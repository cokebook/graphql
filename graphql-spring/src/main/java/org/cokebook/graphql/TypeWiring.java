package org.cokebook.graphql;

import java.lang.annotation.*;

/**
 * TypeWiring Annotation
 *
 * @date 2019/11/29 14:11
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TypeWiring {

    String DEFAULT_TYPE = "Query";

    String type() default DEFAULT_TYPE;

    String field();
}
