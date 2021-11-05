package org.cokebook.graphql;

import graphql.schema.idl.RuntimeWiring;

/**
 * Scalar 注册器
 *
 * @date 2020/3/14 09:54
 */
public interface ScalarRegister {

    /**
     * 向 RuntimeWiring.Builder 注册 scalar type
     *
     * @param builder
     */
    void register(RuntimeWiring.Builder builder);

}
