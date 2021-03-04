/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.serializer;

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
import java.io.ByteArrayOutputStream;

import nl.buildforce.olingo.server.api.OData;
import nl.buildforce.olingo.server.api.serializer.FixedFormatSerializer;
import nl.buildforce.olingo.server.api.serializer.PrimitiveValueSerializerOptions;
import org.apache.commons.io.IOUtils;
import nl.buildforce.olingo.commons.api.data.EntityMediaObject;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveType;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import nl.buildforce.olingo.server.api.serializer.SerializerException;
import nl.buildforce.olingo.server.api.serializer.SerializerStreamResult;
import org.junit.Test;

public class FixedFormatSerializerTest {

  private final FixedFormatSerializer serializer;

  public FixedFormatSerializerTest() {
    serializer = OData.newInstance().createFixedFormatSerializer();
  }

  @Test
  public void binary() throws Exception {
    assertEquals("ABC", IOUtils.toString(serializer.binary(new byte[] { 0x41, 0x42, 0x43 })));
  }

  @Test
  public void count() throws Exception {
    assertEquals("42", IOUtils.toString(serializer.count(42)));
  }

  @Test
  public void primitiveValue() throws Exception {
    EdmPrimitiveType type = OData.newInstance().createPrimitiveTypeInstance(EdmPrimitiveTypeKind.Int32);
    assertEquals("42", IOUtils.toString(serializer.primitiveValue(type, 42,
        PrimitiveValueSerializerOptions.with().nullable(true).build())));
  }
  
  @Test
  public void binaryIntoStreamed() throws Exception {
	  EntityMediaObject mediaObject = new EntityMediaObject();
	  mediaObject.setBytes(new byte[] { 0x41, 0x42, 0x43 });
	  ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    new FixedFormatSerializerImpl().binaryIntoStreamed(mediaObject, outputStream);
    assertEquals(mediaObject.getBytes().length, outputStream.toByteArray().length);
  }
  
  @Test
  public void mediaEntityStreamed() throws Exception {
	  EntityMediaObject mediaObject = new EntityMediaObject();
	  mediaObject.setBytes(new byte[] { 0x41, 0x42, 0x43 });
	  SerializerStreamResult result = serializer.mediaEntityStreamed(mediaObject);
	  assertNotNull(result.getODataContent());
  }

}