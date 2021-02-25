/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri;

import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.server.api.uri.UriResourceKind;
import nl.buildforce.olingo.server.api.uri.UriResourceRoot;

public class UriResourceRootImpl extends UriResourceWithKeysImpl implements UriResourceRoot {

  private final EdmType type;
  private final boolean isCollection;

  public UriResourceRootImpl(EdmType type, boolean isCollection) {
    super(UriResourceKind.root);
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
    return "$root";
  }
}
