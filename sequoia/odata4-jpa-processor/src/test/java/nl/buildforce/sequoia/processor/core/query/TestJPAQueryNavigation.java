package nl.buildforce.sequoia.processor.core.query;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nl.buildforce.sequoia.processor.core.util.IntegrationTestHelper;
import nl.buildforce.sequoia.processor.core.util.TestBase;
import nl.buildforce.olingo.commons.api.ex.ODataException;
import nl.buildforce.olingo.commons.api.http.HttpStatusCode;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestJPAQueryNavigation extends TestBase {

  @Test
  public void testNavigationOneHop() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(emf, "Organizations('3')/Roles");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    ArrayNode orgs = helper.getValues();
    assertEquals(3, orgs.size());
  }

  @Test
  public void testNoNavigationOneEntity() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(emf, "Organizations('3')");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    ObjectNode org = helper.getValue();
    assertEquals("Third Org.", org.get("Name1").asText());
  }

  @Test
  public void testNoNavigationOneEntityCollection() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(emf, "Organizations('1')");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    ObjectNode org = helper.getValue();
    ArrayNode comment = (ArrayNode) org.get("Comment");
    assertEquals(2, comment.size());
  }

  @Test
  public void testNoNavigationOneEntityNoContent() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(emf, "Organizations('1000')");
    helper.assertStatus(404);
  }

  @Test
  public void testNavigationToComplexValue() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(emf,
        "Organizations('3')/AdministrativeInformation/Created");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    ObjectNode created = helper.getValue();
    assertEquals("99", created.get("By").asText());
  }

  @Test
  public void testNavigationOneHopAndOrderBy() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(emf,
        "Organizations('3')/Roles?$orderby=RoleCategory desc");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    ArrayNode orgs = helper.getValues();
    assertEquals(3, orgs.size());
    assertEquals("C", orgs.get(0).get("RoleCategory").asText());
    assertEquals("A", orgs.get(2).get("RoleCategory").asText());
  }

  @Test
  public void testNavigationOneHopReverse() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(emf,
        "BusinessPartnerRoles(BusinessPartnerID='2',RoleCategory='A')/BusinessPartner");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    ObjectNode org = helper.getValue();
    assertEquals("2", org.get("ID").asText());
  }

  @Test
  public void testNavigationViaComplexTypeToComplexType() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(emf,
        "Organizations('3')/AdministrativeInformation/Created/User/AdministrativeInformation");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    ObjectNode admin = helper.getValue();
    ObjectNode created = (ObjectNode) admin.get("Created");
    assertEquals("99", created.get("By").asText());
  }

  @Test
  public void testNavigationViaComplexType() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(emf,
        "Organizations('3')/AdministrativeInformation/Created/User");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    ObjectNode org = helper.getValue();
    assertEquals("99", org.get("ID").asText());
  }

  @Test
  public void testNavigationViaComplexTypeTwoHops() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(emf,
        "Organizations('3')/AdministrativeInformation/Created/User/Address/AdministrativeDivision");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    ObjectNode org = helper.getValue();
    assertEquals("3166-1", org.get("ParentCodeID").asText());
  }

  @Test
  public void testNavigationSelfToOneOneHops() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(emf,
        "AdministrativeDivisions(DivisionCode='BE352',CodeID='NUTS3',CodePublisher='Eurostat')/Parent");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    ObjectNode org = helper.getValue();
    assertEquals("NUTS2", org.get("CodeID").asText());
    assertEquals("BE35", org.get("DivisionCode").asText());
  }

  @Test
  public void testNavigationSelfToOneTwoHops() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(emf,
        "AdministrativeDivisions(DivisionCode='BE352',CodeID='NUTS3',CodePublisher='Eurostat')/Parent/Parent");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    ObjectNode org = helper.getValue();
    assertEquals("NUTS1", org.get("CodeID").asText());
    assertEquals("BE3", org.get("DivisionCode").asText());
  }

  @Test
  public void testNavigationSelfToManyOneHops() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(emf,
        "AdministrativeDivisions(DivisionCode='BE2',CodeID='NUTS1',CodePublisher='Eurostat')/Children?$orderby=DivisionCode desc");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    ArrayNode orgs = helper.getValues();
    assertEquals(5, orgs.size());
    assertEquals("NUTS2", orgs.get(0).get("CodeID").asText());
    assertEquals("BE25", orgs.get(0).get("DivisionCode").asText());
  }

  @Test
  public void testNavigationSelfToManyTwoHops() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(emf,
        "AdministrativeDivisions(DivisionCode='BE2',CodeID='NUTS1',CodePublisher='Eurostat')/Children(DivisionCode='BE25',CodeID='NUTS2',CodePublisher='Eurostat')/Children?$orderby=DivisionCode desc");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    ArrayNode orgs = helper.getValues();
    assertEquals(8, orgs.size());
    assertEquals("NUTS3", orgs.get(0).get("CodeID").asText());
    assertEquals("BE258", orgs.get(0).get("DivisionCode").asText());
  }

  @Test
  public void testNavigationSelfToOneThreeHopsNoResult() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(emf,
        "Organizations('3')/Address/AdministrativeDivision/Parent/Parent");
    helper.assertStatus(HttpStatusCode.NO_CONTENT.getStatusCode());
  }

  @Test
  public void testNavigationSelfToManyOneHopsNoResult() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(emf,
        "Organizations('3')/Address/AdministrativeDivision/Children");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());
  }

  @Test
  public void testNavigationJoinTableDefined() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(emf, "Persons('97')/SupportedOrganizations");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    ArrayNode orgs = helper.getValues();
    assertEquals(2, orgs.size());
  }

  @Test
  public void testNavigationJoinTableDefinedSecondHop() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(emf,
        "BusinessPartnerRoles(BusinessPartnerID='98',RoleCategory='X')/BusinessPartner/nl.buildforce.sequoia.Person/SupportedOrganizations");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    ArrayNode orgs = helper.getValues();
    assertEquals(1, orgs.size());
  }

  @Test
  public void testNavigationJoinTableMappedBy() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(emf, "Organizations('1')/SupportEngineers");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    ArrayNode orgs = helper.getValues();
    assertEquals(2, orgs.size());
  }

  @Test
  public void testNavigationComplexProperty() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(emf, "Organizations('1')/AdministrativeInformation");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    ObjectNode org = helper.getValue();

    assertNotNull(org.get("Created"));
    assertNotNull(org.get("Updated"));
  }

  @Test
  public void testNavigationPrimitiveCollectionProperty() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(emf, "Organizations('1')/Comment");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    ObjectNode org = helper.getValue();
    assertNotNull(org.get("value"));
    assertFalse(org.get("value").isNull());
    ArrayNode values = (ArrayNode) org.get("value");
    assertEquals(2, values.size());
    assertTrue(values.get(0).asText().equals("This is just a test") || values.get(0).asText().equals(
        "This is another test"));
    assertTrue(values.get(1).asText().equals("This is just a test") || values.get(1).asText().equals(
        "This is another test"));
  }

  @Test
  public void testNavigationComplexCollectionProperty() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(emf, "Persons('99')/InhouseAddress");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    ObjectNode org = helper.getValue();
    assertNotNull(org.get("value"));
    assertFalse(org.get("value").isNull());
    ArrayNode values = (ArrayNode) org.get("value");
    assertEquals(2, values.size());
  }

  @Test
  public void testNavigationComplexCollectionPropertyEmptyReult() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(emf, "Persons('98')/InhouseAddress");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());
  }

@Test
  public void testNavigationPrimitiveCollectionPropertyTwoHops() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(emf,
        "BusinessPartnerRoles(BusinessPartnerID='1',RoleCategory='A')/Organization/Comment");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    ObjectNode org = helper.getValue();
    assertNotNull(org.get("value"));
    assertFalse(org.get("value").isNull());
    ArrayNode values = (ArrayNode) org.get("value");
    assertEquals(2, values.size());
    assertTrue(values.get(0).asText().equals("This is just a test") || values.get(0).asText().equals(
        "This is another test"));
    assertTrue(values.get(1).asText().equals("This is just a test") || values.get(1).asText().equals(
        "This is another test"));
  }
}