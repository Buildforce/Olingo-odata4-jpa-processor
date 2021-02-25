/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm.annotation;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmException;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmExpression;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmIf;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlIf;

public class EdmIfImpl extends AbstractEdmAnnotatableDynamicExpression implements EdmIf {

  private EdmExpression guard;
  private EdmExpression _then;
  private EdmExpression _else;
  private final CsdlIf csdlExp;

  public EdmIfImpl(Edm edm, CsdlIf csdlExp) {
    super(edm, "If", csdlExp);
    this.csdlExp = csdlExp;
  }

  @Override
  public EdmExpression getGuard() {
    if (guard == null) {
      if (csdlExp.getGuard() == null) {
        throw new EdmException("Guard clause of an if expression must not be null");
      }
      guard = getExpression(edm, csdlExp.getGuard());
    }
    return guard;
  }

  @Override
  public EdmExpression getThen() {
    if (_then == null) {
      if (csdlExp.getThen() == null) {
        throw new EdmException("Then clause of an if expression must not be null");
      }
      _then = getExpression(edm, csdlExp.getThen());
    }
    return _then;
  }

  @Override
  public EdmExpression getElse() {
    // The else clause might be null in certain conditions so we can`t evaluate this here.
    if (_else == null && csdlExp.getElse() != null) {
      _else = getExpression(edm, csdlExp.getElse());
    }
    return _else;
  }

  @Override
  public EdmExpressionType getExpressionType() {
    return EdmExpressionType.If;
  }
}