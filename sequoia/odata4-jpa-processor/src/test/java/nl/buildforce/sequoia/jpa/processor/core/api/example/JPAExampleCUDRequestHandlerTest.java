package nl.buildforce.sequoia.jpa.processor.core.api.example;

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAAssociationPath;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAEntityType;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAException;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAModelException;
import nl.buildforce.sequoia.jpa.processor.core.exception.ODataJPAProcessException;
import nl.buildforce.sequoia.jpa.processor.core.modify.JPAUpdateResult;
import nl.buildforce.sequoia.jpa.processor.core.processor.JPAModifyUtil;
import nl.buildforce.sequoia.jpa.processor.core.processor.JPARequestEntity;
import nl.buildforce.sequoia.jpa.processor.core.processor.JPARequestLink;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.*;
import nl.buildforce.sequoia.jpa.processor.core.util.TestBase;
import nl.buildforce.sequoia.jpa.processor.core.util.TestHelper;
import org.apache.olingo.commons.api.http.HttpMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManager;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JPAExampleCUDRequestHandlerTest extends TestBase {
  private JPAExampleCUDRequestHandler cut;
  private EntityManager em;
  private JPARequestEntity requestEntity;
  private Map<String, Object> data;
  private Map<String, Object> keys;

  @BeforeEach
  public void setup() throws ODataJPAException {
    helper = new TestHelper(emf, PUNIT_NAME);
    em = mock(EntityManager.class);
    requestEntity = mock(JPARequestEntity.class);
    data = new HashMap<>();
    keys = new HashMap<>();
    doReturn(new JPAModifyUtil()).when(requestEntity).getModifyUtil();
    doReturn(data).when(requestEntity).getData();
    doReturn(keys).when(requestEntity).getKeys();
    cut = new JPAExampleCUDRequestHandler();
  }

  @Test
  public void checkCreateEntity() throws ODataJPAProcessException, ODataJPAModelException {

    doReturn(helper.getJPAEntityType("AdministrativeDivisions")).when(requestEntity).getEntityType();

    data.put("divisionCode", "DE51");
    data.put("codeID", "NUTS2");
    data.put("countryCode", "DEU");
    data.put("codePublisher", "Eurostat");

    final Object act = cut.createEntity(requestEntity, em);
    assertNotNull(act);
    assertEquals("NUTS2", ((AdministrativeDivision) act).getCodeID());
    verify(em).persist(act);
  }

  @Test
  public void checkCreateEntityWithPrimitiveCollection() throws ODataJPAProcessException, ODataJPAModelException {

    doReturn(helper.getJPAEntityType("Organizations")).when(requestEntity).getEntityType();

    List<String> comments = new ArrayList<>(2);
    comments.add("This is just test");
    comments.add("YAT");

    data.put("iD", "504");
    data.put("comment", comments);

    final Organization act = (Organization) cut.createEntity(requestEntity, em);
    assertNotNull(act);
    assertEquals("504", act.getID());
    assertFalse(act.getComment().isEmpty());
    verify(em).persist(act);
  }

  @Test
  public void checkCreateEntityWithComplexCollection() throws ODataJPAProcessException, ODataJPAModelException {

    doReturn(helper.getJPAEntityType("Persons")).when(requestEntity).getEntityType();

    final List<Map<String, Object>> inhouseAddrs = new ArrayList<>(2);
    final Map<String, Object> addr1 = new HashMap<>(4);
    addr1.put("roomNumber", 32);
    addr1.put("floor", 2);
    addr1.put("building", "7");
    addr1.put("taskID", "MAIN");
    inhouseAddrs.add(addr1);

    final Map<String, Object> addr2 = new HashMap<>(4);
    addr2.put("roomNumber", 245);
    addr2.put("floor", -3);
    addr2.put("building", "1");
    addr2.put("taskID", "DEV");
    inhouseAddrs.add(addr2);

    data.put("iD", "707");
    data.put("inhouseAddress", inhouseAddrs);

    final Person act = (Person) cut.createEntity(requestEntity, em);
    assertNotNull(act);
    assertEquals("707", act.getID());
    assertEquals(2, act.getInhouseAddress().size());
    assertNotNull(act.getInhouseAddress().get(0).getTaskID());
    verify(em).persist(act);
  }

  @Test
  public void checkCreateEntityWithComplexCollectionInitiallyNull() throws ODataJPAProcessException,
      ODataJPAModelException {
    assertNull(new Collection().getNested());

    doReturn(helper.getJPAEntityType("Collections")).when(requestEntity).getEntityType();

    final Map<String, Object> complex = new HashMap<>();
    final List<Map<String, Object>> nested = new ArrayList<>();
    final Map<String, Object> nestedItem = new HashMap<>();
    final Map<String, Object> inner = new HashMap<>();
    final List<String> comment = new ArrayList<>();
    inner.put("figure1", 100L);

    nestedItem.put("inner", inner);
    nestedItem.put("number", 50L);
    nested.add(nestedItem);

    comment.add("How about this");

    complex.put("number", 25L);
    complex.put("comment", comment);
    data.put("iD", "707");
    data.put("complex", complex);
    data.put("nested", nested);

    final Collection act = (Collection) cut.createEntity(requestEntity, em);
    assertNotNull(act);

    assertEquals("707", act.getID());
    assertNotNull(act.getNested());
    assertEquals(1, act.getNested().size());
    assertEquals(50L, act.getNested().get(0).getNumber());
    assertEquals(100L, act.getNested().get(0).getInner().getFigure1());
    assertNotNull(act.getComplex());
    assertNotNull(act.getComplex().getComment());
    assertEquals(1, act.getComplex().getComment().size());
  }

  @Test
  public void checkCreateLinkedEntity() throws ODataJPAProcessException, ODataJPAModelException {
    // http://localhost:8080/tutorial/v1/AdministrativeDivisions(DivisionCode='DE5',CodeID='NUTS1',CodePublisher='Eurostat')/Children
    // key = {divisionCode=DE5, codeID=NUTS1, codePublisher=Eurostat}
    // jpaDeepEntities = JPAAssPath ; JPARequestEntity

    final JPAEntityType et = helper.getJPAEntityType("AdministrativeDivisions");
    final JPAAssociationPath path = et.getAssociationPath("Children");
    final Map<JPAAssociationPath, List<JPARequestEntity>> deepEntities = new HashMap<>();
    final JPARequestEntity deepEntity = mock(JPARequestEntity.class);
    final Map<String, Object> deepData = new HashMap<>();
    final AdministrativeDivision parent = new AdministrativeDivision(new AdministrativeDivisionKey("Eurostat", "NUTS1",
        "DE5"));

    doReturn(et).when(requestEntity).getEntityType();
    keys.put("divisionCode", "DE5");
    keys.put("codeID", "NUTS1");
    keys.put("codePublisher", "Eurostat");
    doReturn(deepEntities).when(requestEntity).getRelatedEntities();
    deepEntities.put(path, Collections.singletonList(deepEntity));

    doReturn(et).when(deepEntity).getEntityType();
    doReturn(deepData).when(deepEntity).getData();
    doReturn(new JPAModifyUtil()).when(deepEntity).getModifyUtil();

    deepData.put("DivisionCode", "DE52");
    deepData.put("CodeID", "NUTS2");
    deepData.put("CodePublisher", "Eurostat");
    deepData.put("CountryCode", "DEU");

    doReturn(parent).when(em).getReference(eq(et.getTypeClass()), any());

    final AdministrativeDivision act = (AdministrativeDivision) cut.createEntity(requestEntity, em);
    assertNotNull(act.getChildren());
    assertEquals(1, act.getChildren().size());

  }

  @Test
  public void checkUpdateEntityNotFound() throws ODataJPAModelException {
    final String id = "1";
    final Organization beforImage = new Organization(id);
    final JPAEntityType et = helper.getJPAEntityType("Organizations");
    beforImage.setName1("Example Ltd");

    doReturn(et).when(requestEntity).getEntityType();
    doReturn(null).when(em).find(eq(et.getTypeClass()), any());

    data.put("name1", "Example SE");
    keys.put("iD", id);

    ODataJPAProcessException exception = assertThrows(ODataJPAProcessException.class, () -> cut.updateEntity(
        requestEntity, em, HttpMethod.PATCH));

    assertEquals(404, exception.getStatusCode());

  }

  @Test
  public void checkDeleteSimplePrimitiveProperty() throws ODataJPAProcessException, ODataJPAModelException {
    final String id = "1";
    final Organization beforImage = new Organization(id);
    beforImage.setName1("Example Ltd");

    doReturn(helper.getJPAEntityType("Organizations")).when(requestEntity).getEntityType();
    doReturn(beforImage).when(em).find(Organization.class, id);

    data.put("name1", null);
    keys.put("iD", id);

    final JPAUpdateResult act = cut.updateEntity(requestEntity, em, HttpMethod.DELETE);

    assertFalse(act.wasCreate());
    assertNull(((Organization) act.getModifiedEntity()).getName1());
  }

  @Test
  public void checkDeletePrimitiveCollectionProperty() throws ODataJPAProcessException, ODataJPAModelException {
    final String id = "1";
    final Organization beforImage = new Organization(id);
    beforImage.getComment().add("YAC");

    doReturn(helper.getJPAEntityType("Organizations")).when(requestEntity).getEntityType();
    doReturn(beforImage).when(em).find(Organization.class, id);

    data.put("comment", null);
    keys.put("iD", id);

    final JPAUpdateResult act = cut.updateEntity(requestEntity, em, HttpMethod.DELETE);

    assertFalse(act.wasCreate());
    assertNull(((Organization) act.getModifiedEntity()).getComment());
  }

  @Test
  public void checkDeleteSimpleComplexProperty() throws ODataJPAProcessException, ODataJPAModelException {
    final String id = "1";
    final Organization beforImage = new Organization(id);
    beforImage.setAddress(new PostalAddressData());

    doReturn(helper.getJPAEntityType("Organizations")).when(requestEntity).getEntityType();
    doReturn(beforImage).when(em).find(Organization.class, id);

    data.put("address", null);
    keys.put("iD", id);

    final JPAUpdateResult act = cut.updateEntity(requestEntity, em, HttpMethod.DELETE);

    assertFalse(act.wasCreate());
    assertNull(((Organization) act.getModifiedEntity()).getAddress());
  }

  @Test
  public void checkDeleteComplexCollectionProperty() throws ODataJPAProcessException, ODataJPAModelException {

    final String id = "2";
    final Person beforImage = new Person();
    beforImage.setID(id);
    beforImage.getInhouseAddress().add(new InhouseAddress("DEV", "D-2"));

    doReturn(helper.getJPAEntityType("Persons")).when(requestEntity).getEntityType();
    doReturn(beforImage).when(em).find(Person.class, id);

    data.put("inhouseAddress", null);
    keys.put("iD", id);

    final JPAUpdateResult act = cut.updateEntity(requestEntity, em, HttpMethod.DELETE);

    assertFalse(act.wasCreate());
    assertNull(((Person) act.getModifiedEntity()).getInhouseAddress());
  }

  @Test
  public void checkDeleteSimplePrimitivePropertyDeep() throws ODataJPAProcessException, ODataJPAModelException {
    final String id = "1";
    final Organization beforImage = new Organization(id);
    final PostalAddressData addr = new PostalAddressData();
    final Map<String, Object> addrData = new HashMap<>();
    addr.setPOBox("23145-1235");
    addr.setCityName("Hamburg");
    beforImage.setAddress(addr);
    beforImage.setName1("Example Ltd");

    doReturn(helper.getJPAEntityType("Organizations")).when(requestEntity).getEntityType();
    doReturn(beforImage).when(em).find(Organization.class, id);

    data.put("address", addrData);
    addrData.put("pOBox", null);
    keys.put("iD", id);

    final JPAUpdateResult act = cut.updateEntity(requestEntity, em, HttpMethod.DELETE);

    assertFalse(act.wasCreate());
    assertNull(((Organization) act.getModifiedEntity()).getAddress().getPOBox());
    assertEquals("Hamburg", ((Organization) act.getModifiedEntity()).getAddress().getCityName());
  }

  @Test
  public void checkDeleteEntity() throws ODataJPAProcessException, ODataJPAModelException {
    final String id = "1";
    final Organization beforImage = new Organization(id);

    doReturn(beforImage).when(em).find(Organization.class, id);
    doReturn(helper.getJPAEntityType("Organizations")).when(requestEntity).getEntityType();
    keys.put("iD", id);
    cut.deleteEntity(requestEntity, em);
    verify(em).remove(beforImage);
  }

  @Test
  public void checkDeleteNoErrorIfEntityDoesNotExists() throws ODataJPAProcessException, ODataJPAModelException {
    final String id = "1";
    final Organization beforImage = new Organization(id);

    doReturn(null).when(em).find(Organization.class, id);
    doReturn(helper.getJPAEntityType("Organizations")).when(requestEntity).getEntityType();
    keys.put("iD", id);
    cut.deleteEntity(requestEntity, em);
    verify(em, times(0)).remove(beforImage);
  }

  @Test
  public void checkPatchOneSimplePrimitiveValue() throws ODataJPAModelException, ODataJPAProcessException {
    final String id = "1";
    final Organization beforImage = new Organization(id);
    final JPAEntityType et = helper.getJPAEntityType("Organizations");
    beforImage.setName1("Example Ltd");

    doReturn(et).when(requestEntity).getEntityType();
    doReturn(beforImage).when(em).find(eq(et.getTypeClass()), any());

    data.put("name1", "Example SE");
    keys.put("iD", id);

    final JPAUpdateResult act = cut.updateEntity(requestEntity, em, HttpMethod.PATCH);

    assertFalse(act.wasCreate());
    assertEquals("Example SE", ((Organization) act.getModifiedEntity()).getName1());
  }

  @Test
  public void checkPatchOneSimpleComplexValue() throws ODataJPAModelException, ODataJPAProcessException {

    final String id = "1";
    final Organization beforImage = new Organization(id);
    final PostalAddressData beforeAddr = new PostalAddressData();
    final Map<String, Object> changedAddr = new HashMap<>();

    beforImage.setAddress(beforeAddr);
    beforeAddr.setHouseNumber("45A");
    beforeAddr.setCityName("Test");
    beforeAddr.setPostalCode("12345");

    doReturn(helper.getJPAEntityType("Organizations")).when(requestEntity).getEntityType();
    doReturn(beforImage).when(em).find(Organization.class, id);

    changedAddr.put("houseNumber", "45");
    changedAddr.put("streetName", "Example Street");

    data.put("address", changedAddr);
    keys.put("iD", id);

    final JPAUpdateResult act = cut.updateEntity(requestEntity, em, HttpMethod.DELETE);

    assertFalse(act.wasCreate());
    assertNotNull(((Organization) act.getModifiedEntity()).getAddress());
    final PostalAddressData afterImage = ((Organization) act.getModifiedEntity()).getAddress();

    assertEquals("45", afterImage.getHouseNumber());
    assertEquals("Test", afterImage.getCityName());
    assertEquals("12345", afterImage.getPostalCode());
    assertEquals("Example Street", afterImage.getStreetName());
  }

  @Test
  public void checkPatchEmptyComplexCollectionProperty() throws ODataJPAProcessException, ODataJPAModelException {

    final String id = "2";
    final Person beforImage = new Person();
    final List<Map<String, Object>> newInhouseAddresses = new ArrayList<>(2);
    beforImage.setID(id);
    beforImage.getInhouseAddress().add(new InhouseAddress("DEV", "D-2"));

    doReturn(helper.getJPAEntityType("Persons")).when(requestEntity).getEntityType();
    doReturn(beforImage).when(em).find(Person.class, id);

    data.put("inhouseAddress", newInhouseAddresses);
    keys.put("iD", id);

    final JPAUpdateResult act = cut.updateEntity(requestEntity, em, HttpMethod.DELETE);

    assertFalse(act.wasCreate());
    assertNotNull(((Person) act.getModifiedEntity()).getInhouseAddress());
    assertTrue(((Person) act.getModifiedEntity()).getInhouseAddress().isEmpty());
  }

  @Test
  public void checkPatchComplexCollectionProperty() throws ODataJPAProcessException, ODataJPAModelException {

    final String id = "2";
    final Person beforImage = new Person();
    final List<Map<String, Object>> newInhouseAddresses = new ArrayList<>(2);
    beforImage.setID(id);
    beforImage.getInhouseAddress().add(new InhouseAddress("DEV", "D-2"));

    doReturn(helper.getJPAEntityType("Persons")).when(requestEntity).getEntityType();
    doReturn(beforImage).when(em).find(Person.class, id);

    final Map<String, Object> addr1 = new HashMap<>();
    addr1.put("taskID", "MAIN");
    addr1.put("floor", "U1");
    newInhouseAddresses.add(addr1);
    final Map<String, Object> addr2 = new HashMap<>();
    addr2.put("taskID", "EDU");
    addr2.put("floor", "E");
    newInhouseAddresses.add(addr2);

    data.put("inhouseAddress", newInhouseAddresses);
    keys.put("iD", id);

    final JPAUpdateResult act = cut.updateEntity(requestEntity, em, HttpMethod.PATCH);

    assertFalse(act.wasCreate());
    assertNotNull(((Person) act.getModifiedEntity()).getInhouseAddress());
    final List<InhouseAddress> actInhouseAddrs = ((Person) act.getModifiedEntity()).getInhouseAddress();
    assertEquals(2, actInhouseAddrs.size());
    assertTrue(actInhouseAddrs.get(0).getBuilding() == null || actInhouseAddrs.get(0).getBuilding().isEmpty());
    assertTrue(actInhouseAddrs.get(1).getBuilding() == null || actInhouseAddrs.get(1).getBuilding().isEmpty());
  }

  @Test
  public void checkPatchOnePrimitiveCollectionValue() throws ODataJPAModelException, ODataJPAProcessException {
    final String id = "1";
    final Organization beforImage = new Organization(id);
    final List<String> newComments = Arrays.asList("This is a test", "YAT");
    beforImage.getComment().add("YAC");

    doReturn(helper.getJPAEntityType("Organizations")).when(requestEntity).getEntityType();
    doReturn(beforImage).when(em).find(Organization.class, id);

    data.put("comment", newComments);
    keys.put("iD", id);

    final JPAUpdateResult act = cut.updateEntity(requestEntity, em, HttpMethod.DELETE);

    assertFalse(act.wasCreate());
    assertNotNull(((Organization) act.getModifiedEntity()).getComment());
    final List<String> actComments = ((Organization) act.getModifiedEntity()).getComment();
    assertEquals(2, actComments.size());
    assertTrue(actComments.contains("YAT"));
    assertTrue(actComments.contains("This is a test"));
  }

  @Test
  public void checkPatchCreateBindingLinkBetweenTwoEntities() throws ODataJPAModelException,
      ODataJPAProcessException {
    // URL: ../AdministrativeDivisions(DivisionCode='DE51',CodeID='NUTS2',CodePublisher='Eurostat')
    // Body: {
    // "Parent@odata.bind": ["AdministrativeDivisions(DivisionCode='DE5',CodeID='NUTS1',CodePublisher='Eurostat')" ] }
    final JPAEntityType et = helper.getJPAEntityType("AdministrativeDivisions");
    final JPAAssociationPath path = et.getAssociationPath("Children");
    final Map<JPAAssociationPath, List<JPARequestLink>> relationLinks = new HashMap<>();
    final JPARequestLink link = mock(JPARequestLink.class);
    final Map<String, Object> childKeys = new HashMap<>();
    final AdministrativeDivision parent = new AdministrativeDivision(new AdministrativeDivisionKey("Eurostat", "NUTS1",
        "DE5"));
    final AdministrativeDivision child = new AdministrativeDivision(new AdministrativeDivisionKey("Eurostat", "NUTS2",
        "DE51"));

    doReturn(et).when(requestEntity).getEntityType();
    keys.put("divisionCode", "DE5");
    keys.put("codeID", "NUTS1");
    keys.put("codePublisher", "Eurostat");
    doReturn(relationLinks).when(requestEntity).getRelationLinks();

    relationLinks.put(path, Collections.singletonList(link));
    doReturn(et).when(link).getEntityType();
    doReturn(childKeys).when(link).getRelatedKeys();
    childKeys.put("divisionCode", "DE51");
    childKeys.put("codeID", "NUTS2");
    childKeys.put("codePublisher", "Eurostat");
    doReturn(childKeys).when(link).getValues();

    doReturn(parent).when(em).find(eq(et.getTypeClass()), eq(parent.getKey()));
    doReturn(child).when(em).find(eq(et.getTypeClass()), eq(child.getKey()));

    final JPAUpdateResult act = cut.updateEntity(requestEntity, em, HttpMethod.DELETE);

    assertNotNull(act);
    assertEquals(parent, act.getModifiedEntity());
    assertEquals(1, parent.getChildren().size());
    assertEquals("DE51", parent.getChildren().get(0).getDivisionCode());
  }

}