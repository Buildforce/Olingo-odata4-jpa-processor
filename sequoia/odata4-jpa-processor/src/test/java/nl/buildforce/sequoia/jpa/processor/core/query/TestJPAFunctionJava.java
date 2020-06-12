package nl.buildforce.sequoia.jpa.processor.core.query;

import nl.buildforce.sequoia.jpa.metadata.api.JPAEdmProvider;
import nl.buildforce.sequoia.jpa.processor.core.api.JPAODataCRUDContextAccess;
import nl.buildforce.sequoia.jpa.processor.core.api.JPAODataRequestContextAccess;
import nl.buildforce.sequoia.jpa.processor.core.processor.JPAFunctionRequestProcessor;
import nl.buildforce.sequoia.jpa.processor.core.serializer.JPAOperationSerializer;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.DataSourceHelper;
import nl.buildforce.sequoia.jpa.processor.core.testobjects.TestFunctionParameter;
import org.apache.olingo.commons.api.data.Annotatable;
import org.apache.olingo.commons.api.edm.EdmFunction;
import org.apache.olingo.commons.api.edm.EdmParameter;
import org.apache.olingo.commons.api.edm.EdmReturnType;
import org.apache.olingo.commons.api.edm.EdmType;
import org.apache.olingo.commons.api.edm.constants.EdmTypeKind;
import org.apache.olingo.commons.api.ex.ODataException;
import org.apache.olingo.commons.api.format.ContentType;
import org.apache.olingo.commons.core.edm.primitivetype.EdmInt32;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.ODataLibraryException;
import org.apache.olingo.server.api.ODataRequest;
import org.apache.olingo.server.api.ODataResponse;
import org.apache.olingo.server.api.serializer.SerializerResult;
import org.apache.olingo.server.api.uri.UriInfo;
import org.apache.olingo.server.api.uri.UriParameter;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourceFunction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import static org.eclipse.persistence.config.EntityManagerProperties.NON_JTA_DATASOURCE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestJPAFunctionJava {
  protected static final String PUNIT_NAME = "nl.buildforce.sequoia.jpa";

  private JPAFunctionRequestProcessor cut;
  // private List<UriResource> uriResources;
  private ODataRequest request;
  private ODataResponse response;
  private UriResourceFunction uriResource;
  private EdmFunction edmFunction;

  @BeforeEach
  public void setup() throws ODataException {
    OData odata = mock(OData.class);
    JPAODataCRUDContextAccess context = mock(JPAODataCRUDContextAccess.class);
    JPAODataRequestContextAccess requestContext = mock(JPAODataRequestContextAccess.class);
    EntityManager em = mock(EntityManager.class);
    UriInfo uriInfo = mock(UriInfo.class);
    JPAOperationSerializer serializer = mock(JPAOperationSerializer.class);
    SerializerResult serializerResult = mock(SerializerResult.class);

    DataSource ds = DataSourceHelper.createDataSource(DataSourceHelper.DB_HSQLDB);
    Map<String, Object> properties = new HashMap<>();
    properties.put(NON_JTA_DATASOURCE, ds);
    final EntityManagerFactory emf = Persistence.createEntityManagerFactory(PUNIT_NAME, properties);
        List<UriResource> uriResources = new ArrayList<>();
    when(uriInfo.getUriResourceParts()).thenReturn(uriResources);
    when(context.getEdmProvider()).thenReturn(new JPAEdmProvider(PUNIT_NAME, emf, null, new String[] {
        "nl.buildforce.sequoia.jpa.processor.core", "nl.buildforce.sequoia.jpa.processor.core.testmodel" }));
    when(requestContext.getUriInfo()).thenReturn(uriInfo);
    when(requestContext.getEntityManager()).thenReturn(em);
    when(requestContext.getSerializer()).thenReturn(serializer);
    when(serializer.serialize(any(Annotatable.class), any(EdmType.class), any(ODataRequest.class)))
        .thenReturn(serializerResult);

    request = mock(ODataRequest.class);
    response = mock(ODataResponse.class);
    uriResource = mock(UriResourceFunction.class);
    edmFunction = mock(EdmFunction.class);
    uriResources.add(uriResource);
    when(uriResource.getFunction()).thenReturn(edmFunction);

    cut = new JPAFunctionRequestProcessor(odata, context, requestContext);
  }

  @AfterEach
  public void teardown() {
    TestFunctionParameter.calls = 0;
    TestFunctionParameter.param1 = 0;
    TestFunctionParameter.param2 = 0;
  }

  @Test
  public void testCallsFunction() throws ODataApplicationException, ODataLibraryException {
    EdmParameter edmParamA = mock(EdmParameter.class);
    EdmParameter edmParamB = mock(EdmParameter.class);
    EdmReturnType edmReturn = mock(EdmReturnType.class);
    EdmType edmType = mock(EdmType.class);

    when(edmFunction.getReturnType()).thenReturn(edmReturn);
    when(edmFunction.getName()).thenReturn("Sum");
    when(edmFunction.getNamespace()).thenReturn(PUNIT_NAME);
    when(edmFunction.getParameter("A")).thenReturn(edmParamA);
    when(edmParamA.getType()).thenReturn(new EdmInt32());
    when(edmFunction.getParameter("B")).thenReturn(edmParamB);
    when(edmParamB.getType()).thenReturn(new EdmInt32());
    List<UriParameter> parameterList = buildParameters();
    when(uriResource.getParameters()).thenReturn(parameterList);
    when(edmReturn.getType()).thenReturn(edmType);
    when(edmType.getKind()).thenReturn(EdmTypeKind.PRIMITIVE);

    cut.retrieveData(request, response, ContentType.JSON);
    assertEquals(1, TestFunctionParameter.calls);
  }

  @Test
  public void testProvidesParameter() throws ODataApplicationException, ODataLibraryException {
    EdmParameter edmParamA = mock(EdmParameter.class);
    EdmParameter edmParamB = mock(EdmParameter.class);
    EdmReturnType edmReturn = mock(EdmReturnType.class);
    EdmType edmType = mock(EdmType.class);

    when(edmFunction.getReturnType()).thenReturn(edmReturn);
    when(edmFunction.getName()).thenReturn("Sum");
    when(edmFunction.getNamespace()).thenReturn(PUNIT_NAME);
    when(edmFunction.getParameter("A")).thenReturn(edmParamA);
    when(edmParamA.getType()).thenReturn(new EdmInt32());
    when(edmFunction.getParameter("B")).thenReturn(edmParamB);
    when(edmParamB.getType()).thenReturn(new EdmInt32());
    List<UriParameter> parameterList = buildParameters();
    when(uriResource.getParameters()).thenReturn(parameterList);
    when(edmReturn.getType()).thenReturn(edmType);
    when(edmType.getKind()).thenReturn(EdmTypeKind.PRIMITIVE);

    cut.retrieveData(request, response, ContentType.JSON);
    assertEquals(5, TestFunctionParameter.param1);
    assertEquals(7, TestFunctionParameter.param2);
  }

  private List<UriParameter> buildParameters() {
    UriParameter param1 = mock(UriParameter.class);
    UriParameter param2 = mock(UriParameter.class);
    when(param1.getName()).thenReturn("A");
    when(param1.getText()).thenReturn("5");
    when(param2.getName()).thenReturn("B");
    when(param2.getText()).thenReturn("7");
    return Arrays.asList(param1, param2);
  }

}