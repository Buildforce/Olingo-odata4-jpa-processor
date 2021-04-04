/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.serializer;

import nl.buildforce.olingo.commons.api.Constants;
import nl.buildforce.olingo.commons.api.data.Entity;
import nl.buildforce.olingo.commons.api.data.AbstractEntityCollection;
import nl.buildforce.olingo.commons.api.data.EntityIterator;
import nl.buildforce.olingo.commons.api.data.Property;
import nl.buildforce.olingo.commons.api.edm.EdmComplexType;
import nl.buildforce.olingo.commons.api.edm.EdmEntitySet;
import nl.buildforce.olingo.commons.api.edm.EdmEntityType;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveType;
import nl.buildforce.olingo.server.api.ODataServerError;
import nl.buildforce.olingo.server.api.ServiceMetadata;

/** OData serializer */
public interface ODataSerializer {

  /** The default character set is UTF-8. */
  String DEFAULT_CHARSET =  Constants.UTF8;

  /**
   * Writes the service document into an InputStream.
   * @param serviceMetadata the metadata information for the service
   * @param serviceRoot the service-root URI of this OData service
   */
  SerializerResult serviceDocument(ServiceMetadata serviceMetadata, String serviceRoot) throws SerializerException;

  /**
   * Writes the metadata document into an InputStream.
   * @param serviceMetadata the metadata information for the service
   */
  SerializerResult metadataDocument(ServiceMetadata serviceMetadata) throws SerializerException;

  /**
   * Writes an ODataError into an InputStream.
   * @param error the main error
   */
  SerializerResult error(ODataServerError error) throws SerializerException;

  /**
   * Writes entity-collection data into an InputStream.
   * @param metadata metadata for the service
   * @param entityType the {@link EdmEntityType}
   * @param entitySet the data of the entity set
   * @param options options for the serializer
   */
  SerializerResult entityCollection(ServiceMetadata metadata, EdmEntityType entityType,
      AbstractEntityCollection entitySet, EntityCollectionSerializerOptions options) throws SerializerException;

  /**
   * Writes entity-collection data into an InputStream.
   * @param metadata metadata for the service
   * @param entityType the {@link EdmEntityType}
   * @param entities the data of the entity set
   * @param options options for the serializer
   */
  /*SerializerStreamResult entityCollectionStreamed(ServiceMetadata metadata, EdmEntityType entityType,
      EntityIterator entities, EntityCollectionSerializerOptions options);
*/
  /**
   * Writes entity data into an InputStream.
   * @param metadata metadata for the service
   * @param entityType the {@link EdmEntityType}
   * @param entity the data of the entity
   * @param options options for the serializer
   */
  SerializerResult entity(ServiceMetadata metadata, EdmEntityType entityType, Entity entity,
      EntitySerializerOptions options) throws SerializerException;

  /**
   * Writes primitive-type instance data into an InputStream.
   * @param metadata metadata for the service
   * @param type primitive type
   * @param property property value
   * @param options options for the serializer
   */
  SerializerResult primitive(ServiceMetadata metadata, EdmPrimitiveType type, Property property,
      PrimitiveSerializerOptions options) throws SerializerException;

  /**
   * Writes complex-type instance data into an InputStream.
   * @param metadata metadata for the service
   * @param type complex type
   * @param property property value
   * @param options options for the serializer
   */
  SerializerResult complex(ServiceMetadata metadata, EdmComplexType type, Property property,
      ComplexSerializerOptions options) throws SerializerException;

  /**
   * Writes data of a collection of primitive-type instances into an InputStream.
   * @param metadata metadata for the service
   * @param type primitive type
   * @param property property value
   * @param options options for the serializer
   */
  SerializerResult primitiveCollection(ServiceMetadata metadata, EdmPrimitiveType type, Property property,
      PrimitiveSerializerOptions options) throws SerializerException;

  /**
   * Writes data of a collection of complex-type instances into an InputStream.
   * @param metadata metadata for the service
   * @param type complex type
   * @param property property value
   * @param options options for the serializer
   */
  SerializerResult complexCollection(ServiceMetadata metadata, EdmComplexType type, Property property,
      ComplexSerializerOptions options) throws SerializerException;

  /**
   * Writes a single entity reference into an InputStream.
   * @param metadata metadata for the service
   * @param edmEntitySet {@link EdmEntitySet}
   * @param entity data of the entity
   * @param options {@link ReferenceSerializerOptions}
   */
/*
  SerializerResult reference(ServiceMetadata metadata, EdmEntitySet edmEntitySet, Entity entity,
      ReferenceSerializerOptions options) throws SerializerException;
*/

  /**
   * Writes entity-collection references into an InputStream.
   * @param metadata metadata for the service
   * @param edmEntitySet {@link EdmEntitySet}
   * @param entityCollection data of the entity collection
   * @param options {@link ReferenceCollectionSerializerOptions}
   */
/*
  SerializerResult referenceCollection(ServiceMetadata metadata, EdmEntitySet edmEntitySet,
      AbstractEntityCollection entityCollection, ReferenceCollectionSerializerOptions options)
      throws SerializerException;
*/

}