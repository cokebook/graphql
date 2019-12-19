package org.cokebook.graphql.converter;

import org.cokebook.graphql.TypeConverter;

/**
 * @date 2019/12/18 19:26
 */
public class StringArray2doubleArrayConverter implements TypeConverter<String[], double[]> {

    @Override
    public double[] convert(String[] value) {
        double[] doubles = new double[value.length];
        for (int i = 0; i < value.length; i++) {
            doubles[i] = Double.parseDouble(value[i]);
        }
        return doubles;
    }
}
