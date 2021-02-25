/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri.queryoption.search;

public enum SearchBinaryOperatorKind {
  AND,
  OR;

  public static SearchBinaryOperatorKind get(String operator) {
    if (AND.name().equals(operator)) {
      return AND;
    } else if (OR.name().equals(operator)) {
      return OR;
    } else {
      return null;
    }
  }
}
