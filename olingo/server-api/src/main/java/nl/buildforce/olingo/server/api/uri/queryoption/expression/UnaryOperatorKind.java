/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri.queryoption.expression;

/**
 * Enumeration of supported unary operators<br>
 * For the semantic of these operators please see the ODATA specification for URL conventions
 */
public enum UnaryOperatorKind {

  /**
   * Minus operator
   */
  MINUS("-"),

  /**
   * not operator
   */
  NOT("not");

  private final String syntax;

  /**
   * Constructor for enumeration value
   * @param syntax used in the URI
   */
  UnaryOperatorKind(String syntax) {
    this.syntax = syntax;
  }

  /**
   * @return URI syntax for that operator kind
   */
  @Override
  public String toString() {
    return syntax;
  }

  /**
   * URI syntax to enumeration value
   * @param operator Operator in the syntax used in the URI
   * @return Operator kind which represents the given syntax
   */
  public static UnaryOperatorKind get(String operator) {
    for (UnaryOperatorKind op : UnaryOperatorKind.values()) {
      if (op.toString().equals(operator)) {
        return op;
      }
    }
    return null;
  }

}