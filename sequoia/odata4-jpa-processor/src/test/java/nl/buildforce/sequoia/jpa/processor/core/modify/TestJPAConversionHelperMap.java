package nl.buildforce.sequoia.jpa.processor.core.modify;

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAModelException;
import nl.buildforce.sequoia.jpa.processor.core.exception.ODataJPAProcessorException;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.Organization;
import org.apache.olingo.server.api.serializer.SerializerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class TestJPAConversionHelperMap extends TestJPAConversionHelper {
  @BeforeEach
  public void setUp() {
    cut = new JPAConversionHelper();
  }

  @Override
  @Test
  public void testConvertCompoundKeyToLocation() throws ODataJPAProcessorException, SerializerException,
      ODataJPAModelException {

    Map<String, Object> newPOJO = new HashMap<>();
    newPOJO.put("businessPartnerID", "35");
    newPOJO.put("roleCategory", "A");

    prepareConvertCompoundKeyToLocation();
    String act = cut.convertKeyToLocal(odata, request, edmEntitySet, et, newPOJO);
    assertEquals("localhost.test/BusinessPartnerRoles(BusinessPartnerID='35',RoleCategory='A')", act);
  }

  @Override
  @Test
  public void testConvertEmbeddedIdToLocation() throws ODataJPAProcessorException, SerializerException,
      ODataJPAModelException {

    Map<String, Object> newPOJO = new HashMap<>();
    Map<String, Object> primaryKey = new HashMap<>();

    primaryKey.put("codeID", "NUTS1");
    primaryKey.put("codePublisher", "Eurostat");
    primaryKey.put("divisionCode", "BE1");
    primaryKey.put("language", "fr");
    newPOJO.put("key", primaryKey);

    prepareConvertEmbeddedIdToLocation();

    String act = cut.convertKeyToLocal(odata, request, edmEntitySet, et, newPOJO);
    assertEquals(
        "localhost.test/AdministrativeDivisionDescriptions(DivisionCode='BE1',CodeID='NUTS1',CodePublisher='Eurostat',Language='fr')",
        act);
  }

  @Override
  @Test
  public void testConvertSimpleKeyToLocation() throws ODataJPAProcessorException, SerializerException,
      ODataJPAModelException {

    Map<String, Object> newPOJO = new HashMap<>();
    newPOJO.put("iD", "35");

    prepareConvertSimpleKeyToLocation();
    when(et.getTypeClass()).then((Answer<Class<?>>) invocation -> Organization.class);

    String act = cut.convertKeyToLocal(odata, request, edmEntitySet, et, newPOJO);
    assertEquals("localhost.test/Organisation('35')", act);
  }
}