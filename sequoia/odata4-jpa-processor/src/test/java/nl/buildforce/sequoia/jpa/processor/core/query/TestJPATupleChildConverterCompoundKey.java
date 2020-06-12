package nl.buildforce.sequoia.jpa.processor.core.query;

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAException;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAModelException;
import nl.buildforce.sequoia.jpa.processor.core.converter.JPATupleChildConverter;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.AdministrativeDivisionDescriptionKey;
import nl.buildforce.sequoia.jpa.processor.core.util.ServiceMetadataDouble;
import nl.buildforce.sequoia.jpa.processor.core.util.TestBase;
import nl.buildforce.sequoia.jpa.processor.core.util.TestHelper;
import nl.buildforce.sequoia.jpa.processor.core.util.TupleDouble;
import nl.buildforce.sequoia.jpa.processor.core.util.UriHelperDouble;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.server.api.ODataApplicationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.Tuple;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nl.buildforce.sequoia.jpa.processor.core.converter.JPAExpandResult.ROOT_RESULT_KEY;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestJPATupleChildConverterCompoundKey extends TestBase {
  /*public static final int NO_POSTAL_ADDRESS_FIELDS = 8;
  public static final int NO_ADMIN_INFO_FIELDS = 2;*/
  private JPATupleChildConverter cut;
  private List<Tuple> jpaQueryResult;
  private UriHelperDouble uriHelper;
  private Map<String, String> keyPredicates;

  @BeforeEach
  public void setup() throws ODataJPAException {
    helper = new TestHelper(emf, PUNIT_NAME);
    jpaQueryResult = new ArrayList<>();
    uriHelper = new UriHelperDouble();
    keyPredicates = new HashMap<>();
  }

  @Test
  public void checkConvertsOneResultsTwoKeys() throws ODataApplicationException, ODataJPAModelException {
    // .../BusinessPartnerRoles(BusinessPartnerID='3',RoleCategory='C')

    HashMap<String, List<Tuple>> resultContainer = new HashMap<>(1);
    resultContainer.put("root", jpaQueryResult);

    cut = new JPATupleChildConverter(helper.sd, uriHelper, new ServiceMetadataDouble(nameBuilder,
        "BusinessPartnerRole"));

    HashMap<String, Object> result;

    result = new HashMap<>();
    result.put("BusinessPartnerID", "3");
    result.put("RoleCategory", "C");
    jpaQueryResult.add(new TupleDouble(result));

    uriHelper.setKeyPredicates(keyPredicates, "BusinessPartnerID");
    keyPredicates.put("3", "BusinessPartnerID='3',RoleCategory='C'");

    EntityCollection act = cut.getResult(new JPAExpandQueryResult(resultContainer, null, helper.getJPAEntityType(
        "BusinessPartnerRoles"), Collections.emptyList()), Collections.emptyList()).get(ROOT_RESULT_KEY);

    assertEquals(1, act.getEntities().size());
    assertEquals("3", act.getEntities().get(0).getProperty("BusinessPartnerID").getValue().toString());
    assertEquals("C", act.getEntities().get(0).getProperty("RoleCategory").getValue().toString());

    assertEquals("BusinessPartnerRoles(BusinessPartnerID='3',RoleCategory='C')",
        act.getEntities().get(0).getId().getPath());
  }

  @Test // EmbeddedIds are resolved to elementary key properties
  public void checkConvertsOneResultsEmbeddedKey() throws ODataApplicationException, ODataJPAModelException {
    // .../AdministrativeDivisionDescriptions(CodePublisher='ISO', CodeID='3166-1', DivisionCode='DEU',Language='en')

    HashMap<String, List<Tuple>> resultContainer = new HashMap<>(1);
    resultContainer.put("root", jpaQueryResult);

    cut = new JPATupleChildConverter(helper.sd, uriHelper, new ServiceMetadataDouble(nameBuilder,
        "AdministrativeDivisionDescription"));

    AdministrativeDivisionDescriptionKey country = new AdministrativeDivisionDescriptionKey();
    country.setLanguage("en");

    HashMap<String, Object> result;

    result = new HashMap<>();
    result.put("CodePublisher", "ISO");
    result.put("CodeID", "3166-1");
    result.put("DivisionCode", "DEU");
    result.put("Language", "en");
    jpaQueryResult.add(new TupleDouble(result));
    uriHelper.setKeyPredicates(keyPredicates, "DivisionCode");
    keyPredicates.put("DEU", "CodePublisher='ISO',CodeID='3166-1',DivisionCode='DEU',Language='en'");

    EntityCollection act = cut.getResult(new JPAExpandQueryResult(resultContainer, null, helper.getJPAEntityType(
        "AdministrativeDivisionDescriptions"), Collections.emptyList()), Collections.emptyList()).get(ROOT_RESULT_KEY);

    assertEquals(1, act.getEntities().size());
    assertEquals("ISO", act.getEntities().get(0).getProperty("CodePublisher").getValue().toString());
    assertEquals("en", act.getEntities().get(0).getProperty("Language").getValue().toString());

    assertEquals(
        "AdministrativeDivisionDescriptions(CodePublisher='ISO',CodeID='3166-1',DivisionCode='DEU',Language='en')",
        act.getEntities().get(0).getId().getPath());
  }

}