package nl.buildforce.sequoia.metadata.core.edm.mapper.impl;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

import jakarta.persistence.AttributeConverter;

/**
 * Default converter to convert from {@link java.time.OffsetDateTime} to {@link java.time.ZonedDateTime}. This is
 * required, as Olingo 4.7.1 only supports ZonedDateTime, where as JPA 2.2 supports OffsetDateTime.
 */

public class OffsetDateTimeConverter implements AttributeConverter<ZonedDateTime, OffsetDateTime> {
    @Override
    public OffsetDateTime convertToDatabaseColumn(final ZonedDateTime zonedDateTime) {
        return zonedDateTime == null ? null : zonedDateTime.toOffsetDateTime();
    }

    @Override
    public ZonedDateTime convertToEntityAttribute(final OffsetDateTime dateTimeWithOffset) {
        return dateTimeWithOffset == null ? null : dateTimeWithOffset.toZonedDateTime();
    }

}