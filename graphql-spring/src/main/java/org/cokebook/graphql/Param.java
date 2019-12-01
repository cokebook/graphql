package org.cokebook.graphql;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Param Annotation : A parameter annotation to define the parameter name.
 *
 * @date 2019/11/29 15:36
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Param {
    String value();
}
