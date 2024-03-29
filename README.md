# GraphQL : GraphQL Spring Plugin

    本项目是 GraphQL 的 spring 扩展应用, 其旨在为常规的 spring mvc OR  spring boot 项目提供快捷的 GraphQL 支持.



## 1 使用说明

- 1 引入 `graphql-spring`  依赖

    > 本项目暂时没有MVN 中央仓库JAR, 你可以克隆本项目在本地运行打包!

- 定义你的 graphql schema  

    > 将 graphql-example/resources/schema.graphql 样例文件

- 2 声明 Bean 的方法为TypeWiring :

```java

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
    // 作为查询的简写方案可以直接使用  @Query("book") 替代
    @TypeWiring(field = "book")
    public Book findById(@Param("id") String id) {
        return books.stream().filter(book -> book.getId().equals(id)).findFirst().orElse(null);
    }

    @TypeWiring(type = "Book", field = "author")
    public Author findByBook(Book book) {
        return authorService.findAuthorById(book.getAuthorId());
    }
}

```

- 4 启动应用, 使用命令查询 :

**1 使用通用风格查询模式**

```shell script

curl -L -X GET 'http://localhost:8080/graphql' \
-H 'Content-Type: application/json' \
--data-raw '{"query":"query test($id:ID) {\n  \n  book(id:$id) { name, id} \n\n}","variables":{"id":"2","index":1}}'


```

**2 使用 Url 参数模式查询**

```shell script

curl -L -X GET 'http://localhost:8080/graphql/query?q=query%20test($id:ID)%20%7B%20book(id:$id)%20%7B%20name,%20id%7D%20%7D&id=2'

```

- 5 结果演示

```json
    {
        "code": 0,
        "message": null,
        "data": {
            "book": {
                "name": "Roma",
                "id": "2"
            }
        }
    }
```

## 2 Example 

- 你可以直接下载本项目运行 graphql-example 模块演示系统结果作为学习素材. [点击跳转到样例](./graphql-example)

- 如果你想使用非 @TypeWiring 以及@Query / @Mutation 快捷注解声明 DataFetcher, 你可以使用常规 Spring bean 模式定义 DataFetcher [点击跳转到样例](./graphql-example/src/main/java/org/cokebook/graphql/fetcher/WelcomeDataFetcher.java)    
    
## 3 参考文档

- [GraphQL Java 文档](https://graphql.cn/code/#java)
- [GraphQL Spring Boot Project Example](https://www.graphql-java.com/tutorials/getting-started-with-spring-boot/)
- [Graphql-java Github Project](https://github.com/graphql-java/graphql-java)

## 4 特别说明

-  ~~由于 spring-boot 1.x 和 2.x 版本之间部分接口被调整, 而当前项目基于 1.x 版本构建, 所有如果你想支持 2.x 版本 则你需要调整一些 [GraphQlRunListener](./graphql-spring/src/main/java/org/cokebook/graphql/spring/GraphQlRunListener.java) 类.~~ `已支持`
-  ~~考虑我使用的场景 目前主要集中开发针对查询的场景, 后续会考虑支持变更场景~~ `已支持`