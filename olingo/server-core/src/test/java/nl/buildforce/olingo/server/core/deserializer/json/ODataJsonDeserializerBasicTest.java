/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.deserializer.json;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

import nl.buildforce.olingo.commons.api.format.ContentType;
import nl.buildforce.olingo.server.api.OData;
import nl.buildforce.olingo.server.api.deserializer.ODataDeserializer;
import nl.buildforce.olingo.server.api.deserializer.DeserializerException;
import org.junit.Test;

public class ODataJsonDeserializerBasicTest {

  private final ODataDeserializer deserializer;

  public ODataJsonDeserializerBasicTest() throws DeserializerException {
    deserializer = OData.newInstance().createDeserializer(ContentType.CT_JSON);
  }

  @Test
  public void reference() throws Exception {
    String entityString = "{"
        + "\"@odata.context\": \"$metadata#$ref\","
        + "\"@odata.id\": \"ESAllPrim(0)\""
        + "}";

    InputStream stream = new ByteArrayInputStream(entityString.getBytes());
    List<URI> entityReferences = deserializer.entityReferences(stream).getEntityReferences();
    assertEquals(1, entityReferences.size());
    assertEquals("ESAllPrim(0)", entityReferences.get(0).toASCIIString());
  }

  @Test
  public void references() throws Exception {
    String entityString = "{" +
        "  \"@odata.context\": \"$metadata#Collection($ref)\"," +
        "  \"value\": [" +
        "    { \"@odata.id\": \"ESAllPrim(0)\" }," +
        "    { \"@odata.id\": \"ESAllPrim(1)\" }" +
        "  ]" +
        "}";

    InputStream stream = new ByteArrayInputStream(entityString.getBytes());
    List<URI> entityReferences = deserializer.entityReferences(stream).getEntityReferences();

    assertEquals(2, entityReferences.size());
    assertEquals("ESAllPrim(0)", entityReferences.get(0).toASCIIString());
    assertEquals("ESAllPrim(1)", entityReferences.get(1).toASCIIString());
  }

  @Test
  public void referencesWithOtherAnnotations() throws Exception {
    String entityString = "{" +
        "  \"@odata.context\": \"$metadata#Collection($ref)\"," +
        "  \"value\": [" +
        "    { \"@odata.id\": \"ESAllPrim(0)\" }," +
        "    { \"@odata.nonExistingODataAnnotation\": \"ESAllPrim(1)\" }" +
        "  ]" +
        "}";

    InputStream stream = new ByteArrayInputStream(entityString.getBytes());
    List<URI> entityReferences = deserializer.entityReferences(stream).getEntityReferences();

    assertEquals(1, entityReferences.size());
    assertEquals("ESAllPrim(0)", entityReferences.get(0).toASCIIString());
  }

  @Test
  public void referencesWithCustomAnnotation() throws Exception {
    String entityString = "{" +
        "  \"@odata.context\": \"$metadata#Collection($ref)\"," +
        "  \"value\": [" +
        "    { \"@odata.id\": \"ESAllPrim(0)\" }," +
        "    { \"@invalid\": \"ESAllPrim(1)\" }" +
        "  ]" +
        "}";

    InputStream stream = new ByteArrayInputStream(entityString.getBytes());
    List<URI> entityReferences = deserializer.entityReferences(stream).getEntityReferences();

    assertEquals(1, entityReferences.size());
    assertEquals("ESAllPrim(0)", entityReferences.get(0).toASCIIString());
  }

  @Test
  public void referenceEmpty() throws Exception {
    final String entityString = "{\"@odata.context\": \"$metadata#Collection($ref)\","
        + "  \"value\": [ ]"
        + "}";

    InputStream stream = new ByteArrayInputStream(entityString.getBytes());
    List<URI> entityReferences = deserializer.entityReferences(stream).getEntityReferences();
    assertEquals(0, entityReferences.size());
  }

  @Test(expected = DeserializerException.class)
  public void referencesEmpty() throws Exception {
    /*
     * See OData JSON Format chapter 13
     * ... the object that MUST contain the id of the referenced entity
     */
    InputStream stream = new ByteArrayInputStream(new byte[] { '{', '}' });
    deserializer.entityReferences(stream).getEntityReferences();
  }

  @Test(expected = DeserializerException.class)
  public void referencesNoContent() throws Exception {
    deserializer.entityReferences(new ByteArrayInputStream(new byte[] {}));
  }

  @Test(expected = DeserializerException.class)
  public void referencesInvalidJson() throws Exception {
    deserializer.entityReferences(new ByteArrayInputStream(new byte[] { 'A' }));
  }

  @Test(expected = DeserializerException.class)
  public void referenceValueIsNotAnArray() throws Exception {
    String entityString = "{" +
        "  \"@odata.context\": \"$metadata#Collection($ref)\"," +
        "  \"value\": \"ESAllPrim(0)\"" + // This is not allowed. Value must be followed by an array
        "}";
    deserializer.entityReferences(new ByteArrayInputStream(entityString.getBytes()));
  }

}