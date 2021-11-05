package org.cokebook.graphql.scalars.register;

import graphql.schema.idl.RuntimeWiring;
import org.cokebook.graphql.ScalarRegister;
import org.cokebook.graphql.scalars.Scalars;

/**
 * GraphQL extension scalar type
 *
 * @author wuming
 * @date 2020/3/14 13:15
 */
public class DefaultScalarRegister implements ScalarRegister {

    @Override
    public void register(RuntimeWiring.Builder builder) {
        builder.scalar(Scalars.DATE);
    }
}
