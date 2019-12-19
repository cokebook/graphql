package org.cokebook.graphql.converter;

import org.cokebook.graphql.TypeConverter;

/**
 * @date 2019/12/18 17:38
 */
public class String2DoubleConverter implements TypeConverter<String, Double> {

    @Override
    public Double convert(String value) {
        return Double.parseDouble(value);
    }
}
