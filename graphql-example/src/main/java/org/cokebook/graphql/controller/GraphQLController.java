package org.cokebook.graphql.controller;

import graphql.ExecutionResult;
import graphql.GraphQLError;
import org.cokebook.graphql.GraphQLAdapter;
import org.cokebook.web.utils.WebApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("graphql")
public class GraphQLController {

    private static final Logger log = LoggerFactory.getLogger(GraphQLController.class);

    @Autowired
    private GraphQLAdapter graphQL;


    @GetMapping("/index")
    public WebApi.Response<String> index() {
        return WebApi.success("Welcome to  graphql example for graphql-spring!");
    }

    @PostMapping("/query")
    public WebApi.Response query(@RequestBody String query) {
        ExecutionResult result = graphQL.execute(query);
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
