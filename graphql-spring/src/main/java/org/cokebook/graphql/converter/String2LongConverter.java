package org.cokebook.graphql.converter;

import org.cokebook.graphql.common.TypeConverter;

/**
 * @date 2019/12/18 17:36
 */
public class String2LongConverter implements TypeConverter<String, Long> {

    @Override
    public Long convert(String value) {
        return Long.parseLong(value);
    }
}
