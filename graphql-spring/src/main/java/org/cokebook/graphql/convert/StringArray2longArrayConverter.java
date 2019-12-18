package org.cokebook.graphql.convert;

import org.cokebook.graphql.TypeConverter;

/**
 * @date 2019/12/18 19:21
 */
public class StringArray2longArrayConverter implements TypeConverter<String[], long[]> {
    @Override
    public long[] convert(String[] value) {
        long[] longs = new long[value.length];
        for (int i = 0; i < value.length; i++) {
            longs[i] = Long.parseLong(value[i]);
        }
        return longs;
    }
}
