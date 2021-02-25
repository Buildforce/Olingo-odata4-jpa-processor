/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.provider;

import java.util.List;

import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;

/**
 * The type Csdl complex type.
 */
public class CsdlComplexType extends CsdlStructuralType {

  @Override
  public CsdlComplexType setName(String name) {
    this.name = name;
    return this;
  }

  @Override
  public CsdlComplexType setOpenType(boolean isOpenType) {
    this.isOpenType = isOpenType;
    return this;
  }

  @Override
  public CsdlComplexType setBaseType(String baseType) {
    this.baseType = new FullQualifiedName(baseType);
    return this;
  }

  @Override
  public CsdlComplexType setBaseType(FullQualifiedName baseType) {
    this.baseType = baseType;
    return this;
  }

  @Override
  public CsdlComplexType setAbstract(boolean isAbstract) {
    this.isAbstract = isAbstract;
    return this;
  }

  @Override
  public CsdlComplexType setProperties(List<CsdlProperty> properties) {
    this.properties = properties;
    return this;
  }

  @Override
  public CsdlComplexType setNavigationProperties(List<CsdlNavigationProperty> navigationProperties) {
    this.navigationProperties = navigationProperties;
    return this;
  }

  @Override
  public CsdlComplexType setAnnotations(List<CsdlAnnotation> annotations) {
    this.annotations = annotations;
    return this;
  }

}