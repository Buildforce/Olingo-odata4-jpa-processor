package nl.buildforce.sequoia.processor.core.processor;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

import nl.buildforce.olingo.commons.api.data.Entity;
import nl.buildforce.olingo.commons.api.data.EntityCollection;
import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmEntitySet;
import nl.buildforce.olingo.commons.api.edm.EdmEntityType;
import nl.buildforce.olingo.commons.api.edm.EdmKeyPropertyRef;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveType;
import nl.buildforce.olingo.commons.api.edm.EdmProperty;
import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;
import nl.buildforce.olingo.commons.api.format.ContentType;
import nl.buildforce.olingo.commons.core.edm.primitivetype.EdmString;
import nl.buildforce.olingo.server.api.OData;
import nl.buildforce.olingo.server.api.ODataRequest;
import nl.buildforce.olingo.server.api.ServiceMetadata;
import nl.buildforce.olingo.server.api.serializer.SerializerException;
import nl.buildforce.olingo.server.api.serializer.SerializerResult;
import nl.buildforce.olingo.server.api.uri.UriInfo;
import nl.buildforce.olingo.server.api.uri.UriParameter;
import nl.buildforce.olingo.server.api.uri.UriResource;
import nl.buildforce.olingo.server.api.uri.UriResourceEntitySet;
import nl.buildforce.olingo.server.api.uri.UriResourceKind;
import static nl.buildforce.olingo.commons.api.http.HttpHeader.PREFER;

import nl.buildforce.sequoia.metadata.api.JPAEdmMetadataPostProcessor;
import nl.buildforce.sequoia.metadata.api.JPAEdmProvider;
import nl.buildforce.sequoia.metadata.api.JPAEntityManagerFactory;
import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAEntityType;
import nl.buildforce.sequoia.metadata.core.edm.mapper.exception.ODataJPAException;
import nl.buildforce.sequoia.processor.core.api.JPAAbstractCUDRequestHandler;
import nl.buildforce.sequoia.processor.core.api.JPAODataCRUDContextAccess;
import nl.buildforce.sequoia.processor.core.api.JPAODataRequestContextAccess;
import nl.buildforce.sequoia.processor.core.api.JPAODataTransactionFactory.JPAODataTransaction;
import nl.buildforce.sequoia.processor.core.api.JPAODataTransactionFactory;
import nl.buildforce.sequoia.processor.core.exception.ODataJPAProcessorException;
import nl.buildforce.sequoia.processor.core.exception.ODataJPASerializerException;
import nl.buildforce.sequoia.processor.core.modify.JPAConversionHelper;
import nl.buildforce.sequoia.processor.core.query.EdmEntitySetInfo;
import nl.buildforce.sequoia.processor.core.serializer.JPASerializer;
import nl.buildforce.sequoia.processor.core.testmodel.AdministrativeDivision;
import nl.buildforce.sequoia.processor.core.testmodel.AdministrativeDivisionKey;
import nl.buildforce.sequoia.processor.core.testmodel.DataSourceHelper;
import nl.buildforce.sequoia.processor.core.testmodel.Organization;
import nl.buildforce.sequoia.processor.core.util.TestBase;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import org.mockito.ArgumentMatchers;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class TestJPAModifyProcessor {
  protected static final String LOCATION_HEADER = "Organization('35')";
  protected static final String RETURN_MINIMAL = "return=minimal";
  protected static final String PUNIT_NAME = "nl.buildforce.sequoia";
  protected static EntityManagerFactory emf;
  protected static JPAEdmProvider jpaEdm;
  protected static DataSource ds;

  @BeforeAll
  public static void setupClass() throws ODataJPAException {
    JPAEdmMetadataPostProcessor pP = mock(JPAEdmMetadataPostProcessor.class);

    ds = DataSourceHelper.createDataSource(DataSourceHelper.DB_HSQLDB);
    emf = JPAEntityManagerFactory.getEntityManagerFactory(PUNIT_NAME, ds);
    jpaEdm = new JPAEdmProvider(PUNIT_NAME, emf.getMetamodel(), /*pP,*/ TestBase.enumPackages);

  }

  protected JPACUDRequestProcessor processor;
  protected OData odata;
  protected ServiceMetadata serviceMetadata;
  protected JPAODataCRUDContextAccess sessionContext;
  protected JPAODataRequestContextAccess requestContext;
  protected UriInfo uriInfo;
  protected UriResourceEntitySet uriEts;
  protected EntityManager em;
  protected JPAODataTransaction transaction;
  protected JPASerializer serializer;
  protected EdmEntitySet ets;
  protected EdmEntitySetInfo etsInfo;
  protected List<UriParameter> keyPredicates;
  protected JPAConversionHelper convHelper;
  protected final List<UriResource> pathParts = new ArrayList<>();
  protected SerializerResult serializerResult;
  protected final List<String> header = new ArrayList<>();
  // protected JPAServiceDebugger debugger;
  protected JPAODataTransactionFactory factory;

  @BeforeEach
  public void setUp() throws Exception {
    odata = OData.newInstance();
    sessionContext = mock(JPAODataCRUDContextAccess.class);
    requestContext = mock(JPAODataRequestContextAccess.class);
    serviceMetadata = mock(ServiceMetadata.class);
    uriInfo = mock(UriInfo.class);
    keyPredicates = new ArrayList<>();
    ets = mock(EdmEntitySet.class);
    etsInfo = mock(EdmEntitySetInfo.class);
    serializer = mock(JPASerializer.class);
    uriEts = mock(UriResourceEntitySet.class);
    pathParts.add(uriEts);
    convHelper = mock(JPAConversionHelper.class);
    em = mock(EntityManager.class);
    transaction = mock(JPAODataTransaction.class);
    serializerResult = mock(SerializerResult.class);
    // debugger = mock(JPAServiceDebugger.class);
    factory = mock(JPAODataTransactionFactory.class);

    when(sessionContext.getEdmProvider()).thenReturn(jpaEdm);
    // when(requestContext.getDebugger()).thenReturn(debugger);
    when(requestContext.getEntityManager()).thenReturn(em);
    when(requestContext.getUriInfo()).thenReturn(uriInfo);
    when(requestContext.getSerializer()).thenReturn(serializer);
    when(requestContext.getTransactionFactory()).thenReturn(factory);
    when(uriInfo.getUriResourceParts()).thenReturn(pathParts);
    when(uriEts.getKeyPredicates()).thenReturn(keyPredicates);
    when(uriEts.getEntitySet()).thenReturn(ets);
    when(uriEts.getKind()).thenReturn(UriResourceKind.entitySet);
    when(ets.getName()).thenReturn("Organizations");
    when(factory.createTransaction()).thenReturn(transaction);
    when(etsInfo.getEdmEntitySet()).thenReturn(ets);
    processor = new JPACUDRequestProcessor(odata, serviceMetadata, sessionContext, requestContext, convHelper);

  }

  protected ODataRequest prepareRepresentationRequest(JPAAbstractCUDRequestHandler spy) throws ODataJPAException, SerializerException, ODataJPASerializerException, ODataJPAProcessorException {

    final ODataRequest request = prepareSimpleRequest("return=representation");

    when(requestContext.getCUDRequestHandler()).thenReturn(spy);
    final Organization org = new Organization();
    when(em.find(Organization.class, "35")).thenReturn(org);
    org.setID("35");
    final Edm edm = mock(Edm.class);
    when(serviceMetadata.getEdm()).thenReturn(edm);
    final EdmEntityType edmET = mock(EdmEntityType.class);
    final FullQualifiedName fqn = new FullQualifiedName("nl.buildforce.sequoia.Organization");
    when(edm.getEntityType(fqn)).thenReturn(edmET);
    final List<String> keyNames = new ArrayList<>();
    keyNames.add("ID");
    when(edmET.getKeyPredicateNames()).thenReturn(keyNames);
    final EdmKeyPropertyRef refType = mock(EdmKeyPropertyRef.class);
    when(edmET.getKeyPropertyRef("ID")).thenReturn(refType);
    when(edmET.getFullQualifiedName()).thenReturn(fqn);
    final EdmProperty edmProperty = mock(EdmProperty.class);
    when(refType.getProperty()).thenReturn(edmProperty);
    when(refType.getName()).thenReturn("ID");
    final EdmPrimitiveType type = mock(EdmPrimitiveType.class);
    when(edmProperty.getType()).thenReturn(type);
    when(type.toUriLiteral(ArgumentMatchers.any())).thenReturn("35");

    when(serializer.serialize(ArgumentMatchers.eq(request), ArgumentMatchers.any(EntityCollection.class))).thenReturn(
        serializerResult);
    when(serializerResult.getContent()).thenReturn(new ByteArrayInputStream("{\"ID\":\"35\"}".getBytes()));

    return request;
  }

  protected ODataRequest prepareLinkRequest(JPAAbstractCUDRequestHandler spy)
          throws ODataJPAException, SerializerException, ODataJPASerializerException, ODataJPAProcessorException {

    // .../AdministrativeDivisions(DivisionCode='DE60',CodeID='NUTS2',CodePublisher='Eurostat')
    final ODataRequest request = prepareSimpleRequest("return=representation");
    final Edm edm = mock(Edm.class);
    final EdmEntityType edmET = mock(EdmEntityType.class);

    final FullQualifiedName fqn = new FullQualifiedName("nl.buildforce.sequoia.AdministrativeDivision");
    final List<String> keyNames = new ArrayList<>();

    final AdministrativeDivisionKey key = new AdministrativeDivisionKey("Eurostat", "NUTS2", "DE60");
    final AdministrativeDivision div = new AdministrativeDivision(key);

    when(requestContext.getCUDRequestHandler()).thenReturn(spy);
    when(em.find(AdministrativeDivision.class, key)).thenReturn(div);
    when(serviceMetadata.getEdm()).thenReturn(edm);
    when(edm.getEntityType(fqn)).thenReturn(edmET);
    when(ets.getName()).thenReturn("AdministrativeDivisions");
    when(uriEts.getKeyPredicates()).thenReturn(keyPredicates);
    keyNames.add("DivisionCode");
    keyNames.add("CodeID");
    keyNames.add("CodePublisher");
    when(edmET.getKeyPredicateNames()).thenReturn(keyNames);
    when(edmET.getFullQualifiedName()).thenReturn(fqn);

    EdmPrimitiveType type = EdmString.getInstance();
    EdmKeyPropertyRef refType = mock(EdmKeyPropertyRef.class);
    EdmProperty edmProperty = mock(EdmProperty.class);
    when(edmET.getKeyPropertyRef("DivisionCode")).thenReturn(refType);
    when(refType.getProperty()).thenReturn(edmProperty);
    when(refType.getName()).thenReturn("DivisionCode");
    when(edmProperty.getType()).thenReturn(type);
    when(edmProperty.getMaxLength()).thenReturn(50);

    refType = mock(EdmKeyPropertyRef.class);
    edmProperty = mock(EdmProperty.class);
    when(edmET.getKeyPropertyRef("CodeID")).thenReturn(refType);
    when(refType.getProperty()).thenReturn(edmProperty);
    when(refType.getName()).thenReturn("CodeID");
    when(edmProperty.getType()).thenReturn(type);
    when(edmProperty.getMaxLength()).thenReturn(50);

    refType = mock(EdmKeyPropertyRef.class);
    edmProperty = mock(EdmProperty.class);
    when(edmET.getKeyPropertyRef("CodePublisher")).thenReturn(refType);
    when(refType.getProperty()).thenReturn(edmProperty);
    when(refType.getName()).thenReturn("CodePublisher");
    when(edmProperty.getType()).thenReturn(type);
    when(edmProperty.getMaxLength()).thenReturn(50);

    when(serializer.serialize(ArgumentMatchers.eq(request), ArgumentMatchers.any(EntityCollection.class))).thenReturn(
        serializerResult);
    when(serializerResult.getContent()).thenReturn(new ByteArrayInputStream("{\"ParentCodeID\":\"NUTS1\"}".getBytes()));

    return request;

  }

  protected ODataRequest prepareSimpleRequest() throws ODataJPAException, SerializerException, ODataJPAProcessorException {

    return prepareSimpleRequest(RETURN_MINIMAL);
  }

  @SuppressWarnings("unchecked")
  protected ODataRequest prepareSimpleRequest(String content) throws ODataJPAException, ODataJPAProcessorException, SerializerException {

    final EntityTransaction transaction = mock(EntityTransaction.class);
    when(em.getTransaction()).thenReturn(transaction);

    final ODataRequest request = mock(ODataRequest.class);
    when(request.getHeaders(PREFER)).thenReturn(header);
    when(sessionContext.getEdmProvider()).thenReturn(jpaEdm);
    when(etsInfo.getEdmEntitySet()).thenReturn(ets);
    header.add(content);

    Entity odataEntity = mock(Entity.class);
    when(convHelper.convertInputStream(same(odata), same(request), same(ContentType.CT_JSON), any(List.class)))
        .thenReturn(odataEntity);
    when(convHelper.convertKeyToLocal(ArgumentMatchers.eq(odata), ArgumentMatchers.eq(request), ArgumentMatchers.eq(
        ets), ArgumentMatchers.any(JPAEntityType.class), ArgumentMatchers.any())).thenReturn(LOCATION_HEADER);
    return request;
  }

}