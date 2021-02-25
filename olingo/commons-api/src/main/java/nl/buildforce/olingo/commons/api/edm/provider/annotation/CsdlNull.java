/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.provider.annotation;

import java.util.ArrayList;
import java.util.List;

import nl.buildforce.olingo.commons.api.edm.provider.CsdlAnnotatable;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlAnnotation;

/**
 * The edm:Null expression returns an untyped null value.
 */
public class CsdlNull extends CsdlDynamicExpression implements CsdlAnnotatable {

  private List<CsdlAnnotation> annotations = new ArrayList<>();

  @Override
  public List<CsdlAnnotation> getAnnotations() {
    return annotations;
  }

  public CsdlNull setAnnotations(List<CsdlAnnotation> annotations) {
    this.annotations = annotations;
    return this;
  }
  
  @Override
  public boolean equals (Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof CsdlNull)) {
      return false;
    }
    CsdlNull csdlNull = (CsdlNull) obj;
    return (getAnnotations() == null ? csdlNull.getAnnotations() == null :
        checkAnnotations(csdlNull.getAnnotations()));
  }
  
  private boolean checkAnnotations(List<CsdlAnnotation> csdlNullAnnot) {
    if (csdlNullAnnot == null) {
      return false;
    }
    if (getAnnotations().size() == csdlNullAnnot.size()) {
      for (int i = 0; i < getAnnotations().size() ; i++) {
        if (!getAnnotations().get(i).equals(csdlNullAnnot.get(i))) {
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
    result = prime * result + ((annotations == null) ? 0 : 
      annotations.hashCode());
    return result;
  }

}