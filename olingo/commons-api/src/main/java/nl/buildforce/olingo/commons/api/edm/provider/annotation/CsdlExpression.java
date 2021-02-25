/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.provider.annotation;

import nl.buildforce.olingo.commons.api.edm.provider.CsdlAbstractEdmItem;

public abstract class CsdlExpression extends CsdlAbstractEdmItem {

  /**
   * Return true if the expression is constant
   * @return true if the expression is constant
   */
  public boolean isConstant() {
    return this instanceof CsdlConstantExpression;
  }

  /**
   * Casts the expression to {@link CsdlConstantExpression}.
   * @return Constant Expression
   */
  public CsdlConstantExpression asConstant() {
    return isConstant() ? (CsdlConstantExpression) this : null;
  }

  /**
   * Returns true if the expression is dynamic.
   * @return true if the expression is dynamic
   */
  public boolean isDynamic() {
    return this instanceof CsdlDynamicExpression;
  }

  /**
   * Cast the expression to {@link CsdlDynamicExpression}.
   * @return Dynamic Expression
   */
  public CsdlDynamicExpression asDynamic() {
    return isDynamic() ? (CsdlDynamicExpression) this : null;
  }

}