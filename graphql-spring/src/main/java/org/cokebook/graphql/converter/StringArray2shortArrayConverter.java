package org.cokebook.graphql.converter;

import org.cokebook.graphql.TypeConverter;

/**
 * @date 2019/12/18 19:16
 */
public class StringArray2shortArrayConverter implements TypeConverter<String[], short[]> {

    @Override
    public short[] convert(String[] value) {
        short[] shorts = new short[value.length];
        for (int i = 0; i < value.length; i++) {
            shorts[i] = Short.parseShort(value[i]);
        }
        return shorts;
    }
}
