package nl.buildforce.sequoia.jpa.processor.core.processor;

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAAssociationPath;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAEntityType;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAStructuredType;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAException;

import nl.buildforce.sequoia.jpa.processor.core.api.JPAAbstractCUDRequestHandler;
import nl.buildforce.sequoia.jpa.processor.core.api.JPACUDRequestHandler;
import nl.buildforce.sequoia.jpa.processor.core.api.JPAODataClaimProvider;
import nl.buildforce.sequoia.jpa.processor.core.api.JPAODataClaimsProvider;
import nl.buildforce.sequoia.jpa.processor.core.api.JPAODataGroupProvider;
import nl.buildforce.sequoia.jpa.processor.core.api.JPAODataGroupsProvider;
import nl.buildforce.sequoia.jpa.processor.core.exception.ODataJPAProcessException;
import nl.buildforce.sequoia.jpa.processor.core.exception.ODataJPAProcessorException;
import nl.buildforce.sequoia.jpa.processor.core.exception.ODataJPASerializerException;
import nl.buildforce.sequoia.jpa.processor.core.modify.JPAConversionHelper;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.AdministrativeDivision;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.AdministrativeDivisionKey;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.BusinessPartnerRole;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.Organization;
import org.apache.olingo.commons.api.data.EntityCollection;

import org.apache.olingo.commons.api.edm.EdmEntityContainer;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmNavigationProperty;
import org.apache.olingo.commons.api.edm.EdmNavigationPropertyBinding;
import org.apache.olingo.commons.api.edm.EdmKeyPropertyRef;
import org.apache.olingo.commons.api.edm.EdmPrimitiveType;
import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeException;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.edm.Edm;
import org.apache.olingo.commons.api.edm.EdmProperty;
import org.apache.olingo.commons.api.ex.ODataException;
import org.apache.olingo.commons.api.format.ContentType;
import org.apache.olingo.commons.api.http.HttpHeader;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.*;
import org.apache.olingo.server.api.serializer.SerializerException;
import org.apache.olingo.server.api.uri.UriParameter;
import org.apache.olingo.server.api.uri.UriResourceKind;
import org.apache.olingo.server.api.uri.UriResourceNavigation;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import jakarta.persistence.EntityManager;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Optional;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestJPACreateProcessor extends TestJPAModifyProcessor {

  @Test
  public void testHookIsCalled() throws ODataJPAException, ODataLibraryException, ODataApplicationException {
    ODataResponse response = new ODataResponse();
    ODataRequest request = prepareSimpleRequest();

    RequestHandleSpy spy = new RequestHandleSpy();
    when(requestContext.getCUDRequestHandler()).thenReturn(spy);

    processor.createEntity(request, response, ContentType.JSON, ContentType.JSON);

    assertTrue(spy.called);
  }

  @Test
  public void testEntityTypeProvided() throws ODataJPAException, ODataApplicationException, ODataLibraryException {
    ODataResponse response = new ODataResponse();
    ODataRequest request = prepareSimpleRequest();

    RequestHandleSpy spy = new RequestHandleSpy();
    when(requestContext.getCUDRequestHandler()).thenReturn(spy);

    processor.createEntity(request, response, ContentType.JSON, ContentType.JSON);

    assertEquals("Organization", spy.et.getExternalName());
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testAttributesProvided() throws ODataJPAException, ODataApplicationException, ODataLibraryException {
    ODataResponse response = new ODataResponse();
    ODataRequest request = prepareSimpleRequest();
    Map<String, Object> attributes = new HashMap<>(1);

    attributes.put("ID", "35");

    RequestHandleSpy spy = new RequestHandleSpy();
    when(requestContext.getCUDRequestHandler()).thenReturn(spy);

    when(convHelper.convertProperties(ArgumentMatchers.any(OData.class), ArgumentMatchers.any(JPAStructuredType.class),
        ArgumentMatchers.any(
            List.class))).thenReturn(attributes);

    processor.createEntity(request, response, ContentType.JSON, ContentType.JSON);

    assertNotNull(spy.jpaAttributes);
    assertEquals(1, spy.jpaAttributes.size());
    assertEquals("35", spy.jpaAttributes.get("ID"));
  }

  @Test
  public void testHeadersProvided() throws ODataJPAException, ODataApplicationException, ODataLibraryException {
    final ODataResponse response = new ODataResponse();
    final ODataRequest request = prepareSimpleRequest();
    final Map<String, List<String>> headers = new HashMap<>();

    when(request.getAllHeaders()).thenReturn(headers);
    headers.put("If-Match", Collections.singletonList("2"));

    RequestHandleSpy spy = new RequestHandleSpy();
    when(requestContext.getCUDRequestHandler()).thenReturn(spy);

    processor.createEntity(request, response, ContentType.JSON, ContentType.JSON);

    assertNotNull(spy.headers);
    assertEquals(1, spy.headers.size());
    assertNotNull(spy.headers.get("If-Match"));
    assertEquals("2", spy.headers.get("If-Match").get(0));
  }

  @Test
  public void testClaimsProvided() throws ODataJPAException, ODataApplicationException, ODataLibraryException {
    final ODataResponse response = new ODataResponse();
    final ODataRequest request = prepareSimpleRequest();

    final RequestHandleSpy spy = new RequestHandleSpy();
    final JPAODataClaimProvider provider = new JPAODataClaimsProvider();
    final Optional<JPAODataClaimProvider> claims = Optional.of(provider);
    when(requestContext.getCUDRequestHandler()).thenReturn(spy);
    when(requestContext.getClaimsProvider()).thenReturn(claims);

    processor.createEntity(request, response, ContentType.JSON, ContentType.JSON);

    assertNotNull(spy.claims);
    assertTrue(spy.claims.isPresent());
    assertEquals(provider, spy.claims.get());
  }

  @Test
  public void testGroupsProvided() throws ODataJPAException, ODataLibraryException, ODataApplicationException {
    final ODataResponse response = new ODataResponse();
    final ODataRequest request = prepareSimpleRequest();

    final RequestHandleSpy spy = new RequestHandleSpy();
    final JPAODataGroupsProvider provider = new JPAODataGroupsProvider();
    provider.addGroup("Person");
    // final List<String> groups = new ArrayList<>(Arrays.asList("Person"));
    final Optional<JPAODataGroupProvider> groups = Optional.of(provider);
    when(requestContext.getCUDRequestHandler()).thenReturn(spy);
    when(requestContext.getGroupsProvider()).thenReturn(groups);

    processor.createEntity(request, response, ContentType.JSON, ContentType.JSON);

    assertNotNull(spy.groups);
    assertFalse(spy.groups.isEmpty());
    assertEquals("Person", spy.groups.get(0));
  }

  @Test
  public void testThrowExceptionOnAccessTransaction() throws ODataJPAException, ODataLibraryException, ODataApplicationException {
    ODataResponse response = new ODataResponse();
    ODataRequest request = prepareSimpleRequest();

    JPACUDRequestHandler handler = mock(JPACUDRequestHandler.class);
    when(requestContext.getCUDRequestHandler()).thenReturn(handler);

    when(em.getTransaction().isActive()).thenThrow(new IllegalStateException("Transaction is not accessible when using JTA with JPA-compliant transaction access enabled"));

    processor.createEntity(request, response, ContentType.JSON, ContentType.JSON);
  }

  @Test
  public void testThrowExpectedExceptionInCaseOfError() throws ODataJPAException, SerializerException, ODataJPAProcessException {
    ODataResponse response = new ODataResponse();
    ODataRequest request = prepareSimpleRequest();

    JPACUDRequestHandler handler = mock(JPACUDRequestHandler.class);
    when(requestContext.getCUDRequestHandler()).thenReturn(handler);

    doThrow(new ODataJPAProcessorException(ODataJPAProcessorException.MessageKeys.NOT_SUPPORTED_DELETE,
        HttpStatusCode.BAD_REQUEST)).when(handler).createEntity(any(JPARequestEntity.class), any(EntityManager.class));

    try {
      processor.createEntity(request, response, ContentType.JSON, ContentType.JSON);
    } catch (ODataApplicationException | ODataLibraryException e) {
        assertTrue(true);
      return;
    }
    fail();
  }

  @Test
  public void testThrowUnexpectedExceptionInCaseOfError() throws ODataJPAException, SerializerException, ODataJPAProcessException {
    ODataResponse response = new ODataResponse();
    ODataRequest request = prepareSimpleRequest();

    JPACUDRequestHandler handler = mock(JPACUDRequestHandler.class);
    when(requestContext.getCUDRequestHandler()).thenReturn(handler);

    doThrow(NullPointerException.class).when(handler).createEntity(any(JPARequestEntity.class), any(
        EntityManager.class));

    try {
      processor.createEntity(request, response, ContentType.JSON, ContentType.JSON);
    } catch (ODataApplicationException | ODataLibraryException e) {
        assertTrue(true);
      return;
    }
    fail();
  }

  @Test
  public void testMinimalResponseLocationHeader() throws ODataJPAException, ODataLibraryException, ODataApplicationException {
    ODataResponse response = new ODataResponse();
    ODataRequest request = prepareSimpleRequest();

    RequestHandleSpy spy = new RequestHandleSpy();
    when(requestContext.getCUDRequestHandler()).thenReturn(spy);

    processor.createEntity(request, response, ContentType.JSON, ContentType.JSON);

    assertEquals(LOCATION_HEADER, response.getHeader(HttpHeader.LOCATION));
  }

  @Test
  public void testMinimalResponseODataEntityIdHeader() throws ODataJPAException, ODataLibraryException, ODataApplicationException {
    ODataResponse response = new ODataResponse();
    ODataRequest request = prepareSimpleRequest();

    RequestHandleSpy spy = new RequestHandleSpy();
    when(requestContext.getCUDRequestHandler()).thenReturn(spy);

    processor.createEntity(request, response, ContentType.JSON, ContentType.JSON);

    assertEquals(LOCATION_HEADER, response.getHeader(HttpHeader.ODATA_ENTITY_ID));
  }

  @Test
  public void testMinimalResponseStatusCode() throws ODataJPAException, ODataLibraryException, ODataApplicationException {
    ODataResponse response = new ODataResponse();
    ODataRequest request = prepareSimpleRequest();

    RequestHandleSpy spy = new RequestHandleSpy();
    when(requestContext.getCUDRequestHandler()).thenReturn(spy);

    processor.createEntity(request, response, ContentType.JSON, ContentType.JSON);

    assertEquals(HttpStatusCode.NO_CONTENT.getStatusCode(), response.getStatusCode());
  }

  @Test
  public void testMinimalResponsePreferApplied() throws ODataJPAException, ODataLibraryException, ODataApplicationException {
    ODataResponse response = new ODataResponse();
    ODataRequest request = prepareSimpleRequest();

    RequestHandleSpy spy = new RequestHandleSpy();
    when(requestContext.getCUDRequestHandler()).thenReturn(spy);

    processor.createEntity(request, response, ContentType.JSON, ContentType.JSON);

    assertEquals("return=minimal", response.getHeader(HttpHeader.PREFERENCE_APPLIED));
  }

  @Test
  public void testRepresentationResponseStatusCode() throws ODataJPAException, ODataLibraryException, ODataApplicationException {

    ODataResponse response = new ODataResponse();
    ODataRequest request = prepareRepresentationRequest(new RequestHandleSpy());

    processor.createEntity(request, response, ContentType.JSON, ContentType.JSON);

    assertEquals(HttpStatusCode.CREATED.getStatusCode(), response.getStatusCode());
  }

  @Test
  public void testRepresentationResponseStatusCodeMapResult() throws ODataJPAException, ODataLibraryException, ODataApplicationException {

    ODataResponse response = new ODataResponse();
    ODataRequest request = prepareRepresentationRequest(new RequestHandleMapResultSpy());

    processor.createEntity(request, response, ContentType.JSON, ContentType.JSON);

    assertEquals(HttpStatusCode.CREATED.getStatusCode(), response.getStatusCode());
  }

  @Test
  public void testRepresentationResponseContent() throws ODataJPAException, IOException, ODataLibraryException, ODataApplicationException {

    ODataResponse response = new ODataResponse();
    ODataRequest request = prepareRepresentationRequest(new RequestHandleSpy());

    processor.createEntity(request, response, ContentType.JSON, ContentType.JSON);
    byte[] act = new byte[100];
    response.getContent().read(act);
    String s = new String(act).trim();
    assertEquals("{\"ID\":\"35\"}", s);
  }

  @Test
  public void testRepresentationResponseContentMapResult() throws ODataJPAException, IOException, ODataLibraryException, ODataApplicationException {

    ODataResponse response = new ODataResponse();
    ODataRequest request = prepareRepresentationRequest(new RequestHandleMapResultSpy());

    processor.createEntity(request, response, ContentType.JSON, ContentType.JSON);
    byte[] act = new byte[100];
    response.getContent().read(act);
    String s = new String(act).trim();
    assertEquals("{\"ID\":\"35\"}", s);
  }

  @Test
  public void testRepresentationLocationHeader() throws ODataJPAException, ODataLibraryException, ODataApplicationException {

    ODataResponse response = new ODataResponse();
    ODataRequest request = prepareRepresentationRequest(new RequestHandleSpy());

    processor.createEntity(request, response, ContentType.JSON, ContentType.JSON);

    assertEquals(LOCATION_HEADER, response.getHeader(HttpHeader.LOCATION));
  }

  @Test
  public void testRepresentationLocationHeaderMapResult() throws ODataJPAException, ODataLibraryException, ODataApplicationException {

    ODataResponse response = new ODataResponse();
    ODataRequest request = prepareRepresentationRequest(new RequestHandleMapResultSpy());

    processor.createEntity(request, response, ContentType.JSON, ContentType.JSON);

    assertEquals(LOCATION_HEADER, response.getHeader(HttpHeader.LOCATION));
  }

  @Test
  public void testCallsValidateChangesOnSuccessfulProcessing() throws ODataJPAException, ODataLibraryException, ODataApplicationException {
    ODataResponse response = new ODataResponse();
    ODataRequest request = prepareSimpleRequest();

    RequestHandleSpy spy = new RequestHandleSpy();
    when(requestContext.getCUDRequestHandler()).thenReturn(spy);

    processor.createEntity(request, response, ContentType.JSON, ContentType.JSON);
    assertEquals(1, spy.noValidateCalls);
  }

  @Test
  public void testDoesNotCallsValidateChangesOnForeignTransaction() throws ODataJPAException, ODataLibraryException, ODataApplicationException {
    final ODataResponse response = new ODataResponse();
    final ODataRequest request = prepareSimpleRequest();

    final RequestHandleSpy spy = new RequestHandleSpy();
    when(requestContext.getCUDRequestHandler()).thenReturn(spy);
    when(factory.hasActiveTransaction()).thenReturn(Boolean.TRUE);

    processor.createEntity(request, response, ContentType.JSON, ContentType.JSON);
    assertEquals(0, spy.noValidateCalls);
  }

  @Test
  public void testDoesNotCallsValidateChangesOnError() throws ODataJPAException, SerializerException, ODataJPAProcessException {
    ODataResponse response = new ODataResponse();
    ODataRequest request = prepareSimpleRequest();

    JPACUDRequestHandler handler = mock(JPACUDRequestHandler.class);
    when(requestContext.getCUDRequestHandler()).thenReturn(handler);

    doThrow(new ODataJPAProcessorException(ODataJPAProcessorException.MessageKeys.NOT_SUPPORTED_DELETE,
        HttpStatusCode.BAD_REQUEST)).when(handler).createEntity(any(JPARequestEntity.class), any(EntityManager.class));

    assertThrows(ODataApplicationException.class,
        () -> processor.createEntity(request, response, ContentType.JSON, ContentType.JSON));
    verify(handler, never()).validateChanges(em);
  }

  @Test
  public void testDoesRollbackIfValidateRaisesError() throws ODataJPAException, SerializerException, ODataJPAProcessException {
    ODataResponse response = new ODataResponse();
    ODataRequest request = prepareSimpleRequest();

    JPACUDRequestHandler handler = mock(JPACUDRequestHandler.class);
    when(requestContext.getCUDRequestHandler()).thenReturn(handler);

    doThrow(new ODataJPAProcessorException(ODataJPAProcessorException.MessageKeys.NOT_SUPPORTED_DELETE,
        HttpStatusCode.BAD_REQUEST)).when(handler).validateChanges(em);

    assertThrows(ODataApplicationException.class,
        () -> processor.createEntity(request, response, ContentType.JSON, ContentType.JSON));
    verify(transaction, never()).commit();
    verify(transaction, times(1)).rollback();
  }

  @Test
  public void testDoesRollbackIfCreateRaisesArbitraryError() throws ODataJPAException, SerializerException, ODataJPAProcessException {
    ODataResponse response = new ODataResponse();
    ODataRequest request = prepareSimpleRequest();

    JPACUDRequestHandler handler = mock(JPACUDRequestHandler.class);
    when(requestContext.getCUDRequestHandler()).thenReturn(handler);

    doThrow(new RuntimeException("Test")).when(handler).createEntity(any(), any());

    assertThrows(ODataApplicationException.class,
        () -> processor.createEntity(request, response, ContentType.JSON, ContentType.JSON));
    verify(transaction, never()).commit();
    verify(transaction, times(1)).rollback();
  }

  @Test
  public void testDoesRollbackOnWrongResponse() throws ODataJPAException, SerializerException, ODataJPAProcessException {
    ODataResponse response = new ODataResponse();
    ODataRequest request = prepareSimpleRequest();
    String result = "";
    JPACUDRequestHandler handler = mock(JPACUDRequestHandler.class);
    when(requestContext.getCUDRequestHandler()).thenReturn(handler);
    when(handler.createEntity(any(), any())).thenReturn(result);

    assertThrows(ODataException.class,
        () -> processor.createEntity(request, response, ContentType.JSON, ContentType.JSON));
    verify(transaction, never()).commit();
    verify(transaction, times(1)).rollback();
  }

  @Test
  public void testOwnTransactionCommitted() throws ODataJPAException, ODataLibraryException, ODataApplicationException {
    ODataResponse response = new ODataResponse();
    ODataRequest request = prepareSimpleRequest();

    RequestHandleSpy spy = new RequestHandleSpy();
    when(requestContext.getCUDRequestHandler()).thenReturn(spy);

    processor.createEntity(request, response, ContentType.JSON, ContentType.JSON);
    verify(transaction, times(1)).commit();
  }

  @Test
  public void testResponseCreateChildSameTypeContent() throws ODataJPAException, EdmPrimitiveTypeException, ODataApplicationException, ODataLibraryException {

    when(ets.getName()).thenReturn("AdministrativeDivisions");
    final AdministrativeDivision div = new AdministrativeDivision(new AdministrativeDivisionKey("Eurostat", "NUTS1",
        "DE6"));
    final AdministrativeDivision child = new AdministrativeDivision(new AdministrativeDivisionKey("Eurostat", "NUTS2",
        "DE60"));
    div.getChildren().add(child);
    final RequestHandleSpy spy = new RequestHandleSpy(div);
    final ODataResponse response = new ODataResponse();
    final ODataRequest request = prepareRequestToCreateChild(spy);

    final UriResourceNavigation uriChild = mock(UriResourceNavigation.class);
    final List<UriParameter> uriKeys = new ArrayList<>();
    final EdmNavigationProperty naviProperty = mock(EdmNavigationProperty.class);

    createKeyPredicate(uriKeys, "DivisionCode", "DE6");
    createKeyPredicate(uriKeys, "CodeID", "NUTS1");
    createKeyPredicate(uriKeys, "CodePublisher", "Eurostat");
    when(uriChild.getKind()).thenReturn(UriResourceKind.navigationProperty);
    when(uriChild.getProperty()).thenReturn(naviProperty);
    when(naviProperty.getName()).thenReturn("Children");
    when(uriEts.getKeyPredicates()).thenReturn(uriKeys);
    when(convHelper.convertUriKeys(any(), any(), any())).thenCallRealMethod();
    when(convHelper.buildGetterMap(div)).thenReturn(new JPAConversionHelper().determineGetter(div));
    when(convHelper.buildGetterMap(child)).thenReturn(new JPAConversionHelper().determineGetter(child));
    pathParts.add(uriChild);

    processor = new JPACUDRequestProcessor(odata, serviceMetadata, sessionContext, requestContext, convHelper);
    processor.createEntity(request, response, ContentType.JSON, ContentType.JSON);

    assertNotNull(spy.requestEntity.getKeys());
    assertEquals("DE6", spy.requestEntity.getKeys().get("divisionCode"));
    assertNotNull(spy.requestEntity.getRelatedEntities());
    for (Entry<JPAAssociationPath, List<JPARequestEntity>> c : spy.requestEntity.getRelatedEntities().entrySet())
      assertEquals("Children", c.getKey().getAlias());
  }

  @Test
  public void testResponseCreateChildDifferentTypeContent() throws ODataJPAException, EdmPrimitiveTypeException, ODataApplicationException, ODataLibraryException {

    final Organization org = new Organization("Test");
    final BusinessPartnerRole role = new BusinessPartnerRole();
    role.setBusinessPartner(org);
    role.setRoleCategory("A");
    org.getRoles().add(role);

    final RequestHandleSpy spy = new RequestHandleSpy(org);
    final ODataResponse response = new ODataResponse();
    final ODataRequest request = prepareRequestToCreateChild(spy);

    final UriResourceNavigation uriChild = mock(UriResourceNavigation.class);
    final List<UriParameter> uriKeys = new ArrayList<>();
    final EdmNavigationProperty naviProperty = mock(EdmNavigationProperty.class);
    final EdmNavigationPropertyBinding naviBinding = mock(EdmNavigationPropertyBinding.class);
    final EdmEntityContainer container = mock(EdmEntityContainer.class);
    final List<EdmNavigationPropertyBinding> naviBindings = new ArrayList<>(0);
    final EdmEntitySet targetEts = mock(EdmEntitySet.class);
    naviBindings.add(naviBinding);

    createKeyPredicate(uriKeys, "ID", "Test");
    when(uriChild.getKind()).thenReturn(UriResourceKind.navigationProperty);
    when(uriChild.getProperty()).thenReturn(naviProperty);
    when(naviProperty.getName()).thenReturn("Roles");
    when(uriEts.getKeyPredicates()).thenReturn(uriKeys);
    when(convHelper.convertUriKeys(any(), any(), any())).thenCallRealMethod();
    when(convHelper.buildGetterMap(org)).thenReturn(new JPAConversionHelper().determineGetter(org));
    when(convHelper.buildGetterMap(role)).thenReturn(new JPAConversionHelper().determineGetter(role));
    when(ets.getNavigationPropertyBindings()).thenReturn(naviBindings);
    when(naviBinding.getPath()).thenReturn("Roles");
    when(naviBinding.getTarget()).thenReturn("BusinessPartnerRoles");
    when(ets.getEntityContainer()).thenReturn(container);
    when(container.getEntitySet("BusinessPartnerRoles")).thenReturn(targetEts);

    final FullQualifiedName fqn = new FullQualifiedName("nl.buildforce.sequoia.jpa.BusinessPartnerRole");
    final List<String> keyNames = Arrays.asList("BusinessPartnerID", "RoleCategory");
    final Edm edm = mock(Edm.class);
    final EdmEntityType edmET = mock(EdmEntityType.class);

    when(serviceMetadata.getEdm()).thenReturn(edm);
    when(edm.getEntityType(fqn)).thenReturn(edmET);
    when(edmET.getKeyPredicateNames()).thenReturn(keyNames);
    createKeyProperty(fqn, edmET, "BusinessPartnerID", "Test");
    createKeyProperty(fqn, edmET, "RoleCategory", "A");
    // edmType.getFullQualifiedName().getFullQualifiedNameAsString()

    pathParts.add(uriChild);
    // return serviceMetadata.getEdm().getEntityType(es.getODataEntityType().getExternalFQN());
    // nl.buildforce.sequoia.jpa.BusinessPartnerRole
    processor = new JPACUDRequestProcessor(odata, serviceMetadata, sessionContext, requestContext, convHelper);
    processor.createEntity(request, response, ContentType.JSON, ContentType.JSON);

    assertNotNull(spy.requestEntity.getKeys());
    assertEquals("Test", spy.requestEntity.getKeys().get("iD"));
    assertNotNull(spy.requestEntity.getRelatedEntities());
    for (Entry<JPAAssociationPath, List<JPARequestEntity>> c : spy.requestEntity.getRelatedEntities().entrySet())
      assertEquals("Roles", c.getKey().getAlias());
  }

  protected ODataRequest prepareRequestToCreateChild(JPAAbstractCUDRequestHandler spy) throws ODataJPAException, EdmPrimitiveTypeException, SerializerException, ODataJPAProcessorException, ODataJPASerializerException {
    // .../AdministrativeDivisions(DivisionCode='DE6',CodeID='NUTS1',CodePublisher='Eurostat')/Children
    final ODataRequest request = prepareSimpleRequest("return=representation");

    final FullQualifiedName fqn = new FullQualifiedName("nl.buildforce.sequoia.jpa.AdministrativeDivision");
    final List<String> keyNames = Arrays.asList("DivisionCode", "CodeID", "CodePublisher");
    final Edm edm = mock(Edm.class);
    final EdmEntityType edmET = mock(EdmEntityType.class);

    when(requestContext.getCUDRequestHandler()).thenReturn(spy);

    when(serviceMetadata.getEdm()).thenReturn(edm);
    when(edm.getEntityType(fqn)).thenReturn(edmET);
    when(edmET.getKeyPredicateNames()).thenReturn(keyNames);

    createKeyProperty(fqn, edmET, "DivisionCode", "DE6");
    createKeyProperty(fqn, edmET, "CodeID", "NUTS1");
    createKeyProperty(fqn, edmET, "CodePublisher", "Eurostat");

    createKeyProperty(fqn, edmET, "DivisionCode", "DE60");
    createKeyProperty(fqn, edmET, "CodeID", "NUTS2");
    createKeyProperty(fqn, edmET, "CodePublisher", "Eurostat");

    when(serializer.serialize(ArgumentMatchers.eq(request), ArgumentMatchers.any(EntityCollection.class))).thenReturn(
        serializerResult);
    when(serializerResult.getContent()).thenReturn(new ByteArrayInputStream("{\"ID\":\"35\"}".getBytes()));

    return request;
  }

  private void createKeyPredicate(final List<UriParameter> uriKeys, String name, String value) {
    UriParameter key = mock(UriParameter.class);
    uriKeys.add(key);
    when(key.getName()).thenReturn(name);
    when(key.getText()).thenReturn("'" + value + "'");
  }

  private void createKeyProperty(final FullQualifiedName fqn, final EdmEntityType edmET, String name, String value)
      throws EdmPrimitiveTypeException {
    final EdmKeyPropertyRef refType = mock(EdmKeyPropertyRef.class);
    when(edmET.getKeyPropertyRef(name)).thenReturn(refType);
    when(edmET.getFullQualifiedName()).thenReturn(fqn);
    final EdmProperty edmProperty = mock(EdmProperty.class);
    when(refType.getProperty()).thenReturn(edmProperty);
    when(refType.getName()).thenReturn(name);
    EdmPrimitiveType type = mock(EdmPrimitiveType.class);
    when(edmProperty.getType()).thenReturn(type);
    when(type.valueToString(ArgumentMatchers.eq(value), ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers
        .any(), ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(value);
    when(type.toUriLiteral(ArgumentMatchers.anyString())).thenReturn(value);
  }

  static class RequestHandleSpy extends JPAAbstractCUDRequestHandler {
    public int noValidateCalls;
    public JPAEntityType et;
    public Map<String, Object> jpaAttributes;
    public EntityManager em;
    public boolean called = false;
    public Map<String, List<String>> headers;
    public JPARequestEntity requestEntity;
    private final Object result;
    public Optional<JPAODataClaimProvider> claims;
    public List<String> groups;

    RequestHandleSpy(Object result) {
      this.result = result;
    }

    RequestHandleSpy() {
      this.result = new Organization();
      ((Organization) result).setID("35");
    }

    @Override
    public Object createEntity(final JPARequestEntity requestEntity, EntityManager em) {

      this.et = requestEntity.getEntityType();
      this.jpaAttributes = requestEntity.getData();
      this.em = em;
      this.headers = requestEntity.getAllHeader();
      this.called = true;
      this.requestEntity = requestEntity;
      this.claims = requestEntity.getClaims();
      this.groups = requestEntity.getGroups();
      return result;
    }

    @Override
    public void validateChanges(final EntityManager em) {
      this.noValidateCalls++;
    }

  }

  static class RequestHandleMapResultSpy extends JPAAbstractCUDRequestHandler {
    public JPAEntityType et;
    public Map<String, Object> jpaAttributes;
    public EntityManager em;
    public boolean called = false;
    public JPARequestEntity requestEntity;

    @Override
    public Object createEntity(final JPARequestEntity requestEntity, EntityManager em) {
      Map<String, Object> result = new HashMap<>();
      result.put("iD", "35");
      this.et = requestEntity.getEntityType();
      this.jpaAttributes = requestEntity.getData();
      this.em = em;
      this.called = true;
      this.requestEntity = requestEntity;
      return result;
    }
  }
}