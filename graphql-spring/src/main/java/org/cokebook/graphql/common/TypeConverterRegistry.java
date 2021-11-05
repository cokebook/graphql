package org.cokebook.graphql.common;

/**
 * Type Converter Register
 *
 * @date 2019/12/19 10:16
 */
public interface TypeConverterRegistry {

    /**
     * 注册类型转换器
     *
     * @param source
     * @param target
     * @param typeConverter
     * @param <S>
     * @param <T>
     */
    <S, T> void register(Class<S> source, Class<T> target, TypeConverter<S, T> typeConverter);

}
