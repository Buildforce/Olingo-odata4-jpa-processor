/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.serializer;

/**
 * The different types of representations that form the body of either the
 * OData request or the OData response, primarily used for content negotiation.
 */
public enum RepresentationType {
  /** service document */
  SERVICE,
  /** metadata document */
  METADATA,
  /** batch request or response */
  BATCH,
  /** error document */
  ERROR,
  /** single entity */
  ENTITY,
  /** collection of entities (entity set) */
  COLLECTION_ENTITY,
  /** single primitive-type instance */
  PRIMITIVE,
  /** collection of primitive-type instances */
  COLLECTION_PRIMITIVE,
  /** single complex-type instance */
  COMPLEX,
  /** collection of complex-type instances */
  COLLECTION_COMPLEX,
  /** differences */
  // DIFFERENCES,
  /** media entity */
  MEDIA,
  /** binary-type instance */
  BINARY,
  /** single reference */
  REFERENCE,
  /** collection of references */
  COLLECTION_REFERENCE,
  /** textual raw value of a primitive-type instance (except binary) */
  VALUE,
  /** count of instances */
  COUNT,
  /** parameters of an action */
  ACTION_PARAMETERS

}