package nl.buildforce.sequoia.processor.core.query;

import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAAttribute;
import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAEntityType;
import nl.buildforce.sequoia.metadata.core.edm.mapper.exception.ODataJPAException;
import nl.buildforce.sequoia.metadata.core.edm.mapper.exception.ODataJPAModelException;
import nl.buildforce.sequoia.processor.core.api.JPAODataCRUDContextAccess;
import nl.buildforce.sequoia.processor.core.api.JPAODataRequestContextAccess;
// import nl.buildforce.sequoia.processor.core.api.JPAServiceDebugger;
import nl.buildforce.sequoia.processor.core.database.JPADefaultDatabaseProcessor;
import nl.buildforce.sequoia.processor.core.util.TestBase;
import nl.buildforce.sequoia.processor.core.util.TestHelper;
import nl.buildforce.olingo.commons.api.edm.EdmEntityType;
import nl.buildforce.olingo.commons.api.edm.EdmNavigationProperty;
import nl.buildforce.olingo.server.api.OData;
import nl.buildforce.olingo.server.api.ODataApplicationException;
import nl.buildforce.olingo.server.api.uri.UriParameter;
import nl.buildforce.olingo.server.api.uri.UriResource;
import nl.buildforce.olingo.server.api.uri.UriResourceEntitySet;
import nl.buildforce.olingo.server.api.uri.UriResourceNavigation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Expression;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestJPAExpandJoinQuery extends TestBase {
  private JPAExpandJoinQuery cut;
  private JPAODataCRUDContextAccess sessionContext;
  private JPAODataRequestContextAccess requestContext;
  private TestHelper helper;
  private JPAKeyPair orgPair;
  private JPAKeyPair adminPair;
  private Optional<JPAKeyBoundary> orgBoundary;
  private Optional<JPAKeyBoundary> adminBoundary;
  @SuppressWarnings("rawtypes")
  private Map<JPAAttribute, Comparable> simpleKey;

  @BeforeEach
  public void setup() throws ODataJPAException {
    // createHeaders();
    helper = new TestHelper(emf, PUNIT_NAME);
    EntityManager em = emf.createEntityManager();
    sessionContext = mock(JPAODataCRUDContextAccess.class);
    requestContext = mock(JPAODataRequestContextAccess.class);
    orgPair = new JPAKeyPair(helper.getJPAEntityType("Organizations").getKey());
    orgBoundary = Optional.of(new JPAKeyBoundary(1, orgPair));
    adminPair = new JPAKeyPair(helper.getJPAEntityType("AdministrativeDivisions").getKey());
    adminBoundary = Optional.of(new JPAKeyBoundary(1, adminPair));
    // JPAServiceDebugger debugger = mock(JPAServiceDebugger.class);

    when(sessionContext.getEdmProvider()).thenReturn(helper.edmProvider);
    when(sessionContext.getOperationConverter()).thenReturn(new JPADefaultDatabaseProcessor());
//    when(requestContext.getDebugger()).thenReturn(debugger);
    when(requestContext.getClaimsProvider()).thenReturn(Optional.empty());
    when(requestContext.getEntityManager()).thenReturn(em);
  }

  @Test
  public void testSelectAllWithAllExpand() throws ODataJPAException, ODataApplicationException {
    // .../Organizations?$expand=Roles&$format=json
    JPAInlineItemInfo item = createOrgExpandRoles(null, null);
    cut = new JPAExpandJoinQuery(OData.newInstance(), sessionContext, item, headers, requestContext, Optional.empty());
    JPAExpandQueryResult act = cut.execute();
    assertEquals(4, act.getNoResults());
    assertEquals(7, act.getNoResultsDeep());
  }

  @Test
  public void testSelectOrgByIdWithAllExpand() throws ODataJPAException, ODataApplicationException {

    // .../Organizations('2')?$expand=Roles&$format=json
    UriParameter key = mock(UriParameter.class);
    when(key.getName()).thenReturn("ID");
    when(key.getText()).thenReturn("'2'");
    List<UriParameter> keyPredicates = new ArrayList<>();
    keyPredicates.add(key);
    JPAInlineItemInfo item = createOrgExpandRoles(keyPredicates, null);

    cut = new JPAExpandJoinQuery(OData.newInstance(), sessionContext, item, headers, requestContext, Optional.empty());
    JPAExpandQueryResult act = cut.execute();
    assertEquals(1, act.getNoResults());
    assertEquals(2, act.getNoResultsDeep());
  }

  @Test
  public void testSelectWithMinBoundary() throws ODataJPAException, ODataApplicationException {
    // .../Organizations?$expand=Roles&$skip=2&$format=json
    JPAInlineItemInfo item = createOrgExpandRoles(null, null);
    setSimpleKey(3);
    cut = new JPAExpandJoinQuery(OData.newInstance(), sessionContext, item, headers, requestContext, orgBoundary);
    final JPAExpandQueryResult act = cut.execute();
    String query = cut.getSQLString();

    assertTrue(query.contains("(t1.\"ID\" = '3')" /*".\"ID\" = ?"*/));
    assertEquals(1, act.getNoResults());
    assertEquals(3, act.getNoResultsDeep());
  }

  @Test
  public void testSelectWithMinBoundaryEmbedded() throws ODataJPAException, ODataApplicationException {
    // .../Organizations?$expand=Roles&$skip=2&$format=json
    JPAInlineItemInfo item = createAdminDivExpandChildren(null, null);
    setComplexKey("Eurostat", "NUTS1", "BE2");
    cut = new JPAExpandJoinQuery(OData.newInstance(), sessionContext, item, headers, requestContext, adminBoundary);
    final JPAExpandQueryResult act = cut.execute();
    assertTrue(cut.getSQLString().contains(
            "(((t1.\"DivisionCode\" = 'BE2') AND (t1.\"CodeID\" = 'NUTS1')) AND (t1.\"CodePublisher\" = 'Eurostat')) AND ((t0.\"ParentDivisionCode\" = t1.\"DivisionCode\") AND ((t0.\"CodePublisher\" = t1.\"CodePublisher\")"));
        //"(((t1.\"DivisionCode\" = ?) AND (t1.\"CodeID\" = ?)) AND (t1.\"CodePublisher\" = ?)) "));
    assertEquals(1, act.getNoResults());
    assertEquals(5, act.getNoResultsDeep());
  }

  @Test
  public void testSelectWithMinMaxBoundary() throws ODataJPAException, ODataApplicationException {
    // .../Organizations?$expand=Roles&$top=3&$format=json
    final JPAInlineItemInfo item = createOrgExpandRoles(null, null);
    setSimpleKey(2);
    setSimpleKey(1);
    cut = new JPAExpandJoinQuery(OData.newInstance(), sessionContext, item, headers, requestContext, orgBoundary);
    final JPAExpandQueryResult act = cut.execute();
    String query = cut.getSQLString();

    assertTrue(cut.getSQLString().contains(
            "((t1.\"ID\" >= '1') AND (t1.\"ID\" <= '2'))"

            /*".\"ID\" >= ?"*/));
//    assertTrue(cut.getSQLString().contains(".\"ID\" <= ?"));
    assertEquals(2, act.getNoResults());
    assertEquals(3, act.getNoResultsDeep());
  }

  @Test
  public void testSelectWithMinMaxBoundaryEmbeddedOnlyLastDiffers() throws ODataJPAException, ODataApplicationException {

    JPAInlineItemInfo item = createAdminDivExpandChildren(null, null);
    setComplexKey("Eurostat", "NUTS1", "BE1");
    setComplexKey("Eurostat", "NUTS2", "BE25");
    cut = new JPAExpandJoinQuery(OData.newInstance(), sessionContext, item, headers, requestContext, adminBoundary);
    final JPAExpandQueryResult act = cut.execute();
    String query =cut.getSQLString();

    assertTrue(query.contains(
    "(((t1.\"DivisionCode\" >= 'BE1') AND (t1.\"CodeID\" = 'NUTS1')) AND (t1.\"CodePublisher\" = 'Eurostat')) OR (t1.\"CodeID\" > 'NUTS1')) AND (t1.\"CodePublisher\" = 'Eurostat')) OR (t1.\"CodePublisher\" > 'Eurostat')) AND ((((((t1.\"DivisionCode\" <= 'BE25') AND (t1.\"CodeID\" = 'NUTS2')) AND (t1.\"CodePublisher\" = 'Eurostat')) OR (t1.\"CodeID\" < 'NUTS2')) AND (t1.\"CodePublisher\" = 'Eurostat')) OR (t1.\"CodePublisher\" < 'Eurostat'))) AND ((t0.\"ParentDivisionCode\" = t1.\"DivisionCode\") AND ((t0.\"CodePublisher\" = t1.\"CodePublisher\""));
      /*  "(((t1.\"DivisionCode\" >= ?) AND (t1.\"CodeID\" = ?)) AND (t1.\"CodePublisher\" = ?))"));*/
/*
    assertTrue(query.contains(
        "(((t1.\"DivisionCode\" <= ?) AND (t1.\"CodeID\" = ?)) AND (t1.\"CodePublisher\" = ?))"));
    assertTrue(query.contains(
        "(t1.\"CodeID\" > ?)) AND (t1.\"CodePublisher\" = ?))"));
    assertTrue(query.contains(
        "(t1.\"CodeID\" < ?)) AND (t1.\"CodePublisher\" = ?))"));
*/
    assertEquals(9, act.getNoResults());
    assertEquals(34, act.getNoResultsDeep());
  }

  @Test
  public void testSQLStringNotEmptyAfterExecute() throws ODataJPAException, ODataApplicationException {
    // .../Organizations?$expand=Roles&$format=json
    JPAInlineItemInfo item = createOrgExpandRoles(null, null);
    cut = new JPAExpandJoinQuery(OData.newInstance(), sessionContext, item, headers, requestContext, Optional.empty());
    assertTrue(cut.getSQLString().isEmpty());
    cut.execute();
    assertFalse(cut.getSQLString().isEmpty());
  }

  private JPAInlineItemInfo createAdminDivExpandChildren(final List<UriParameter> keyPredicates,
                                                         Expression<Boolean> expression)
      throws ODataJPAModelException, ODataApplicationException {

    JPAEntityType et = helper.getJPAEntityType("AdministrativeDivisions");
    JPAExpandItemWrapper uriInfo = mock(JPAExpandItemWrapper.class);
    UriResourceEntitySet uriEts = mock(UriResourceEntitySet.class);
    when(uriEts.getKeyPredicates()).thenReturn(keyPredicates);
    EdmEntityType edmType = mock(EdmEntityType.class);

    List<JPANavigationPropertyInfo> hops = new ArrayList<>();
    JPANavigationPropertyInfo hop = new JPANavigationPropertyInfo(helper.sd, uriEts, et.getAssociationPath(
        "Children"), null);
    hops.add(hop);

    JPAInlineItemInfo item = mock(JPAInlineItemInfo.class);
    UriResourceNavigation target = mock(UriResourceNavigation.class);
    EdmNavigationProperty targetProperty = mock(EdmNavigationProperty.class);
    when(targetProperty.getName()).thenReturn("Children");
    when(target.getProperty()).thenReturn(targetProperty);
    List<UriResource> resourceParts = new ArrayList<>();
    resourceParts.add(target);

    hop = new JPANavigationPropertyInfo(helper.sd, null, null, et);
    hops.add(hop);

    when(item.getEntityType()).thenReturn(et);
    when(item.getUriInfo()).thenReturn(uriInfo);
    when(item.getHops()).thenReturn(hops);
    when(item.getExpandAssociation()).thenReturn(et.getAssociationPath("Children"));
    when(uriInfo.getUriResourceParts()).thenReturn(resourceParts);
    when(uriEts.getType()).thenReturn(edmType);
    when(edmType.getNamespace()).thenReturn(PUNIT_NAME);
    when(edmType.getName()).thenReturn("AdministrativeDivision");
    return item;
  }

  private JPAInlineItemInfo createOrgExpandRoles(final List<UriParameter> keyPredicates, Expression<Boolean> expression)
      throws ODataJPAModelException, ODataApplicationException {
    JPAEntityType et = helper.getJPAEntityType("BusinessPartnerRoles");
    JPAExpandItemWrapper uriInfo = mock(JPAExpandItemWrapper.class);
    UriResourceEntitySet uriEts = mock(UriResourceEntitySet.class);
    when(uriEts.getKeyPredicates()).thenReturn(keyPredicates);
    EdmEntityType edmType = mock(EdmEntityType.class);

    List<JPANavigationPropertyInfo> hops = new ArrayList<>();
    JPANavigationPropertyInfo hop = new JPANavigationPropertyInfo(helper.sd, uriEts, helper.getJPAEntityType(
        "Organizations").getAssociationPath("Roles"), null);
    hops.add(hop);

    JPAInlineItemInfo item = mock(JPAInlineItemInfo.class);
    UriResourceNavigation target = mock(UriResourceNavigation.class);
    EdmNavigationProperty targetProperty = mock(EdmNavigationProperty.class);
    when(targetProperty.getName()).thenReturn("Roles");
    when(target.getProperty()).thenReturn(targetProperty);
    List<UriResource> resourceParts = new ArrayList<>();
    resourceParts.add(target);

    hop = new JPANavigationPropertyInfo(helper.sd, null, null, et);
    hops.add(hop);

    when(item.getEntityType()).thenReturn(et);
    when(item.getUriInfo()).thenReturn(uriInfo);
    when(item.getHops()).thenReturn(hops);
    when(item.getExpandAssociation()).thenReturn(helper.getJPAEntityType("Organizations")
        .getAssociationPath("Roles"));
    when(uriInfo.getUriResourceParts()).thenReturn(resourceParts);
    when(uriEts.getType()).thenReturn(edmType);
    when(edmType.getNamespace()).thenReturn(PUNIT_NAME);
    when(edmType.getName()).thenReturn("Organization");
    return item;
  }

  private void setSimpleKey(final Integer value) throws ODataJPAModelException {
    simpleKey = new HashMap<>(1);
    simpleKey.put(helper.getJPAEntityType("Organizations").getKey().get(0), value);
    orgPair.setValue(simpleKey);
  }

  private void setComplexKey(final String key1, final String key2, final String key3)
      throws ODataJPAModelException {
    simpleKey = new HashMap<>(3);
    final JPAEntityType et = helper.getJPAEntityType("AdministrativeDivisions");
    simpleKey.put(et.getKey().get(0), key3);
    simpleKey.put(et.getKey().get(1), key2);
    simpleKey.put(et.getKey().get(2), key1);
    adminPair.setValue(simpleKey);
  }

}