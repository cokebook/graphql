package org.cokebook.graphql.util;

import org.cokebook.graphql.Param;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Method Param  Helper : To get the method param's name
 *
 * @date 2019/11/29 16:14
 * @see Param
 */
public class MethodParamHelper {

    private static final Map<Method, List<String>> METHOD_PARAM_NAME_CACHE = new ConcurrentHashMap<>(32);

    /**
     * Get the method param's name
     *
     * @param method
     * @return
     */
    public static List<String> getParamNames(Method method) {

        if (METHOD_PARAM_NAME_CACHE.containsKey(method)) {
            return METHOD_PARAM_NAME_CACHE.get(method);
        }

        METHOD_PARAM_NAME_CACHE.computeIfAbsent(method, new Function<Method, List<String>>() {
            @Override
            public List<String> apply(Method method) {
                Parameter[] parameters = method.getParameters();
                return Arrays.stream(parameters).map(parameter -> {
                    Param param = parameter.getAnnotation(Param.class);
                    if (param != null && param.value() != null) {
                        return param.value();
                    }
                    return parameter.getName();
                }).collect(Collectors.toList());
            }
        });
        return METHOD_PARAM_NAME_CACHE.get(method);
    }

}
