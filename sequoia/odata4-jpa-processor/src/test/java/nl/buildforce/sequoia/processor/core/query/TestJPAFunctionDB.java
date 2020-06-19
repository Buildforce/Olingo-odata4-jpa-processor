package nl.buildforce.sequoia.processor.core.query;

import nl.buildforce.sequoia.metadata.api.JPAEdmProvider;
import nl.buildforce.sequoia.metadata.core.edm.annotation.EdmFunctionType;
import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPADataBaseFunction;
import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAOperationResultParameter;
import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAServiceDocument;
import nl.buildforce.sequoia.metadata.core.edm.mapper.exception.ODataJPAModelException;
import nl.buildforce.sequoia.processor.core.api.JPAODataCRUDContextAccess;
import nl.buildforce.sequoia.processor.core.api.JPAODataDatabaseProcessor;
import nl.buildforce.sequoia.processor.core.api.JPAODataRequestContextAccess;
import nl.buildforce.sequoia.processor.core.processor.JPAFunctionRequestProcessor;
import nl.buildforce.sequoia.processor.core.serializer.JPAOperationSerializer;
import nl.buildforce.olingo.commons.api.data.Annotatable;
import nl.buildforce.olingo.commons.api.edm.EdmFunction;
import nl.buildforce.olingo.commons.api.edm.EdmReturnType;
import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;
import nl.buildforce.olingo.commons.api.ex.ODataException;
import nl.buildforce.olingo.commons.api.format.ContentType;
import nl.buildforce.olingo.commons.core.edm.primitivetype.EdmBoolean;
import nl.buildforce.olingo.server.api.OData;
import nl.buildforce.olingo.server.api.ODataApplicationException;
import nl.buildforce.olingo.server.api.ODataLibraryException;
import nl.buildforce.olingo.server.api.ODataRequest;
import nl.buildforce.olingo.server.api.ODataResponse;
import nl.buildforce.olingo.server.api.serializer.SerializerResult;
import nl.buildforce.olingo.server.api.uri.UriInfo;
import nl.buildforce.olingo.server.api.uri.UriResource;
import nl.buildforce.olingo.server.api.uri.UriResourceFunction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;

import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TestJPAFunctionDB {
  protected static final String PUNIT_NAME = "nl.buildforce.sequoia";

  private JPAODataDatabaseProcessor dbProcessor;

  private OData odata;
  private JPAODataCRUDContextAccess context;
  private JPAODataRequestContextAccess requestContext;
  private ODataRequest request;
  private ODataResponse response;
  private JPAFunctionRequestProcessor cut;
  private EdmFunction edmFunction;
  private UriInfo uriInfo;
  private List<UriResource> uriResources;
  private UriResourceFunction uriResource;
  private JPAServiceDocument sd;
  private JPADataBaseFunction function;
  private JPAOperationSerializer serializer;
  private SerializerResult serializerResult;
  private EntityManager em;

  @BeforeEach
  public void setup() throws ODataException {
    final JPAEdmProvider provider = mock(JPAEdmProvider.class);

    em = mock(EntityManager.class);
    request = mock(ODataRequest.class);
    response = mock(ODataResponse.class);
    uriInfo = mock(UriInfo.class);
    odata = mock(OData.class);
    serializer = mock(JPAOperationSerializer.class);
    serializerResult = mock(SerializerResult.class);
    context = mock(JPAODataCRUDContextAccess.class);
    requestContext = mock(JPAODataRequestContextAccess.class);
    dbProcessor = mock(JPAODataDatabaseProcessor.class);
    sd = mock(JPAServiceDocument.class);
    uriResource = mock(UriResourceFunction.class);
    function = mock(JPADataBaseFunction.class);
    uriResources = new ArrayList<>();
    edmFunction = mock(EdmFunction.class);

    when(requestContext.getSerializer()).thenReturn(serializer);
    when(serializer.serialize(any(Annotatable.class), any(EdmType.class), any(ODataRequest.class)))
        .thenReturn(serializerResult);
    when(requestContext.getUriInfo()).thenReturn(uriInfo);
    when(requestContext.getEntityManager()).thenReturn(em);
    when(uriInfo.getUriResourceParts()).thenReturn(uriResources);
    when(context.getDatabaseProcessor()).thenReturn(dbProcessor);
    when(context.getEdmProvider()).thenReturn(provider);
    when(provider.getServiceDocument()).thenReturn(sd);
    uriResources.add(uriResource);
    when(uriResource.getFunction()).thenReturn(edmFunction);
    when(sd.getFunction(edmFunction)).thenReturn(function);
    when(function.getFunctionType()).thenReturn(EdmFunctionType.UserDefinedFunction);
    cut = new JPAFunctionRequestProcessor(odata, context, requestContext);
  }

  @Test
  public void testCallsFunctionWithBooleanReturnType() throws ODataApplicationException, ODataLibraryException,
          ODataJPAModelException {

    EdmReturnType edmReturnType = mock(EdmReturnType.class);
    JPAOperationResultParameter resultParam = mock(JPAOperationResultParameter.class);
    when(function.getResultParameter()).thenReturn(resultParam);
    when(resultParam.getTypeFQN()).thenReturn(new FullQualifiedName(PUNIT_NAME, "CheckRights"));
    when(resultParam.getType()).thenAnswer((Answer<Class<?>>) invocation -> Boolean.class);

    when(edmFunction.getReturnType()).thenReturn(edmReturnType);
    when(edmReturnType.getType()).thenReturn(new EdmBoolean());

    cut.retrieveData(request, response, ContentType.JSON);
    verify(dbProcessor, times(1)).executeFunctionQuery(eq(uriResources), eq(function), eq(em));
  }

  @Test
  public void testCallsFunctionCount() throws ODataApplicationException, ODataLibraryException,
      ODataJPAModelException {

    EdmReturnType edmReturnType = mock(EdmReturnType.class);
    JPAOperationResultParameter resultParam = mock(JPAOperationResultParameter.class);
    when(function.getResultParameter()).thenReturn(resultParam);
    when(resultParam.getTypeFQN()).thenReturn(new FullQualifiedName(PUNIT_NAME, "CheckRights"));
    when(resultParam.getType()).thenAnswer((Answer<Class<?>>) invocation -> Boolean.class);

    when(edmFunction.getReturnType()).thenReturn(edmReturnType);
    when(edmReturnType.getType()).thenReturn(new EdmBoolean());

    cut.retrieveData(request, response, ContentType.JSON);
    verify(dbProcessor, times(1)).executeFunctionQuery(eq(uriResources), eq(function), eq(em));
  }

}