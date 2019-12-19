package org.cokebook.graphql;

import graphql.ExecutionResult;

/**
 * @date 2019/12/19 15:39
 */
public interface GraphQLAdapter {
    /**
     * 执行 graphQL 查询
     *
     * @param query
     * @return
     */
    ExecutionResult execute(String query);
}
