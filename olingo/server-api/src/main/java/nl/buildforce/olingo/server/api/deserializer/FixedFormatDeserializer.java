/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.deserializer;

import java.io.InputStream;
import java.util.List;

import nl.buildforce.olingo.commons.api.data.Parameter;
import nl.buildforce.olingo.commons.api.edm.EdmParameter;
import nl.buildforce.olingo.commons.api.edm.EdmProperty;
import nl.buildforce.olingo.server.api.deserializer.batch.BatchDeserializerException;
import nl.buildforce.olingo.server.api.deserializer.batch.BatchOptions;
import nl.buildforce.olingo.server.api.deserializer.batch.BatchRequestPart;

public interface FixedFormatDeserializer {

  /**
   * Reads binary data from an InputStream.
   * @param content the binary data as input stream
   * @return the binary data
   */
  byte[] binary(InputStream content) throws DeserializerException;

  /**
   * Reads primitive-type data from an InputStream.
   * @param content the textual value as input stream
   * @param property EDM property
   */
  Object primitiveValue(InputStream content, EdmProperty property) throws DeserializerException;

  /**
   * Reads parameter data (in URI syntax) from a String.
   * @param content the textual value as String
   * @param parameter EDM parameter
   */
  Parameter parameter(String content, EdmParameter parameter) throws DeserializerException;

  /**
   * Reads batch data from an InputStream.
   * @param content the data as multipart input stream
   * @param boundary the boundary between the parts
   * @param options options for the deserializer
   * @return a list of batch-request parts
   */
  List<BatchRequestPart> parseBatchRequest(InputStream content, String boundary, BatchOptions options)
      throws BatchDeserializerException;
}