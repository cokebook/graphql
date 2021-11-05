package org.cokebook.graphql.service;

import org.cokebook.graphql.TypeWiring;
import org.springframework.stereotype.Service;

/**
 * @date 2019/12/23 19:29
 */
@Service
public class IndexService {

    @TypeWiring(field = "index")
    public String index() {
        return "Index Service: Hello World";
    }

}
