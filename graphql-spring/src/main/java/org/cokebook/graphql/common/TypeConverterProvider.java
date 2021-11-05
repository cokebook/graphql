package org.cokebook.graphql.common;

/**
 * @date 2019/12/19 10:42
 */
public interface TypeConverterProvider {
    /**
     * TypeConverterRegister post process
     *
     * @param registry
     */
    void postprocess(TypeConverterRegistry registry);

}
