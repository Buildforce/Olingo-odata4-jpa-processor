/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.queryoption.apply;

import java.util.Collections;
import java.util.List;

import nl.buildforce.olingo.commons.api.edm.EdmAnnotation;
import nl.buildforce.olingo.commons.api.edm.EdmMapping;
import nl.buildforce.olingo.commons.api.edm.EdmProperty;
import nl.buildforce.olingo.commons.api.edm.EdmTerm;
import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.commons.api.edm.constants.EdmTypeKind;

/** A dynamic EDM property containing an aggregation. */
public class DynamicProperty implements EdmProperty {

  private final String name;
  private final EdmType propertyType;
  private Integer precision;
  private Integer scale;

  /** Creates a dynamic property with a mandatory name and an optional type. */
  public DynamicProperty(String name, EdmType type) {
    this.name = name;
    propertyType = type;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public EdmType getType() {
    return propertyType;
  }

  @Override
  public boolean isCollection() {
    return false;
  }

  @Override
  public EdmMapping getMapping() {
    return null;
  }

  @Override
  public String getMimeType() {
    return null;
  }

  @Override
  public boolean isNullable() {
    return false;
  }

  @Override
  public Integer getMaxLength() {
    return null;
  }

  @Override
  public Integer getPrecision() {
    return precision;
  }

  @Override
  public Integer getScale() {
    return scale;
  }

  /*@Override
  public SRID getSrid() {
    return null;
  }*/

  @Override
  public boolean isUnicode() {
    return true;
  }

  @Override
  public String getDefaultValue() {
    return null;
  }

  @Override
  public boolean isPrimitive() {
    return propertyType != null && propertyType.getKind() == EdmTypeKind.PRIMITIVE;
  }

  @Override
  public EdmAnnotation getAnnotation(EdmTerm term, String qualifier) {
    return null;
  }

  @Override
  public List<EdmAnnotation> getAnnotations() {
    return Collections.emptyList();
  }
  
/*  @Override
  public EdmType getTypeWithAnnotations() {
    return propertyType;
  }*/
  
  public DynamicProperty setPrecision(Integer precision) {
    this.precision = precision;
    return this;
  }
  
  public DynamicProperty setScale(Integer scale) {
    this.scale = scale;
    return this;
  }
}