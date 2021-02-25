/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm.annotation;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmPath;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlPath;

public class EdmPathImpl extends AbstractEdmDynamicExpression implements EdmPath {

  private final CsdlPath csdlExp;

  public EdmPathImpl(Edm edm, CsdlPath csdlExp) {
    super(edm, "Path");
    this.csdlExp = csdlExp;
  }

  @Override
  public String getValue() {
    return csdlExp.getValue();
  }
  
  @Override
  public EdmExpressionType getExpressionType() {
    return EdmExpressionType.Path;
  }
}
