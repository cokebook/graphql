package org.cokebook.graphql.converter;

import org.cokebook.graphql.TypeConverter;

/**
 * @date 2019/12/18 17:29
 */
public class String2IntegerConverter implements TypeConverter<String, Integer> {
    @Override
    public Integer convert(String value) {
        return Integer.parseInt(value);
    }
}
