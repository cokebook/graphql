package org.cokebook.graphql.spring;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.cokebook.graphql.TypeWiring;
import org.cokebook.graphql.TypeWiringDataFetcher;
import org.cokebook.graphql.common.ArgumentResolvers;
import org.cokebook.graphql.common.MethodParameter;
import org.cokebook.graphql.common.MethodParameterHelper;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Bean Method Data Fetcher
 */
@Slf4j
public class BeanMethodDataFetcher implements TypeWiringDataFetcher {

    private ApplicationContext applicationContext;
    private String beanName;
    private Method method;
    private TypeWiring typeWiring;

    public BeanMethodDataFetcher(ApplicationContext applicationContext, String beanName, Method method) {
        this.applicationContext = applicationContext;
        this.beanName = beanName;
        this.method = method;
        this.typeWiring = method.getAnnotation(TypeWiring.class);
    }

    @Override
    public String getType() {
        return typeWiring.type();
    }

    @Override
    public String getField() {
        return typeWiring.field();
    }

    @Override
    public Object get(DataFetchingEnvironment environment) throws Exception {
        Object bean = applicationContext.getBean(beanName);
        if (method.getReturnType().isAssignableFrom(CompletableFuture.class)) {
            return doGet(environment, bean);
        } else {
            return CompletableFuture.supplyAsync(() -> {
                try {
                    return doGet(environment, bean);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    log.error("Run TypeWiring exception: type = {} , field = {}", typeWiring.type(), typeWiring.field(), e);
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private Object doGet(DataFetchingEnvironment environment, Object bean) throws IllegalAccessException, InvocationTargetException {
        if (TypeWiring.DEFAULT_TYPE.equals(typeWiring.type())) {
            final List<MethodParameter> parameters = MethodParameterHelper.getParams(method);
            final List<Object> pValues = parameters.stream().map(parameter -> {
                return ArgumentResolvers.parse(environment.getArguments(), parameter);
            }).collect(Collectors.toList());
            return method.invoke(bean, pValues.toArray(new Object[pValues.size()]));
        }
        return method.invoke(bean, (Object) environment.getSource());
    }

    public Method getMethod() {
        return method;
    }
}
