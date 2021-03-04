/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.serializer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import nl.buildforce.olingo.commons.api.data.EntityMediaObject;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveType;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeException;
import nl.buildforce.olingo.server.api.ODataResponse;
import nl.buildforce.olingo.server.api.serializer.FixedFormatSerializer;
import nl.buildforce.olingo.server.api.serializer.PrimitiveValueSerializerOptions;
import nl.buildforce.olingo.server.core.ODataWritableContent;
import nl.buildforce.olingo.server.api.deserializer.batch.ODataResponsePart;
import nl.buildforce.olingo.server.api.serializer.BatchSerializerException;
import nl.buildforce.olingo.server.api.serializer.SerializerException;
import nl.buildforce.olingo.server.api.serializer.SerializerStreamResult;

public class FixedFormatSerializerImpl implements FixedFormatSerializer {

  @Override
  public InputStream binary(byte[] binary) {
    return new ByteArrayInputStream(binary);
  }
  
  protected void binary(EntityMediaObject mediaEntity,
                        OutputStream outputStream) throws SerializerException {
	  try {
		outputStream.write(mediaEntity.getBytes());
	} catch (IOException e) {
		throw new SerializerException("IO Exception occured ", e, SerializerException.MessageKeys.IO_EXCEPTION);
	}
  }
  
  public void binaryIntoStreamed(EntityMediaObject mediaEntity,
                                 OutputStream outputStream) throws SerializerException {
	binary(mediaEntity, outputStream);
  }
  
  @Override
  public SerializerStreamResult mediaEntityStreamed(EntityMediaObject mediaEntity) {
	  return ODataWritableContent.with(mediaEntity, this).build();
  }

  @Override
  public InputStream count(Integer count) {
      return new ByteArrayInputStream(count.toString().getBytes(StandardCharsets.UTF_8));
  }

  @Override
  public InputStream primitiveValue(EdmPrimitiveType type, Object value,
                                    PrimitiveValueSerializerOptions options) throws SerializerException {
    try {
      String result = type.valueToString(value,
          options.isNullable(), options.getMaxLength(),
          options.getPrecision(), options.getScale(), options.isUnicode());
      return new ByteArrayInputStream(result.getBytes(StandardCharsets.UTF_8));
    } catch (EdmPrimitiveTypeException e) {
      throw new SerializerException("Error in primitive-value formatting.", e,
          SerializerException.MessageKeys.WRONG_PRIMITIVE_VALUE,
          type.getFullQualifiedName().getFullQualifiedNameAsString(), value.toString());
    }
  }

  @Override
  public InputStream asyncResponse(ODataResponse odataResponse) throws SerializerException {
    AsyncResponseSerializer serializer = new AsyncResponseSerializer();
    return serializer.serialize(odataResponse);
  }

  // TODO: Signature refactoring for writeBatchResponse
  @Override
  public InputStream batchResponse(List<ODataResponsePart> batchResponses, String boundary)
      throws BatchSerializerException {
    BatchResponseSerializer serializer = new BatchResponseSerializer();

    return serializer.serialize(batchResponses, boundary);
  }

}