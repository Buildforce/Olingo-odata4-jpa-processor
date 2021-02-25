/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm.annotation;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmException;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmExpression;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmLogicalOrComparisonExpression;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlLogicalOrComparisonExpression;
//CHECKSTYLE:OFF
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlLogicalOrComparisonExpression.LogicalOrComparisonExpressionType;

//CHECKSTYLE:ON

public abstract class AbstractEdmLogicalOrComparisonExpression
    extends AbstractEdmAnnotatableDynamicExpression implements EdmLogicalOrComparisonExpression {

  private EdmExpression left;
  private EdmExpression right;
  private final CsdlLogicalOrComparisonExpression csdlExp;

  public AbstractEdmLogicalOrComparisonExpression(Edm edm, CsdlLogicalOrComparisonExpression csdlExp) {
    super(edm, csdlExp.getType().toString(), csdlExp);
    this.csdlExp = csdlExp;
  }

  @Override
  public EdmExpressionType getExpressionType() {
    switch (csdlExp.getType()) {
    case And:
      return EdmExpressionType.And;
    case Or:
      return EdmExpressionType.Or;
    case Not:
      return EdmExpressionType.Not;
    case Eq:
      return EdmExpressionType.Eq;
    case Ne:
      return EdmExpressionType.Ne;
    case Gt:
      return EdmExpressionType.Gt;
    case Ge:
      return EdmExpressionType.Ge;
    case Lt:
      return EdmExpressionType.Lt;
    case Le:
      return EdmExpressionType.Le;
    default:
      throw new EdmException("Invalid Expressiontype for logical or comparison expression: " + csdlExp.getType());
    }
  }

  @Override
  public EdmExpression getLeftExpression() {
    if (left == null) {
      if (csdlExp.getLeft() == null) {
        throw new EdmException("Comparison Or Logical expression MUST have a left and right expression.");
      }
      left = getExpression(edm, csdlExp.getLeft());
      if (csdlExp.getType() == LogicalOrComparisonExpressionType.Not) {
        right = left;
      }
    }
    return left;
  }

  @Override
  public EdmExpression getRightExpression() {
    if (right == null) {
      if (csdlExp.getRight() == null) {
        throw new EdmException("Comparison Or Logical expression MUST have a left and right expression.");
      }
      right = getExpression(edm, csdlExp.getRight());
      if (csdlExp.getType() == LogicalOrComparisonExpressionType.Not) {
        left = right;
      }
    }
    return right;
  }
}