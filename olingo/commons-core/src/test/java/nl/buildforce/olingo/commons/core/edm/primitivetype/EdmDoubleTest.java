/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm.primitivetype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;

import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveType;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.junit.Test;

public class EdmDoubleTest extends PrimitiveTypeBaseTest {

  private final EdmPrimitiveType instance = EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.Double);

  @Test
  public void compatibility() {
    assertTrue(instance.isCompatible(EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.Byte)));
    assertTrue(instance.isCompatible(EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.SByte)));
    assertTrue(instance.isCompatible(EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.Int16)));
    assertTrue(instance.isCompatible(EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.Int32)));
    assertTrue(instance.isCompatible(EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.Int64)));
    assertTrue(instance.isCompatible(EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.Single)));
  }

  @Test
  public void toUriLiteral() {
    assertEquals("127E42", instance.toUriLiteral("127E42"));
  }

  @Test
  public void fromUriLiteral() throws Exception {
    assertEquals("127E42", instance.fromUriLiteral("127E42"));
  }

  @Test
  public void valueToString() throws Exception {
    assertEquals("0", instance.valueToString(0, null, null, null, null, null));
    assertEquals("8", instance.valueToString((byte) 8, null, null, null, null, null));
    assertEquals("16", instance.valueToString((short) 16, null, null, null, null, null));
    assertEquals("32", instance.valueToString(32, null, null, null, null, null));
    assertEquals("255", instance.valueToString(255L, null, null, null, null, null));
    assertEquals("0.00390625", instance.valueToString(1.0 / 256, null, null, null, null, null));
    assertEquals("4.2E-41", instance.valueToString(42e-42, null, null, null, null, null));
    assertEquals("INF", instance.valueToString(Double.POSITIVE_INFINITY, null, null, null, null, null));
    assertEquals("-INF", instance.valueToString(Double.NEGATIVE_INFINITY, null, null, null, null, null));
    assertEquals("NaN", instance.valueToString(Double.NaN, null, null, null, null, null));
    assertEquals("-0.125", instance.valueToString(-0.125f, null, null, null, null, null));
    assertEquals("INF", instance.valueToString(Float.POSITIVE_INFINITY, null, null, null, null, null));
    assertEquals("-INF", instance.valueToString(Float.NEGATIVE_INFINITY, null, null, null, null, null));
    assertEquals("NaN", instance.valueToString(Float.NaN, null, null, null, null, null));
    assertEquals("-1234567890.12345", instance.valueToString(new BigDecimal("-1234567890.12345"), null, null, null,
        null, null));

    expectContentErrorInValueToString(instance, 3234567890123456L);
    expectContentErrorInValueToString(instance, new BigDecimal("98765432109876543"));
    expectContentErrorInValueToString(instance, new BigDecimal(BigInteger.ONE, 324));
    expectContentErrorInValueToString(instance, new BigDecimal(BigInteger.ONE.negate(), -309));

    expectTypeErrorInValueToString(instance, 'A');
  }

  @Test
  public void valueOfString() throws Exception {
    assertEquals(Double.valueOf(1.42), instance.valueOfString("1.42", null, null, null, null, null, Double.class));
    assertEquals(Float.valueOf(-42.25F), instance.valueOfString("-42.25", null, null, null, null, null, Float.class));
    assertEquals(Double.valueOf(42.0), instance.valueOfString("42", null, null, null, null, null, Double.class));
    assertEquals(Double.valueOf(42E42), instance.valueOfString("42E42", null, null, null, null, null, Double.class));
    assertEquals(BigDecimal.TEN, instance.valueOfString("10", null, null, null, null, null, BigDecimal.class));

    assertEquals(Byte.valueOf((byte) 0), instance.valueOfString("0", null, null, null, null, null, Byte.class));
    assertEquals(Short.valueOf((short) 1), instance.valueOfString("1.00", null, null, null, null, null, Short.class));
    assertEquals(Integer.valueOf(42), instance.valueOfString("4.2E1", null, null, null, null, null, Integer.class));
    assertEquals(Long.valueOf(1234567890), instance.valueOfString("1234567890E-00", null, null, null, null, null, Long.class));
    assertEquals(Double.valueOf(-0.043099999999995475), instance.valueOfString("-0.043099999999995475", null, null, null, null, null, Double.class));

    assertEquals(Double.valueOf(Double.NaN), instance.valueOfString("NaN", null, null, null, null, null, Double.class));
    assertEquals(Double.valueOf(Double.NEGATIVE_INFINITY), instance.valueOfString("-INF", null, null, null, null, null, Double.class));
    assertEquals(Float.valueOf(Float.POSITIVE_INFINITY), instance.valueOfString("INF", null, null, null, null, null, Float.class));

    // General principle of robustness: be conservative in what you do, be liberal in what you accept from others.
    assertEquals(Double.valueOf(0), instance.valueOfString("0.", null, null, null, null, null, Double.class));
    assertEquals(Double.valueOf(0), instance.valueOfString(".0", null, null, null, null, null, Double.class));

    assertEquals(Double.valueOf(1234567890.1234567), instance.valueOfString("1234567890.12345678", null, null, null, null, null, Double.class));
    assertEquals(Double.valueOf("42E400" /*Inf*/), instance.valueOfString("42E400", null, null, null, null, null, Double.class));
    assertEquals(Double.valueOf(42), instance.valueOfString("42F", null, null, null, null, null, Double.class));
    assertEquals(Double.valueOf(0x42P42), instance.valueOfString("0x42P42", null, null, null, null, null, Double.class));

    expectContentErrorInValueOfString(instance, "42.42.42");

    expectUnconvertibleErrorInValueOfString(instance, "INF", BigDecimal.class);
    expectUnconvertibleErrorInValueOfString(instance, "NaN", BigDecimal.class);
    expectUnconvertibleErrorInValueOfString(instance, "1234567.0625", Float.class);
    expectUnconvertibleErrorInValueOfString(instance, "-INF", Integer.class);
    expectUnconvertibleErrorInValueOfString(instance, "NaN", Integer.class);
    expectUnconvertibleErrorInValueOfString(instance, "5E-1", Byte.class);
    expectUnconvertibleErrorInValueOfString(instance, "5E-1", Short.class);
    expectUnconvertibleErrorInValueOfString(instance, "5E-1", Integer.class);
    expectUnconvertibleErrorInValueOfString(instance, "5E-1", Long.class);
    expectUnconvertibleErrorInValueOfString(instance, "-129", Byte.class);
    expectUnconvertibleErrorInValueOfString(instance, "128", Byte.class);
    expectUnconvertibleErrorInValueOfString(instance, "-32769", Short.class);
    expectUnconvertibleErrorInValueOfString(instance, "32768", Short.class);
    expectUnconvertibleErrorInValueOfString(instance, "-2147483649", Integer.class);
    expectUnconvertibleErrorInValueOfString(instance, "2147483648", Integer.class);
    expectUnconvertibleErrorInValueOfString(instance, "-922337203685477.5E10", Long.class);
    expectUnconvertibleErrorInValueOfString(instance, "922337203685477.5E10", Long.class);

    expectTypeErrorInValueOfString(instance, "1.42");
  }
}