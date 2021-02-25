/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri;

import nl.buildforce.olingo.commons.api.edm.EdmNavigationProperty;
import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.server.api.uri.UriResourceNavigation;
import nl.buildforce.olingo.server.api.uri.UriResourceKind;

public class UriResourceNavigationPropertyImpl extends UriResourceWithKeysImpl implements UriResourceNavigation {

  private final EdmNavigationProperty navigationProperty;

  public UriResourceNavigationPropertyImpl(EdmNavigationProperty property) {
    super(UriResourceKind.navigationProperty);
    navigationProperty = property;
  }

  @Override
  public EdmNavigationProperty getProperty() {
    return navigationProperty;
  }

  @Override
  public EdmType getType() {
    return navigationProperty.getType();
  }

  @Override
  public boolean isCollection() {
    return navigationProperty.isCollection() && keyPredicates == null;
  }

  @Override
  public String getSegmentValue() {
    return navigationProperty.getName();
  }
}
