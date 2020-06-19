package nl.buildforce.sequoia.processor.core.util;

import nl.buildforce.olingo.commons.api.edm.EdmProperty;
import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.server.api.uri.UriResourceKind;
import nl.buildforce.olingo.server.api.uri.UriResourceProperty;

import static org.junit.jupiter.api.Assertions.fail;

public class UriResourcePropertyDouble implements UriResourceProperty {
  private final EdmProperty property;

  public UriResourcePropertyDouble(EdmProperty property) {
    this.property = property;
  }

  @Override
  public EdmType getType() {
    fail();
    return null;
  }

  @Override
  public boolean isCollection() {
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
    fail();
    return null;
  }

  @Override
  public String getSegmentValue() {
    fail();
    return null;
  }

  @Override
  public EdmProperty getProperty() {

    return property;
  }

}