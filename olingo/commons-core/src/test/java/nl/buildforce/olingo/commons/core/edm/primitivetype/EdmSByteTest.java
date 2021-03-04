/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm.primitivetype;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;

import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveType;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.junit.Test;

public class EdmSByteTest extends PrimitiveTypeBaseTest {

  private final EdmPrimitiveType instance = EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.SByte);

  @Test
  public void toUriLiteral() {
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
    assertEquals("64", instance.valueToString(64L, null, null, null, null, null));
    assertEquals("-128", instance.valueToString(BigInteger.valueOf(Byte.MIN_VALUE), null, null, null, null, null));

    expectContentErrorInValueToString(instance, -129);
    expectContentErrorInValueToString(instance, 128);
    expectContentErrorInValueToString(instance, BigInteger.valueOf(128));

    expectTypeErrorInValueToString(instance, 'A');
  }

  @Test
  public void valueOfString() throws Exception {
    assertEquals(Byte.valueOf((byte) 1), instance.valueOfString("1", null, null, null, null, null, Byte.class));
    assertEquals(Short.valueOf((short) -2), instance.valueOfString("-2", null, null, null, null, null, Short.class));
    assertEquals(Byte.valueOf((byte) 127), instance.valueOfString("127", null, null, null, null, null, Byte.class));
    assertEquals(Byte.valueOf((byte) -128), instance.valueOfString("-128", null, null, null, null, null, Byte.class));
    assertEquals(Integer.valueOf(0), instance.valueOfString("0", null, null, null, null, null, Integer.class));
    assertEquals(Long.valueOf(0), instance.valueOfString("0", null, null, null, null, null, Long.class));
    assertEquals(BigInteger.TEN, instance.valueOfString("10", null, null, null, null, null, BigInteger.class));

    expectContentErrorInValueOfString(instance, "128");
    expectContentErrorInValueOfString(instance, "-129");
    expectContentErrorInValueOfString(instance, "1.0");

    expectTypeErrorInValueOfString(instance, "1");
  }

}