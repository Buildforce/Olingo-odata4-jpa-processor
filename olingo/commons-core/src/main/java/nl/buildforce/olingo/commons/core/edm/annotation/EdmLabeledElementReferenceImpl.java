/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm.annotation;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmLabeledElementReference;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlLabeledElementReference;

public class EdmLabeledElementReferenceImpl extends AbstractEdmDynamicExpression implements EdmLabeledElementReference {

  private final CsdlLabeledElementReference csdlExp;

  public EdmLabeledElementReferenceImpl(Edm edm, CsdlLabeledElementReference csdlExp) {
    super(edm, "LabeledElementReference");
    this.csdlExp = csdlExp;
  }

  @Override
  public String getValue() {
    return csdlExp.getValue();
  }
  
  @Override
  public EdmExpressionType getExpressionType() {
    return EdmExpressionType.LabeledElementReference;
  }
}
