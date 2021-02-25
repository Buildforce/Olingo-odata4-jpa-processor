/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm.primitivetype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;

import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveType;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.junit.Test;

public class EdmInt64Test extends PrimitiveTypeBaseTest {

  private final EdmPrimitiveType instance = EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.Int64);

  @Test
  public void compatibility() {
    assertTrue(instance.isCompatible(EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.Byte)));
    assertTrue(instance.isCompatible(EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.SByte)));
    assertTrue(instance.isCompatible(EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.Int16)));
    assertTrue(instance.isCompatible(EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.Int32)));
  }

  @Test
  public void toUriLiteral() throws Exception {
    assertEquals("127", instance.toUriLiteral("127"));
  }

  @Test
  public void fromUriLiteral() throws Exception {
    assertEquals("127", instance.fromUriLiteral("127"));
  }

  @Test
  public void valueToString() throws Exception {
    assertEquals("0", instance.valueToString(0, null, null, null, null, null));
    assertEquals("8", instance.valueToString((byte) 8, null, null, null, null, null));
    assertEquals("16", instance.valueToString((short) 16, null, null, null, null, null));
    assertEquals("32", instance.valueToString(32, null, null, null, null, null));
    assertEquals("255", instance.valueToString(255L, null, null, null, null, null));
    assertEquals("12345678901", instance.valueToString(12345678901L, null, null, null, null, null));
    assertEquals("1234567890123456789", instance.valueToString(new BigInteger("1234567890123456789"), null, null, null,
        null, null));
    assertEquals("-1234567890123456789", instance.valueToString(new BigInteger("-1234567890123456789"), null, null,
        null, null, null));

    expectContentErrorInValueToString(instance, new BigInteger("123456789012345678901"));

    expectTypeErrorInValueToString(instance, 1.0);
  }

  @Test
  public void valueOfString() throws Exception {
    assertEquals(Short.valueOf((short) 1), instance.valueOfString("1", null, null, null, null, null, Short.class));
    assertEquals(Integer.valueOf(2), instance.valueOfString("2", null, null, null, null, null, Integer.class));
    assertEquals(Long.valueOf(-1234567890123456789L), instance.valueOfString("-1234567890123456789", null, null, null,
        null, null, Long.class));
    assertEquals(BigInteger.ONE, instance.valueOfString("1", null, null, null, null, null, BigInteger.class));
    assertEquals(Long.valueOf(0), instance.valueOfString("0", null, null, null, null, null, Long.class));
    assertEquals(Byte.valueOf((byte) 0), instance.valueOfString("0", null, null, null, null, null, Byte.class));

    expectContentErrorInValueOfString(instance, "-12345678901234567890");
    expectContentErrorInValueOfString(instance, "1.0");
    expectContentErrorInValueOfString(instance, "0L");
    expectContentErrorInValueOfString(instance, "0x42");

    expectUnconvertibleErrorInValueOfString(instance, "-129", Byte.class);
    expectUnconvertibleErrorInValueOfString(instance, "128", Byte.class);
    expectUnconvertibleErrorInValueOfString(instance, "-32769", Short.class);
    expectUnconvertibleErrorInValueOfString(instance, "32768", Short.class);
    expectUnconvertibleErrorInValueOfString(instance, "-2147483649", Integer.class);
    expectUnconvertibleErrorInValueOfString(instance, "2147483648", Integer.class);

    expectTypeErrorInValueOfString(instance, "1");
  }
}