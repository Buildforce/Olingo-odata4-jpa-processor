/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.provider;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Csdl referential constraint.
 */
public class CsdlReferentialConstraint extends CsdlAbstractEdmItem implements CsdlAnnotatable {

  private String property;

  private String referencedProperty;

  private List<CsdlAnnotation> annotations = new ArrayList<>();

  /**
   * Gets property.
   *
   * @return the property
   */
  public String getProperty() {
    return property;
  }

  /**
   * Sets property.
   *
   * @param property the property
   * @return the property
   */
  public CsdlReferentialConstraint setProperty(String property) {
    this.property = property;
    return this;
  }

  /**
   * Gets referenced property.
   *
   * @return the referenced property
   */
  public String getReferencedProperty() {
    return referencedProperty;
  }

  /**
   * Sets referenced property.
   *
   * @param referencedProperty the referenced property
   * @return the referenced property
   */
  public CsdlReferentialConstraint setReferencedProperty(String referencedProperty) {
    this.referencedProperty = referencedProperty;
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
  public CsdlReferentialConstraint setAnnotations(List<CsdlAnnotation> annotations) {
    this.annotations = annotations;
    return this;
  }

}