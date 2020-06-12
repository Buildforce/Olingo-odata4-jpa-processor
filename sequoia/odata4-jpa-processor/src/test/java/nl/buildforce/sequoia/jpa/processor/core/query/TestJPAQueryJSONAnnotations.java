package nl.buildforce.sequoia.jpa.processor.core.query;

import com.fasterxml.jackson.databind.node.ObjectNode;
import nl.buildforce.sequoia.jpa.processor.core.util.IntegrationTestHelper;
import nl.buildforce.sequoia.jpa.processor.core.util.TestBase;
import org.apache.olingo.commons.api.ex.ODataException;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TestJPAQueryJSONAnnotations extends TestBase {

  @Test
  public void testEntityWithMetadataFullContainNavigationLink() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(emf,
        "Organizations('3')?$format=application/json;odata.metadata=full");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    ObjectNode org = helper.getValue();
    assertNotNull(org.get("Roles@odata.navigationLink"));
    assertEquals("Organizations('3')/Roles", org.get("Roles@odata.navigationLink").asText());
  }

  @Test
  public void testEntityWithMetadataMinimalWithoutNavigationLink() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(emf,
        "Organizations('3')?$format=application/json;odata.metadata=minimal");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    ObjectNode org = helper.getValue();
    assertNull(org.get("Roles@odata.navigationLink"));
  }

  @Test
  public void testEntityWithMetadataNoneWithoutNavigationLink() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(emf,
        "Organizations('3')?$format=application/json;odata.metadata=none");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    ObjectNode org = helper.getValue();
    assertNull(org.get("Roles@odata.navigationLink"));
  }

  @Test
  public void testEntityExpandWithMetadataFullContainNavigationLink() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(emf,
        "Organizations('3')?$expand=Roles&$format=application/json;odata.metadata=full");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    ObjectNode org = helper.getValue();
    assertNotNull(org.get("Roles@odata.navigationLink"));
    assertEquals("Organizations('3')/Roles", org.get("Roles@odata.navigationLink").asText());
  }

  @Test
  public void testEntityWithMetadataFullContainNavigationLinkOfComplex() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(emf,
        "Organizations('3')?$format=application/json;odata.metadata=full");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    ObjectNode org = helper.getValue();
    ObjectNode admin = (ObjectNode) org.get("AdministrativeInformation");
    ObjectNode created = (ObjectNode) admin.get("Created");
    assertNotNull(created.get("User@odata.navigationLink"));
    assertEquals("Organizations('3')/AdministrativeInformation/Created/User", created.get("User@odata.navigationLink")
        .asText());
  }

}