/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm.annotation;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmException;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmExpression;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmUrlRef;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlUrlRef;

public class EdmUrlRefImpl extends AbstractEdmAnnotatableDynamicExpression implements EdmUrlRef {

  private final CsdlUrlRef csdlExp;
  private EdmExpression value;

  public EdmUrlRefImpl(Edm edm, CsdlUrlRef csdlExp) {
    super(edm, "UrlRef", csdlExp);
    this.csdlExp = csdlExp;
  }

  @Override
  public EdmExpression getValue() {
    if (value == null) {
      if (csdlExp.getValue() == null) {
        throw new EdmException("URLRef expressions require an expression value.");
      }
      value = getExpression(edm, csdlExp.getValue());
    }
    return value;
  }
  
  @Override
  public EdmExpressionType getExpressionType() {
    return EdmExpressionType.UrlRef;
  }
}
