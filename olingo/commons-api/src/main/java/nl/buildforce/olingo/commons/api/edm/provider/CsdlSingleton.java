/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.provider;

import java.util.List;

import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;

/**
 * The type Csdl singleton.
 */
public class CsdlSingleton extends CsdlBindingTarget {

  @Override
  public CsdlSingleton setName(String name) {
    this.name = name;
    return this;
  }

  @Override
  public CsdlSingleton setType(String type) {
    this.type = new FullQualifiedName(type);
    return this;
  }

  @Override
  public CsdlSingleton setType(FullQualifiedName type) {
    this.type = type;
    return this;
  }

  @Override
  public CsdlSingleton setNavigationPropertyBindings(
      List<CsdlNavigationPropertyBinding> navigationPropertyBindings) {
    this.navigationPropertyBindings = navigationPropertyBindings;
    return this;
  }

  @Override
  public CsdlSingleton setAnnotations(List<CsdlAnnotation> annotations) {
    super.setAnnotations(annotations);
    return this;
  }

  @Override
  public CsdlSingleton setTitle(String title) {
    super.setTitle(title);
    return this;
  }

  @Override
  public CsdlSingleton setMapping(CsdlMapping mapping) {
    this.mapping = mapping;
    return this;
  }

}