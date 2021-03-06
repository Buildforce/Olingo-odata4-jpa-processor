package nl.buildforce.sequoia.processor.core.api;

import nl.buildforce.sequoia.metadata.core.edm.mapper.exception.ODataJPAModelException;
import nl.buildforce.sequoia.processor.core.exception.ODataJPAFilterException;
import nl.buildforce.sequoia.processor.core.testmodel.DataSourceHelper;
import nl.buildforce.sequoia.processor.core.util.HttpServletRequestDouble;
import nl.buildforce.sequoia.processor.core.util.HttpServletResponseDouble;
import nl.buildforce.sequoia.processor.core.util.TestBase;
import nl.buildforce.olingo.commons.api.ex.ODataException;
import nl.buildforce.olingo.server.api.OData;
import nl.buildforce.olingo.server.api.ODataHttpHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;

import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TestJPAODataGetHandler extends TestBase {
  private JPAODataHandler cut;
  private HttpServletRequestDouble request;
  private HttpServletResponseDouble response;
  private static final String PUNIT_NAME = "nl.buildforce.sequoia";

  @BeforeEach
  public void setup() {
    request = new HttpServletRequestDouble(uriPrefix + "Organizations", new StringBuffer(), headers);
    response = new HttpServletResponseDouble();
  }

/*
  @Test
  public void testCanCreateInstanceWithPunit() throws ODataJPAFilterException {
      new JPAODataHandler(PUNIT_NAME);
  }

  @Test
  public void testCanCreateInstanceWithPunitAndDs() throws ODataJPAFilterException {
    final DataSource ds = DataSourceHelper.createDataSource(DataSourceHelper.DB_DERBY);
      new JPAODataHandler(PUNIT_NAME, ds);
  }

  @Test
  public void testPropertiesInstanceWithPunitAndDs() throws ODataJPAFilterException {
    final DataSource ds = DataSourceHelper.createDataSource(DataSourceHelper.DB_DERBY);
    cut = new JPAODataHandler(PUNIT_NAME, ds);
    // assertNotNull(cut.getJPAODataContext());
    assertNotNull(cut.ds);
    assertNotNull(cut.emf);
    assertNotNull(cut.odata);
    assertNotNull(cut.namespace);
    assertNotNull(cut.jpaMetamodel);
  }

  @Test
  public void testProcessWithoutEntityManager() throws ODataJPAFilterException, ODataJPAModelException {
    final DataSource ds = DataSourceHelper.createDataSource(DataSourceHelper.DB_DERBY);
    cut = new JPAODataHandler(PUNIT_NAME, ds);
    cut.getJPAODataContext().setTypePackage(enumPackages);
    cut.process(request, response);
    assertNotNull(cut.jpaMetamodel);
    assertEquals(200, response.getStatus());
  }

  @Test
  public void testProcessWithEntityManager() throws ODataJPAFilterException, ODataJPAModelException {
    final DataSource ds = DataSourceHelper.createDataSource(DataSourceHelper.DB_DERBY);
    final EntityManager em = emf.createEntityManager();
    cut = new JPAODataHandler(PUNIT_NAME, ds);
    cut.getJPAODataContext().setTypePackage(enumPackages);
    cut.process(request, response, em);
    assertNotNull(cut.jpaMetamodel);
    assertEquals(200, response.getStatus());

  }

  @Test
  @SuppressWarnings("deprecation")
  public void testProcessWithClaims() throws ODataJPAFilterException, ODataJPAModelException {
    final DataSource ds = DataSourceHelper.createDataSource(DataSourceHelper.DB_DERBY);
    final EntityManager em = emf.createEntityManager();
    final JPAODataClaimProvider claims = new JPAODataClaimsProvider();
    cut = new JPAODataHandler(PUNIT_NAME, ds);
    cut.getJPAODataContext().setTypePackage(enumPackages);
    cut.process(request, response, claims, em);
    assertNotNull(cut.jpaMetamodel);
    assertEquals(200, response.getStatus());

  }

  @Test
  public void testGetSessionContext() throws ODataJPAFilterException {
    cut = new JPAODataHandler(PUNIT_NAME);
    assertNotNull(cut.getJPAODataContext());
  }

  @Test
  public void testGetRequestContext() throws  ODataJPAFilterException {
    cut = new JPAODataHandler(PUNIT_NAME);
    assertNotNull(cut.getJPAODataRequestContext());
  }
*/

 @Test
  public void testGetHandlerProvidingContext() throws ODataException {
    final JPAODataCRUDContextAccess context = new JPAODataServiceContext(PUNIT_NAME, ds);
    cut = new JPAODataHandler(context);
    assertNotNull(cut);
  }

   @Test
  public void testGetRequestContextProvidingSessionContext() throws ODataException {
    final JPAODataCRUDContextAccess context = new JPAODataServiceContext(PUNIT_NAME, ds);
    cut = new JPAODataHandler(context);
    assertNotNull(cut.getJPAODataRequestContext());
  }

  @Test
  public void testPropertiesInstanceProvidingSessionContext() throws ODataException {

    final JPAODataCRUDContextAccess context = new JPAODataServiceContext(PUNIT_NAME, ds);
    cut = new JPAODataHandler(context);
    assertNull(cut.ds);
    assertNotNull(cut.odata);
    assertNull(cut.namespace);
  }

  @Test
  public void testProcessOnlyProvidingSessionContext() throws ODataException {

    final JPAODataCRUDContextAccess context = new JPAODataServiceContext(PUNIT_NAME, ds, enumPackages);
    new JPAODataHandler(context).process(request, response);
    assertEquals(200, response.getStatus());
  }

  @Test
  public void testProcessWithEntityManagerProvidingSessionContext() throws ODataException {

    final JPAODataCRUDContextAccess context = new JPAODataServiceContext(PUNIT_NAME, ds, enumPackages);
    cut = new JPAODataHandler(context);
    cut.getJPAODataRequestContext().setEntityManager(emf.createEntityManager());
    cut.process(request, response);
    assertEquals(200, response.getStatus());
  }

/*  @Test
  public void testProcessOnlyProvidingSessionContextWithEm() throws ODataException {

    final JPAODataCRUDContextAccess context = JPAODataServiceContext.with()
        .setPUnit(PUNIT_NAME)
        .setTypePackage(enumPackages)
        .build();
    cut = new JPAODataHandler(context);
    cut.getJPAODataRequestContext().setEntityManager(emf.createEntityManager());
    cut.process(request, response);
    assertEquals(200, response.getStatus());
  }

  @Test
  public void testProcessWithEm() throws ODataJPAFilterException, ODataJPAModelException {
    cut = new JPAODataHandler(PUNIT_NAME);
    cut.getJPAODataContext().setTypePackage(enumPackages);
    cut.process(request, response, emf.createEntityManager());
    assertEquals(200, response.getStatus());
  }

  @Test
  public void testMappingPathInSessionContextCreatesMapper() throws ODataException {
    final OData odata = mock(OData.class);
    final ODataHttpHandler handler = mock(ODataHttpHandler.class);
    when(odata.createHandler(any())).thenReturn(handler);
    final JPAODataCRUDContextAccess context = JPAODataServiceContext.with()
        .setDataSource(ds)
        .setPUnit(PUNIT_NAME)
        .setRequestMappingPath("/test")
        .build();
    cut = new JPAODataHandler(context, odata);
    cut.process(request, response);
    verify(handler, times(1)).process(isA(HttpServletRequestWrapper.class), any());
  }

*/  @Test
  public void testEmptyMappingPathInSessionContextNoMapper() throws ODataException {
    final OData odata = mock(OData.class);
    final ODataHttpHandler handler = mock(ODataHttpHandler.class);
    when(odata.createHandler(any())).thenReturn(handler);
    final JPAODataCRUDContextAccess context = new JPAODataServiceContext(PUNIT_NAME, ds);
    cut = new JPAODataHandler(context, odata);
    cut.process(request, response);
    verify(handler, times(1)).process(argThat(new HttpRequestMatcher()), any());
  }
/*
  @Test
  public void testEmptyMappingPathInSessionContextEmptyMapper() throws ODataException {
    final OData odata = mock(OData.class);
    final ODataHttpHandler handler = mock(ODataHttpHandler.class);
    when(odata.createHandler(any())).thenReturn(handler);
    final JPAODataCRUDContextAccess context = JPAODataServiceContext.with()
        .setDataSource(ds)
        .setPUnit(PUNIT_NAME)
        .setRequestMappingPath("")
        .build();
    cut = new JPAODataHandler(context, odata);
    cut.process(request, response);
    verify(handler, times(1)).process(argThat(new HttpRequestMatcher()), any());
  }
*/
  public static class HttpRequestMatcher implements ArgumentMatcher<HttpServletRequest> {
    @Override
    public boolean matches(final HttpServletRequest argument) {
      return argument != null && !(argument instanceof HttpServletRequestWrapper);
    }
  }

}