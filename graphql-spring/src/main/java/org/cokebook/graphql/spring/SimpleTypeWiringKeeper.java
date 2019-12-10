package org.cokebook.graphql.spring;

import org.cokebook.graphql.TypeWiring;
import org.cokebook.graphql.TypeWiringKeeper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * TypeWiringKeeperAdapter
 *
 * @date 2019/11/29 14:21
 */
public class SimpleTypeWiringKeeper extends InstantiationAwareBeanPostProcessorAdapter implements TypeWiringKeeper {

    private final Map<Method, String> typeWiringMethods = new HashMap<>(10);
    private final Set<String> processedBeanNames = new HashSet<>();

    @Override
    public Class<?> predictBeanType(Class<?> beanClass, String beanName) throws BeansException {
        if (!processedBeanNames.contains(beanName)) {
            processedBeanNames.add(beanName);
            ReflectionUtils.doWithMethods(beanClass, method -> {
                if (method.getAnnotation(TypeWiring.class) != null) {
                    typeWiringMethods.put(method, beanName);
                }
            });
        }
        return null;
    }

    @Override
    public Map<Method, String> typeWiringMethods() {
        return typeWiringMethods;
    }
}
