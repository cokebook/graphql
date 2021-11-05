package org.cokebook.graphql.converter;

import org.cokebook.graphql.common.TypeConverter;

/**
 * @date 2019/12/18 18:18
 */
public class Boolean2StringConverter implements TypeConverter<Boolean, String> {
    @Override
    public String convert(Boolean value) {
        return value.toString();
    }
}
