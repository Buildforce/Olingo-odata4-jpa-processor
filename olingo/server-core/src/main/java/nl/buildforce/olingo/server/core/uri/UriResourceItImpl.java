/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri;

import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.server.api.uri.UriResourceIt;
import nl.buildforce.olingo.server.api.uri.UriResourceKind;

/**
 * Covers Function imports and BoundFunction in URI
 */
public class UriResourceItImpl extends UriResourceWithKeysImpl implements UriResourceIt {

  private final EdmType type;
  private final boolean isCollection;

  public UriResourceItImpl(EdmType type, boolean isCollection) {
    super(UriResourceKind.it);
    this.type = type;
    this.isCollection = isCollection;
  }

  @Override
  public EdmType getType() {
    return type;
  }

  @Override
  public boolean isCollection() {
    return keyPredicates == null && isCollection;
  }

  @Override
  public String getSegmentValue() {
    return "$it";
  }

}