package org.cokebook.graphql.spring;

import org.cokebook.graphql.TypeWiring;
import org.cokebook.graphql.TypeWiringDataFetcher;
import org.cokebook.graphql.TypeWiringKeeper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.*;

/**
 * SimpleTypeWiringKeeper
 *
 * @date 2019/11/29 14:21
 */
public class SimpleTypeWiringKeeper extends InstantiationAwareBeanPostProcessorAdapter implements TypeWiringKeeper, ApplicationContextAware {

    private final Map<Method, String> typeWiringMethods = new HashMap<>(10);
    private final Set<String> processedBeanNames = new HashSet<>();

    private final Set<TypeWiringDataFetcher> dataFetchers = new HashSet<>();
    private ApplicationContext applicationContext;

    @Override
    public Class<?> predictBeanType(Class<?> beanClass, String beanName) throws BeansException {
        if (!processedBeanNames.contains(beanName)) {
            processedBeanNames.add(beanName);
            ReflectionUtils.doWithMethods(beanClass, method -> {
                if (method.getAnnotation(TypeWiring.class) != null) {
                    dataFetchers.add(new BeanMethodDataFetcher(applicationContext, beanName, method));
                }
            });
        }
        return null;
    }

    @Override
    public Set<TypeWiringDataFetcher> dataFetchers() {
        return Collections.unmodifiableSet(dataFetchers);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


}
