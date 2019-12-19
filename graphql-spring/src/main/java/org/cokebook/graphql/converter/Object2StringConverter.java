package org.cokebook.graphql.converter;

import org.cokebook.graphql.TypeConverter;

/**
 * @date 2019/12/18 18:19
 */
public class Object2StringConverter<T extends Object> implements TypeConverter<T, String> {

    @Override
    public String convert(Object value) {
        return value.toString();
    }
}
