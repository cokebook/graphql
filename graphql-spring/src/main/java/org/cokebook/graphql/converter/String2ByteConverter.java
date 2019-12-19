package org.cokebook.graphql.converter;

import org.cokebook.graphql.TypeConverter;

/**
 * @date 2019/12/18 17:31
 */
public class String2ByteConverter implements TypeConverter<String, Byte> {
    @Override
    public Byte convert(String value) {
        return Byte.parseByte(value);
    }
}
