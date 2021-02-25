/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri;

import nl.buildforce.olingo.server.api.uri.UriResource;
import nl.buildforce.olingo.server.api.uri.UriResourceKind;

/**
 * Abstract class for resource-path elements in URI.
 */
public abstract class UriResourceImpl implements UriResource {
  private final UriResourceKind kind;

  public UriResourceImpl(UriResourceKind kind) {
    this.kind = kind;
  }

  @Override
  public UriResourceKind getKind() {
    return kind;
  }

  @Override
  public String toString() {
    return getSegmentValue();
  }
}
