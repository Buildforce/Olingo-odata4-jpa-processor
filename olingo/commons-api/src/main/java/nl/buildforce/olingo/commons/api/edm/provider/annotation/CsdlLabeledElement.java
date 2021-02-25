/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/package nl.buildforce.olingo.commons.api.edm.provider.annotation;

import java.util.ArrayList;
import java.util.List;

import nl.buildforce.olingo.commons.api.edm.provider.CsdlAnnotatable;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlAnnotation;

/**
 * The edm:LabeledElement expression assigns a name to a child expression. The value of the child expression can
 * then be reused elsewhere with an edm:LabeledElementReference (See {@link CsdlLabeledElementReference}) expression.
 */
public class CsdlLabeledElement extends CsdlDynamicExpression implements CsdlAnnotatable {

  private String name;
  private CsdlExpression value;
  private List<CsdlAnnotation> annotations = new ArrayList<>();

  @Override
  public List<CsdlAnnotation> getAnnotations() {
    return annotations;
  }
  
  public CsdlLabeledElement setAnnotations(List<CsdlAnnotation> annotations) {
    this.annotations = annotations;
    return this;
  }
  
  /**
   * Returns the assigned name
   * @return assigned name
   */
  public String getName() {
    return name;
  }

  public CsdlLabeledElement setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Returns the child expression
   *
   * @return child expression
   */
  public CsdlExpression getValue() {
    return value;
  }

  public CsdlLabeledElement setValue(CsdlExpression value) {
    this.value = value;
    return this;
  }
  
  @Override
  public boolean equals (Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof CsdlLabeledElement)) {
      return false;
    }
    CsdlLabeledElement csdlLabelledEle = (CsdlLabeledElement) obj;
    return (getName() == null ? csdlLabelledEle.getName() == null :
            getName().equals(csdlLabelledEle.getName()))
        && (getValue() == null ? csdlLabelledEle.getValue() == null :
            getValue().equals(csdlLabelledEle.getValue()))
        && (getAnnotations() == null ? csdlLabelledEle.getAnnotations() == null :
            checkAnnotations(csdlLabelledEle.getAnnotations()));
  }
  
  private boolean checkAnnotations(List<CsdlAnnotation> csdlLabelledEleAnnotations) {
    if (csdlLabelledEleAnnotations == null) {
      return false;
    }
    if (getAnnotations().size() == csdlLabelledEleAnnotations.size()) {
      for (int i = 0; i < getAnnotations().size() ; i++) {
        if (!getAnnotations().get(i).equals(
            csdlLabelledEleAnnotations.get(i))) {
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
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((value == null) ? 0 : value.hashCode());
    result = prime * result + ((annotations == null) ? 0 : annotations.hashCode());
    return result;
  }

}