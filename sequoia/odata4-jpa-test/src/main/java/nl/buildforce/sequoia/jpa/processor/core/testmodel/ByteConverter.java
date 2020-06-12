package nl.buildforce.sequoia.jpa.processor.core.testmodel;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

//This converter has to be mentioned at all columns it is applicable
@Converter()
public class ByteConverter implements AttributeConverter<String, byte[]> {

  @Override
  public byte[] convertToDatabaseColumn(String entityString) {
    return entityString.getBytes();
  }

  @Override
  public String convertToEntityAttribute(byte[] dbString) {
    return new String(dbString);
  }

}