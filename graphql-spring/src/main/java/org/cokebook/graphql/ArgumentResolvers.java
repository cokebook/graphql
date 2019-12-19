package org.cokebook.graphql;

import com.alibaba.fastjson.JSONObject;
import org.cokebook.graphql.common.MethodParameter;
import org.cokebook.graphql.convert.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 参数值解析器
 *
 * @date 2019/12/18 15:29
 */
public class ArgumentResolvers {

    static {
        converters = new ConcurrentHashMap<>(32);
        registerDefault();
    }


    public static final ConcurrentMap<TypePair, TypeConverter> converters;

    public static Object parse(Map<String, Object> context, MethodParameter parameter) {
        if (parameter.hasAnnotation(JSON.class)) {
            return new JSONObject(context).toJavaObject(parameter.getType());
        }
        Object pValue = context.get(parameter.getName());
        if (pValue == null || parameter.getType().equals(pValue.getClass()) || parameter.getType().isAssignableFrom(pValue.getClass())) {
            return pValue;
        }
        final TypeConverter<Object, Object> typeConverter = get(pValue.getClass(), parameter.getType());
        if (typeConverter != null) {
            return typeConverter.convert(pValue);
        }
        throw new UnsupportedTypeConvertException(pValue.getClass(), parameter.getType(), pValue);
    }

    public static <S, T> TypeConverter<S, T> get(Class<S> sClass, Class<T> tClass) {
        return converters.get(TypePair.create(sClass, tClass));
    }

    public static <S, T> void register(Class<S> source, Class<T> target, TypeConverter<S, T> typeConverter) {
        converters.put(TypePair.create(source, target), typeConverter);
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

            if (source != null ? !source.equals(typePair.source) : typePair.source != null) {
                return false;
            }
            return target != null ? target.equals(typePair.target) : typePair.target == null;
        }

        @Override
        public int hashCode() {
            int result = source != null ? source.hashCode() : 0;
            result = 31 * result + (target != null ? target.hashCode() : 0);
            return result;
        }
    }

    private static void registerDefault() {

        register(String.class, Boolean.class, new String2BooleanConverter());
        register(String.class, Byte.class, new String2ByteConverter());
        register(String.class, Short.class, new String2ShortConverter());
        register(String.class, Integer.class, new String2IntegerConverter());
        register(String.class, Long.class, new String2LongConverter());
        register(String.class, Float.class, new String2FloatConverter());
        register(String.class, Double.class, new String2DoubleConverter());
        register(String.class, Character.class, new String2CharConverter());

        register(String.class, boolean.class, new String2BooleanConverter());
        register(String.class, byte.class, new String2ByteConverter());
        register(String.class, short.class, new String2ShortConverter());
        register(String.class, int.class, new String2IntegerConverter());
        register(String.class, long.class, new String2LongConverter());
        register(String.class, float.class, new String2FloatConverter());
        register(String.class, double.class, new String2DoubleConverter());
        register(String.class, char.class, new String2CharConverter());

        register(String.class, BigDecimal.class, new String2BigDecimalConverter());

        register(Boolean.class, String.class, new Object2StringConverter());
        register(Byte.class, String.class, new Object2StringConverter());
        register(Short.class, String.class, new Object2StringConverter());
        register(Integer.class, String.class, new Object2StringConverter());
        register(Long.class, String.class, new Object2StringConverter());
        register(Float.class, String.class, new Object2StringConverter());
        register(Double.class, String.class, new Object2StringConverter());
        register(Character.class, String.class, new Object2StringConverter());

        register(boolean.class, String.class, new Object2StringConverter());
        register(byte.class, String.class, new Object2StringConverter());
        register(short.class, String.class, new Object2StringConverter());
        register(int.class, String.class, new Object2StringConverter());
        register(long.class, String.class, new Object2StringConverter());
        register(float.class, String.class, new Object2StringConverter());
        register(double.class, String.class, new Object2StringConverter());
        register(char.class, String.class, new Object2StringConverter<>());

        register(String[].class, boolean[].class, new StringArray2booleanArrayConverter());
        register(String[].class, byte[].class, new StringArray2byteArrayConverter());
        register(String[].class, int[].class, new StringArray2intArrayConverter());
        register(String[].class, long[].class, new StringArray2longArrayConverter());
        register(String[].class, short[].class, new StringArray2shortArrayConverter());
        register(String[].class, float[].class, new StringArray2floatArrayConverter());
        register(String[].class, double[].class, new StringArray2doubleArrayConverter());
        register(String[].class, char[].class, new StringArray2charArrayConverter());
    }


}
