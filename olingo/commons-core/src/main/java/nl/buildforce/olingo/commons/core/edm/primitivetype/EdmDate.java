/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm.primitivetype;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeException;

/**
 * Implementation of the EDM primitive type Date.
 */
public final class EdmDate extends SingletonPrimitiveType {

  private static final EdmDate INSTANCE = new EdmDate();

  public static EdmDate getInstance() {
    return INSTANCE;
  }

  @Override
  public Class<?> getDefaultType() {
    return Calendar.class;
  }

  @SuppressWarnings("unchecked")
  @Override
  protected <T> T internalValueOfString(String value, Boolean isNullable, Integer maxLength,
                                        Integer precision, Integer scale, Boolean isUnicode, Class<T> returnType)
      throws EdmPrimitiveTypeException {
    LocalDate date;
    try {
      date = LocalDate.parse(value);
    } catch (DateTimeParseException ex) {
      throw new EdmPrimitiveTypeException("The literal '" + value + "' has illegal content.");
    }

    // appropriate types
    if (returnType.isAssignableFrom(LocalDate.class)) {
      return (T) date;
    } else if (returnType.isAssignableFrom(java.sql.Date.class)) {
      return (T) java.sql.Date.valueOf(date);
    }

    // inappropriate types, which need to be supported for backward compatibility
    ZonedDateTime zdt = LocalDateTime.of(date, LocalTime.MIDNIGHT).atZone(ZoneId.systemDefault());
    if (returnType.isAssignableFrom(Calendar.class)) {
      return (T) GregorianCalendar.from(zdt);
    } else if (returnType.isAssignableFrom(Long.class)) {
      return (T) Long.valueOf(zdt.toInstant().toEpochMilli());
    } else if (returnType.isAssignableFrom(java.sql.Date.class)) {
      throw new EdmPrimitiveTypeException("The value type " + returnType + " is not supported.");
    } else if (returnType.isAssignableFrom(java.sql.Timestamp.class)) {
      return (T) java.sql.Timestamp.from(zdt.toInstant());
    } else if (returnType.isAssignableFrom(java.util.Date.class)) {
      return (T) java.util.Date.from(zdt.toInstant());
    } else {
      throw new EdmPrimitiveTypeException("The value type " + returnType + " is not supported.");
    }
  }

  @Override
  protected <T> String internalValueToString(T value, Boolean isNullable, Integer maxLength,
                                             Integer precision, Integer scale, Boolean isUnicode) throws EdmPrimitiveTypeException {
    // appropriate types
    if (value instanceof LocalDate) {
      return value.toString();
    } else if (value instanceof java.sql.Date) {
      return value.toString();
    }

    // inappropriate types, which need to be supported for backward compatibility
    if (value instanceof GregorianCalendar) {
      GregorianCalendar calendar = (GregorianCalendar) value;
      return calendar.toZonedDateTime().toLocalDate().toString();
    }

    long millis;
    if (value instanceof Long) {
      millis = (Long) value;
    } else if (value instanceof java.util.Date) {
      millis = ((java.util.Date) value).getTime();
    } else {
      throw new EdmPrimitiveTypeException("The value type " + value.getClass() + " is not supported.");
    }

    ZonedDateTime zdt = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault());

    return zdt.toLocalDate().toString();
  }
}
