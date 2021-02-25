/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri;

import nl.buildforce.olingo.commons.api.edm.EdmEntitySet;
import nl.buildforce.olingo.commons.api.edm.EdmEntityType;
import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.server.api.uri.UriResourceEntitySet;
import nl.buildforce.olingo.server.api.uri.UriResourceKind;

public class UriResourceEntitySetImpl extends UriResourceWithKeysImpl implements UriResourceEntitySet {

  private final EdmEntitySet edmEntitySet;

  public UriResourceEntitySetImpl(EdmEntitySet edmEntitySet) {
    super(UriResourceKind.entitySet);
    this.edmEntitySet = edmEntitySet;
  }

  @Override
  public EdmEntitySet getEntitySet() {
    return edmEntitySet;
  }

  @Override
  public EdmEntityType getEntityType() {
    return edmEntitySet.getEntityType();
  }

  @Override
  public EdmType getType() {
    return edmEntitySet.getEntityType();
  }

  @Override
  public boolean isCollection() {
    return keyPredicates == null;
  }

  @Override
  public String getSegmentValue() {
    return edmEntitySet.getName();
  }
}
