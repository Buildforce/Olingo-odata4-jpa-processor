/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.provider;

import java.util.List;

import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;

/**
 * The type Csdl entity type.
 */
public class CsdlEntityType extends CsdlStructuralType {

  private List<CsdlPropertyRef> key;

  private boolean hasStream;

  /**
   * Has stream.
   *
   * @return the boolean
   */
  public boolean hasStream() {
    return hasStream;
  }

  /**
   * Has stream.
   * Duplicate getter according to java naming conventions.
   *
   * @return the boolean
   */
  public boolean isHasStream() {
    return hasStream;
  }

  /**
   * Sets has stream.
   *
   * @param hasStream the has stream
   * @return the has stream
   */
  public CsdlEntityType setHasStream(boolean hasStream) {
    this.hasStream = hasStream;
    return this;
  }

  /**
   * Gets key.
   *
   * @return the key
   */
  public List<CsdlPropertyRef> getKey() {
    return key;
  }

  /**
   * Sets key.
   *
   * @param key the key
   * @return the key
   */
  public CsdlEntityType setKey(List<CsdlPropertyRef> key) {
    this.key = key;
    return this;
  }

  @Override
  public CsdlEntityType setName(String name) {
    this.name = name;
    return this;
  }

  @Override
  public CsdlEntityType setOpenType(boolean isOpenType) {
    this.isOpenType = isOpenType;
    return this;
  }

  @Override
  public CsdlEntityType setBaseType(String baseType) {
    this.baseType = new FullQualifiedName(baseType);
    return this;
  }

  @Override
  public CsdlEntityType setBaseType(FullQualifiedName baseType) {
    this.baseType = baseType;
    return this;
  }

  @Override
  public CsdlEntityType setAbstract(boolean isAbstract) {
    this.isAbstract = isAbstract;
    return this;
  }

  @Override
  public CsdlEntityType setProperties(List<CsdlProperty> properties) {
    this.properties = properties;
    return this;
  }

  @Override
  public CsdlEntityType setNavigationProperties(List<CsdlNavigationProperty> navigationProperties) {
    this.navigationProperties = navigationProperties;
    return this;
  }

  @Override
  public CsdlEntityType setAnnotations(List<CsdlAnnotation> annotations) {
    this.annotations = annotations;
    return this;
  }

}