/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.provider.annotation;

/**
 *  The edm:Path expression enables a value to be obtained by traversing an object graph. 
 *  It can be used in annotations that target entity containers, entity sets, entity types, complex types, 
 *  navigation properties of structured types, and properties of structured types.
 */
public class CsdlPath extends CsdlDynamicExpression {
  
  private String value;

  /**
   * Returns the target value of the expression
   *
   * @return target value of the expression
   */
  public String getValue() {
    return value;
  }

  public CsdlPath setValue(String value) {
    this.value = value;
    return this;
  }
  
  @Override
  public boolean equals (Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof CsdlPath)) {
      return false;
    }
    CsdlPath csdlPath = (CsdlPath) obj;
    return (getValue() == null ? csdlPath.getValue() == null :
            getValue().equals(csdlPath.getValue()));
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((value == null) ? 0 : value.hashCode());
    return result;
  }

}