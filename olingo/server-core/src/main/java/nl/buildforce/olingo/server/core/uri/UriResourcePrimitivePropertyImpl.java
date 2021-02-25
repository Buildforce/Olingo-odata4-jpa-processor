/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri;

import nl.buildforce.olingo.commons.api.edm.EdmProperty;
import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.server.api.uri.UriResourceKind;
import nl.buildforce.olingo.server.api.uri.UriResourcePrimitiveProperty;

public class UriResourcePrimitivePropertyImpl extends UriResourceTypedImpl implements UriResourcePrimitiveProperty {

  private final EdmProperty property;

  public UriResourcePrimitivePropertyImpl(EdmProperty property) {
    super(UriResourceKind.primitiveProperty);
    this.property = property;
  }

  @Override
  public EdmProperty getProperty() {
    return property;
  }

  @Override
  public EdmType getType() {
    return property.getType();
  }

  @Override
  public boolean isCollection() {
    return property.isCollection();
  }

  @Override
  public String getSegmentValue() {
    return property.getName();
  }
}
