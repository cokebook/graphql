package org.cokebook.graphql.fetcher;

import graphql.schema.DataFetchingEnvironment;
import org.cokebook.graphql.TypeWiring;
import org.cokebook.graphql.TypeWiringDataFetcher;
import org.springframework.stereotype.Component;

/**
 * Welcome Data Fetcher
 *
 * @date 2019/12/23 19:18
 */
@Component
public class WelcomeDataFetcher implements TypeWiringDataFetcher {
    @Override
    public String getType() {
        return TypeWiring.DEFAULT_TYPE;
    }

    @Override
    public String getField() {
        return "index";
    }

    @Override
    public Object get(DataFetchingEnvironment environment) throws Exception {
        return "Hello World";
    }
}
