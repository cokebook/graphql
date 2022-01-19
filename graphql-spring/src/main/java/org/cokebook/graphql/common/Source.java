package org.cokebook.graphql.common;


import java.lang.annotation.*;

/**
 * 特殊定制 Graphql Env Source 对象 标识注解
 *
 * @date 2019/11/29 15:36
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Param(Source.OBJECT_NAME)
public @interface Source {
    String OBJECT_NAME = "-source-";
}
