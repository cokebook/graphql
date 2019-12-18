package org.cokebook.graphql.controller;

import graphql.ExecutionResult;
import graphql.GraphQL;
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
    private GraphQL graphQL;


    @GetMapping("/index")
    public WebApi.Response<String> index() {
        return WebApi.success("Welcome to  graphql example for graphql-spring!");
    }

    @PostMapping("/query")
    public WebApi.Response query(@RequestBody String query) {
        ExecutionResult result = graphQL.execute(query);
        if (result.getErrors().isEmpty()) {
            return WebApi.success(result.getData());
        }
        log.error("there is some error on request! query = {}, result = {}", query, result);
        return WebApi.error("query failed, please check you query or contact to admin!");
    }

    @GetMapping("test")
    public WebApi.Response<String> test(String test) {
        return WebApi.success(test);
    }

}
