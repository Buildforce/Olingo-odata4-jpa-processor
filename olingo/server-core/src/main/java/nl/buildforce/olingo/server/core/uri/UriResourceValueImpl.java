/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri;

import nl.buildforce.olingo.server.api.uri.UriResourceKind;
import nl.buildforce.olingo.server.api.uri.UriResourceValue;

public class UriResourceValueImpl extends UriResourceImpl implements UriResourceValue {

  public UriResourceValueImpl() {
    super(UriResourceKind.value);
  }

  @Override
  public String getSegmentValue() {
    return "$value";
  }

}