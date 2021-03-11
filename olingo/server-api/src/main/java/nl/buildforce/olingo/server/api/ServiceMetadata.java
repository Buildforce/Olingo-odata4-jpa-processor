/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api;

import java.util.List;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edmx.EdmxReference;
import nl.buildforce.olingo.server.api.etag.ServiceMetadataETagSupport;

/**
 * Metadata of an OData service like the Entity Data Model.
 */
public interface ServiceMetadata {
  /**
   * Gets the entity data model.
   * @return entity data model of this service
   */
  Edm getEdm();

// --Commented out by Inspection START (''21-03-09 23:53):
//  /**
//   * Get the data-service version.
//   * @return data service version of this service
//   */
//  ODataServiceVersion getDataServiceVersion();
// --Commented out by Inspection STOP (''21-03-09 23:53)

  /**
   * Gets the list of references defined for this service.
   * @return list of defined emdx references of this service
   */
  List<EdmxReference> getReferences();

  /**
   * Gets the helper for ETag support of the metadata document (may be NULL).
   * @return metadata ETag support
   */
  ServiceMetadataETagSupport getServiceMetadataETagSupport();

}