/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm.primitivetype;

import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveType;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class EdmBinaryTest extends PrimitiveTypeBaseTest {

  private final EdmPrimitiveType instance = EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.Binary);

  @Test
  public void validate() {
    assertTrue(instance.validate(null, null, null, null, null, null));
    assertTrue(instance.validate(null, true, null, null, null, null));
    assertFalse(instance.validate(null, false, null, null, null, null));
    assertTrue(instance.validate("", null, null, null, null, null));
    assertFalse(instance.validate("????", null, null, null, null, null));

    assertTrue(instance.validate("qrvM3e7_", null, null, null, null, null));
    assertTrue(instance.validate("qrvM3e7_", null, 6, null, null, null));
    assertFalse(instance.validate("qrvM3e7_", null, 5, null, null, null));
  }

  @Test
  public void toUriLiteral() {
    assertEquals("binary'+hKqoQ=='", instance.toUriLiteral("+hKqoQ=="));
    assertEquals("binary''", instance.toUriLiteral(""));
  }

  @Test
  public void fromUriLiteral() throws Exception {
    assertEquals("+hKqoQ==", instance.fromUriLiteral("binary'+hKqoQ=='"));
    assertEquals("", instance.fromUriLiteral("binary''"));

    expectErrorInFromUriLiteral(instance, "");
    expectErrorInFromUriLiteral(instance, "binary'\"");
    expectErrorInFromUriLiteral(instance, "X''");
    expectErrorInFromUriLiteral(instance, "Xinary''");
  }

  @Test
  public void valueToString() throws Exception {
    byte[] binary = new byte[] { (byte) 0xAA, (byte) 0xBB, (byte) 0xCC, (byte) 0xDD, (byte) 0xEE, (byte) 0xFF };

    assertEquals("qrvM3e7/", instance.valueToString(binary, null, null, null, null, null));

    assertEquals("qrvM3e7/", instance.valueToString(binary, null, 6, null, null, null));
    assertEquals("qrvM3e7/", instance.valueToString(binary, null, Integer.MAX_VALUE, null, null, null));

    assertEquals("qg==", instance.valueToString(new Byte[] {(byte) 170}, null, null, null, null, null));

    expectFacetsErrorInValueToString(instance, binary, null, 3, null, null, null);

    expectTypeErrorInValueToString(instance, 0);
  }

  @Test
  public void valueOfString() throws Exception {
    byte[] binary = new byte[] { (byte) 0xAA, (byte) 0xBB, (byte) 0xCC, (byte) 0xDD, (byte) 0xEE, (byte) 0xFF };

      assertArrayEquals(binary, instance.valueOfString("qrvM3e7_", null, null, null, null, null, byte[].class));
      assertArrayEquals(new Byte[]{binary[0], binary[1], binary[2]}, instance.valueOfString("qrvM", null, null,
              null, null, null, Byte[].class));

      assertArrayEquals(binary, instance.valueOfString("qrvM3e7_", null, 6, null, null, null, byte[].class));
      assertArrayEquals(new byte[]{42}, instance.valueOfString("Kg==", null, 1, null, null, null,
              byte[].class));
      assertArrayEquals(new byte[]{42}, instance.valueOfString("Kg", null, 1, null, null, null,
              byte[].class));
      assertArrayEquals(new byte[]{1, 2}, instance.valueOfString("AQI=", null, 2, null, null, null,
              byte[].class));
      assertArrayEquals(binary, instance.valueOfString("qrvM3e7_", null, 6, null, null, null,
              byte[].class));
      assertArrayEquals(binary, instance.valueOfString("qrvM3e7_", null, Integer.MAX_VALUE, null, null, null,
              byte[].class));
      assertArrayEquals(binary, instance.valueOfString("\nqrvM\n3e7_\r\n", null, 6, null, null, null,
              byte[].class));

    expectFacetsErrorInValueOfString(instance, "qrvM3e7_", null, 3, null, null, null);
    expectContentErrorInValueOfString(instance, "@");

    expectTypeErrorInValueOfString(instance, "qrvM3e7_");
  }

}