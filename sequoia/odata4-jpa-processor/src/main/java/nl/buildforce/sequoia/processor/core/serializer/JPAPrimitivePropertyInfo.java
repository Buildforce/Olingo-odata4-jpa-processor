package nl.buildforce.sequoia.processor.core.serializer;

import nl.buildforce.olingo.commons.api.data.Property;

class JPAPrimitivePropertyInfo {
  private final String path;
  private final Property property;

  public JPAPrimitivePropertyInfo(final String path, final Property property) {
    this.path = path;
    this.property = property;
  }

  String getPath() {
    return path;
  }

  Property getProperty() {
    return property;
  }
}