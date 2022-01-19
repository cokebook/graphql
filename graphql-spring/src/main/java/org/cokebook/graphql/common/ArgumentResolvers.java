package org.cokebook.graphql.common;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import org.springframework.boot.jackson.JsonComponent;

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
        /* 判断是否为嵌套属性, 如果是基于 xPath 风格获取属性, 关于是否替换 json 工具问题以后在考虑如何更好的扩展 */
        Object pValue = parameter.isNested() ? JSONPath.eval(context, "$." + parameter.getName())
                : context.get(parameter.getName());
        if (pValue == null || parameter.getType().equals(pValue.getClass())
                || parameter.getType().isAssignableFrom(pValue.getClass())) {
            return pValue;
        }
        final TypeConverter<Object, Object> typeConverter = manager.get(pValue.getClass(), parameter.getType());
        if (typeConverter != null) {
            return typeConverter.convert(pValue);
        }
        return pValue;
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
