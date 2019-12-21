package org.cokebook.graphql;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * The Type Wiring Keeper
 *
 * @date 2019/11/29 14:56
 */
public interface TypeWiringKeeper {
    /**
     * Get the dataFetchers of {@link TypeWiringDataFetcher}
     *
     * @return
     */
    Set<TypeWiringDataFetcher> dataFetchers();
}
