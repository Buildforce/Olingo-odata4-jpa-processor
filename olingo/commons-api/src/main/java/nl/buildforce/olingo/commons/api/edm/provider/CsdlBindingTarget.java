/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.provider;

import java.util.ArrayList;
import java.util.List;

import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;

/**
 * The type Csdl binding target.
 */
public abstract class CsdlBindingTarget extends CsdlAbstractEdmItem implements CsdlNamed, CsdlAnnotatable {

  /**
   * The Name.
   */
  protected String name;

  /**
   * The human readable title.
   */
  protected String title;

  /**
   * The Type.
   */
  protected FullQualifiedName type;

  /**
   * The Navigation property bindings.
   */
  protected List<CsdlNavigationPropertyBinding> navigationPropertyBindings =
          new ArrayList<>();

  /**
   * Mapping for server use cases
   */
  protected CsdlMapping mapping;

  private List<CsdlAnnotation> annotations = new ArrayList<>();

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
  public CsdlBindingTarget setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Gets type.
   *
   * @return the type
   */
  public String getType() {
    return type.getFullQualifiedNameAsString();
  }

  /**
   * Gets type fQN.
   *
   * @return the type fQN
   */
  public FullQualifiedName getTypeFQN() {
    return type;
  }

  /**
   * Sets type.
   *
   * @param type the type
   * @return the type
   */
  public CsdlBindingTarget setType(String type) {
    this.type = new FullQualifiedName(type);
    return this;
  }

  /**
   * Sets type.
   *
   * @param type the type
   * @return the type
   */
  public CsdlBindingTarget setType(FullQualifiedName type) {
    this.type = type;
    return this;
  }

  /**
   * Gets navigation property bindings.
   *
   * @return the navigation property bindings
   */
  public List<CsdlNavigationPropertyBinding> getNavigationPropertyBindings() {
    return navigationPropertyBindings;
  }

  /**
   * Sets navigation property bindings.
   *
   * @param navigationPropertyBindings the navigation property bindings
   * @return the navigation property bindings
   */
  public CsdlBindingTarget setNavigationPropertyBindings(
      List<CsdlNavigationPropertyBinding> navigationPropertyBindings) {
    this.navigationPropertyBindings = navigationPropertyBindings;
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
  public CsdlBindingTarget setAnnotations(List<CsdlAnnotation> annotations) {
    this.annotations = annotations;
    return this;
  }

  public String getTitle() {
    return title;
  }

  /**
   * A human readable title for this instance
   * @param title
   * @return this instance
   */
  public CsdlBindingTarget setTitle(String title) {
    this.title = title;
    return this;
  }

  public CsdlMapping getMapping() {
    return mapping;
  }

  /**
   * Sets the mapping object for this binding target.
   * @param mapping
   * @return this instance
   */
  public CsdlBindingTarget setMapping(CsdlMapping mapping) {
    this.mapping = mapping;
    return this;
  }

}