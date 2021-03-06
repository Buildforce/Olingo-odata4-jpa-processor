/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.queryoption.apply;

import nl.buildforce.olingo.server.api.uri.queryoption.SearchOption;
import nl.buildforce.olingo.server.api.uri.queryoption.apply.Search;

/**
 * Represents the search transformation.
 */
public class SearchImpl implements Search {

  // private SearchOption searchOption;

  @Override
  public Kind getKind() {
    return Kind.SEARCH;
  }

/*
  @Override
  public SearchOption getSearchOption() {
    return searchOption;
  }

*/
  public SearchImpl setSearchOption(SearchOption searchOption) {
    // this.searchOption = searchOption;
    return this;
  }

}