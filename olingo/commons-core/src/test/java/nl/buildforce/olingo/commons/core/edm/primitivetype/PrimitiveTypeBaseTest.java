/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm.primitivetype;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveType;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeException;

public abstract class PrimitiveTypeBaseTest {

private void expectErrorInValueToString(EdmPrimitiveType instance,
                                        Object value, Boolean isNullable, Integer maxLength,
                                        Integer precision, Integer scale, Boolean isUnicode,
                                        String message) {
    try {
      instance.valueToString(value, isNullable, maxLength, precision, scale, isUnicode);
      fail("Expected exception not thrown");
    } catch (EdmPrimitiveTypeException e) {
      assertNotNull(e.getLocalizedMessage());
      assertThat(e.getLocalizedMessage(), containsString(message));
    }
  }

  private void expectErrorInValueToString(EdmPrimitiveType instance, Object value, String message) {
    expectErrorInValueToString(instance, value, null, null, null, null, null, message);
  }

  protected void expectTypeErrorInValueToString(EdmPrimitiveType instance, Object value) {
    expectErrorInValueToString(instance, value, "value type");
  }

  protected void expectContentErrorInValueToString(EdmPrimitiveType instance, Object value) {
    expectErrorInValueToString(instance, value, "' is not valid.");
  }

  protected void expectFacetsErrorInValueToString(EdmPrimitiveType instance, Object value,
                                                  Boolean isNullable, Integer maxLength, Integer precision,
                                                  Integer scale, Boolean isUnicode) {
    expectErrorInValueToString(instance, value, isNullable, maxLength, precision, scale, isUnicode,
        "facets' constraints");
  }

  protected void expectNullErrorInValueToString(EdmPrimitiveType instance) {
    expectErrorInValueToString(instance, null, false, null, null, null, null, "The value NULL is not allowed.");
  }

  private void expectErrorInValueOfString(EdmPrimitiveType instance,
                                          String value, Boolean isNullable, Integer maxLength, Integer precision,
                                          Integer scale, Boolean isUnicode, Class<?> returnType,
                                          String message) {

    try {
      instance.valueOfString(value, isNullable, maxLength, precision, scale, isUnicode, returnType);
      fail("Expected exception not thrown");
    } catch (EdmPrimitiveTypeException e) {
      assertNotNull(e.getLocalizedMessage());
      assertThat(e.getLocalizedMessage(), containsString(message));
    }
  }

  protected void expectTypeErrorInValueOfString(EdmPrimitiveType instance, String value) {
    expectErrorInValueOfString(instance, value, null, null, null, null, null, Class.class,
        "The value type class java.lang.Class is not supported.");
  }

  protected void expectUnconvertibleErrorInValueOfString(EdmPrimitiveType instance, String value,
                                                         Class<?> type) {
    expectErrorInValueOfString(instance, value, true, Integer.MAX_VALUE, Integer.MAX_VALUE, null, true,
        type, "cannot be converted to");
  }

  protected void expectContentErrorInValueOfString(EdmPrimitiveType instance, String value) {
    expectErrorInValueOfString(instance, value, null, null, null, null, null, instance.getDefaultType(),
        "illegal content");
  }

  protected void expectFacetsErrorInValueOfString(EdmPrimitiveType instance, String value,
                                                  Boolean isNullable, Integer maxLength, Integer precision,
                                                  Integer scale, Boolean isUnicode) {
    expectErrorInValueOfString(instance, value, isNullable, maxLength, precision, scale, isUnicode,
        instance.getDefaultType(), "facets' constraints");
  }

  protected void expectNullErrorInValueOfString(EdmPrimitiveType instance) {
    expectErrorInValueOfString(instance, null, false, null, null, null, null, instance.getDefaultType(),
        "The literal 'null' is not allowed.");
  }

  protected void expectErrorInFromUriLiteral(EdmPrimitiveType instance, String value) {
    try {
      instance.fromUriLiteral(value);
      fail("Expected exception not thrown");
    } catch (EdmPrimitiveTypeException e) {
      assertNotNull(e.getLocalizedMessage());
      assertThat(e.getLocalizedMessage(), containsString("' has illegal content."));
    }
  }

  protected void setTimeZone(Calendar dateTime, String ID) {
	TimeZone timeZone = TimeZone.getTimeZone(ID);
	setTimeZone(dateTime, timeZone);
  }

  protected void setTimeZone(Calendar dateTime, TimeZone timeZone) {
	dateTime.setTimeZone(timeZone);
	// ensure that the internal fields are recomputed so that the calendar can be correctly cloned
    dateTime.get(Calendar.YEAR);
  }
  protected void assertEqualCalendar(Calendar expected, Calendar actual) {
    assertEquals(normalize(expected), normalize(actual));
  }

  private ZonedDateTime normalize(Calendar cal) {
	GregorianCalendar calendar = (GregorianCalendar) cal;
	ZonedDateTime zdt = calendar.toZonedDateTime();
	ZoneId normalizedZoneId = calendar.getTimeZone().toZoneId().normalized();
	return zdt.withZoneSameInstant(normalizedZoneId);
   }

}
