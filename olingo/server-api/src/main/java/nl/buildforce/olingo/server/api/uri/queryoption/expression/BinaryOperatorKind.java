/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri.queryoption.expression;

/**
 * Enumeration of supported binary operators<br>
 * For the semantic of these operators please see the ODATA specification for URL conventions
 */
public enum BinaryOperatorKind {

  /**
   * OData has operator used for OData enumerations
   */
  HAS("has"),
  
  /**
   * In operator
   */
  IN("in"),

  /**
   * Multiplication operator
   */
  MUL("mul"),

  /**
   * Division operator
   */
  DIV("div"),

  /**
   * Modulo operator
   */
  MOD("mod"),

  /**
   * Addition operator
   */
  ADD("add"),

  /**
   * Subtraction operator
   */
  SUB("sub"),

  /**
   * Greater than operator (">")
   */
  GT("gt"),

  /**
   * Greater than or equals (">=") operator
   */
  GE("ge"),

  /**
   * Lesser than operator ("<")
   */
  LT("lt"),

  /**
   * Lesser operator or equals ("<=") operator
   */
  LE("le"),

  /**
   * Equality operator
   */
  EQ("eq"),

  /**
   * Inequality operator
   */
  NE("ne"),

  /**
   * And operator
   */
  AND("and"),

  /**
   * Or operator
   */
  OR("or");

  private final String syntax;

  /**
   * Constructor for enumeration value
   * @param syntax used in the URI
   */
  BinaryOperatorKind(String syntax) {
    this.syntax = syntax;
  }

  /**
   * URI syntax to enumeration value
   * @param operator Operator in the syntax used in the URI
   * @return Operator kind which represents the given syntax
   */
  public static BinaryOperatorKind get(String operator) {
    for (BinaryOperatorKind op : BinaryOperatorKind.values()) {
      if (op.toString().equals(operator)) {
        return op;
      }
    }
    return null;
  }

  /**
   * @return URI syntax for that operator kind
   */
  @Override
  public String toString() {
    return syntax;
  }

}