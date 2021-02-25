/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.annotation;

/**
 * Super type of all annotation expressions
 * A expression is either constant or dynamic
 */
public interface EdmExpression {

  enum EdmExpressionType {
    //Constant
    Binary,
    Bool,
    Date,
    DateTimeOffset,
    Decimal,
    Duration,
    EnumMember,
    Float,
    Guid,
    Int,
    String,
    TimeOfDay,
    //Dynamic
    //Logical
    And,
    Or,
    Not,
    //Comparison
    Eq,
    Ne,
    Gt,
    Ge,
    Lt,
    Le,
    //Other
    AnnotationPath,
    Apply,
    Cast,
    Collection,
    If,
    IsOf,
    LabeledElement,
    LabeledElementReference,
    Null,
    NavigationPropertyPath,
    Path,
    PropertyPath,
    Record,
    UrlRef
  }
  
  /**
   * See {@link EdmExpressionType} for details.
   * @return the type of this expression
   */
  EdmExpressionType getExpressionType();
  
  /**
   * Will return the name of the expression e.g. Apply or Cast.
   * @return the name of the expression
   */
  String getExpressionName();

  /**
   * Return true if the expression is constant
   * @return true if the expression is constant
   */
  boolean isConstant();
  
  /**
   * Casts the expression to {@link EdmConstantExpression}
   * @return Constant Expression
   */
  EdmConstantExpression asConstant();
  
  /**
   * Return true if the expression is dynamic
   * @return true if the expression is dynamic
   */
  boolean isDynamic();
  
  /**
   * Cast the expression to {@link EdmDynamicExpression}
   * @return Dynamic Expression
   */
  EdmDynamicExpression asDynamic();
}
