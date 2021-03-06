/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri;

import nl.buildforce.olingo.commons.api.edm.EdmEntityType;
import nl.buildforce.olingo.commons.api.edm.EdmSingleton;
import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.server.api.uri.UriResourceSingleton;
import nl.buildforce.olingo.server.api.uri.UriResourceKind;

public class UriResourceSingletonImpl extends UriResourceTypedImpl implements UriResourceSingleton {

  private final EdmSingleton singleton;

  public UriResourceSingletonImpl(EdmSingleton singleton) {
    super(UriResourceKind.singleton);
    this.singleton = singleton;
  }

  @Override
  public EdmSingleton getSingleton() {
    return singleton;
  }

/*
  @Override
  public EdmEntityType getEntityTypeFilter() {
    return (EdmEntityType) getTypeFilter();
  }
*/

  @Override
  public EdmType getType() {
    return singleton.getEntityType();
  }

  @Override
  public EdmEntityType getEntityType() {
    return singleton.getEntityType();
  }

  @Override
  public boolean isCollection() {
    return false;
  }

  @Override
  public String getSegmentValue() {
    return singleton.getName();
  }

}