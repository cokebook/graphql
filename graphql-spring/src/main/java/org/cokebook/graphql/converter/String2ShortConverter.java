package org.cokebook.graphql.converter;

import org.cokebook.graphql.TypeConverter;

/**
 * @date 2019/12/18 17:35
 */
public class String2ShortConverter implements TypeConverter<String, Short> {
    @Override
    public Short convert(String value) {
        return Short.parseShort(value);
    }
}
