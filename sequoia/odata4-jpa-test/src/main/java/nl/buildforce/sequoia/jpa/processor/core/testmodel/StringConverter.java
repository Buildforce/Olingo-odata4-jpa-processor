package nl.buildforce.sequoia.jpa.processor.core.testmodel;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

//This converter has to be mentioned at all columns it is applicable
@Converter()
public class StringConverter implements AttributeConverter<String, String> {

  @Override
  public String convertToDatabaseColumn(String entityString) {
    return entityString;
  }

  @Override
  public String convertToEntityAttribute(String dbString) {
    return dbString;
  }

}