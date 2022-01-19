package org.cokebook.graphql;

import java.lang.annotation.*;

/**
 * Graphql 内置特殊类型  Mutation 注解, 该注解用于在变更场景下简化 {@link TypeWiring} 的编写
 * <p>
 * Note: 如果 {@link TypeWiring} 和 {@link Mutation} 同时存在优先使用 {@link TypeWiring}
 *
 * @author wuming
 * @date 2022/1/19
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Mutation {
    String value();
}
