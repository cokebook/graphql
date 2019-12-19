package org.cokebook.graphql.common;

/**
 * 数值转换器
 *
 * @date 2019/12/18 15:40
 */
@FunctionalInterface
public interface TypeConverter<S, T> {
    /**
     * 值转换
     *
     * @param value
     * @return <code>T 类型的值</code>
     */
    T convert(S value);
}
