package org.cokebook.graphql.common;

import java.lang.annotation.*;

/**
 * Param Annotation : A parameter annotation to define the parameter name.
 *
 * @date 2019/11/29 15:36
 */
@Inherited
@Target({ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Param {
    /**
     * 参数名称
     *
     * @return the param name
     */
    String value();
}
