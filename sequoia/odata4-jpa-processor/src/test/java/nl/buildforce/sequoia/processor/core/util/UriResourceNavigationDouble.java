package nl.buildforce.sequoia.processor.core.util;

import nl.buildforce.olingo.commons.api.edm.EdmEntityType;
import nl.buildforce.olingo.commons.api.edm.EdmNavigationProperty;
import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.server.api.uri.UriParameter;
import nl.buildforce.olingo.server.api.uri.UriResourceKind;
import nl.buildforce.olingo.server.api.uri.UriResourceNavigation;

import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

public class UriResourceNavigationDouble implements UriResourceNavigation {
  private final EdmType type;
  private final EdmNavigationProperty property;

  public UriResourceNavigationDouble(EdmEntityType naviTargetEntity) {
    this(naviTargetEntity, null);
  }

  public UriResourceNavigationDouble(EdmType type, EdmNavigationProperty property) {
    this.type = type;
    this.property = property;
  }

  @Override
  public EdmType getType() {
    return type;
  }

  @Override
  public boolean isCollection() {
    fail();
    return false;
  }

  @Override
  public String getSegmentValue(boolean includeFilters) {
    fail();
    return null;
  }

  @Override
  public String toString(boolean includeFilters) {
    fail();
    return null;
  }

  @Override
  public UriResourceKind getKind() {
    return UriResourceKind.navigationProperty;
  }

  @Override
  public String getSegmentValue() {
    fail();
    return null;
  }

  @Override
  public EdmNavigationProperty getProperty() {
    return property;
  }

  @Override
  public List<UriParameter> getKeyPredicates() {
    fail();
    return null;
  }

  @Override
  public EdmType getTypeFilterOnCollection() {
    fail();
    return null;
  }

  @Override
  public EdmType getTypeFilterOnEntry() {
    fail();
    return null;
  }

}