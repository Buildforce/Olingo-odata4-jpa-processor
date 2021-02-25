/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.provider;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Csdl on delete.
 */
public class CsdlOnDelete extends CsdlAbstractEdmItem implements CsdlAnnotatable {

  private CsdlOnDeleteAction action = CsdlOnDeleteAction.None;
  
  private List<CsdlAnnotation> annotations = new ArrayList<>();
  /**
   * Gets action.
   *
   * @return the action
   */
  public CsdlOnDeleteAction getAction() {
    return action;
  }

  /**
   * Sets action.
   *
   * @param action the action
   * @return the action
   */
  public CsdlOnDelete setAction(CsdlOnDeleteAction action) {
    this.action = action;
    return this;
  }

  @Override
  public List<CsdlAnnotation> getAnnotations() {
    return annotations;
  }

  /**
   * Sets annotations.
   *
   * @param annotations the annotations
   * @return the annotations
   */
  public CsdlOnDelete setAnnotations(List<CsdlAnnotation> annotations) {
    this.annotations = annotations;
    return this;
  }

}