package org.cokebook.graphql.converter.provider;

import org.cokebook.graphql.common.TypeConverterProvider;
import org.cokebook.graphql.common.TypeConverterRegistry;
import org.cokebook.graphql.converter.*;

import java.math.BigDecimal;

/**
 * common type converter provider
 *
 * @date 2019/12/19 10:46
 */
public class CommonTypeConverterProvider implements TypeConverterProvider {

    @Override
    public void postprocess(TypeConverterRegistry registry) {

        registry.register(String.class, Boolean.class, new String2BooleanConverter());
        registry.register(String.class, Byte.class, new String2ByteConverter());
        registry.register(String.class, Short.class, new String2ShortConverter());
        registry.register(String.class, Integer.class, new String2IntegerConverter());
        registry.register(String.class, Long.class, new String2LongConverter());
        registry.register(String.class, Float.class, new String2FloatConverter());
        registry.register(String.class, Double.class, new String2DoubleConverter());
        registry.register(String.class, Character.class, new String2CharConverter());

        registry.register(String.class, boolean.class, new String2BooleanConverter());
        registry.register(String.class, byte.class, new String2ByteConverter());
        registry.register(String.class, short.class, new String2ShortConverter());
        registry.register(String.class, int.class, new String2IntegerConverter());
        registry.register(String.class, long.class, new String2LongConverter());
        registry.register(String.class, float.class, new String2FloatConverter());
        registry.register(String.class, double.class, new String2DoubleConverter());
        registry.register(String.class, char.class, new String2CharConverter());

        registry.register(String.class, BigDecimal.class, new String2BigDecimalConverter());

        registry.register(Boolean.class, String.class, new Object2StringConverter());
        registry.register(Byte.class, String.class, new Object2StringConverter());
        registry.register(Short.class, String.class, new Object2StringConverter());
        registry.register(Integer.class, String.class, new Object2StringConverter());
        registry.register(Long.class, String.class, new Object2StringConverter());
        registry.register(Float.class, String.class, new Object2StringConverter());
        registry.register(Double.class, String.class, new Object2StringConverter());
        registry.register(Character.class, String.class, new Object2StringConverter());

        registry.register(boolean.class, String.class, new Object2StringConverter());
        registry.register(byte.class, String.class, new Object2StringConverter());
        registry.register(short.class, String.class, new Object2StringConverter());
        registry.register(int.class, String.class, new Object2StringConverter());
        registry.register(long.class, String.class, new Object2StringConverter());
        registry.register(float.class, String.class, new Object2StringConverter());
        registry.register(double.class, String.class, new Object2StringConverter());
        registry.register(char.class, String.class, new Object2StringConverter<>());

        registry.register(String[].class, boolean[].class, new StringArray2booleanArrayConverter());
        registry.register(String[].class, byte[].class, new StringArray2byteArrayConverter());
        registry.register(String[].class, int[].class, new StringArray2intArrayConverter());
        registry.register(String[].class, long[].class, new StringArray2longArrayConverter());
        registry.register(String[].class, short[].class, new StringArray2shortArrayConverter());
        registry.register(String[].class, float[].class, new StringArray2floatArrayConverter());
        registry.register(String[].class, double[].class, new StringArray2doubleArrayConverter());
        registry.register(String[].class, char[].class, new StringArray2charArrayConverter());

    }
}
