/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.serializer.xml;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.util.Collections;

import nl.buildforce.olingo.server.api.OData;
import nl.buildforce.olingo.server.api.ServiceMetadata;
import nl.buildforce.olingo.server.api.serializer.ODataSerializer;
import nl.buildforce.olingo.server.core.ServiceMetadataImpl;
import org.apache.commons.io.IOUtils;
import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmEntityContainer;
import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlEdmProvider;
import nl.buildforce.olingo.commons.api.format.ContentType;
import nl.buildforce.olingo.server.api.serializer.SerializerException;
import org.junit.BeforeClass;
import org.junit.Test;

public class ServiceDocumentXmlSerializerTest {
  private static ODataSerializer serializer;

  @BeforeClass
  public static void init() throws SerializerException {
    serializer = OData.newInstance().createSerializer(ContentType.APPLICATION_ATOM_XML);
  }

  @Test
  public void writeServiceWithEmptyMockedEdm() throws Exception {
    Edm edm = mock(Edm.class);
    EdmEntityContainer container = mock(EdmEntityContainer.class);
    when(container.getFullQualifiedName()).thenReturn(new FullQualifiedName("service", "test"));
    when(container.getEntitySets()).thenReturn(Collections.emptyList());
    when(container.getFunctionImports()).thenReturn(Collections.emptyList());
    when(container.getSingletons()).thenReturn(Collections.emptyList());
    when(edm.getEntityContainer()).thenReturn(container);
    ServiceMetadata metadata = mock(ServiceMetadata.class);
    when(metadata.getEdm()).thenReturn(edm);

    assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
        + "<app:service xmlns:atom=\"https://www.w3.org/2005/Atom\" "
        + "xmlns:app=\"https://www.w3.org/2007/app\" "
        + "xmlns:metadata=\"http://docs.oasis-open.org/odata/ns/metadata\" "
        + "metadata:context=\"http://host/svc/$metadata\">"
        + "<app:workspace><atom:title>service.test</atom:title></app:workspace>"
        + "</app:service>",
        IOUtils.toString(serializer.serviceDocument(metadata, "http://host/svc").getContent()));
  }

  @Test
  public void writeServiceDocument() throws Exception {
    CsdlEdmProvider provider = new MetadataDocumentXmlSerializerTest.LocalProvider();
    ServiceMetadata serviceMetadata = new ServiceMetadataImpl(provider,
        Collections.emptyList(), null);
    InputStream metadataStream = serializer.serviceDocument(serviceMetadata, "http://host/svc").getContent();
    String metadata = IOUtils.toString(metadataStream);
    assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
        + "<app:service xmlns:atom=\"https://www.w3.org/2005/Atom\" "
        + "xmlns:app=\"https://www.w3.org/2007/app\" "
        + "xmlns:metadata=\"http://docs.oasis-open.org/odata/ns/metadata\" "
        + "metadata:context=\"http://host/svc/$metadata\">"
        + "<app:workspace>"
        + "<atom:title>org.olingo.container</atom:title>"
        + "<app:collection href=\"ESAllPrim\" metadata:name=\"ESAllPrim\">"
        + "<atom:title>ESAllPrim</atom:title>"
        + "</app:collection>"
        + "<metadata:function-import href=\"FINRTInt16\" metadata:name=\"FINRTInt16\">"
        + "<atom:title>FINRTInt16</atom:title>"
        + "</metadata:function-import>"
        + "<metadata:function-import href=\"FINRTET\" metadata:name=\"FINRTET\">"
        + "<atom:title>FINRTET</atom:title>"
        + "</metadata:function-import>"
        + "<metadata:singleton href=\"SI\" metadata:name=\"SI\">"
        + "<atom:title>SI</atom:title>"
        + "</metadata:singleton>"
        + "</app:workspace>"
        + "</app:service>",
        metadata);
  }

}