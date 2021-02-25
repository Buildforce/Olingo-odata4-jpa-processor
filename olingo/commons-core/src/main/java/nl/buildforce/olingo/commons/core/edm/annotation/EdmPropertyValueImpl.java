/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm.annotation;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmException;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmExpression;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmPropertyValue;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlPropertyValue;
import nl.buildforce.olingo.commons.core.edm.AbstractEdmAnnotatable;

public class EdmPropertyValueImpl extends AbstractEdmAnnotatable implements EdmPropertyValue {

  private EdmExpression value;
  private final CsdlPropertyValue csdlExp;

  public EdmPropertyValueImpl(Edm edm, CsdlPropertyValue csdlExp) {
    super(edm, csdlExp);
    this.csdlExp = csdlExp;
  }

  @Override
  public String getProperty() {
    if (csdlExp.getProperty() == null) {
      throw new EdmException("PropertyValue expressions require a referenced property value.");
    }
    return csdlExp.getProperty();
  }

  @Override
  public EdmExpression getValue() {
    if (value == null) {
      if (csdlExp.getValue() == null) {
        throw new EdmException("PropertyValue expressions require an expression value.");
      }
      value = AbstractEdmExpression.getExpression(edm, csdlExp.getValue());
    }
    return value;
  }
}