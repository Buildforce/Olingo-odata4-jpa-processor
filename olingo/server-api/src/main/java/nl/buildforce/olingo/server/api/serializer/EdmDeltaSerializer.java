/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.serializer;

import nl.buildforce.olingo.commons.api.data.Delta;
import nl.buildforce.olingo.commons.api.edm.EdmEntityType;
import nl.buildforce.olingo.server.api.ServiceMetadata;

public interface EdmDeltaSerializer {
  
  /**
   * Writes collection of delta-response into an InputStream.
   * Information from the EDM is used in addition to information from the data and preferred,
   * but the serializer works without any EDM information as well.
   * Linked data is always written as expanded items (so closed reference loops have to be avoided).
   * @param metadata             metadata for the service
   * @param referencedEntityType the {@link EdmEntityType} or <code>null</code> if not available
   * @param delta     the delta data as entity collection
   * @param options              options for the serializer
   */
  SerializerResult entityCollection(ServiceMetadata metadata, EdmEntityType referencedEntityType,
                                    Delta delta, EntityCollectionSerializerOptions options) throws SerializerException;

}
