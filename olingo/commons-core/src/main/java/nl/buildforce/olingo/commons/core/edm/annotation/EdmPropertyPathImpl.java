/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm.annotation;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmPropertyPath;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlPropertyPath;

public class EdmPropertyPathImpl extends AbstractEdmDynamicExpression implements EdmPropertyPath {

  private final CsdlPropertyPath csdlExp;

  public EdmPropertyPathImpl(Edm edm, CsdlPropertyPath csdlExp) {
    super(edm, "PropertyPath");
    this.csdlExp = csdlExp;
  }

  @Override
  public String getValue() {
    return csdlExp.getValue();
  }

  @Override
  public EdmExpressionType getExpressionType() {
    return EdmExpressionType.PropertyPath;
  }
}
