package org.cokebook.graphql.convert;

import org.cokebook.graphql.TypeConverter;

/**
 * @date 2019/12/18 18:19
 */
public class SimpleObject2StringConverter<T extends Object> implements TypeConverter<T, String> {

    @Override
    public String convert(Object value) {
        return value.toString();
    }
}
