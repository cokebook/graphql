package org.cokebook.graphql;

import graphql.schema.DataFetcher;

public interface TypeWiringDataFetcher extends DataFetcher {

    String getType();

    String getField();
}
