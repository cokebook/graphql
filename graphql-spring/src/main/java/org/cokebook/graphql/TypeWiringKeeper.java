package org.cokebook.graphql;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * The Type Wiring Keeper
 *
 * @date 2019/11/29 14:56
 */
public interface TypeWiringKeeper {

    /**
     * Type Wiring Methods
     *
     * @return
     */
    Map<Method, String> typeWiringMethods();
}
