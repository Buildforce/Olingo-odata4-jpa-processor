/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core;

import nl.buildforce.olingo.commons.api.http.HttpHeader;
import nl.buildforce.olingo.commons.api.http.HttpStatusCode;
import nl.buildforce.olingo.server.api.OData;
import nl.buildforce.olingo.server.api.ODataRequest;
import nl.buildforce.olingo.server.api.ODataResponse;
import nl.buildforce.olingo.server.api.ServiceMetadata;

public class DefaultRedirectProcessor implements RedirectProcessor {

  @Override
  public void init(OData odata, ServiceMetadata serviceMetadata) {
    // No init needed
  }

  @Override
  public void redirect(ODataRequest request, ODataResponse response) {
    response.setStatusCode(HttpStatusCode.TEMPORARY_REDIRECT.getStatusCode());

    String location;

    String rawUri = request.getRawRequestUri();
    String rawQueryPath = request.getRawQueryPath();
    if (rawQueryPath == null) {
      location = request.getRawRequestUri() + "/";
    } else {
      location = rawUri.substring(0, rawUri.indexOf(rawQueryPath) - 1) + "/?" + rawQueryPath;
    }

    response.setHeader(HttpHeader.LOCATION, location);
  }
}
