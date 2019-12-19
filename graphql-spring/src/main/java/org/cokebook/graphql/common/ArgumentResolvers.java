package org.cokebook.graphql.common;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 参数值解析器
 *
 * @date 2019/12/18 15:29
 */
public class ArgumentResolvers {

    private static final TypeConverterManager manager = new TypeConverterManager();

    static {
        ServiceLoader<TypeConverterProvider> loader = ServiceLoader.load(TypeConverterProvider.class);
        for (TypeConverterProvider provider : loader) {
            provider.postprocess(manager);
        }
    }

    public static Object parse(Map<String, Object> context, MethodParameter parameter) {
        if (parameter.hasAnnotation(JSON.class)) {
            return new JSONObject(context).toJavaObject(parameter.getType());
        }
        Object pValue = context.get(parameter.getName());
        if (pValue == null || parameter.getType().equals(pValue.getClass()) || parameter.getType().isAssignableFrom(pValue.getClass())) {
            return pValue;
        }
        final TypeConverter<Object, Object> typeConverter = manager.get(pValue.getClass(), parameter.getType());
        if (typeConverter != null) {
            return typeConverter.convert(pValue);
        }
        throw new UnsupportedTypeConvertException(pValue.getClass(), parameter.getType(), pValue);
    }


    /**
     * 类型转换不支持异常
     */
    public static class UnsupportedTypeConvertException extends RuntimeException {

        private Class<?> source;
        private Class<?> target;
        private Object value;

        public UnsupportedTypeConvertException(Class<?> source, Class<?> target, Object value) {
            super("Unsupported type convert form '" + source + "' to '" + target + "'. value = " + value);
            this.source = source;
            this.target = target;
            this.value = value;
        }
    }

    public static class TypePair<S, T> {

        public static final <S, T> TypePair create(Class<S> source, Class<T> target) {
            if (source == null || target == null) {
                throw new IllegalArgumentException("the param 'source' & 'target' can't be null!");
            }
            return new TypePair(source, target);
        }

        private final Class<S> source;
        private final Class<T> target;

        private TypePair(Class<S> source, Class<T> target) {

            this.source = source;
            this.target = target;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            TypePair typePair = (TypePair) o;

            if (!Objects.equals(source, typePair.source)) {
                return false;
            }
            return Objects.equals(target, typePair.target);
        }

        @Override
        public int hashCode() {
            int result = source != null ? source.hashCode() : 0;
            result = 31 * result + (target != null ? target.hashCode() : 0);
            return result;
        }
    }

    private static class TypeConverterManager implements TypeConverterRegistry {

        public final ConcurrentMap<TypePair, TypeConverter> converters = new ConcurrentHashMap<>(32);

        @Override
        public <S, T> void register(Class<S> source, Class<T> target, TypeConverter<S, T> typeConverter) {
            converters.put(TypePair.create(source, target), typeConverter);
        }

        public <S, T> TypeConverter<S, T> get(Class<S> source, Class<T> target) {
            return (TypeConverter<S, T>) converters.get(TypePair.create(source, target));
        }

    }


}
