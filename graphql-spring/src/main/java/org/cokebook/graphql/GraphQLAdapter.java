package org.cokebook.graphql;

import graphql.ExecutionResult;

import java.util.Map;

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

    /**
     * 执行 graphQL 查询
     *
     * @param query
     * @return
     */
    ExecutionResult execute(String query, Map<String, Object> variables);
}
