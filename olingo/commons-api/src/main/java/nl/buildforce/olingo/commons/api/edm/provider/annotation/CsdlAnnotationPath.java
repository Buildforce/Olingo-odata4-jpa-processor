/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.provider.annotation;

/**
 * Represents an annotation path
 */
public class CsdlAnnotationPath extends CsdlDynamicExpression {

  private String value;

  public CsdlAnnotationPath setValue(String value) {
    this.value = value;
    return this;
  }

  /**
   * Value of the path
   * @return value of the path
   */
  public String getValue() {
    return value;
  }
  
  @Override
  public boolean equals (Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof CsdlAnnotationPath)) {
      return false;
    }
    CsdlAnnotationPath csdlAnnotPath = (CsdlAnnotationPath) obj;
     
    return getValue() == null ? csdlAnnotPath.getValue() == null :
            getValue().equals(csdlAnnotPath.getValue());
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((value == null) ? 0 : value.hashCode());
    return result;
  }

}