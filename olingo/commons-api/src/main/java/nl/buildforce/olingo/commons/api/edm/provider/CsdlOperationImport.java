/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.provider;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Csdl operation import.
 */
public abstract class CsdlOperationImport extends CsdlAbstractEdmItem implements CsdlNamed, CsdlAnnotatable {

  /**
   * The Name.
   */
  protected String name;
  /**
   * The Entity set.
   */
  protected String entitySet;
  /**
   * The Annotations.
   */
  protected List<CsdlAnnotation> annotations = new ArrayList<>();

  @Override
  public String getName() {
    return name;
  }

  /**
   * Sets name.
   *
   * @param name the name
   * @return the name
   */
  public CsdlOperationImport setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Gets entity set.
   *
   * @return the entity set
   */
  public String getEntitySet() {
    return entitySet;
  }

  /**
   * Sets entity set.
   *
   * @param entitySet the entity set
   * @return the entity set
   */
  public CsdlOperationImport setEntitySet(String entitySet) {
    this.entitySet = entitySet;
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
  public CsdlOperationImport setAnnotations(List<CsdlAnnotation> annotations) {
    this.annotations = annotations;
    return this;
  }

}