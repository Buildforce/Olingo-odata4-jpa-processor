package nl.buildforce.sequoia.jpa.processor.core.query;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nl.buildforce.sequoia.jpa.processor.core.api.JPAODataGroupsProvider;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.ImageLoader;
import nl.buildforce.sequoia.jpa.processor.core.util.IntegrationTestHelper;
import nl.buildforce.sequoia.jpa.processor.core.util.TestBase;
import org.apache.olingo.commons.api.ex.ODataException;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class TestJPAQuerySelectByPath extends TestBase {

@Test
  public void testNavigationToOwnPrimitiveProperty() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(emf, "Organizations('3')/Name1");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    ObjectNode org = helper.getValue();
    assertEquals("Third Org.", org.get("value").asText());
  }

  @Test
  public void testNavigationToOwnEmptyPrimitiveProperty() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(emf, "Persons('99')/BirthDay");
    helper.assertStatus(HttpStatusCode.NO_CONTENT.getStatusCode());
  }

  @Test
  public void testNavigationToOwnPrimitivePropertyEntityDoesNotExistEntity() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(emf, "Persons('9999')/BirthDay");
    helper.assertStatus(404);
  }

  @Test
  public void testNavigationToOwnPrimitiveDescriptionProperty() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(emf, "Organizations('3')/LocationName");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    ObjectNode org = helper.getValue();
    assertEquals("Vereinigte Staaten von Amerika", org.get("value").asText());
  }

  @Test
  public void testNavigationToComplexProperty() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(emf, "Organizations('4')/Address");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    ObjectNode org = helper.getValue();
    assertEquals("USA", org.get("Country").asText());
  }

  @Test
  public void testNavigationToNotExistingComplexProperty() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(emf, "Persons('97')/CommunicationData");
    helper.assertStatus(HttpStatusCode.NO_CONTENT.getStatusCode());
  }

  @Test
  public void testNavigationToNestedComplexProperty() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(emf,
        "Organizations('4')/AdministrativeInformation/Created");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    ObjectNode org = helper.getValue();
    assertEquals("98", org.get("By").asText());
  }

  @Test
  public void testNavigationViaComplexAndNaviPropertyToPrimitive() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(emf,
        "Organizations('3')/AdministrativeInformation/Created/User/FirstName");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    ObjectNode org = helper.getValue();
    assertEquals("Max", org.get("value").asText());
  }

  @Test
  public void testNavigationToComplexPropertySelect() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(emf,
        "Organizations('4')/Address?$select=Country,Region");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    ObjectNode org = helper.getValue();
    assertEquals(3, org.size()); // Node "@odata.context" is also counted
    assertEquals("USA", org.get("Country").asText());
    assertEquals("US-UT", org.get("Region").asText());
  }

  @Test
  public void testNavigationToComplexPropertyExpand() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(emf, "Organizations('4')/Address");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    ObjectNode org = helper.getValue();
    assertEquals("USA", org.get("Country").asText());
  }

  @Test
  public void testNavigationToComplexPrimitiveProperty() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(emf, "Organizations('1')/Address/Region");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    ObjectNode org = helper.getValue();
    assertEquals("US-CA", org.get("value").asText());
    assertTrue(org.get("@odata.context").asText().endsWith("$metadata#Organizations/Address/Region"));
  }

  @Test
  public void testNavigationToCollection() throws IOException, ODataException {

    final IntegrationTestHelper helper = new IntegrationTestHelper(emf, "Organizations('1')/Comment");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());
  }

  @Test
  public void testNavigationToCollectionWithoutEntries() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(emf, "Organizations('4')/Comment");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    ObjectNode org = helper.getValue();
    ArrayNode act = (ArrayNode) org.get("value");
    assertNotNull(act);
    assertEquals(0, act.size());
  }

  @Test
  public void testNavigationToSimplePrimitiveGroupedPropertyNoGroups() throws IOException, ODataException {

    final IntegrationTestHelper helper = new IntegrationTestHelper(emf, "BusinessPartnerWithGroupss('1')/Country");
    helper.assertStatus(HttpStatusCode.NO_CONTENT.getStatusCode());
  }

  @Test
  public void testNavigationToSimpleComplexGroupedPropertyNoGroups() throws IOException, ODataException {

    final IntegrationTestHelper helper = new IntegrationTestHelper(emf,
        "BusinessPartnerWithGroupss('1')/CommunicationData");
    helper.assertStatus(HttpStatusCode.NO_CONTENT.getStatusCode());
  }

  @Test
  public void testNavigationToCollectionComplexGroupedPropertyNoGroups() throws IOException, ODataException {

    final IntegrationTestHelper helper = new IntegrationTestHelper(emf,
        "BusinessPartnerWithGroupss('99')/InhouseAddress");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());
    final ArrayNode act = (ArrayNode) helper.getValue().get("value");
    assertEquals(2, act.size());
    final ObjectNode addr = (ObjectNode) act.get(0);
    assertFalse(addr.get("TaskID").isNull());
    assertTrue(addr.get("RoomNumber").isNull());
  }

  @Test
  public void testNavigationToCollectionGroupedPropertyNoGroups() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(emf, "BusinessPartnerWithGroupss('1')/Comment");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());
    // Ensure empty result works correct
    helper = new IntegrationTestHelper(emf, "BusinessPartnerWithGroupss('1')/Comment");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());
  }

  @Test
  public void testNoNavigationButGroupsWithoutGroup() throws IOException, ODataException {

    final IntegrationTestHelper helper = new IntegrationTestHelper(emf, "BusinessPartnerWithGroupss('99')");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    final ObjectNode act = helper.getValue();
    assertPresentNotNull(act, "ETag");
    assertPresentButNull(act, "CreationDateTime");
    assertPresentButNull(act, "Country");
    assertPresentButAllNull(act, "CommunicationData");
    assertPresentNotEmpty(act, "InhouseAddress", "RoomNumber");
    assertPresentButNull(act, "Comment");
  }

  @Test
  public void testNoNavigationButGroupsWithOneGroup() throws IOException, ODataException {

    final JPAODataGroupsProvider groups = new JPAODataGroupsProvider();
    groups.addGroup("Person");
    final IntegrationTestHelper helper = new IntegrationTestHelper(emf, "BusinessPartnerWithGroupss('99')", groups);
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    ObjectNode act = helper.getValue();
    assertPresentNotNull(act, "ETag");
    assertPresentButNull(act, "CreationDateTime");
    assertPresentNotNull(act, "Country");
    assertPresentNotNull(act, "CommunicationData");
    assertPresentNotEmpty(act, "InhouseAddress", "RoomNumber");
    assertPresentButNull(act, "Comment");
  }

  @Test
  public void testNavigationToStreamValue() throws IOException, ODataException {
    new ImageLoader().loadPerson(emf.createEntityManager(), "OlingoOrangeTM.png", "99");

    IntegrationTestHelper helper = new IntegrationTestHelper(emf, "PersonImages('99')/$value");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    byte[] act = helper.getBinaryResult();
    assertEquals(93316, act.length, 0);
  }

  @Test
  public void testNavigationToStreamValueVia() throws IOException, ODataException {
    new ImageLoader().loadPerson(emf.createEntityManager(), "OlingoOrangeTM.png", "99");

    IntegrationTestHelper helper = new IntegrationTestHelper(emf, "Persons('99')/Image/$value");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    byte[] act = helper.getBinaryResult();
    assertEquals(93316, act.length, 0);
  }

  @Test
  public void testNavigationToComplexAttributeValue() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(emf,
        "Organizations('4')/AdministrativeInformation/Created/By/$value");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    String act = helper.getRawResult();
    assertEquals("98", act);
  }

  @Test
  public void testNavigationToPrimitiveAttributeValue() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(emf,
        "Organizations('4')/ID/$value");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    String act = helper.getRawResult();
    assertEquals("4", act);
  }

  private void assertPresentButNull(final ObjectNode act, final String property) {
    assertTrue(act.has(property));
    final JsonNode target = act.get(property);
    if (target instanceof ArrayNode)
      assertEquals(0, target.size());
    else
      assertTrue(target.isNull());
  }

  private void assertPresentButAllNull(final ObjectNode act, final String property) {
    assertTrue(act.has(property));
    final JsonNode target = act.get(property);
    if (target instanceof ObjectNode) {
      target.forEach(n -> assertTrue(n.isNull()));
    } else {
      assertTrue(target.isNull());
    }
  }

  private void assertPresentNotNull(final ObjectNode act, final String property) {
    assertTrue(act.has(property));
    final JsonNode target = act.get(property);
    if (target instanceof ArrayNode)
        assertNotEquals(0, target.size());
    else
      assertFalse(act.get(property).isNull());
  }

  private void assertPresentNotEmpty(final ObjectNode act, final String property, final String nullProperty) {
    assertTrue(act.has(property));
    final JsonNode target = act.get(property);
    if (target instanceof ArrayNode) {
      assertTrue(target.size() > 0);
      target.forEach(n -> assertFalse(n.isNull()));
      assertTrue(target.get(0).get(nullProperty).isNull());
    } else {
      fail();
    }
  }

}