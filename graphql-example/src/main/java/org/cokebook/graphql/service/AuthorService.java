package org.cokebook.graphql.service;


import org.cokebook.graphql.Mutation;
import org.cokebook.graphql.entity.Author;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @date 2019/11/28 17:34
 */
@Service
public class AuthorService {

    private static List<Author> authors = new ArrayList<>(
            Arrays.asList(
                    new Author("author-1", "Joanne", "Rowling"),
                    new Author("author-2", "Herman", "Melville"),
                    new Author("author-3", "Anne", "Rice")
            )
    );

    public Author findAuthorById(String id) {
        return authors
                .stream()
                .filter(author -> author.getId().equals(id))
                .findFirst()
                .orElse(null);
    }


    @Mutation("createAuthor")
    public Author create() {
        Author author = new Author(System.currentTimeMillis() + "-", "Add-Author", "1");
        authors.add(author);
        return author;
    }

}
