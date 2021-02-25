/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri;

import nl.buildforce.olingo.server.api.uri.UriResourceCount;
import nl.buildforce.olingo.server.api.uri.UriResourceKind;

public class UriResourceCountImpl extends UriResourceImpl implements UriResourceCount {

  public UriResourceCountImpl() {
    super(UriResourceKind.count);
  }

  @Override
  public String getSegmentValue() {
    return "$count";
  }
}
