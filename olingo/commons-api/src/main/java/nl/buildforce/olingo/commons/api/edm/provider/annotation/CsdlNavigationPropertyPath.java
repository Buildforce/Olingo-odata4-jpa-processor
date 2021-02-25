/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.provider.annotation;

/**
 * The edm:NavigationPropertyPath expression provides a value for terms or term properties that specify the
 * built-in abstract type Edm.NavigationPropertyPath
 */
public class CsdlNavigationPropertyPath extends CsdlDynamicExpression {
  
  private String value;

  /**
   * Returns the navigation property path itself.
   *
   * @return navigation property
   */
  public String getValue() {
    return value;
  }

  public CsdlNavigationPropertyPath setValue(String value) {
    this.value = value;
    return this;
  }
  
  @Override
  public boolean equals (Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof CsdlNavigationPropertyPath)) {
      return false;
    }
    CsdlNavigationPropertyPath csdlNavPropPath = (CsdlNavigationPropertyPath) obj;
    return (getValue() == null ? csdlNavPropPath.getValue() == null :
            getValue().equals(csdlNavPropPath.getValue()));
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((value == null) ? 0 : value.hashCode());
    return result;
  }

}