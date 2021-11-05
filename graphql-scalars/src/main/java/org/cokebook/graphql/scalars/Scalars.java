package org.cokebook.graphql.scalars;

import graphql.schema.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Def custom scalar type
 *
 * @date 2020/3/13 15:58
 */
public class Scalars {

    private static final Map<Pattern, String> DATE_FORMAT_MAP = new HashMap<>();

    static {
        DATE_FORMAT_MAP.put(Pattern.compile("^\\d{4}-\\d{2}-\\d{2}}$"), "yyyy-MM-dd");
        DATE_FORMAT_MAP.put(Pattern.compile("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$"), "yyyy-MM-dd HH:mm:ss");
    }


    public static final GraphQLScalarType DATE = new GraphQLScalarType("Date", "a custom scalar for java.util.Date", new Coercing<Date, Long>() {
        @Override
        public Long serialize(Object dataFetcherResult) throws CoercingSerializeException {
            return ((Date) dataFetcherResult).getTime();
        }

        @Override
        public Date parseValue(Object input) throws CoercingParseValueException {
            if (input instanceof Number) {
                return new Date(((Number) input).longValue());
            }
            if (input instanceof String) {
                try {
                    for (Pattern pattern : DATE_FORMAT_MAP.keySet()) {
                        if (pattern.matcher((String) input).matches()) {
                            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_MAP.get(pattern));
                            return sdf.parse((String) input);
                        }
                    }
                } catch (ParseException e) {
                    throw new CoercingParseValueException("Change value to Date failed! value =" + input, e);
                }
            }
            throw new CoercingParseValueException("Change value to Date failed! value =" + input);
        }

        @Override
        public Date parseLiteral(Object input) throws CoercingParseLiteralException {
            return parseValue(input);
        }
    });


}
