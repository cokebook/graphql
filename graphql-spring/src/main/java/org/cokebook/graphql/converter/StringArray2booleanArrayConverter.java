package org.cokebook.graphql.converter;

import org.cokebook.graphql.TypeConverter;

/**
 * @date 2019/12/18 19:10
 */
public class StringArray2booleanArrayConverter implements TypeConverter<String[], boolean[]> {

    @Override
    public boolean[] convert(String[] value) {
        boolean[] booleans = new boolean[value.length];
        for (int i = 0; i < value.length; i++) {
            booleans[i] = Boolean.parseBoolean(value[i]);
        }
        return booleans;
    }
}
