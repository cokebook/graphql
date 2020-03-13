package org.cokebook.graphql.controller;

import com.google.common.collect.Maps;
import graphql.ExecutionResult;
import graphql.GraphQLError;
import org.cokebook.graphql.GraphQLAdapter;
import org.cokebook.utils.WebApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


public class GraphQlControllerRegister {


    @RestController
    @RequestMapping("graphql")
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
        public WebApi.Response query(@RequestParam("q") String query, @RequestParam Map<String, Object> params) {
            Map<String, Object> variables = Maps.filterKeys(params, key -> !QUERY_PARAM_NAME.equalsIgnoreCase(key));
            ExecutionResult result = graphQl.execute(query, variables);
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

    }
}
