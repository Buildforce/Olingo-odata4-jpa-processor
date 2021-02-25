/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.processor;

import nl.buildforce.olingo.server.api.OData;
import nl.buildforce.olingo.server.api.ServiceMetadata;

/**
 * <p>Base interface for all processor types.</p>
 * <p>Processors are responsible to read and write data and marshalling content
 * within a request - response cycle.</p>
 */
public interface Processor {

  /**
   * Initializes the processor for each HTTP request - response cycle.
   * @param odata Olingo's root object, acting as a factory for various object types
   * @param serviceMetadata metadata of the OData service like the EDM that have to be created
   * before the OData request handling takes place
   */
  void init(OData odata, ServiceMetadata serviceMetadata);
}
