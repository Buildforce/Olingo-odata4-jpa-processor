/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.provider.annotation;

import java.util.ArrayList;
import java.util.List;

import nl.buildforce.olingo.commons.api.edm.provider.CsdlAnnotatable;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlAnnotation;

/**
 * The edm:Record expression enables a new entity type or complex type instance to be constructed.
 * A record expression contains zero or more edm:PropertyValue (See {@link CsdlRecord} )elements.
 */
public class CsdlRecord extends CsdlDynamicExpression implements CsdlAnnotatable {

  private String type;
  private List<CsdlPropertyValue> propertyValues = new ArrayList<>();
  private List<CsdlAnnotation> annotations = new ArrayList<>();

  @Override
  public List<CsdlAnnotation> getAnnotations() {
    return annotations;
  }

  public CsdlRecord setAnnotations(List<CsdlAnnotation> annotations) {
    this.annotations = annotations;
    return this;
  }

  /**
   * Returns the entity type or complex type to be constructed.
   * @return Entity type or complex type
   */
  public String getType() {
    return type;
  }

  public CsdlRecord setType(String type) {
    this.type = type;
    return this;
  }

  /**
   * List of edm:PropertyValues (See {@link CsdlPropertyValue}
   * @return List of edm:PropertyValues (See
   */
  public List<CsdlPropertyValue> getPropertyValues() {
    return propertyValues;
  }

  public CsdlRecord setPropertyValues(List<CsdlPropertyValue> propertyValues) {
    this.propertyValues = propertyValues;
    return this;
  }
  
  @Override
  public boolean equals (Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof CsdlRecord)) {
      return false;
    }
    CsdlRecord csdlRecord = (CsdlRecord) obj;
    return (getType() == null ? csdlRecord.getType() == null :
            getType().equals(csdlRecord.getType()))
        && (getAnnotations() == null ? csdlRecord.getAnnotations() == null :
            checkAnnotations(csdlRecord.getAnnotations()))
        && (getPropertyValues() == null ? csdlRecord.getPropertyValues() == null :
            checkPropertyValues(csdlRecord.getPropertyValues()));
  }
  
  private boolean checkPropertyValues(List<CsdlPropertyValue> csdlRecordpropertyValues) {
    if (csdlRecordpropertyValues == null) {
      return false;
    }
    if (getPropertyValues().size() == csdlRecordpropertyValues.size()) {
      for (int i = 0; i < getPropertyValues().size(); i++) {
        if (!getPropertyValues().get(i).equals(
            csdlRecordpropertyValues.get(i))) {
          return false;
        }
      }
    } else {
      return false;
    }
    return true;
  }

  private boolean checkAnnotations(List<CsdlAnnotation> csdlRecordAnnot) {
    if (csdlRecordAnnot == null) {
      return false;
    }
    if (getAnnotations().size() == csdlRecordAnnot.size()) {
      for (int i = 0; i < getAnnotations().size() ; i++) {
        if (!getAnnotations().get(i).equals(csdlRecordAnnot.get(i))) {
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
    result = prime * result + ((type == null) ? 0 : type.hashCode());
    result = prime * result + ((propertyValues == null) ? 0 : 
      propertyValues.hashCode());
    result = prime * result + ((annotations == null) ? 0 : 
      annotations.hashCode());
    return result;
  }

}