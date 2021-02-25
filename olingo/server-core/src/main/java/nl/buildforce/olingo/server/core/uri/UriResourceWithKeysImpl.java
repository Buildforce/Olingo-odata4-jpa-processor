/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri;

import java.util.Collections;
import java.util.List;

import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.server.api.uri.UriResourcePartTyped;
import nl.buildforce.olingo.server.api.uri.UriParameter;
import nl.buildforce.olingo.server.api.uri.UriResourceKind;

public abstract class UriResourceWithKeysImpl extends UriResourceImpl implements UriResourcePartTyped {

  private EdmType collectionTypeFilter;
  protected List<UriParameter> keyPredicates;
  private EdmType entryTypeFilter;

  public UriResourceWithKeysImpl(UriResourceKind kind) {
    super(kind);
  }

  public EdmType getTypeFilterOnCollection() {
    return collectionTypeFilter;
  }

  public EdmType getTypeFilterOnEntry() {
    return entryTypeFilter;
  }

  public List<UriParameter> getKeyPredicates() {
    return keyPredicates == null ?
        Collections.emptyList() :
        Collections.unmodifiableList(keyPredicates);
  }

  public UriResourceWithKeysImpl setKeyPredicates(List<UriParameter> list) {
    keyPredicates = list;
    return this;
  }

  public UriResourceWithKeysImpl setEntryTypeFilter(EdmType entryTypeFilter) {
    this.entryTypeFilter = entryTypeFilter;
    return this;
  }

  public UriResourceWithKeysImpl setCollectionTypeFilter(EdmType collectionTypeFilter) {
    this.collectionTypeFilter = collectionTypeFilter;
    return this;
  }

  @Override
  public String getSegmentValue(boolean includeFilters) {
    if (includeFilters) {
      StringBuilder tmp = new StringBuilder();
      if (collectionTypeFilter != null) {
        tmp.append(getFQN(collectionTypeFilter));
      }

      if (entryTypeFilter != null) {
        if (tmp.length() == 0) {
          tmp.append(getFQN(entryTypeFilter));
        } else {
          tmp.append("/()").append(getFQN(entryTypeFilter));
        }
      }

      if (tmp.length() != 0) {
        return getSegmentValue() + "/" + tmp;
      }
    }

    return getSegmentValue();
  }

  @Override
  public String toString(boolean includeFilters) {
    return getSegmentValue(includeFilters);
  }

  private String getFQN(EdmType type) {
    return type.getFullQualifiedName().getFullQualifiedNameAsString();
  }

}