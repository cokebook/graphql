package org.cokebook.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

/**
 * @date 2019/12/18 13:52
 */
public final class Clazz {

    private Clazz() {
        throw new UnsupportedOperationException(Clazz.class.getName() + " can't be instance");
    }

    /**
     * primitive type set
     */
    private static final Set<Class> PRIMITIVE_TYPES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            boolean.class, byte.class, short.class, int.class, long.class, float.class, double.class, char.class
    )));

    /**
     * primitive wrapper type set
     */
    private static final Set<Class> PRIMITIVE_WRAPPER_TYPE = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            Boolean.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Character.class
    )));

    /**
     * common java type set
     */
    private static final Set<Class> COMMON_JAVA_TYPE = (new Function<Object, Set<Class>>() {
        @Override
        public Set<Class> apply(Object o) {
            final Set<Class> target = new HashSet<>();
            target.addAll(PRIMITIVE_TYPES);
            target.addAll(PRIMITIVE_WRAPPER_TYPE);
            target.addAll(Arrays.asList(
                    String.class
            ));
            return Collections.unmodifiableSet(target);
        }
    }).apply(null);


    public static boolean isPrimitive(Class<?> type) {
        return PRIMITIVE_TYPES.contains(type);
    }

    public static boolean isPrimitiveWrapper(Class<?> type) {
        return PRIMITIVE_WRAPPER_TYPE.contains(type);
    }

    public static boolean isPrimitiveOrWrapper(Class<?> type) {
        return isPrimitive(type) || isPrimitiveWrapper(type);
    }

}
