package org.cokebook.graphql.converter;

import org.cokebook.graphql.TypeConverter;

/**
 * @date 2019/12/18 17:29
 */
public class String2BooleanConverter implements TypeConverter<String, Boolean> {
    @Override
    public Boolean convert(String value) {
        return Boolean.parseBoolean(value);
    }
}
