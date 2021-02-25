/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmElement;
import nl.buildforce.olingo.commons.api.edm.EdmException;
import nl.buildforce.olingo.commons.api.edm.EdmNavigationProperty;
import nl.buildforce.olingo.commons.api.edm.EdmProperty;
import nl.buildforce.olingo.commons.api.edm.EdmStructuredType;
import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;
import nl.buildforce.olingo.commons.api.edm.constants.EdmTypeKind;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlNavigationProperty;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlProperty;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlStructuralType;

public abstract class AbstractEdmStructuredType extends EdmTypeImpl implements EdmStructuredType {

  protected EdmStructuredType baseType;
  protected FullQualifiedName baseTypeName;

  private final CsdlStructuralType providerStructuredType;

  private List<String> propertyNames;
  private Map<String, EdmProperty> properties;
  private List<String> navigationPropertyNames;
  private Map<String, EdmNavigationProperty> navigationProperties;

  public AbstractEdmStructuredType(
      Edm edm,
      FullQualifiedName typeName,
      EdmTypeKind kind,
      CsdlStructuralType structuredType) {

    super(edm, typeName, kind, structuredType);
    baseTypeName = structuredType.getBaseTypeFQN();
    providerStructuredType = structuredType;
  }

  protected abstract EdmStructuredType buildBaseType(FullQualifiedName baseTypeName);

  protected abstract void checkBaseType();

  @Override
  public List<String> getPropertyNames() {
    if (propertyNames == null) {
      List<String> localPropertyNames = new ArrayList<>();
      checkBaseType();
      if (baseType != null) {
        localPropertyNames.addAll(baseType.getPropertyNames());
      }
      localPropertyNames.addAll(getProperties().keySet());
      propertyNames = Collections.unmodifiableList(localPropertyNames);
    }
    return propertyNames;
  }

  @Override
  public List<String> getNavigationPropertyNames() {
    if (navigationPropertyNames == null) {
      ArrayList<String> localNavigatinPropertyNames = new ArrayList<>();
      checkBaseType();
      if (baseType != null) {
        localNavigatinPropertyNames.addAll(baseType.getNavigationPropertyNames());
      }
      localNavigatinPropertyNames.addAll(getNavigationProperties().keySet());
      navigationPropertyNames = Collections.unmodifiableList(localNavigatinPropertyNames);
    }
    return navigationPropertyNames;
  }

  @Override
  public EdmElement getProperty(String name) {
    EdmElement property = getStructuralProperty(name);
    if (property == null) {
      property = getNavigationProperty(name);
    }
    return property;
  }

  @Override
  public EdmProperty getStructuralProperty(String name) {
    EdmProperty property = null;
    checkBaseType();
    if (baseType != null) {
      property = baseType.getStructuralProperty(name);
    }
    if (property == null) {
      property = getProperties().get(name);
    }
    return property;
  }

  @Override
  public EdmNavigationProperty getNavigationProperty(String name) {
    EdmNavigationProperty property = null;
    checkBaseType();
    if (baseType != null) {
      property = baseType.getNavigationProperty(name);
    }
    if (property == null) {
      property = getNavigationProperties().get(name);
    }
    return property;
  }

  @Override
  public boolean compatibleTo(EdmType targetType) {
    EdmStructuredType sourceType = this;
    if (targetType == null) {
      throw new EdmException("Target type must not be null");
    }
    while (!sourceType.getName().equals(targetType.getName())
        || !sourceType.getNamespace().equals(targetType.getNamespace())) {

      sourceType = sourceType.getBaseType();
      if (sourceType == null) {
        return false;
      }
    }

    return true;
  }

  public Map<String, EdmProperty> getProperties() {
    if (properties == null) {
      Map<String, EdmProperty> localPorperties = new LinkedHashMap<>();
      List<CsdlProperty> structureTypeProperties = providerStructuredType.getProperties();
      for (CsdlProperty property : structureTypeProperties) {
        localPorperties.put(property.getName(), new EdmPropertyImpl(edm, property));
      }
      properties = Collections.unmodifiableMap(localPorperties);
    }
    return properties;
  }

  public Map<String, EdmNavigationProperty> getNavigationProperties() {
    if (navigationProperties == null) {
      Map<String, EdmNavigationProperty> localNavigationProperties =
              new LinkedHashMap<>();
      List<CsdlNavigationProperty> structuredTypeNavigationProperties =
          providerStructuredType.getNavigationProperties();

      if (structuredTypeNavigationProperties != null) {
        for (CsdlNavigationProperty navigationProperty : structuredTypeNavigationProperties) {
          localNavigationProperties.put(navigationProperty.getName(),
              new EdmNavigationPropertyImpl(edm, navigationProperty));
        }
      }

      navigationProperties = Collections.unmodifiableMap(localNavigationProperties);
    }
    return navigationProperties;
  }

  @Override
  public boolean isOpenType() {
    return providerStructuredType.isOpenType();
  }

  @Override
  public boolean isAbstract() {
    return providerStructuredType.isAbstract();
  }
}
