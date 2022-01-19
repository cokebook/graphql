package org.cokebook.graphql.service;


import org.cokebook.graphql.Query;
import org.cokebook.graphql.TypeWiring;
import org.cokebook.graphql.common.JSON;
import org.cokebook.graphql.common.Param;
import org.cokebook.graphql.common.Source;
import org.cokebook.graphql.entity.Author;
import org.cokebook.graphql.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @date 2019/11/28 17:32
 */
@Service
public class BookService {

    @Autowired
    private AuthorService authorService;

    private static List<Book> books = Arrays.asList(
            new Book("1", "Harry Potter and the Philosopher's Stone", "223", "author-1"),
            new Book("2", "Roma", "30", "author-2"),
            new Book("3", "TLP", "10", "author-2"),
            new Book("4", "BUSHI", "12", "author-3")
    );

    @Query("book")
    public Book findById(@Param("id") String id) {
        System.out.println("test to print current-thread:" + Thread.currentThread().getName() + ": book + id = " + id);
        return books.stream().filter(book -> book.getId().equals(id)).findFirst().orElse(null);
    }

    @TypeWiring(field = "books")
    public List<Book> findAllBook() {
        System.out.println("test to print current-thread:" + Thread.currentThread().getName() + ": book");
        return books;
    }

    @TypeWiring(field = "findBooksByNames")
    public List<Book> findBook(@JSON BookQueryParam param) {
        Set<String> names = new HashSet<>(Arrays.asList(param.names));
        return books.stream().filter(book -> names.contains(book.getName()))
                .collect(Collectors.toList());
    }

    @TypeWiring(field = "findBooksByNames2")
    public List<Book> findBookByNames(List<String> names) {
        return books.stream().filter(book -> names.contains(book.getName()))
                .collect(Collectors.toList());
    }

    @TypeWiring(type = "Book", field = "author")
    public Author findByBook(@Source Book book) {
        return authorService.findAuthorById(book.getAuthorId());
    }

    /**
     * 演示使用 @Source 绑定当前 Source 对象
     *
     * @param book
     * @param index
     * @return
     */
    @TypeWiring(type = "Book", field = "author_name")
    public String authorName(@Source Book book, int index) {
        // 测试样例
        return authorName(book.getAuthorId(), index);
    }

    /**
     * 演示使用 @Source 绑定当前 Source 对象特定属性
     *
     * @param authorId
     * @param index
     * @return
     */
    @TypeWiring(type = "Book", field = "author_name2")
    public String authorName(@Source("authorId") String authorId, int index) {
        // 测试样例
        Author author = authorService.findAuthorById(authorId);
        if (author != null) {
            if (index == 1) {
                return author.getFirstName();
            } else if (index == 2) {
                return author.getLastName();
            }
        }
        return String.valueOf("index = " + index);
    }

    public static class BookQueryParam {
        private String[] names;

        public String[] getNames() {
            return names;
        }

        public void setNames(String[] names) {
            this.names = names;
        }
    }

}
