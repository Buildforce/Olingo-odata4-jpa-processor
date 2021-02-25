/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri;

import nl.buildforce.olingo.commons.api.edm.EdmComplexType;
import nl.buildforce.olingo.commons.api.edm.EdmProperty;
import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.server.api.uri.UriResourceComplexProperty;
import nl.buildforce.olingo.server.api.uri.UriResourceKind;

public class UriResourceComplexPropertyImpl extends UriResourceTypedImpl implements UriResourceComplexProperty {

  private final EdmProperty property;

  public UriResourceComplexPropertyImpl(EdmProperty property) {
    super(UriResourceKind.complexProperty);
    this.property = property;
  }

  @Override
  public EdmProperty getProperty() {
    return property;
  }

  @Override
  public EdmComplexType getComplexType() {
    return (EdmComplexType) getType();
  }

  @Override
  public EdmComplexType getComplexTypeFilter() {
    return (EdmComplexType) getTypeFilter();
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
