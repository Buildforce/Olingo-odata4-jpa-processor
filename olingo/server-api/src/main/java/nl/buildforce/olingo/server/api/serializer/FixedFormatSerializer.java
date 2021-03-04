/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.serializer;

import java.io.InputStream;
import java.util.List;

import nl.buildforce.olingo.commons.api.data.EntityMediaObject;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveType;
import nl.buildforce.olingo.server.api.ODataResponse;
import nl.buildforce.olingo.server.api.deserializer.batch.ODataResponsePart;

/** OData serializer for fixed output formats. */
public interface FixedFormatSerializer {

  /**
   * Writes binary output into an InputStream.
   * @param binary the binary data
   */
  InputStream binary(byte[] binary);
  
  /**
   * Writes bytes to an Input stream
   * @param mediaEntity bytes
   * @return
   */
  SerializerStreamResult mediaEntityStreamed(EntityMediaObject mediaEntity);
  
  /**
   * Writes a count into an InputStream as plain text.
   * @param count the count
   */
  InputStream count(Integer count);

  /**
   * Writes the raw value of a primitive-type instance into an InputStream.
   * @param type the primitive type
   * @param value the value
   * @param options options for the serializer
   */
  InputStream primitiveValue(EdmPrimitiveType type, Object value, PrimitiveValueSerializerOptions options)
      throws SerializerException;

  /**
   * Serializes a batch response.
   * @param batchResponses the response parts
   * @param boundary the boundary between the parts
   * @return response as an input stream
   */
  InputStream batchResponse(List<ODataResponsePart> batchResponses, String boundary) throws BatchSerializerException;

  /**
   * Serializes a ODataResponse into an async response.
   * @param odataResponse the response parts
   * @return response as an input stream
   */
  InputStream asyncResponse(ODataResponse odataResponse) throws SerializerException;

}