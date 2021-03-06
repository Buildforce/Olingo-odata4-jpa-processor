package nl.buildforce.sequoia.processor.core.query;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import java.io.IOException;

import nl.buildforce.olingo.commons.api.ex.ODataException;
import nl.buildforce.olingo.commons.api.http.HttpStatusCode;
import nl.buildforce.sequoia.processor.core.api.JPAODataGroupsProvider;
import nl.buildforce.sequoia.processor.core.util.IntegrationTestHelper;
import nl.buildforce.sequoia.processor.core.util.TestBase;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

public class TestJPAQueryCollection extends TestBase {

  @Test
  public void testSelectPropertyAndCollection() throws IOException, ODataException {

    final IntegrationTestHelper helper = new IntegrationTestHelper(emf, "Organizations?$select=ID,Comment&orderby=ID");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    final ArrayNode orgs = helper.getValues();
    ObjectNode org = (ObjectNode) orgs.get(0);
    assertNotNull(org.get("ID"));
    ArrayNode comment = (ArrayNode) org.get("Comment");
    assertEquals(2, comment.size());
  }

  // @Ignore // See https://issues.apache.org/jira/browse/OLINGO-1231
  @Test
  public void testSelectPropertyOfCollection() throws IOException, ODataException {

    final IntegrationTestHelper helper = new IntegrationTestHelper(emf,
        "Persons('99')/InhouseAddress?$select=Building");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    final ArrayNode buildings = helper.getValues();
    assertEquals(2, buildings.size());
    ObjectNode building = (ObjectNode) buildings.get(0);
    TextNode number = (TextNode) building.get("Building");
    assertNotNull(number);
  }

  @Test
  public void testSelectAllWithComplexCollection() throws IOException, ODataException {

    final IntegrationTestHelper helper = new IntegrationTestHelper(emf,
        "Persons('99')?$select=*");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    final ObjectNode person = helper.getValue();
    ArrayNode comment = (ArrayNode) person.get("InhouseAddress");
    assertEquals(2, comment.size());
  }

  @Test
  public void testSelectAllWithPrimitiveCollection() throws IOException, ODataException {

    final IntegrationTestHelper helper = new IntegrationTestHelper(emf,
        "Organizations('1')?$select=*");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    final ObjectNode person = helper.getValue();
    ArrayNode comment = (ArrayNode) person.get("Comment");
    assertEquals(2, comment.size());
  }

  @Test
  public void testSelectWithNestedComplexCollection() throws IOException, ODataException {

    final IntegrationTestHelper helper = new IntegrationTestHelper(emf,
        "Collections('504')?$select=Nested");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    final ObjectNode collection = helper.getValue();
    ArrayNode nested = (ArrayNode) collection.get("Nested");
    assertEquals(1, nested.size());
    ObjectNode n = (ObjectNode) nested.get(0);
    assertEquals(1L, n.get("Number").asLong());
    assertFalse(n.get("Inner") instanceof NullNode);
    assertEquals(6L, n.get("Inner").get("Figure3").asLong());
  }

  @Test
  public void testSelectComplexContainingCollection() throws IOException, ODataException {

    final IntegrationTestHelper helper = new IntegrationTestHelper(emf,
        "Collections('502')?$select=Complex");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    final ObjectNode collection = helper.getValue();
    final ObjectNode complex = (ObjectNode) collection.get("Complex");
    assertEquals(32L, complex.get("Number").asLong());
    assertFalse(complex.get("Address") instanceof NullNode);
    assertEquals(2, complex.get("Address").size());
    for (int i = 0; i < complex.get("Address").size(); i++) {
      final ObjectNode address = (ObjectNode) complex.get("Address").get(i);
      if (address.get("Building").asText().equals("1")) {
        assertEquals("DEV", address.get("TaskID").asText());
        return;
      }
    }
    fail("Task not found");
  }

  @Test
  public void testSelectComplexContainingTwoCollections() throws IOException, ODataException {

    final IntegrationTestHelper helper = new IntegrationTestHelper(emf,
        "Collections('501')?$select=Complex");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    final ObjectNode collection = helper.getValue();
    ObjectNode complex = (ObjectNode) collection.get("Complex");
    assertEquals(-1L, complex.get("Number").asLong());
    assertFalse(complex.get("Address") instanceof NullNode);
    assertEquals(1, complex.get("Address").size());
    assertEquals("MAIN", complex.get("Address").get(0).get("TaskID").asText());
    assertFalse(complex.get("Comment") instanceof NullNode);
    assertEquals(1, complex.get("Comment").size());
    assertEquals("This is another test", complex.get("Comment").get(0).asText());
  }

  @Test
  public void testSelectAllWithComplexContainingCollection() throws IOException, ODataException {

    final IntegrationTestHelper helper = new IntegrationTestHelper(emf, "Collections('502')");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    final ObjectNode collection = helper.getValue();
    ObjectNode complex = (ObjectNode) collection.get("Complex");
    assertEquals(32L, complex.get("Number").asLong());
    assertFalse(complex.get("Address") instanceof NullNode);
    assertEquals(2, complex.get("Address").size());
    for (int i = 0; i < complex.get("Address").size(); i++) {
      final ObjectNode address = (ObjectNode) complex.get("Address").get(i);
      if (address.get("Building").asText().equals("1")) {
        assertEquals("DEV", address.get("TaskID").asText());
        return;
      }
    }
    fail("Task not found");
  }

  @Test
  public void testSelectAllDeepComplexContainingCollection() throws IOException, ODataException {

    final IntegrationTestHelper helper = new IntegrationTestHelper(emf, "CollectionDeeps('501')");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    final ObjectNode collection = helper.getValue();
    ObjectNode complex = (ObjectNode) collection.get("FirstLevel");
    assertEquals(1, complex.get("LevelID").asInt());
    assertFalse(complex.get("SecondLevel") instanceof NullNode);
    ObjectNode second = (ObjectNode) complex.get("SecondLevel");
    ArrayNode address = (ArrayNode) second.get("Address");
    assertEquals(32, address.get(0).get("RoomNumber").asInt());
  }

  @Test
  public void testSelectOnlyOneCollectionDeepComplex() throws IOException, ODataException {

    final IntegrationTestHelper helper = new IntegrationTestHelper(emf,
        "CollectionDeeps('502')?$select=FirstLevel/SecondLevel/Comment");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    final ObjectNode collection = helper.getValue();
    final TextNode actId = (TextNode) collection.get("ID");
    assertEquals("502", actId.asText());
    ObjectNode complex = (ObjectNode) collection.get("FirstLevel");
    assertFalse(complex.get("SecondLevel") instanceof NullNode);
    ObjectNode second = (ObjectNode) complex.get("SecondLevel");
    ArrayNode comment = (ArrayNode) second.get("Comment");
    assertEquals(2, comment.size());
  }

  @Test
  public void testSelectOnlyNoCollectionDeepComplex() throws IOException, ODataException {

    final IntegrationTestHelper helper = new IntegrationTestHelper(emf,
        "CollectionDeeps('501')?$select=FirstLevel/SecondLevel/Number");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    final ObjectNode collection = helper.getValue();
    final TextNode actId = (TextNode) collection.get("ID");
    assertEquals("501", actId.asText());
    final ObjectNode complex = (ObjectNode) collection.get("FirstLevel");
    assertFalse(complex.get("SecondLevel") instanceof NullNode);
    final ObjectNode second = (ObjectNode) complex.get("SecondLevel");
    final IntNode number = (IntNode) second.get("Number");
    assertEquals(-1, number.asInt());
  }

  @Test
  public void testSelectCollectionWithoutRequiredGroup() throws IOException, ODataException {

    final IntegrationTestHelper helper = new IntegrationTestHelper(emf,
        "BusinessPartnerWithGroupss('1')?$select=Comment");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    final ObjectNode collection = helper.getValue();
    final ArrayNode act = (ArrayNode) collection.get("Comment");
    assertEquals(0, act.size());

  }

  @Test
  public void testSelectCollectionWithRequiredGroup() throws IOException, ODataException {
    final JPAODataGroupsProvider groups = new JPAODataGroupsProvider();
    groups.addGroup("Company");
    final IntegrationTestHelper helper = new IntegrationTestHelper(emf,
        "BusinessPartnerWithGroupss('1')?$select=Comment", groups);
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    final ObjectNode collection = helper.getValue();
    final ArrayNode act = (ArrayNode) collection.get("Comment");
    assertEquals(2, act.size());
  }

  @Test
  public void testSelectCollection() throws IOException, ODataException {

    final IntegrationTestHelper helper = new IntegrationTestHelper(emf,
        "Organizations('1')?$select=Comment");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    final ObjectNode collection = helper.getValue();
    final ArrayNode act = (ArrayNode) collection.get("Comment");
    assertEquals(2, act.size());
  }

  @Test
  public void testSelectCollectionWithTop() throws IOException, ODataException {

    final IntegrationTestHelper helper = new IntegrationTestHelper(emf,
        "Organizations?$select=Comment&$top=2");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    final ObjectNode collection = helper.getValue();
    final ArrayNode act = ((ArrayNode) collection.get("value"));
    assertEquals(2, act.size());
    assertEquals(2, act.get(0).get("Comment").size());
  }

  @Test
  public void testSelectCollectionWithTopAndOrderBy() throws IOException, ODataException {

    final IntegrationTestHelper helper = new IntegrationTestHelper(emf,
        "Organizations?$select=Comment&$top=2&orderby=Name1");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    final ObjectNode collection = helper.getValue();
    final ArrayNode act = ((ArrayNode) collection.get("value"));
    assertEquals(2, act.size());
    assertEquals(2, act.get(0).get("Comment").size());
  }

}