package nl.buildforce.sequoia.processor.core.modify;

import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAAttribute;
import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAEntityType;
import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAPath;
import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAStructuredType;
import nl.buildforce.sequoia.metadata.core.edm.mapper.exception.ODataJPAModelException;
import nl.buildforce.sequoia.processor.core.exception.ODataJPAProcessorException;
import nl.buildforce.olingo.commons.api.data.Entity;
import nl.buildforce.olingo.commons.api.data.Property;
import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmEntitySet;
import nl.buildforce.olingo.commons.api.edm.EdmEntityType;
import nl.buildforce.olingo.commons.api.edm.EdmStructuredType;
import nl.buildforce.olingo.server.api.OData;
import nl.buildforce.olingo.server.api.ODataRequest;
import nl.buildforce.olingo.server.api.serializer.SerializerException;
import nl.buildforce.olingo.server.api.uri.UriHelper;
import nl.buildforce.olingo.server.api.uri.UriParameter;
import nl.buildforce.olingo.server.api.uri.UriResourceEntitySet;
import nl.buildforce.olingo.server.api.uri.queryoption.ExpandOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SelectOption;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class TestJPAConversionHelper {

  protected JPAConversionHelper cut;
  protected EdmEntitySet edmEntitySet;
  protected ODataRequest request;
  protected OData odata;
  protected JPAEntityType et;

  @Test
  public abstract void testConvertCompoundKeyToLocation() throws ODataJPAProcessorException, SerializerException,
      ODataJPAModelException;

  @Test
  public abstract void testConvertEmbeddedIdToLocation() throws ODataJPAProcessorException, SerializerException,
      ODataJPAModelException;

  @Test
  public abstract void testConvertSimpleKeyToLocation() throws ODataJPAProcessorException, SerializerException,
      ODataJPAModelException;

  protected void prepareConvertCompoundKeyToLocation() throws ODataJPAModelException {

    final List<JPAPath> keyPath = new ArrayList<>();

    request = mock(ODataRequest.class);
    et = mock(JPAEntityType.class);
    when(request.getRawBaseUri()).thenReturn("localhost.test");
    when(et.getKeyPath()).thenReturn(keyPath);
    edmEntitySet = mock(EdmEntitySet.class);

    addKeyAttribute(keyPath, "BusinessPartnerID", "businessPartnerID");
    addKeyAttribute(keyPath, "RoleCategory", "roleCategory");

    odata = mock(OData.class);
    UriHelper uriHelper = new UriHelperSpy(UriHelperSpy.COMPOUND_KEY);
    when(odata.createUriHelper()).thenReturn(uriHelper);
  }

  protected void prepareConvertSimpleKeyToLocation() throws ODataJPAModelException {
    final List<JPAPath> keyPath = new ArrayList<>();

    request = mock(ODataRequest.class);
    et = mock(JPAEntityType.class);
    when(request.getRawBaseUri()).thenReturn("localhost.test");
    edmEntitySet = mock(EdmEntitySet.class);
    when(et.getKeyPath()).thenReturn(keyPath);

    addKeyAttribute(keyPath, "ID", "iD");

    odata = mock(OData.class);
    UriHelper uriHelper = new UriHelperSpy(UriHelperSpy.SINGLE);
    when(odata.createUriHelper()).thenReturn(uriHelper);
  }

  void addKeyAttribute(final List<JPAPath> keyPath, final String externalName, final String internalName) {
    JPAPath key;
    JPAAttribute keyAttribute;
    key = mock(JPAPath.class);
    keyPath.add(key);
    keyAttribute = mock(JPAAttribute.class);
    when(keyAttribute.getExternalName()).thenReturn(externalName);
    when(keyAttribute.getInternalName()).thenReturn(internalName);
    when(key.getLeaf()).thenReturn(keyAttribute);
  }

  protected void prepareConvertEmbeddedIdToLocation() throws ODataJPAModelException {
    List<JPAPath> keyPath = new ArrayList<>();

    request = mock(ODataRequest.class);
    when(request.getRawBaseUri()).thenReturn("localhost.test");

    edmEntitySet = mock(EdmEntitySet.class);
    et = mock(JPAEntityType.class);
    when(et.getKeyPath()).thenReturn(keyPath);
    JPAPath key = mock(JPAPath.class);
    keyPath.add(key);
    JPAAttribute keyAttribute = mock(JPAAttribute.class);
    when(keyAttribute.getExternalName()).thenReturn("Key");
    when(keyAttribute.getInternalName()).thenReturn("key");
    when(keyAttribute.isComplex()).thenReturn(true);
    when(keyAttribute.isKey()).thenReturn(true);
    when(key.getLeaf()).thenReturn(keyAttribute);

    JPAStructuredType st = mock(JPAStructuredType.class);
    when(keyAttribute.getStructuredType()).thenReturn(st);
    keyPath = new ArrayList<>();
    when(st.getPathList()).thenReturn(keyPath);

    addKeyAttribute(keyPath, "CodeID", "codeID");
    addKeyAttribute(keyPath, "CodePublisher", "codePublisher");
    addKeyAttribute(keyPath, "DivisionCode", "divisionCode");
    addKeyAttribute(keyPath, "Language", "language");

    odata = mock(OData.class);
    UriHelper uriHelper = new UriHelperSpy(UriHelperSpy.EMBEDDED_ID);
    when(odata.createUriHelper()).thenReturn(uriHelper);

  }

  static class UriHelperSpy implements UriHelper {
    public static final String EMBEDDED_ID = "EmbeddedId";
    public static final String COMPOUND_KEY = "CompoundKey";
    public static final String SINGLE = "SingleID";
    private final String mode;

    public UriHelperSpy(String mode) {
      this.mode = mode;
    }

    @Override
    public String buildCanonicalURL(EdmEntitySet edmEntitySet, Entity entity) {
      switch (mode) {
        case EMBEDDED_ID: {
          assertEquals(4, entity.getProperties().size());
          int found = 0;
          for (final Property property : entity.getProperties()) {
            if (property.getName().equals("DivisionCode") && property.getValue().equals("BE1"))
              found++;
            else if (property.getName().equals("Language") && property.getValue().equals("fr"))
              found++;
          }
          assertEquals(2, found, "Not all key attributes found");
          return "AdministrativeDivisionDescriptions(DivisionCode='BE1',CodeID='NUTS1',CodePublisher='Eurostat',Language='fr')";
        }
        case COMPOUND_KEY: {
          assertEquals(2, entity.getProperties().size());
          int found = 0;
          for (final Property property : entity.getProperties()) {
            if (property.getName().equals("BusinessPartnerID") && property.getValue().equals("35"))
              found++;
            else if (property.getName().equals("RoleCategory") && property.getValue().equals("A"))
              found++;
          }
          assertEquals(2, found, "Not all key attributes found");
          return "BusinessPartnerRoles(BusinessPartnerID='35',RoleCategory='A')";
        }
        case SINGLE:
          assertEquals(1, entity.getProperties().size());
          assertEquals("35", entity.getProperties().get(0).getValue());
          return "Organisation('35')";
      }
      fail();
      return null;

    }

    @Override
    public String buildContextURLKeyPredicate(List<UriParameter> keys) {
      fail();
      return null;
    }

    @Override
    public String buildContextURLSelectList(EdmStructuredType type, ExpandOption expand, SelectOption select) {
      fail();
      return null;
    }

    @Override
    public String buildKeyPredicate(EdmEntityType edmEntityType, Entity entity) {
      fail();
      return null;
    }

    @Override
    public UriResourceEntitySet parseEntityId(Edm edm, String entityId, String rawServiceRoot) {
      fail();
      return null;
    }

  }
}