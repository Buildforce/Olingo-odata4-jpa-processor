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
      return switch (csdlExp.getType()) {
          case And -> EdmExpressionType.And;
          case Or -> EdmExpressionType.Or;
          case Not -> EdmExpressionType.Not;
          case Eq -> EdmExpressionType.Eq;
          case Ne -> EdmExpressionType.Ne;
          case Gt -> EdmExpressionType.Gt;
          case Ge -> EdmExpressionType.Ge;
          case Lt -> EdmExpressionType.Lt;
          case Le -> EdmExpressionType.Le;
          default -> throw new EdmException("Invalid Expressiontype for logical or comparison expression: " + csdlExp.getType());
      };
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