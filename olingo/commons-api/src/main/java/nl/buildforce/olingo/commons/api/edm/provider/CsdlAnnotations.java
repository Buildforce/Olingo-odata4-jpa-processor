/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.provider;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Csdl annotations.
 */
public class CsdlAnnotations extends CsdlAbstractEdmItem implements CsdlAnnotatable {

  private List<CsdlAnnotation> annotations = new ArrayList<>();

  private String target;

  private String qualifier;

  /**
   * Gets target.
   *
   * @return the target
   */
  public String getTarget() {
    return target;
  }

  /**
   * Sets target.
   *
   * @param target the target
   * @return the target
   */
  public CsdlAnnotations setTarget(String target) {
    this.target = target;
    return this;
  }

  /**
   * Gets qualifier.
   *
   * @return the qualifier
   */
  public String getQualifier() {
    return qualifier;
  }

  /**
   * Sets qualifier.
   *
   * @param qualifier the qualifier
   * @return the qualifier
   */
  public CsdlAnnotations setQualifier(String qualifier) {
    this.qualifier = qualifier;
    return this;
  }

  @Override
  public List<CsdlAnnotation> getAnnotations() {
    return annotations;
  }

  /**
   * Sets a list of annotations
   * @param annotations list of annotations
   * @return this instance
   */
  public CsdlAnnotations setAnnotations(List<CsdlAnnotation> annotations) {
    this.annotations = annotations;
    return this;
  }
  
  /**
   * Gets annotation.
   *
   * @param term the term
   * @return the annotation
   */
  public CsdlAnnotation getAnnotation(String term) {
    CsdlAnnotation result = null;
    for (CsdlAnnotation annotation : getAnnotations()) {
      if (term.equals(annotation.getTerm())) {
        result = annotation;
      }
    }
    return result;
  }

}