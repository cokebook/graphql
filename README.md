# GraphQL : GraphQL Spring Plugin

    本项目是 GraphQL 的 spring 扩展应用, 其旨在为常规的 spring mvc OR  spring boot 项目提供快捷的 GraphQL 支持.



## 1 使用说明

- 1 引入 `graphql-spring`  依赖

- 2 创建你的GraphQL 查询入口 :

```java

@RestController
@RequestMapping("graphql")
public class GraphQLController {

    private static final Logger log = LoggerFactory.getLogger(GraphQLController.class);

    @Autowired
    private GraphQLAdapter graphQL;

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

```

- 3 声明 Bean 的方法为TypeWiring :

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

```bash
    curl -X POST \
      http://localhost:8080/graphql/query \
      -H 'cache-control: no-cache' \
      -H 'content-type: application/json' \
      -H 'postman-token: 9314c65e-e333-e5cb-8693-7f39fd381856' \
      -d '{
        
        book(id:"book-1") {
            id
            name
            author {
                firstName
            }
            
        }
        
    }'
```


## 2 Example 

    你可以直接下载本项目运行 graphql-example 模块演示系统结果作为学习素材.
    
## 3 参考文档

- [GraphQL Java 文档](https://graphql.cn/code/#java)
- [GraphQL Spring Boot Project Example](https://www.graphql-java.com/tutorials/getting-started-with-spring-boot/)
- [Graphql-java Github Project](https://github.com/graphql-java/graphql-java)

## 4 特别说明

- 由于 spring-boot 1.x 和 2.x 版本之间部分接口被调整, 而当前项目基于 1.x 版本构建, 所有如果你想支持 2.x 版本 则你需要调整一些 [GraphQlRunListener](graphql-spring/src/java/org/cokebook/graphql/spring/GraphQlRunListener.java) 类.