package nl.buildforce.sequoia.processor.core.api;

import nl.buildforce.sequoia.processor.core.exception.ODataJPAProcessorException;
import nl.buildforce.olingo.commons.api.ex.ODataException;
import nl.buildforce.olingo.commons.api.format.ContentType;
import nl.buildforce.olingo.commons.api.http.HttpStatusCode;
import nl.buildforce.olingo.server.api.OData;
import nl.buildforce.olingo.server.api.ODataRequest;
import nl.buildforce.olingo.server.api.ODataResponse;
import nl.buildforce.olingo.server.api.ServiceMetadata;
import nl.buildforce.olingo.server.api.serializer.SerializerException;
import nl.buildforce.olingo.server.api.uri.UriInfo;
import nl.buildforce.olingo.server.api.uri.UriResource;
import nl.buildforce.olingo.server.api.uri.UriResourceKind;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestJPAODataRequestProcessor {

  private static JPAODataRequestProcessor cut;
  private static ODataRequest request;
  private static ODataResponse response;
  private static UriInfo uriInfo;
  private static OData odata;

  static Stream<Executable> modifyMediaTypeMethodsProvider() {
    return Stream.of(() -> cut.createMediaEntity(null, null, null, null, null), () -> cut.updateMediaEntity(null, null, null, null, null), () -> cut.deleteMediaEntity(null, null, null));
  }

  static Stream<Executable> updatePrimitiveValueMethodsProvider() {
    return Stream.of(() -> cut.updatePrimitiveValue());
  }

  static Stream<Executable> modifyComplexValueMethodsProvider() {
    return Stream.of(() -> cut.updateComplex(null, null, null, null, null), () -> cut.deleteComplex(null, null, null));
  }

  static Stream<Executable> throwsSerializerExceptionMethodsProvider() throws SerializerException {
    // when(odata.createSerializer(ContentType.APPLICATION_JSON)).thenThrow(SerializerException.class);
    when(odata.createSerializer(ContentType.APPLICATION_JSON, Collections.emptyList()))
        .thenThrow(SerializerException.class);
    return Stream.of(() -> cut.createEntity(request, response, uriInfo, ContentType.APPLICATION_JSON, ContentType.APPLICATION_JSON), () -> cut.updateEntity(request, response, uriInfo, ContentType.APPLICATION_JSON, ContentType.APPLICATION_JSON), () -> cut.readEntity(request, response, uriInfo, ContentType.APPLICATION_JSON));
  }

  @BeforeAll
  public static void classSetup() {
    EntityManager em = mock(EntityManager.class);
    JPAODataClaimsProvider claims = new JPAODataClaimsProvider();
    JPAODataCRUDContextAccess sessionContext = mock(JPAODataCRUDContextAccess.class);
    JPAODataRequestContextAccess requestContext = mock(JPAODataRequestContextAccess.class);
    request = mock(ODataRequest.class);
    response = mock(ODataResponse.class);
    uriInfo = mock(UriInfo.class);
    odata = mock(OData.class);
    ServiceMetadata serviceMetadata = mock(ServiceMetadata.class);
    List<UriResource> resourceParts = new ArrayList<>(0);
    UriResource resourcePart = mock(UriResource.class);
    resourceParts.add(resourcePart);

    when(uriInfo.getUriResourceParts()).thenReturn(resourceParts);
    when(resourcePart.getKind()).thenReturn(UriResourceKind.navigationProperty);
    when(requestContext.getClaimsProvider()).thenReturn(Optional.ofNullable(claims));
    when(requestContext.getEntityManager()).thenReturn(em);
    cut = new JPAODataRequestProcessor(sessionContext, requestContext);
    cut.init(odata, serviceMetadata);
  }

  @ParameterizedTest
  @MethodSource("modifyMediaTypeMethodsProvider")
  public void checkModifyMediaEntityThrowsNotImplemented(final Executable m) {

    final ODataJPAProcessorException act = assertThrows(ODataJPAProcessorException.class, m);
    assertEquals(HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), act.getStatusCode());
  }

  @ParameterizedTest
  @MethodSource("updatePrimitiveValueMethodsProvider")
  public void checkUpdatePrimitiveValueThrowsNotImplemented(final Executable m) {

    final ODataJPAProcessorException act = assertThrows(ODataJPAProcessorException.class, m);
    assertEquals(HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), act.getStatusCode());
  }

  @ParameterizedTest
  @MethodSource("throwsSerializerExceptionMethodsProvider")
  public void checkCreateEntityPropagateSerializerException(final Executable m) {

    assertThrows(ODataException.class, m);
  }

  @Test
  public void checkUpdateEntityPropagateSerializerException() {

    assertThrows(ODataException.class, () -> cut.updateEntity(request, response, uriInfo, ContentType.APPLICATION_JSON, ContentType.APPLICATION_JSON));
  }

}