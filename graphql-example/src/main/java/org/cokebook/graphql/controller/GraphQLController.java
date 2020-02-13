package org.cokebook.graphql.controller;

import com.google.common.collect.Maps;
import graphql.ExecutionResult;
import graphql.GraphQLError;
import org.cokebook.graphql.GraphQLAdapter;
import org.cokebook.web.utils.WebApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("graphql")
public class GraphQLController {

    private static final Logger log = LoggerFactory.getLogger(GraphQLController.class);
    /**
     * graphql query param name
     */
    private static final String QUERY_PARAM_NAME = "q";

    @Autowired
    private GraphQLAdapter graphQL;


    @GetMapping("/index")
    public WebApi.Response<String> index() {
        return WebApi.success("Welcome to  graphql example for graphql-spring!");
    }

    @GetMapping("/query")
    public WebApi.Response query(@RequestParam("q") String query, @RequestParam Map<String, Object> params) {
        Map<String, Object> variables = Maps.filterKeys(params, key -> !QUERY_PARAM_NAME.equalsIgnoreCase(key));
        ExecutionResult result = graphQL.execute(query, variables);
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
