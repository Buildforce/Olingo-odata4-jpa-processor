/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri;

import nl.buildforce.olingo.server.api.uri.UriResourceKind;
import nl.buildforce.olingo.server.api.uri.UriResourceRef;

public class UriResourceRefImpl extends UriResourceImpl implements UriResourceRef {

  public UriResourceRefImpl() {
    super(UriResourceKind.ref);
  }

  @Override
  public String getSegmentValue() {
    return "$ref";
  }
}
