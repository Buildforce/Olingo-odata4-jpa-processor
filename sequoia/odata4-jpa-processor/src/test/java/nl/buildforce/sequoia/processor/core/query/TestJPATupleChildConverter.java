package nl.buildforce.sequoia.processor.core.query;

import nl.buildforce.sequoia.metadata.core.edm.mapper.exception.ODataJPAException;
import nl.buildforce.sequoia.metadata.core.edm.mapper.exception.ODataJPAModelException;
import nl.buildforce.sequoia.processor.core.converter.JPATupleChildConverter;
import nl.buildforce.sequoia.processor.core.util.ServiceMetadataDouble;
import nl.buildforce.sequoia.processor.core.util.TestBase;
import nl.buildforce.sequoia.processor.core.util.TestHelper;
import nl.buildforce.sequoia.processor.core.util.TupleDouble;
import nl.buildforce.sequoia.processor.core.util.UriHelperDouble;
import nl.buildforce.olingo.commons.api.data.ComplexValue;
import nl.buildforce.olingo.commons.api.data.EntityCollection;
import nl.buildforce.olingo.commons.api.data.ValueType;
import nl.buildforce.olingo.server.api.ODataApplicationException;
import nl.buildforce.sequoia.processor.core.converter.JPAExpandResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.Tuple;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestJPATupleChildConverter extends TestBase {
  public static final int NO_POSTAL_ADDRESS_FIELDS = 8;
  public static final int NO_ADMIN_INFO_FIELDS = 2;
  private JPATupleChildConverter cut;
  private List<Tuple> jpaQueryResult;
  private UriHelperDouble uriHelper;
  private Map<String, String> keyPredicates;
  private final HashMap<String, List<Tuple>> queryResult = new HashMap<>(1);

  @BeforeEach
  public void setup() throws ODataJPAException {
    helper = new TestHelper(emf, PUNIT_NAME);
    jpaQueryResult = new ArrayList<>();

    queryResult.put(JPAExpandResult.ROOT_RESULT_KEY, jpaQueryResult);
    uriHelper = new UriHelperDouble();
    keyPredicates = new HashMap<>();
    uriHelper.setKeyPredicates(keyPredicates, "ID");

    cut = new JPATupleChildConverter(helper.sd, uriHelper, new ServiceMetadataDouble(nameBuilder, "Organization"));

  }

  @Test
  public void checkConvertsEmptyResult() throws ODataApplicationException, ODataJPAModelException {

    assertNotNull(cut.getResult(new JPAExpandQueryResult(queryResult, null, helper.getJPAEntityType("Organizations"),
        Collections.emptyList()), Collections.emptyList()));
  }

  @Test
  public void checkConvertsOneResultOneElement() throws ODataApplicationException, ODataJPAModelException {
    HashMap<String, Object> result = new HashMap<>();

    result.put("ID", "1");
    jpaQueryResult.add(new TupleDouble(result));

    keyPredicates.put("1", "Organizations('1')");

    EntityCollection act = cut.getResult(new JPAExpandQueryResult(queryResult, null, helper.getJPAEntityType(
        "Organizations"), Collections.emptyList()), Collections.emptyList()).get(JPAExpandResult.ROOT_RESULT_KEY);
    assertEquals(1, act.getEntities().size());
    assertEquals("1", act.getEntities().get(0).getProperty("ID").getValue().toString());
  }

  @Test
  public void checkConvertsOneResultOneKey() throws ODataApplicationException, ODataJPAModelException {
    HashMap<String, Object> result = new HashMap<>();
    keyPredicates.put("1", "'1'");

    result.put("ID", "1");
    jpaQueryResult.add(new TupleDouble(result));

    EntityCollection act = cut.getResult(new JPAExpandQueryResult(queryResult, null, helper.getJPAEntityType(
        "Organizations"), Collections.emptyList()), Collections.emptyList()).get(JPAExpandResult.ROOT_RESULT_KEY);
    assertEquals(1, act.getEntities().size());
    assertEquals("Organizations" + "('1')", act.getEntities().get(0).getId().getPath());
  }

  @Test
  public void checkConvertsTwoResultsOneElement() throws ODataApplicationException, ODataJPAModelException {
    HashMap<String, Object> result;

    result = new HashMap<>();
    result.put("ID", "1");
    jpaQueryResult.add(new TupleDouble(result));

    result = new HashMap<>();
    result.put("ID", "5");
    jpaQueryResult.add(new TupleDouble(result));

    keyPredicates.put("1", "Organizations('1')");
    keyPredicates.put("5", "Organizations('5')");

    EntityCollection act = cut.getResult(new JPAExpandQueryResult(queryResult, null, helper.getJPAEntityType(
        "Organizations"), Collections.emptyList()), Collections.emptyList()).get(JPAExpandResult.ROOT_RESULT_KEY);
    assertEquals(2, act.getEntities().size());
    assertEquals("1", act.getEntities().get(0).getProperty("ID").getValue().toString());
    assertEquals("5", act.getEntities().get(1).getProperty("ID").getValue().toString());
  }

  @Test
  public void checkConvertsOneResultsTwoElements() throws ODataApplicationException, ODataJPAModelException {
    HashMap<String, Object> result;

    result = new HashMap<>();
    result.put("ID", "1");
    result.put("Name1", "Willi");
    jpaQueryResult.add(new TupleDouble(result));

    keyPredicates.put("1", "Organizations('1')");

    EntityCollection act = cut.getResult(new JPAExpandQueryResult(queryResult, null, helper.getJPAEntityType(
        "Organizations"), Collections.emptyList()), Collections.emptyList()).get(JPAExpandResult.ROOT_RESULT_KEY);
    assertEquals(1, act.getEntities().size());
    assertEquals("1", act.getEntities().get(0).getProperty("ID").getValue().toString());
    assertEquals("Willi", act.getEntities().get(0).getProperty("Name1").getValue().toString());
  }

  @Test
  public void checkConvertsOneResultsTwoElementsSelectionWithEtag() throws ODataApplicationException,
      ODataJPAModelException {

    cut = new JPATupleChildConverter(helper.sd, uriHelper, new ServiceMetadataDouble(nameBuilder,
        "BusinessPartnerProtected"));
    HashMap<String, Object> result;
    result = new HashMap<>();
    result.put("ID", "1");
    result.put("ETag", 2);
    jpaQueryResult.add(new TupleDouble(result));

    EntityCollection act = cut.getResult(new JPAExpandQueryResult(queryResult, null, helper.getJPAEntityType(
        "BusinessPartnerProtecteds"), Collections.emptyList()), Collections.emptyList()).get(JPAExpandResult.ROOT_RESULT_KEY);
    assertEquals(1, act.getEntities().size());
    assertEquals(1, act.getEntities().get(0).getProperties().size());
    assertEquals("1", act.getEntities().get(0).getProperties().get(0).getValue());
    assertEquals("ID", act.getEntities().get(0).getProperties().get(0).getName());
    assertEquals("2", act.getEntities().get(0).getETag());
  }

  @Test
  public void checkConvertsOneResultsOneComplexElement() throws ODataApplicationException, ODataJPAModelException {
    HashMap<String, Object> result;

    result = new HashMap<>();
    result.put("ID", "1");
    result.put("Address/CityName", "Test City");
    result.put("Address/Country", "GB");
    result.put("Address/PostalCode", "ZE1 3AA");
    result.put("Address/StreetName", "Test Road");
    result.put("Address/HouseNumber", "123");
    result.put("Address/POBox", "155");
    result.put("Address/Region", "GB-12");
    result.put("Address/CountryName", "Willi");
    jpaQueryResult.add(new TupleDouble(result));

    keyPredicates.put("1", "Organizations('1')");

    EntityCollection act = cut.getResult(new JPAExpandQueryResult(queryResult, null, helper.getJPAEntityType(
        "Organizations"), Collections.emptyList()), Collections.emptyList()).get(JPAExpandResult.ROOT_RESULT_KEY);
    assertEquals(1, act.getEntities().size());

    assertEquals(ValueType.COMPLEX, act.getEntities().get(0).getProperty("Address").getValueType());
    ComplexValue value = (ComplexValue) act.getEntities().get(0).getProperty("Address").getValue();
    assertEquals(NO_POSTAL_ADDRESS_FIELDS -1, value.getValue().size());
  }

  @Test
  public void checkConvertsOneResultsOneNestedComplexElement() throws ODataApplicationException,
      ODataJPAModelException {
    HashMap<String, Object> result;

//    AdministrativeInformation adminInfo = new AdministrativeInformation();
//    adminInfo.setCreated(new ChangeInformation("Joe Doe", Timestamp.valueOf("2016-01-22 12:25:23")));
//    adminInfo.setUpdated(new ChangeInformation("Joe Doe", Timestamp.valueOf("2016-01-24 14:29:45")));
    result = new HashMap<>();
    result.put("ID", "1");
    result.put("AdministrativeInformation/Created/By", "Joe Doe");
    result.put("AdministrativeInformation/Created/At", "2016-01-22 12:25:23");
    result.put("AdministrativeInformation/Updated/By", "Joe Doe");
    result.put("AdministrativeInformation/Updated/At", "2016-01-24 14:29:45");
    jpaQueryResult.add(new TupleDouble(result));

    keyPredicates.put("1", "Organizations('1')");

    EntityCollection act = cut.getResult(new JPAExpandQueryResult(queryResult, null, helper.getJPAEntityType(
        "Organizations"), Collections.emptyList()), Collections.emptyList()).get(JPAExpandResult.ROOT_RESULT_KEY);
    assertEquals(1, act.getEntities().size());
    // Check first level
    assertEquals(ValueType.COMPLEX, act.getEntities().get(0).getProperty("AdministrativeInformation").getValueType());
    ComplexValue value = (ComplexValue) act.getEntities().get(0).getProperty("AdministrativeInformation").getValue();
    assertEquals(NO_ADMIN_INFO_FIELDS, value.getValue().size());
    // Check second level
    assertEquals(ValueType.COMPLEX, value.getValue().get(0).getValueType());
  }

  @Test
  public void checkConvertsOneResultsOneElementOfComplexElement() throws ODataApplicationException,
      ODataJPAModelException {
    HashMap<String, Object> result;

    result = new HashMap<>();
    result.put("ID", "1");
    result.put("Address/Region", "CA");
    jpaQueryResult.add(new TupleDouble(result));

    keyPredicates.put("1", "Organizations('1')");

    EntityCollection act = cut.getResult(new JPAExpandQueryResult(queryResult, null, helper.getJPAEntityType(
        "Organizations"), Collections.emptyList()), Collections.emptyList()).get(JPAExpandResult.ROOT_RESULT_KEY);
    assertEquals(1, act.getEntities().size());
    assertEquals("CA", ((ComplexValue) act.getEntities().get(0).getProperty("Address").getValue()).getValue().get(0)
        .getValue().toString());
  }

  @Test
  public void checkConvertMediaStreamStaticMime() throws ODataJPAModelException, NumberFormatException,
      ODataApplicationException {

    HashMap<String, List<Tuple>> result = new HashMap<>(1);
    result.put("root", jpaQueryResult);

    cut = new JPATupleChildConverter(helper.sd, uriHelper, new ServiceMetadataDouble(nameBuilder, "PersonImage"));

    HashMap<String, Object> entityResult;
    byte[] image = { -119, 10 };
    entityResult = new HashMap<>();
    entityResult.put("ID", "1");
    entityResult.put("Image", image);
    jpaQueryResult.add(new TupleDouble(entityResult));

    EntityCollection act = cut.getResult(new JPAExpandQueryResult(result, null, helper.getJPAEntityType(
        "PersonImages"), Collections.emptyList()), Collections.emptyList()).get(JPAExpandResult.ROOT_RESULT_KEY);

    assertEquals("image/png", act.getEntities().get(0).getMediaContentType());
  }

  @Test
  public void checkConvertMediaStreamDynamicMime() throws ODataJPAModelException, NumberFormatException,
      ODataApplicationException {

    HashMap<String, List<Tuple>> result = new HashMap<>(1);
    result.put("root", jpaQueryResult);

    cut = new JPATupleChildConverter(helper.sd, uriHelper, new ServiceMetadataDouble(nameBuilder,
        "OrganizationImage"));

    HashMap<String, Object> entityResult;
    byte[] image = { -119, 10 };
    entityResult = new HashMap<>();
    entityResult.put("ID", "9");
    entityResult.put("Image", image);
    entityResult.put("MimeType", "image/svg+xml");
    jpaQueryResult.add(new TupleDouble(entityResult));

    EntityCollection act = cut.getResult(new JPAExpandQueryResult(result, null, helper.getJPAEntityType(
        "OrganizationImages"), Collections.emptyList()), Collections.emptyList()).get(JPAExpandResult.ROOT_RESULT_KEY);
    assertEquals("image/svg+xml", act.getEntities().get(0).getMediaContentType());
    assertEquals(2, act.getEntities().get(0).getProperties().size());
  }

}