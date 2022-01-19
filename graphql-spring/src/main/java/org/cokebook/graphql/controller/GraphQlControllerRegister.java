package org.cokebook.graphql.controller;

import com.google.common.collect.Maps;
import graphql.ExecutionResult;
import graphql.GraphQLError;
import lombok.Data;
import org.cokebook.graphql.GraphQLAdapter;
import org.cokebook.utils.WebApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


public class GraphQlControllerRegister {


    @RestController
    @RequestMapping({"graphql"})
    @ConditionalOnClass(RestController.class)
    public static class GraphQlController {

        private static final Logger log = LoggerFactory.getLogger(GraphQlControllerRegister.class);
        /**
         * graphql query param name
         */
        private static final String QUERY_PARAM_NAME = "q";

        @Autowired
        private GraphQLAdapter graphQl;

        @RequestMapping(value = "/query", method = {RequestMethod.GET, RequestMethod.POST})
        public WebApi.Response query(@RequestParam("q") String query, HttpServletRequest request) {
            return doQuery(new Query(query), getParams(request));
        }

        @RequestMapping(method = {RequestMethod.GET})
        public WebApi.Response query2(@RequestBody Query query, HttpServletRequest request) {
            return doQuery(query, getParams(request));
        }

        public WebApi.Response doQuery(Query query, Map<String, Object> params) {
            Map<String, Object> variables = Maps.filterKeys(params, key -> !QUERY_PARAM_NAME.equalsIgnoreCase(key));
            /* 合并 请求参数和 query 自带查询参数: 并设置 Query 自带查询参数优先级更高 */
            for (Map.Entry<String, Object> var : query.getVariables().entrySet()) {
                variables.put(var.getKey(), var.getValue());
            }
            ExecutionResult result = graphQl.execute(query.getQuery(), variables);
            if (!result.getErrors().isEmpty()) {
                log.info("graphQL query errors = {}", result.getErrors());
                final StringBuilder errors = new StringBuilder();
                for (GraphQLError error : result.getErrors()) {
                    errors.append(error.getMessage());
                    errors.append(",");
                }
                return WebApi.error(errors.substring(0, errors.length() - 1));
            }
            return WebApi.success(result.getData());
        }


        public static class Query {

            private String query;

            private Map<String, Object> variables = new HashMap<>(4);

            public Query() {
            }

            public Query(String query) {
                this.query = query;
            }

            public String getQuery() {
                return query;
            }

            public void setQuery(String query) {
                this.query = query;
            }

            public Map<String, Object> getVariables() {
                return variables;
            }

            public void setVariables(Map<String, Object> variables) {
                this.variables = variables;
            }
        }
    }

    public static Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, String[]> params = request.getParameterMap();
        return params.entrySet().stream().map(entry -> {
            if (entry.getValue() == null || entry.getValue().length == 0) {
                return null;
            } else if (entry.getValue().length == 1) {
                return new Tuple(entry.getKey(), entry.getValue()[0]);
            }
            return new Tuple(entry.getKey(), Arrays.asList(entry.getValue()));
        }).filter(Objects::nonNull).collect(Collectors.toMap(Tuple::getFirst, Tuple::getSecond));
    }

    @Data
    private static class Tuple {
        public Tuple(String first, Object second) {
            this.first = first;
            this.second = second;
        }

        private String first;
        private Object second;
    }
}
