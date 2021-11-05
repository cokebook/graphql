package org.cokebook.graphql.converter;

import org.cokebook.graphql.common.TypeConverter;

/**
 * @date 2019/12/18 17:39
 */
public class String2CharConverter implements TypeConverter<String, Character> {


    @Override
    public Character convert(String value) {
        if (value.length() == 1) {
            return value.charAt(0);
        }
        throw new IllegalStateException("String value '" + value + "' can't be convert to char");
    }
}
