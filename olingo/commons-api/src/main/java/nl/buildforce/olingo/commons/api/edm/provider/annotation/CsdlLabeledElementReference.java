/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.provider.annotation;

/**
 * The edm:LabeledElementReference expression returns the value of an
 * edm:LabeledElement (see {@link CsdlLabeledElement}) expression.
 */
public class CsdlLabeledElementReference extends CsdlDynamicExpression {

  private String value;

  /**
   * Returns the value of the edm:LabeledElement expression
   * @return value of the edm:LabeledElement expression
   */
  public String getValue() {
    return value;
  }

  public CsdlLabeledElementReference setValue(String value) {
    this.value = value;
    return this;
  }
  
  @Override
  public boolean equals (Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof CsdlLabeledElementReference)) {
      return false;
    }
    CsdlLabeledElementReference csdlLabelledEleRef = (CsdlLabeledElementReference) obj;
    return (getValue() == null ? csdlLabelledEleRef.getValue() == null :
            getValue().equals(csdlLabelledEleRef.getValue()));
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((value == null) ? 0 : value.hashCode());
    return result;
  }

}