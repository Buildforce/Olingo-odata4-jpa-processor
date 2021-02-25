/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.parser.search;

import nl.buildforce.olingo.server.api.uri.queryoption.search.SearchBinary;
import nl.buildforce.olingo.server.api.uri.queryoption.search.SearchExpression;
import nl.buildforce.olingo.server.api.uri.queryoption.search.SearchTerm;
import nl.buildforce.olingo.server.api.uri.queryoption.search.SearchUnary;

public abstract class SearchExpressionImpl implements SearchExpression {

  @Override
  public boolean isSearchTerm() {
    return this instanceof SearchTerm;
  }

  @Override
  public SearchTerm asSearchTerm() {
    return isSearchTerm() ? (SearchTerm) this : null;
  }

  @Override
  public boolean isSearchBinary() {
    return this instanceof SearchBinary;
  }

  @Override
  public SearchBinary asSearchBinary() {
    return isSearchBinary() ? (SearchBinary) this : null;
  }

  @Override
  public boolean isSearchUnary() {
    return this instanceof SearchUnary;
  }

  @Override
  public SearchUnary asSearchUnary() {
    return isSearchUnary() ? (SearchUnary) this : null;
  }

}
