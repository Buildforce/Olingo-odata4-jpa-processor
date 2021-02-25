/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmException;
import nl.buildforce.olingo.commons.api.edm.EdmMapping;
import nl.buildforce.olingo.commons.api.edm.EdmProperty;
import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlProperty;

public class EdmPropertyImpl extends AbstractEdmNamed implements EdmProperty {

  private final CsdlProperty property;
  private EdmTypeInfo typeInfo;
  private EdmType propertyType;

  public EdmPropertyImpl(Edm edm, CsdlProperty property) {
    super(edm, property.getName(), property);

    this.property = property;

  }

  @Override
  public EdmType getType() {
    if (propertyType == null) {
      if (typeInfo == null) {
        buildTypeInfo();
      }
      propertyType = typeInfo.getType();
      if (propertyType == null) {
        throw new EdmException("Cannot find type with name: " + typeInfo.getFullQualifiedName());
      }
    }

    return propertyType;
  }

  /*@Override
  public EdmType getTypeWithAnnotations() {
    if (propertyType == null) {
      if (typeInfo == null) {
        buildTypeInfoWithAnnotations();
      }
      propertyType = typeInfo.getType();
      if (propertyType == null) {
        throw new EdmException("Cannot find type with name: " + typeInfo.getFullQualifiedName());
      }
    }

    return propertyType;
  }*/
  
  private void buildTypeInfo() {
    if (property.getType() == null) {
      throw new EdmException("Property " + property.getName() + " must hava a full qualified type.");
    }
    typeInfo = new EdmTypeInfo.Builder().setEdm(edm).setTypeExpression(property.getType()).build();
  }

/*
  private void buildTypeInfoWithAnnotations() {
    if (property.getType() == null) {
      throw new EdmException("Property " + property.getName() + " must hava a full qualified type.");
    }
    typeInfo = new EdmTypeInfo.Builder().setEdm(edm).setIncludeAnnotations(true)
        .setTypeExpression(property.getType()).build();
  }
*/

  @Override
  public boolean isCollection() {
    return property.isCollection();
  }

  @Override
  public EdmMapping getMapping() {
    return property.getMapping();
  }

  @Override
  public String getMimeType() {
    return property.getMimeType();
  }

  @Override
  public boolean isNullable() {
    return property.isNullable();
  }

  @Override
  public Integer getMaxLength() {
    return property.getMaxLength();
  }

  @Override
  public Integer getPrecision() {
    return property.getPrecision();
  }

  @Override
  public Integer getScale() {
    return property.getScale();
  }

  /*
  @Override
  public SRID getSrid() {
    return property.getSrid();
  }*/

  @Override
  public boolean isUnicode() {
    return property.isUnicode();
  }

  @Override
  public String getDefaultValue() {
    return property.getDefaultValue();
  }

  @Override
  public boolean isPrimitive() {
    if (typeInfo == null) {
      buildTypeInfo();
    }
    return typeInfo.isPrimitiveType();
  }
}