/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.queryoption;

import nl.buildforce.olingo.server.api.uri.queryoption.SearchOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SystemQueryOptionKind;
import nl.buildforce.olingo.server.api.uri.queryoption.search.SearchExpression;

public class SearchOptionImpl extends SystemQueryOptionImpl implements SearchOption {

  private SearchExpression searchExpression;

  public SearchOptionImpl() {
    setKind(SystemQueryOptionKind.SEARCH);
  }

  @Override
  public SearchExpression getSearchExpression() {
    return searchExpression;
  }

  public SearchOptionImpl setSearchExpression(SearchExpression searchExpression) {
    this.searchExpression = searchExpression;
    return this;
  }

}