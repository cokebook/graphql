package org.cokebook.graphql.service;


import org.cokebook.graphql.Param;
import org.cokebook.graphql.TypeWiring;
import org.cokebook.graphql.entity.Author;
import org.cokebook.graphql.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @date 2019/11/28 17:32
 */
@Service
public class BookService {

    @Autowired
    private AuthorService authorService;

    private static List<Book> books = Arrays.asList(
            new Book("book-1", "Harry Potter and the Philosopher's Stone", "223", "author-1"),
            new Book("book-2", "Roma", "30", "author-2"),
            new Book("book-3", "TLP", "10", "author-2"),
            new Book("book-4", "BUSHI", "12", "author-3")
    );

    @TypeWiring(field = "book")
    public Book findById(@Param("id") String id) {
        return books.stream().filter(book -> book.getId().equals(id)).findFirst().orElse(null);
    }

    @TypeWiring(type = "Book", field = "author")
    public Author findByBook(Book book) {
        return authorService.findAuthorById(book.getAuthorId());
    }
}
