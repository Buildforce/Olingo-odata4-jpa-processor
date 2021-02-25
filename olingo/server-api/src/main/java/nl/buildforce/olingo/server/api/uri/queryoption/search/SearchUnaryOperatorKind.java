/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri.queryoption.search;

public enum SearchUnaryOperatorKind {
  NOT;

  public static SearchUnaryOperatorKind get(String operator) {
    return NOT.name().equals(operator) ? NOT : null;
  }
}
