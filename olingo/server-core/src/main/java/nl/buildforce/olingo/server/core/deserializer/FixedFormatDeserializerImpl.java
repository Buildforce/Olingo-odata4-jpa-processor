/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.deserializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import nl.buildforce.olingo.commons.api.data.Parameter;
import nl.buildforce.olingo.commons.api.data.ValueType;
import nl.buildforce.olingo.commons.api.edm.EdmParameter;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveType;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeException;
import nl.buildforce.olingo.commons.api.edm.EdmProperty;
import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.commons.api.edm.constants.EdmTypeKind;
import nl.buildforce.olingo.commons.api.format.ContentType;
import nl.buildforce.olingo.server.api.deserializer.FixedFormatDeserializer;
import nl.buildforce.olingo.server.api.deserializer.DeserializerException;
import nl.buildforce.olingo.server.api.deserializer.batch.BatchDeserializerException;
import nl.buildforce.olingo.server.api.deserializer.batch.BatchOptions;
import nl.buildforce.olingo.server.api.deserializer.batch.BatchRequestPart;
import nl.buildforce.olingo.server.core.deserializer.batch.BatchParser;
import nl.buildforce.olingo.server.core.deserializer.json.ODataJsonDeserializer;

public class FixedFormatDeserializerImpl implements FixedFormatDeserializer {

  private static final int DEFAULT_BUFFER_SIZE = 128;

  @Override
  public byte[] binary(InputStream content) throws DeserializerException {
    ByteArrayOutputStream result = new ByteArrayOutputStream();
    byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
    int count;
    try {
      while ((count = content.read(buffer)) > -1) {
        result.write(buffer, 0, count);
      }
      result.flush();
    } catch (IOException e) {
      throw new DeserializerException("An I/O exception occurred.", e,
          DeserializerException.MessageKeys.IO_EXCEPTION);
    }
    return result.toByteArray();
  }

  @Override
  public Object primitiveValue(InputStream content, EdmProperty property) throws DeserializerException {
    if (property == null || !property.isPrimitive()) {
      throw new DeserializerException("Wrong property.", DeserializerException.MessageKeys.NOT_IMPLEMENTED);
    }
    try {
      StringWriter writer = new StringWriter();
      InputStreamReader reader = new InputStreamReader(content, StandardCharsets.UTF_8);
      int c = -1;
      while ((c = reader.read()) != -1) {
        writer.append((char) c);
      }
      EdmPrimitiveType type = (EdmPrimitiveType) property.getType();
      return type.valueOfString(writer.toString(),
          property.isNullable(), property.getMaxLength(), property.getPrecision(), property.getScale(),
          property.isUnicode(), type.getDefaultType());
    } catch (EdmPrimitiveTypeException e) {
      throw new DeserializerException("The value is not valid.", e,
          DeserializerException.MessageKeys.INVALID_VALUE_FOR_PROPERTY, property.getName());
    } catch (IOException e) {
      throw new DeserializerException("An I/O exception occurred.", e,
          DeserializerException.MessageKeys.IO_EXCEPTION);
    }
  }

  @Override
  public Parameter parameter(String content, EdmParameter parameter) throws DeserializerException {
    EdmType type = parameter.getType();
    EdmTypeKind kind = type.getKind();
    if ((kind == EdmTypeKind.PRIMITIVE || kind == EdmTypeKind.DEFINITION || kind == EdmTypeKind.ENUM)
        && !parameter.isCollection()) {
      // The content is a primitive URI literal.
      Parameter result = new Parameter();
      result.setName(parameter.getName());
      result.setType(type.getFullQualifiedName().getFullQualifiedNameAsString());
      EdmPrimitiveType primitiveType = (EdmPrimitiveType) type;
      try {
        if (parameter.getMapping() == null) {
          result.setValue(type.getKind() == EdmTypeKind.ENUM ? ValueType.ENUM : ValueType.PRIMITIVE,
              primitiveType.valueOfString(primitiveType.fromUriLiteral(content),
                  parameter.isNullable(), parameter.getMaxLength(), 
                  parameter.getPrecision(), parameter.getScale(), true,
                   primitiveType.getDefaultType()));
        } else {
          result.setValue(type.getKind() == EdmTypeKind.ENUM ? ValueType.ENUM : ValueType.PRIMITIVE,
              primitiveType.valueOfString(primitiveType.fromUriLiteral(content),
                  parameter.isNullable(), parameter.getMaxLength(), 
                  parameter.getPrecision(), parameter.getScale(), true,
                        parameter.getMapping().getMappedJavaClass()));
        }
      } catch (EdmPrimitiveTypeException e) {
        throw new DeserializerException(
            "Invalid value '" + content + "' for parameter " + parameter.getName(), e,
            DeserializerException.MessageKeys.INVALID_VALUE_FOR_PROPERTY, parameter.getName());
      }
      return result;
    } else {
      // The content is a JSON array or object.
      return new ODataJsonDeserializer(ContentType.CT_JSON).parameter(content, parameter);
    }
  }

  @Override
  public List<BatchRequestPart> parseBatchRequest(InputStream content, String boundary,
                                                  BatchOptions options)
          throws BatchDeserializerException {
    BatchParser parser = new BatchParser();

    return parser.parseBatchRequest(content, boundary, options);
  }

}