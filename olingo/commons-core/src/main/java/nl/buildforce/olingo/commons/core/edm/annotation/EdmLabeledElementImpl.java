/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm.annotation;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmException;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmExpression;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmLabeledElement;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlLabeledElement;

public class EdmLabeledElementImpl
    extends AbstractEdmAnnotatableDynamicExpression implements EdmLabeledElement {

  private EdmExpression value;
  private final CsdlLabeledElement csdlLableledElement;

  public EdmLabeledElementImpl(Edm edm, CsdlLabeledElement csdlExp) {
    super(edm, "LabeledElement", csdlExp);
      csdlLableledElement = csdlExp;
  }

  @Override
  public String getName() {
    if (csdlLableledElement.getName() == null) {
      throw new EdmException("The LabeledElement expression must have a name attribute.");
    }
    return csdlLableledElement.getName();
  }

  @Override
  public EdmExpression getValue() {
    if (value == null) {
      if (csdlLableledElement.getValue() == null) {
        throw new EdmException("The LabeledElement expression must have a child expression");
      }
      value = getExpression(edm, csdlLableledElement.getValue());
    }
    return value;
  }

  @Override
  public EdmExpressionType getExpressionType() {
    return EdmExpressionType.LabeledElement;
  }
}