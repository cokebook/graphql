package org.cokebook.graphql;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @date 2019/11/29 14:56
 */
public interface TypeWiringKeeper {

    Map<Method, String> typeWiringMethods();
}
