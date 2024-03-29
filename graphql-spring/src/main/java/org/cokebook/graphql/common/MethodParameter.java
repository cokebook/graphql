package org.cokebook.graphql.common;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * Method Parameter
 *
 * @date 2019/12/18 17:21
 */
public class MethodParameter {

    public static final String JSON_FIELD_DELIMITER = ".";


    private final Method method;
    private final Parameter parameter;
    private final String name;
    private final Class type;

    public MethodParameter(Method method, Parameter parameter, String name) {
        this.method = method;
        this.parameter = parameter;
        this.name = name;
        this.type = parameter.getType();
    }

    public <T extends Annotation> T getAnnotation(Class<T> annoClass) {
        return parameter.getAnnotation(annoClass);
    }

    public boolean hasAnnotation(Class<? extends Annotation> annoClass) {
        return getAnnotation(annoClass) != null;
    }

    public Method getMethod() {
        return method;
    }

    public String getName() {
        return name;
    }

    public Class getType() {
        return type;
    }

    public boolean isNested() {
        return getName().indexOf(JSON_FIELD_DELIMITER) != -1;
    }

}
