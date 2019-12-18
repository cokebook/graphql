package org.cokebook.graphql.convert;

import org.cokebook.graphql.TypeConverter;

/**
 * @date 2019/12/18 19:14
 */
public class StringArray2byteArrayConverter implements TypeConverter<String[], byte[]> {
    @Override
    public byte[] convert(String[] value) {
        byte[] bytes = new byte[value.length];
        for (int i = 0; i < value.length; i++) {
            bytes[i] = Byte.parseByte(value[i]);
        }
        return bytes;
    }
}
