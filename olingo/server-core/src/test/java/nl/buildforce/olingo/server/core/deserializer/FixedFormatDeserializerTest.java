/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.deserializer;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import nl.buildforce.olingo.server.api.OData;
import nl.buildforce.olingo.server.api.deserializer.FixedFormatDeserializer;
import org.apache.commons.io.IOUtils;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import nl.buildforce.olingo.commons.api.edm.EdmProperty;
import org.junit.Test;
import org.mockito.Mockito;

public class FixedFormatDeserializerTest {

  private static final OData oData = OData.newInstance();
  private final FixedFormatDeserializer deserializer = oData.createFixedFormatDeserializer();

  @Test
  public void binary() throws Exception {
    assertArrayEquals(new byte[] { 0x41, 0x42, 0x43 },
        deserializer.binary(IOUtils.toInputStream("ABC")));
  }

  @Test
  public void binaryLong() throws Exception {
    assertEquals(4 * 3 * 26,
        deserializer.binary(IOUtils.toInputStream(
            "ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZ")).length);
  }

  @Test
  public void primitiveValue() throws Exception {
    EdmProperty property = Mockito.mock(EdmProperty.class);
    Mockito.when(property.getType()).thenReturn(oData.createPrimitiveTypeInstance(EdmPrimitiveTypeKind.Int64));
    Mockito.when(property.isPrimitive()).thenReturn(true);
    assertEquals(42L, deserializer.primitiveValue(IOUtils.toInputStream("42"), property));
  }

  @Test
  public void primitiveValueLong() throws Exception {
    EdmProperty property = Mockito.mock(EdmProperty.class);
    Mockito.when(property.getType()).thenReturn(oData.createPrimitiveTypeInstance(EdmPrimitiveTypeKind.String));
    Mockito.when(property.isPrimitive()).thenReturn(true);
    Mockito.when(property.isUnicode()).thenReturn(true);
    Mockito.when(property.getMaxLength()).thenReturn(61);
    String value = "ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZ\n"
        + "ÄÖÜ€\uFDFC\n"
        + String.valueOf(Character.toChars(0x1F603));
    assertEquals(value, deserializer.primitiveValue(IOUtils.toInputStream(value), property));
  }
}
