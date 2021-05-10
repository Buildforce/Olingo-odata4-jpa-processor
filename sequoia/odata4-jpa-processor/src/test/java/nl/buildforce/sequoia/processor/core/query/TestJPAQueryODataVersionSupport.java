package nl.buildforce.sequoia.processor.core.query;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

import nl.buildforce.olingo.commons.api.ex.ODataException;
import nl.buildforce.olingo.commons.api.http.HttpStatusCode;
import nl.buildforce.sequoia.processor.core.util.IntegrationTestHelper;
import nl.buildforce.sequoia.processor.core.util.TestBase;

import org.junit.jupiter.api.Test;
import static nl.buildforce.olingo.commons.api.http.HttpHeader.ODATA_MAX_VERSION;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestJPAQueryODataVersionSupport extends TestBase {

  @Test
  public void testEntityWithMetadataFullVersion400() throws IOException, ODataException {
    // createHeaders();
    addHeader(ODATA_MAX_VERSION, "4.00");
    IntegrationTestHelper helper = new IntegrationTestHelper(emf,
        "Organizations('3')?$format=application/json;odata.metadata=full", headers);
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    ObjectNode org = helper.getValue();
    assertNotNull(org.get("@odata.context"));
    assertNotNull(org.get("@odata.type"));
  }

  @Test
  public void testEntityWithMetadataFullVersion401() throws IOException, ODataException {
    // createHeaders();
    addHeader(ODATA_MAX_VERSION, "4.01");
    IntegrationTestHelper helper = new IntegrationTestHelper(emf,
        "Organizations('3')?$format=application/json;odata.metadata=full", headers);
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    ObjectNode org = helper.getValue();
    assertNotNull(org.get("@context"));
    assertNotNull(org.get("@type"));
  }

  @Test
  public void testEntityNavigationWithMetadataFullVersion400() throws IOException, ODataException {
    // createHeaders();
    addHeader(ODATA_MAX_VERSION, "4.00");
    IntegrationTestHelper helper = new IntegrationTestHelper(emf,
        "Organizations('3')/Roles?$format=application/json;odata.metadata=full", headers);
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    ObjectNode act = helper.getValue();
    assertNotNull(act.get("@odata.context"));
    assertNotNull(act.get("value").get(1).get("@odata.type"));
  }

  @Test
  public void testEntityNavigationWithMetadataFullVersion401() throws IOException, ODataException {
    // createHeaders();
    addHeader(ODATA_MAX_VERSION, "4.01");
    IntegrationTestHelper helper = new IntegrationTestHelper(emf,
        "Organizations('3')/Roles?$format=application/json;odata.metadata=full", headers);
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    ObjectNode act = helper.getValue();
    assertNotNull(act.get("@context"));
    assertNotNull(act.get("value").get(1).get("@type"));
  }

  @Test
  public void testComplexPropertyWithMetadataFullVersion400() throws IOException, ODataException {
    // createHeaders();
    addHeader(ODATA_MAX_VERSION, "4.00");
    IntegrationTestHelper helper = new IntegrationTestHelper(emf,
        "Organizations('3')/CommunicationData?$format=application/json;odata.metadata=full", headers);
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    ObjectNode org = helper.getValue();
    assertNotNull(org.get("@odata.context"));
    assertNotNull(org.get("@odata.type"));
  }

  @Test
  public void testComplexPropertyWithMetadataFullVersion401() throws IOException, ODataException {
    // createHeaders();
    addHeader(ODATA_MAX_VERSION, "4.01");
    IntegrationTestHelper helper = new IntegrationTestHelper(emf,
        "Organizations('3')/CommunicationData?$format=application/json;odata.metadata=full", headers);
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    ObjectNode org = helper.getValue();
    assertNotNull(org.get("@context"));
    assertNotNull(org.get("@type"));
  }

@Test
  public void testPrimitivePropertyWithMetadataFullVersion400() throws IOException, ODataException {
    // createHeaders();
    addHeader(ODATA_MAX_VERSION, "4.00");
    IntegrationTestHelper helper = new IntegrationTestHelper(emf,
        "Organizations('3')/LocationName?$format=application/json;odata.metadata=full", headers);
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    ObjectNode org = helper.getValue();
    assertNotNull(org.get("@odata.context"));
  }

@Test
  public void testPrimitivePropertyWithMetadataFullVersion401() throws IOException, ODataException {
    // createHeaders();
    addHeader(ODATA_MAX_VERSION, "4.01");
    IntegrationTestHelper helper = new IntegrationTestHelper(emf,
        "Organizations('3')/LocationName?$format=application/json;odata.metadata=full", headers);
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    ObjectNode org = helper.getValue();
    assertNotNull(org.get("@context"));
  }

}