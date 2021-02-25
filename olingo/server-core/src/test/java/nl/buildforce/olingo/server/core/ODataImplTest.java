/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import nl.buildforce.olingo.commons.api.format.ContentType;
import nl.buildforce.olingo.server.api.OData;
import nl.buildforce.olingo.server.api.ServiceMetadata;
import nl.buildforce.olingo.server.api.deserializer.DeserializerException;
import nl.buildforce.olingo.server.api.serializer.SerializerException;
import org.junit.Test;
import org.mockito.Mockito;

public class ODataImplTest {

  private final OData odata = OData.newInstance();

  @Test
  public void serializerSupportedFormats() throws SerializerException {
    assertNotNull(odata.createSerializer(ContentType.JSON_NO_METADATA));
    assertNotNull(odata.createSerializer(ContentType.CT_JSON));
    assertNotNull(odata.createSerializer(ContentType.APPLICATION_JSON));
    assertNotNull(odata.createSerializer(ContentType.JSON_FULL_METADATA));
    List<String> versions = new ArrayList<String>();
    versions.add("4.01");
    assertNotNull(odata.createSerializer(ContentType.JSON_FULL_METADATA, versions));
  }

  @Test
  public void deserializerSupportedFormats() throws DeserializerException {
    assertNotNull(odata.createDeserializer(ContentType.JSON_NO_METADATA));
    assertNotNull(odata.createDeserializer(ContentType.CT_JSON));
    assertNotNull(odata.createDeserializer(ContentType.JSON_FULL_METADATA));
    assertNotNull(odata.createDeserializer(ContentType.APPLICATION_JSON));
    List<String> versions = new ArrayList<String>();
    versions.add("4.01");
    assertNotNull(odata.createDeserializer(ContentType.APPLICATION_JSON, versions));
  }

  public void xmlDeserializer() throws DeserializerException {
    assertNotNull(odata.createDeserializer(ContentType.APPLICATION_XML));
  }
  
  @Test(expected=DeserializerException.class)
  public void deserializerWithoutContentType() throws DeserializerException {
    odata.createDeserializer(null);
  }
  
  @Test(expected=DeserializerException.class)
  public void deserializerWithoutContentTypeAndWithVersions() throws DeserializerException {
    List<String> versions = new ArrayList<String>();
    versions.add("4.01");
    odata.createDeserializer(null, versions);
  }
  
  @Test(expected=SerializerException.class)
  public void deltaSerializer() throws SerializerException {
    List<String> versions = new ArrayList<String>();
    versions.add("4.01");
    odata.createEdmDeltaSerializer(null, versions);
  }
  
  @Test(expected=SerializerException.class)
  public void edmAssitedSerializer() throws SerializerException {    
    odata.createEdmAssistedSerializer(null);
  }
  
  @Test(expected=DeserializerException.class)
  public void deserializer1() throws DeserializerException {
    List<String> versions = new ArrayList<String>();
    versions.add("4.01");
    odata.createDeserializer(null, null, versions);
  }
  
  @Test(expected=DeserializerException.class)
  public void deserializer2() throws DeserializerException {
    odata.createDeserializer(null, Mockito.mock(ServiceMetadata.class));
  }
  
  @Test(expected=SerializerException.class)
  public void serializerWithVersions() throws SerializerException {
    List<String> versions = new ArrayList<String>();
    versions.add("4.01");
    odata.createSerializer(null, versions);
  }
  
  @Test(expected=SerializerException.class)
  public void serializer() throws SerializerException {
    odata.createSerializer(null);
  }

}