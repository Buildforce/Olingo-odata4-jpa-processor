/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core;

import nl.buildforce.olingo.server.api.ODataRequest;
import nl.buildforce.olingo.server.api.ODataResponse;
import nl.buildforce.olingo.server.api.processor.Processor;

public interface RedirectProcessor extends Processor {

  void redirect(ODataRequest request, ODataResponse response);

}