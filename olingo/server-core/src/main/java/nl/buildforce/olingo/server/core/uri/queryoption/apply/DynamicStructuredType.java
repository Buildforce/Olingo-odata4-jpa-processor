/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.queryoption.apply;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import nl.buildforce.olingo.commons.api.edm.EdmAnnotation;
import nl.buildforce.olingo.commons.api.edm.EdmElement;
import nl.buildforce.olingo.commons.api.edm.EdmNavigationProperty;
import nl.buildforce.olingo.commons.api.edm.EdmProperty;
import nl.buildforce.olingo.commons.api.edm.EdmStructuredType;
import nl.buildforce.olingo.commons.api.edm.EdmTerm;
import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;
import nl.buildforce.olingo.commons.api.edm.constants.EdmTypeKind;

/** A dynamic structured type used to incorporate dynamic properties containing aggregations. */
public class DynamicStructuredType implements EdmStructuredType, Cloneable {

  private final EdmStructuredType startType;
  private Map<String, EdmProperty> properties;

  public DynamicStructuredType(EdmStructuredType startType) {
    this.startType = startType;
  }

  public DynamicStructuredType addProperty(EdmProperty property) {
    if (properties == null) {
      properties = new LinkedHashMap<>();
    }
    properties.put(property.getName(), property);
    return this;
  }

  @Override
  public EdmElement getProperty(String name) {
    EdmElement property = startType.getProperty(name);
    return property == null ?
        properties == null ? null : properties.get(name) :
        property;
  }

  @Override
  public List<String> getPropertyNames() {
    if (properties == null || properties.isEmpty()) {
      return startType.getPropertyNames();
    } else {
      List<String> names = new ArrayList<>(startType.getPropertyNames());
      names.addAll(properties.keySet());
      return Collections.unmodifiableList(names);
    }
  }

  @Override
  public EdmProperty getStructuralProperty(String name) {
    EdmProperty property = startType.getStructuralProperty(name);
    return property == null ?
        properties == null ? null : properties.get(name) :
        property;
  }

  @Override
  public EdmNavigationProperty getNavigationProperty(String name) {
    return startType.getNavigationProperty(name);
  }

  @Override
  public List<String> getNavigationPropertyNames() {
    return startType.getNavigationPropertyNames();
  }

  @Override
  public String getNamespace() {
    return startType.getNamespace();
  }

  @Override
  public String getName() {
    return startType.getName();
  }

  @Override
  public FullQualifiedName getFullQualifiedName() {
    return startType.getFullQualifiedName();
  }

  @Override
  public EdmTypeKind getKind() {
    return startType.getKind();
  }

  @Override
  public EdmAnnotation getAnnotation(EdmTerm term, String qualifier) {
    return startType.getAnnotation(term, qualifier);
  }

  @Override
  public List<EdmAnnotation> getAnnotations() {
    return startType.getAnnotations();
  }

  @Override
  public EdmStructuredType getBaseType() {
    return startType.getBaseType();
  }

  @Override
  public boolean compatibleTo(EdmType targetType) {
    return startType.compatibleTo(targetType);
  }

  @Override
  public boolean isOpenType() {
    return startType.isOpenType();
  }

  @Override
  public boolean isAbstract() {
    return startType.isAbstract();
  }
}
