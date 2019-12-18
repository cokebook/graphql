package org.cokebook.graphql.convert;

import org.cokebook.graphql.TypeConverter;

import java.math.BigDecimal;

/**
 * @date 2019/12/18 18:40
 */
public class String2BigDecimalConverter implements TypeConverter<String, BigDecimal> {

    @Override
    public BigDecimal convert(String value) {
        return new BigDecimal(value);
    }
}
