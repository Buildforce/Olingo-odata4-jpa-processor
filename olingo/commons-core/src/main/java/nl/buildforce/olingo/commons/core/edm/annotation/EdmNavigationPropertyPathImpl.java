/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm.annotation;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmNavigationPropertyPath;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlNavigationPropertyPath;

public class EdmNavigationPropertyPathImpl extends AbstractEdmDynamicExpression implements EdmNavigationPropertyPath {

  private final CsdlNavigationPropertyPath csdlExp;

  public EdmNavigationPropertyPathImpl(Edm edm, CsdlNavigationPropertyPath csdlExp) {
    super(edm, "NavigationPropertyPath");
    this.csdlExp = csdlExp;
  }

  @Override
  public String getValue() {
    return csdlExp.getValue();
  }
  
  @Override
  public EdmExpressionType getExpressionType() {
    return EdmExpressionType.NavigationPropertyPath;
  }
}
