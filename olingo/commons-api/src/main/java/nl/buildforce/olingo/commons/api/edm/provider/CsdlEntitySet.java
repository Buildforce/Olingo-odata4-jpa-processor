/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.provider;

import java.util.List;

import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;

/**
 * The type Csdl entity set.
 */
public class CsdlEntitySet extends CsdlBindingTarget {

  // Default for EntitySets is true
  private boolean includeInServiceDocument = true;

  @Override
  public CsdlEntitySet setName(String name) {
    this.name = name;
    return this;
  }

  @Override
  public CsdlEntitySet setNavigationPropertyBindings(
      List<CsdlNavigationPropertyBinding> navigationPropertyBindings) {
    this.navigationPropertyBindings = navigationPropertyBindings;
    return this;
  }

  @Override
  public CsdlEntitySet setAnnotations(List<CsdlAnnotation> annotations) {
    super.setAnnotations(annotations);
    return this;
  }


  @Override
  public CsdlEntitySet setType(String type) {
    this.type = new FullQualifiedName(type);
    return this;
  }

  @Override
  public CsdlEntitySet setType(FullQualifiedName type) {
    this.type = type;
    return this;
  }

  /**
   * Is include in service document.
   *
   * @return the boolean
   */
  public boolean isIncludeInServiceDocument() {
    return includeInServiceDocument;
  }

  /**
   * Sets include in service document.
   *
   * @param includeInServiceDocument the include in service document
   * @return the include in service document
   */
  public CsdlEntitySet setIncludeInServiceDocument(boolean includeInServiceDocument) {
    this.includeInServiceDocument = includeInServiceDocument;
    return this;
  }
  
  @Override
  public CsdlEntitySet setTitle(String title) {
    super.setTitle(title);
    return this;
  }
  
  @Override
  public CsdlEntitySet setMapping(CsdlMapping mapping) {
    this.mapping = mapping;
    return this;
  }

}