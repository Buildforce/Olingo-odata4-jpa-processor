package nl.buildforce.sequoia.jpa.processor.core.modify;

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAModelException;
import nl.buildforce.sequoia.jpa.processor.core.exception.ODataJPAProcessorException;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.AdministrativeDivisionDescription;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.AdministrativeDivisionDescriptionKey;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.BusinessPartnerRole;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.Organization;
import org.apache.olingo.server.api.serializer.SerializerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestJPAConversionHelperEntity extends TestJPAConversionHelper {

  @BeforeEach
  public void setUp() {
    cut = new JPAConversionHelper();
  }

  @Override
  @Test
  public void testConvertSimpleKeyToLocation() throws ODataJPAProcessorException, SerializerException,
      ODataJPAModelException {

    Organization newPOJO = new Organization();
    newPOJO.setID("35");

    prepareConvertSimpleKeyToLocation();
    String act = cut.convertKeyToLocal(odata, request, edmEntitySet, et, newPOJO);
    assertEquals("localhost.test/Organisation('35')", act);
  }

  @Override
  @Test
  public void testConvertCompoundKeyToLocation() throws ODataJPAProcessorException, SerializerException,
      ODataJPAModelException {

    BusinessPartnerRole newPOJO = new BusinessPartnerRole();
    newPOJO.setBusinessPartnerID("35");
    newPOJO.setRoleCategory("A");

    prepareConvertCompoundKeyToLocation();
    String act = cut.convertKeyToLocal(odata, request, edmEntitySet, et, newPOJO);
    assertEquals("localhost.test/BusinessPartnerRoles(BusinessPartnerID='35',RoleCategory='A')", act);
  }

  @Override
  @Test
  public void testConvertEmbeddedIdToLocation() throws ODataJPAProcessorException, SerializerException,
      ODataJPAModelException {

    AdministrativeDivisionDescription newPOJO = new AdministrativeDivisionDescription();
    AdministrativeDivisionDescriptionKey primaryKey = new AdministrativeDivisionDescriptionKey();
    primaryKey.setCodeID("NUTS1");
    primaryKey.setCodePublisher("Eurostat");
    primaryKey.setDivisionCode("BE1");
    primaryKey.setLanguage("fr");
    newPOJO.setKey(primaryKey);

    prepareConvertEmbeddedIdToLocation();

    String act = cut.convertKeyToLocal(odata, request, edmEntitySet, et, newPOJO);
    assertEquals(
        "localhost.test/AdministrativeDivisionDescriptions(DivisionCode='BE1',CodeID='NUTS1',CodePublisher='Eurostat',Language='fr')",
        act);

  }
}