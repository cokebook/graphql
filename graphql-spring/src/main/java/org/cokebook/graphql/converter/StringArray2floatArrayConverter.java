package org.cokebook.graphql.converter;

import org.cokebook.graphql.common.TypeConverter;

/**
 * @date 2019/12/18 19:24
 */
public class StringArray2floatArrayConverter implements TypeConverter<String[], float[]> {

    @Override
    public float[] convert(String[] value) {
        float[] floats = new float[value.length];
        for (int i = 0; i < value.length; i++) {
            floats[i] = Float.parseFloat(value[i]);
        }
        return floats;
    }
}
