package org.cokebook.graphql.spring;

import org.cokebook.graphql.TypeWiring;
import org.cokebook.graphql.TypeWiringDataFetcher;
import org.cokebook.graphql.TypeWiringKeeper;
import org.cokebook.graphql.TypeWirings;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ReflectionUtils;

import java.util.*;

/**
 * SimpleTypeWiringKeeper
 *
 * @date 2019/11/29 14:21
 */
public class SimpleTypeWiringKeeper extends InstantiationAwareBeanPostProcessorAdapter implements TypeWiringKeeper, ApplicationContextAware {

    private ApplicationContext applicationContext;
    private final Set<String> processedBeanNames = new HashSet<>();
    private final Map<TypeFieldPair<String, String>, TypeWiringDataFetcher> beanMethodDataFetchers = new HashMap<>(32);

    @Override
    public Class<?> predictBeanType(Class<?> beanClass, String beanName) throws BeansException {
        if (!processedBeanNames.contains(beanName)) {
            processedBeanNames.add(beanName);
            ReflectionUtils.doWithMethods(beanClass, method -> {
                TypeWiring typeWiring = TypeWirings.parse(method);
                if (typeWiring != null) {
                    BeanMethodDataFetcher dataFetcher = new BeanMethodDataFetcher(applicationContext, beanName, method, typeWiring);
                    TypeFieldPair<String, String> pair = TypeFieldPair.create(dataFetcher.getType(), dataFetcher.getField());
                    if (!pair.validate()) {
                        throw new IllegalStateException("@TypeWiring type and field can't be null. method = " + method);
                    }
                    if (beanMethodDataFetchers.containsKey(pair)) {
                        BeanMethodDataFetcher old = (BeanMethodDataFetcher) beanMethodDataFetchers.get(pair);
                        throw new IllegalStateException("Duplicated @TypeWiring found. type = '" + dataFetcher.getType() + "' field = '" + dataFetcher.getField() + "'"
                                + "! first-method = " + old.getMethod() + ", current-method = " + dataFetcher.getMethod());
                    }
                    beanMethodDataFetchers.put(pair, dataFetcher);
                }
            });
        }
        return null;
    }

    /**
     * 针对用户自定义 {@link TypeWiringDataFetcher} bean 具有更高的匹配优先级.  即假如
     * 存在一个 <code>TypeWiringDateFetcher</code> bean(type = Query, field = findXyz)
     * 并且同时存在一个 <code> @TypeWiring(type = "Query", field = "findXyz") </code>.
     * 在筛选 DataFetcher 时将选择前者.
     *
     * @return
     */
    @Override
    public Set<TypeWiringDataFetcher> dataFetchers() {
        final Map<TypeFieldPair<String, String>, TypeWiringDataFetcher> target = new HashMap(beanMethodDataFetchers);
        target.putAll(getDataFetcherBeansMap());
        return Collections.unmodifiableSet(new HashSet<>(target.values()));
    }

    private Map<TypeFieldPair<String, String>, TypeWiringDataFetcher> getDataFetcherBeansMap() {
        final Map<TypeFieldPair<String, String>, TypeWiringDataFetcher> candidates = new HashMap<>(32);
        Collection<TypeWiringDataFetcher> beansOfTypeWiringDataFetcher = applicationContext.getBeansOfType(TypeWiringDataFetcher.class).values();
        for (TypeWiringDataFetcher dataFetcher : beansOfTypeWiringDataFetcher) {
            TypeFieldPair<String, String> pair = TypeFieldPair.create(dataFetcher.getType(), dataFetcher.getField());
            if (!pair.validate()) {
                throw new IllegalStateException("TypeWiringDataFetcher bean error , No type or field defined for class = " + dataFetcher.getClass());
            }
            if (candidates.containsKey(pair)) {
                TypeWiringDataFetcher existsBean = candidates.get(pair);
                throw new IllegalStateException("Duplicated TypeWiringDataFetcher bean found. type = '" + dataFetcher.getType() + "' field = '" + dataFetcher.getField() + "'"
                        + "! first-class = " + existsBean.getClass() + ", current-class = " + dataFetcher.getClass());
            }
            candidates.put(pair, dataFetcher);
        }
        return candidates;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private static class TypeFieldPair<T, F> {

        public static <T, F> TypeFieldPair<T, F> create(T first, F second) {
            return new TypeFieldPair<>(first, second);
        }

        private T type;
        private F field;

        public TypeFieldPair(T type, F field) {
            this.type = type;
            this.field = field;
        }

        public boolean validate() {
            return type != null && field != null;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            TypeFieldPair<?, ?> tuple = (TypeFieldPair<?, ?>) o;

            if (!Objects.equals(type, tuple.type)) {
                return false;
            }
            return Objects.equals(field, tuple.field);
        }

        @Override
        public int hashCode() {
            int result = type != null ? type.hashCode() : 0;
            result = 31 * result + (field != null ? field.hashCode() : 0);
            return result;
        }
    }

}
