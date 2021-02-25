/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.parser.search;

import nl.buildforce.olingo.server.api.uri.queryoption.search.SearchTerm;

public class SearchTermImpl extends SearchExpressionImpl implements SearchTerm {
  private final String term;

  public SearchTermImpl(String term) {
    this.term = term;
  }

  @Override
  public String getSearchTerm() {
    return term;
  }

  @Override
  public String toString() {
    return "'" + term + "'";
  }
}
