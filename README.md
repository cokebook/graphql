# graphql-spring

> 本项目的目的在于为常规的基于 spring boot 的 http web 服务提供便捷 Graphql 支持！
> 项目的作用是在常规的四层OR三层系统  和 GraphQl 应用结构之间搭建一座桥梁 是各开发可以与最小实现对 GraphQL 的兼容


### 项目操作指南

- 1 执行 graphql-example 项目中的 Bootstrap 类 : 启动应用

- 2 执行以下请求观察响应 

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
  
 - 3 结果如下说明应用正常
 
    ```json
   
   {
     "code": 0,
     "message": null,
     "data": {
       "book": {
         "id": "book-1",
         "name": "Harry Potter and the Philosopher's Stone",
         "author": {
           "firstName": "Joanne"
         }
       }
     }
   }


    ```