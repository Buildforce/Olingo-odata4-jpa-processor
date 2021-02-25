/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.provider.annotation;

/**
 * The edm:PropertyPath expression provides a value for terms or term properties that specify the built-in
 * abstract type Edm.PropertyPath.
 */
public class CsdlPropertyPath extends CsdlDynamicExpression {

  private String value;

  /**
   * Returns the property path itself.
   * @return the property path itself
   */
  public String getValue() {
    return value;
  }

  public CsdlPropertyPath setValue(String value) {
    this.value = value;
    return this;
  }
  
  @Override
  public boolean equals (Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof CsdlPropertyPath)) {
      return false;
    }
    CsdlPropertyPath csdlPropPath = (CsdlPropertyPath) obj;
    return getValue() == null ? csdlPropPath.getValue() == null :
            getValue().equals(csdlPropPath.getValue());
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((value == null) ? 0 : value.hashCode());
    return result;
  }

}