package org.cokebook.graphql;

import java.lang.annotation.*;
import java.lang.reflect.Method;

/**
 * Tool for {@link TypeWiring}
 *
 * @author wuming
 * @date 2022/1/19
 */
public class TypeWirings {

    public static TypeWiring parse(Method method) {
        TypeWiring typeWiring = method.getAnnotation(TypeWiring.class);
        if (typeWiring != null) {
            return typeWiring;
        }
        Query query = method.getAnnotation(Query.class);
        Mutation mutation = method.getAnnotation(Mutation.class);
        if (query != null && mutation != null) {
            throw new IllegalStateException("@Query and @Mutation both exists for method = " + method.getDeclaringClass().getName() + "#" + method.getName());
        }
        if (query != null) {
            return create(TypeWiring.INNER_TYPE_QUERY, query.value());
        } else if (mutation != null) {
            return create(TypeWiring.INNER_TYPE_MUTATION, mutation.value());
        }
        return null;
    }

    private static TypeWiring create(String type, String field) {
        return new TypeWiring() {
            @Override
            public String type() {
                return type;
            }

            @Override
            public String field() {
                return field;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return TypeWiring.class;
            }
        };
    }

}
