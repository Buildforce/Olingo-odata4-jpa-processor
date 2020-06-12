package nl.buildforce.sequoia.jpa.processor.core.query;

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAException;
import nl.buildforce.sequoia.jpa.processor.core.converter.JPAEntityResultConverter;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.AdministrativeDivision;
import nl.buildforce.sequoia.jpa.processor.core.util.EdmEntityTypeDouble;
import nl.buildforce.sequoia.jpa.processor.core.util.TestBase;
import nl.buildforce.sequoia.jpa.processor.core.util.TestHelper;
import nl.buildforce.sequoia.jpa.processor.core.util.UriHelperDouble;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.serializer.SerializerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestJPAInstanceResultConverter extends TestBase {
  public static final int NO_POSTAL_ADDRESS_FIELDS = 8;
  public static final int NO_ADMIN_INFO_FIELDS = 2;
  private JPAEntityResultConverter cut;
  private List<Object> jpaQueryResult;

    @BeforeEach
  public void setup() throws ODataJPAException {
    helper = new TestHelper(emf, PUNIT_NAME);
    jpaQueryResult = new ArrayList<>();
    HashMap<String, String> keyStrings = new HashMap<>();
    keyStrings.put("BE21", "DivisionCode='BE21',CodeID='NUTS2',CodePublisher='Eurostat'");
    keyStrings.put("BE22", "DivisionCode='BE22',CodeID='NUTS2',CodePublisher='Eurostat'");

        UriHelperDouble uriHelper = new UriHelperDouble();
    uriHelper.setKeyPredicates(keyStrings, "DivisionCode");
    cut = new JPAEntityResultConverter(uriHelper, helper.sd,
        jpaQueryResult, new EdmEntityTypeDouble(nameBuilder, "AdministrativeDivision"));
  }

  @Test
  public void checkConvertsEmptyResult() throws ODataApplicationException, SerializerException, URISyntaxException {
    assertNotNull(cut.getResult());
  }

  @Test
  public void checkConvertsOneResult() throws ODataApplicationException, SerializerException, URISyntaxException {
    AdministrativeDivision division = firstResult();

    jpaQueryResult.add(division);

    EntityCollection act = cut.getResult();
    assertEquals(1, act.getEntities().size());
  }

  @Test
  public void checkConvertsTwoResult() throws ODataApplicationException, SerializerException, URISyntaxException {

    jpaQueryResult.add(firstResult());
    jpaQueryResult.add(secondResult());
    EntityCollection act = cut.getResult();
    assertEquals(2, act.getEntities().size());
  }

  @Test
  public void checkConvertsOneResultOneElement() throws ODataApplicationException, SerializerException,
      URISyntaxException {
    AdministrativeDivision division = firstResult();

    jpaQueryResult.add(division);

    EntityCollection act = cut.getResult();
    assertEquals(1, act.getEntities().size());
    assertEquals("BE21", act.getEntities().get(0).getProperty("DivisionCode").getValue().toString());

  }

  @Test
  public void checkConvertsOneResultMultiElement() throws ODataApplicationException, SerializerException,
      URISyntaxException {
    AdministrativeDivision division = firstResult();

    jpaQueryResult.add(division);

    EntityCollection act = cut.getResult();
    assertEquals(1, act.getEntities().size());
    assertEquals("BE21", act.getEntities().get(0).getProperty("DivisionCode").getValue().toString());
    assertEquals("BE2", act.getEntities().get(0).getProperty("ParentDivisionCode").getValue().toString());
    assertEquals("0", act.getEntities().get(0).getProperty("Population").getValue().toString());
  }

  AdministrativeDivision firstResult() {
    AdministrativeDivision division = new AdministrativeDivision();

    division.setCodePublisher("Eurostat");
    division.setCodeID("NUTS2");
    division.setDivisionCode("BE21");
    division.setCountryCode("BEL");
    division.setParentCodeID("NUTS1");
    division.setParentDivisionCode("BE2");
    division.setAlternativeCode("");
    division.setArea(0);
    division.setPopulation(0);
    return division;
  }

  private Object secondResult() {
    AdministrativeDivision division = new AdministrativeDivision();

    division.setCodePublisher("Eurostat");
    division.setCodeID("NUTS2");
    division.setDivisionCode("BE22");
    division.setCountryCode("BEL");
    division.setParentCodeID("NUTS1");
    division.setParentDivisionCode("BE2");
    division.setAlternativeCode("");
    division.setArea(0);
    division.setPopulation(0);
    return division;
  }
}