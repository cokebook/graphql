package org.cokebook.graphql.converter;

import org.cokebook.graphql.common.TypeConverter;

/**
 * @date 2019/12/18 19:19
 */
public class StringArray2intArrayConverter implements TypeConverter<String[], int[]> {
    @Override
    public int[] convert(String[] value) {
        int[] ints = new int[value.length];
        for (int i = 0; i < value.length; i++) {
            ints[i] = Integer.parseInt(value[i]);
        }
        return ints;
    }
}
