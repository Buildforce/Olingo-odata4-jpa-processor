/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.provider.annotation;

import java.util.ArrayList;
import java.util.List;

import nl.buildforce.olingo.commons.api.edm.provider.CsdlAbstractEdmItem;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlAnnotatable;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlAnnotation;

/**
 * The edm:PropertyValue element supplies a value to a property on the type instantiated by an 
 * edm:Record expression (See {@link nl.buildforce.olingo.commons.api.edm.annotation.EdmRecord}).
 * The value is obtained by evaluating an expression.
 */
public class CsdlPropertyValue extends CsdlAbstractEdmItem implements CsdlAnnotatable {

  private String property;
  private CsdlExpression value;
  private List<CsdlAnnotation> annotations = new ArrayList<>();

  @Override
  public List<CsdlAnnotation> getAnnotations() {
    return annotations;
  }
  
  public CsdlPropertyValue setAnnotations(List<CsdlAnnotation> annotations) {
    this.annotations = annotations;
    return this;
  }
  
  /**
   * Property name
   * @return Property name
   */
  public String getProperty() {
    return property;
  }

  public CsdlPropertyValue setProperty(String property) {
    this.property = property;
    return this;
  }

  /**
   * Evaluated value of the expression (property value)
   * @return evaluated value of the expression
   */
  public CsdlExpression getValue() {
    return value;
  }

  public CsdlPropertyValue setValue(CsdlExpression value) {
    this.value = value;
    return this;
  }
  
  @Override
  public boolean equals (Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof CsdlPropertyValue)) {
      return false;
    }
    CsdlPropertyValue csdlPropertyValue = (CsdlPropertyValue) obj;
    
    return (getProperty() == null ? csdlPropertyValue.getProperty() == null :
            getProperty().equalsIgnoreCase(csdlPropertyValue.getProperty()))
        && (getValue() == null ? csdlPropertyValue.getValue() == null :
            getValue().equals(csdlPropertyValue.getValue()))
        && (getAnnotations() == null ? csdlPropertyValue.getAnnotations() == null :
            checkAnnotations(csdlPropertyValue.getAnnotations()));
  }
  
  private boolean checkAnnotations(List<CsdlAnnotation> csdlPropertyValueAnnot) {
    if (csdlPropertyValueAnnot == null) {
      return false;
    }
    if (getAnnotations().size() == csdlPropertyValueAnnot.size()) {
      for (int i = 0; i < getAnnotations().size() ; i++) {
        if (!getAnnotations().get(i).equals(
            csdlPropertyValueAnnot.get(i))) {
          return false;
        }
      }
    } else {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((property == null) ? 0 : property.hashCode());
    result = prime * result + ((value == null) ? 0 : value.hashCode());
    result = prime * result + ((annotations == null) ? 0 : 
      annotations.hashCode());
    return result;
  }

}