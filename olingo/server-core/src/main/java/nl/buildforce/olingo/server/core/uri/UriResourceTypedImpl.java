/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri;

import nl.buildforce.olingo.commons.api.edm.EdmStructuredType;
import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.server.api.uri.UriResourcePartTyped;
import nl.buildforce.olingo.server.api.uri.UriResourceKind;

public abstract class UriResourceTypedImpl extends UriResourceImpl implements UriResourcePartTyped {

  private EdmType typeFilter;

  public UriResourceTypedImpl(UriResourceKind kind) {
    super(kind);
  }

  public EdmType getTypeFilter() {
    return typeFilter;
  }

  public UriResourceTypedImpl setTypeFilter(EdmStructuredType typeFilter) {
    this.typeFilter = typeFilter;
    return this;
  }

  @Override
  public String getSegmentValue(boolean includeFilters) {
    return getSegmentValue() + (includeFilters && typeFilter != null ?
        "/" + typeFilter.getFullQualifiedName().getFullQualifiedNameAsString() : "");
  }

  @Override
  public String toString(boolean includeFilters) {
    return getSegmentValue(includeFilters);
  }

}