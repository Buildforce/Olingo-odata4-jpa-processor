/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.serializer;

import java.util.List;

import nl.buildforce.olingo.commons.api.format.ContentType;
import nl.buildforce.olingo.server.api.OlingoExtension;

/**
 * <p>Processors that supports custom content types can implement this interface.</p>
 * <p>The processor can also remove default content types if the default (de-)serializers
 * of Olingo are not used. By default this interface is not implemented and
 * a processor supports content types implemented by Olingo's default (de-)serializer
 * (e.g., <code>application/xml</code> for the metadata and
 * </code>application/json</code> for the service document).</p>
 * <p>Requesting a content type that is not supported results in an HTTP error
 * 406 (Not Acceptable); sending content of an unsupported type results in an
 * HTTP error 415 (Unsupported Media Type).</p>
 */
public interface CustomContentTypeSupport extends OlingoExtension {

  /**
   * Returns a list of supported content types.
   * @param defaultContentTypes content types supported by Olingo's (de-)serializer
   * @param type the current type of representation
   * @return modified list of supported content types
   */
  List<ContentType> modifySupportedContentTypes(
      List<ContentType> defaultContentTypes, RepresentationType type);
}
