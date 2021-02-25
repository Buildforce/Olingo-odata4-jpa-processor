/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.provider.annotation;

import java.util.ArrayList;
import java.util.List;

import nl.buildforce.olingo.commons.api.edm.provider.CsdlAnnotatable;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlAnnotation;

/**
 * Represents a edm:If expression
 */
public class CsdlIf extends CsdlDynamicExpression implements CsdlAnnotatable {

  private CsdlExpression guard;
  private CsdlExpression _then;
  private CsdlExpression _else;
  private List<CsdlAnnotation> annotations = new ArrayList<>();

  @Override
  public List<CsdlAnnotation> getAnnotations() {
    return annotations;
  }
  
  public CsdlIf setAnnotations(List<CsdlAnnotation> annotations) {
    this.annotations = annotations;
    return this;
  }

  /**
   * Returns the first expression of the edm:If expression.
   * This expression represents the condition of the if expression
   *
   * @return First expression of the if expression
   */
  public CsdlExpression getGuard() {
    return guard;
  }

  public CsdlIf setGuard(CsdlExpression guard) {
    this.guard = guard;
    return this;
  }

  /**
   * Return the second expression of the edm:If expression.
   * If the condition of the condition is evaluated to true,
   * this expression as to be executed.
   *
   * @return Second Expression of the edm:If expression
   */
  public CsdlExpression getThen() {
    return _then;
  }

  public CsdlIf setThen(CsdlExpression _then) {
    this._then = _then;
    return this;
  }

  /**
   * Return the third expression of the edm:If expression.
   * If the condition of the condition is evaluated to false,
   * this expression as to be executed.
   *
   * @return Third Expression of the edm:If expression
   */
  public CsdlExpression getElse() {
    return _else;
  }

  public CsdlIf setElse(CsdlExpression _else) {
    this._else = _else;
    return this;
  }
  
  @Override
  public boolean equals (Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof CsdlIf)) {
      return false;
    }
    CsdlIf csdlIf = (CsdlIf) obj;
    return (getGuard() == null ? csdlIf.getGuard() == null :
            getGuard().equals(csdlIf.getGuard()))
        && (getThen() == null ? csdlIf.getThen() == null :
            getThen().equals(csdlIf.getThen()))
        && (getElse() == null ? csdlIf.getElse() == null :
            getElse().equals(csdlIf.getElse()))
        && (getAnnotations() == null ? csdlIf.getAnnotations() == null :
            checkAnnotations(csdlIf.getAnnotations()));
  }
  
  private boolean checkAnnotations(List<CsdlAnnotation> csdlIfAnnotations) {
    if (csdlIfAnnotations == null) {
      return false;
    }
    if (getAnnotations().size() == csdlIfAnnotations.size()) {
      for (int i = 0; i < getAnnotations().size(); i++) {
        if (!getAnnotations().get(i).equals(csdlIfAnnotations.get(i))) {
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
    result = prime * result + ((guard == null) ? 0 : guard.hashCode());
    result = prime * result + ((_then == null) ? 0 : _then.hashCode());
    result = prime * result + ((_else == null) ? 0 : _else.hashCode());
    result = prime * result + ((annotations == null) ? 0 : annotations.hashCode());
    return result;
  }

}