/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm.primitivetype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Calendar;
import java.util.UUID;

import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveType;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import nl.buildforce.olingo.commons.api.edm.constants.EdmTypeKind;

import org.junit.Test;

public class CommonPrimitiveTypeTest extends PrimitiveTypeBaseTest {

  @Test
  public void nameSpace() {
    for (EdmPrimitiveTypeKind kind : EdmPrimitiveTypeKind.values()) {
      assertEquals(EdmPrimitiveType.EDM_NAMESPACE, EdmPrimitiveTypeFactory.getInstance(kind).getNamespace());
    }
  }

  @Test
  public void names() {
    for (EdmPrimitiveTypeKind kind : EdmPrimitiveTypeKind.values()) {
      assertEquals(kind.name(), EdmPrimitiveTypeFactory.getInstance(kind).getName());
    }
  }

  @Test
  public void kind() {
    for (EdmPrimitiveTypeKind kind : EdmPrimitiveTypeKind.values()) {
      assertEquals(EdmTypeKind.PRIMITIVE, EdmPrimitiveTypeFactory.getInstance(kind).getKind());
    }
  }

  @Test
  public void toStringAll() {
    for (EdmPrimitiveTypeKind kind : EdmPrimitiveTypeKind.values()) {
      assertEquals(kind.getFullQualifiedName().toString(), EdmPrimitiveTypeFactory.getInstance(kind).toString());
    }
  }

  @Test
  public void compatibility() {
    for (EdmPrimitiveTypeKind kind : EdmPrimitiveTypeKind.values()) {
      EdmPrimitiveType instance = EdmPrimitiveTypeFactory.getInstance(kind);
      assertTrue(instance.isCompatible(instance));
      assertFalse(instance.isCompatible(EdmPrimitiveTypeFactory.getInstance(
          (kind == EdmPrimitiveTypeKind.String ? EdmPrimitiveTypeKind.Binary : EdmPrimitiveTypeKind.String))));
    }
  }

  @Test
  public void defaultType() {
    assertEquals(byte[].class, EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.Binary).getDefaultType());
    assertEquals(Boolean.class,
        EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.Boolean).getDefaultType());
    assertEquals(Short.class, EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.Byte).getDefaultType());
    assertEquals(Calendar.class, EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.Date).getDefaultType());
    assertEquals(OffsetDateTime.class,
        EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.DateTimeOffset).getDefaultType());
    assertEquals(BigDecimal.class,
        EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.Decimal).getDefaultType());
    assertEquals(Double.class, EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.Double).getDefaultType());
    assertEquals(BigDecimal.class,
        EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.Duration).getDefaultType());
    assertEquals(UUID.class, EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.Guid).getDefaultType());
    assertEquals(Short.class, EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.Int16).getDefaultType());
    assertEquals(Integer.class, EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.Int32).getDefaultType());
    assertEquals(Long.class, EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.Int64).getDefaultType());
    assertEquals(Byte.class, EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.SByte).getDefaultType());
    assertEquals(Float.class, EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.Single).getDefaultType());
    assertEquals(String.class, EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.String).getDefaultType());
    assertEquals(Calendar.class, EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.TimeOfDay).getDefaultType());
  }

  @Test
  public void validate() {
    for (EdmPrimitiveTypeKind kind : EdmPrimitiveTypeKind.values()) {
      EdmPrimitiveType instance = EdmPrimitiveTypeFactory.getInstance(kind);
      assertTrue(instance.validate(null, null, null, null, null, null));
      assertTrue(instance.validate(null, true, null, null, null, null));
      assertFalse(instance.validate(null, false, null, null, null, null));
      if (kind != EdmPrimitiveTypeKind.Stream) {
        assertFalse(instance.validate("ä", null, null, null, null, false));
      }
      if (kind != EdmPrimitiveTypeKind.String && kind != EdmPrimitiveTypeKind.Binary
          && kind != EdmPrimitiveTypeKind.Stream) {

        assertFalse(instance.validate("", null, null, null, null, null));
      }
      if (kind != EdmPrimitiveTypeKind.String && kind != EdmPrimitiveTypeKind.Stream) {
        assertFalse(instance.validate("ä", null, null, null, null, null));
      }
    }

    assertTrue(EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.Binary).
        validate("abcd", null, 3, null, null, null));
    assertFalse(EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.Binary).
        validate("abcd", null, 2, null, null, null));

    assertTrue(EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.Decimal).
        validate("1", null, null, null, null, null));
    assertFalse(EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.Decimal).
        validate("1.2", null, null, null, 0, null));
  }

  @Test
  public void uriLiteral() throws Exception {
    for (EdmPrimitiveTypeKind kind : EdmPrimitiveTypeKind.values()) {
      EdmPrimitiveType instance = EdmPrimitiveTypeFactory.getInstance(kind);
      assertEquals("test", instance.fromUriLiteral(instance.toUriLiteral("test")));
      assertNull(instance.toUriLiteral(null));
      assertNull(instance.fromUriLiteral(null));
    }
  }
}