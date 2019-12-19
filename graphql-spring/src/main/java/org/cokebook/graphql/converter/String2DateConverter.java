package org.cokebook.graphql.converter;

import org.cokebook.graphql.TypeConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @date 2019/12/18 18:01
 */
public class String2DateConverter implements TypeConverter<String, Date> {

    private static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    @Override
    public Date convert(String value) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
            return sdf.parse(value);
        } catch (ParseException e) {
            throw new IllegalStateException("Parse string value '" + value + "' to Date with pattern = '" + YYYY_MM_DD_HH_MM_SS + "' failed!");
        }
    }
}
