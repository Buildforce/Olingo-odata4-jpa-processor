package nl.buildforce.sequoia.jpa.processor.core.testmodel;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

//This converter has to be mentioned at all columns it is applicable
@Converter(autoApply = false)
public class DateTimeConverter implements AttributeConverter<LocalDateTime, Timestamp> {

  @Override
  public Timestamp convertToDatabaseColumn(LocalDateTime locDateTime) {
    return (locDateTime == null ? null : Timestamp.from(locDateTime.toInstant(ZoneOffset.UTC)));
  }

  @Override
  public LocalDateTime convertToEntityAttribute(Timestamp sqlTimestamp) {
    return (sqlTimestamp == null ? null : sqlTimestamp.toLocalDateTime());
  }

}