package org.cokebook.graphql.converter;

import org.cokebook.graphql.TypeConverter;

/**
 * @date 2019/12/18 19:27
 */
public class StringArray2charArrayConverter implements TypeConverter<String[], char[]> {
    @Override
    public char[] convert(String[] value) {
        TypeConverter<String, Character> converter = new String2CharConverter();
        char[] chars = new char[value.length];
        for (int i = 0; i < value.length; i++) {
            chars[i] = converter.convert(value[i]);
        }
        return chars;
    }
}
