/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm.annotation;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmNull;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlNull;

public class EdmNullImpl extends AbstractEdmAnnotatableDynamicExpression implements EdmNull {

  public EdmNullImpl(Edm edm, CsdlNull csdlExp) {
    super(edm, "Null", csdlExp);
  }
  
  @Override
  public EdmExpressionType getExpressionType() {
    return EdmExpressionType.Null;
  }
}
