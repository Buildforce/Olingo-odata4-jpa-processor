/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.provider.annotation;

import java.util.ArrayList;
import java.util.List;

import nl.buildforce.olingo.commons.api.edm.provider.CsdlAnnotatable;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlAnnotation;

/**
 * The edm:UrlRef expression enables a value to be obtained by sending a GET request to the value of
 * the UrlRef expression.
 */
public class CsdlUrlRef extends CsdlDynamicExpression implements CsdlAnnotatable {

  private CsdlExpression value;
  private List<CsdlAnnotation> annotations = new ArrayList<>();

  @Override
  public List<CsdlAnnotation> getAnnotations() {
    return annotations;
  }
  
  public CsdlUrlRef setAnnotations(List<CsdlAnnotation> annotations) {
    this.annotations = annotations;
    return this;
  }

  /**
   * Returns a expression of type Edm.String
   * @return expression of type Edm.String
   */
  public CsdlExpression getValue() {
    return value;
  }

  public CsdlUrlRef setValue(CsdlExpression value) {
    this.value = value;
    return this;
  }
  
  @Override
  public boolean equals (Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof CsdlUrlRef)) {
      return false;
    }
    CsdlUrlRef csdlUrlRef = (CsdlUrlRef) obj;
    return (getValue() == null ? csdlUrlRef.getValue() == null :
            getValue().equals(csdlUrlRef.getValue()))
        && (getAnnotations() == null ? csdlUrlRef.getAnnotations() == null :
            checkAnnotations(csdlUrlRef.getAnnotations()));
  }
  
  private boolean checkAnnotations(List<CsdlAnnotation> csdlUrlRefAnnot) {
    if (csdlUrlRefAnnot == null) {
      return false;
    }
    if (getAnnotations().size() == csdlUrlRefAnnot.size()) {
      for (int i = 0; i < getAnnotations().size() ; i++) {
        if (!getAnnotations().get(i).equals(csdlUrlRefAnnot.get(i))) {
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
    result = prime * result + ((value == null) ? 0 : value.hashCode());
    result = prime * result + ((annotations == null) ? 0 : annotations.hashCode());
    return result;
  }

}