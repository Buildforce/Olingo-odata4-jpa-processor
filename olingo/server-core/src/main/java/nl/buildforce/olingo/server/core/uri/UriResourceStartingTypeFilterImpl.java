/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri;

import nl.buildforce.olingo.commons.api.edm.EdmType;

public class UriResourceStartingTypeFilterImpl extends UriResourceWithKeysImpl {

  private final EdmType type;
  private final boolean isCollection;

  public UriResourceStartingTypeFilterImpl(EdmType type, boolean isCollection) {
    super(null);
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
    return type.getFullQualifiedName().getFullQualifiedNameAsString();
  }

}