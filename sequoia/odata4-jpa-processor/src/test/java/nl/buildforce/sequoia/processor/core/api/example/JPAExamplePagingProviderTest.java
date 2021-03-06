package nl.buildforce.sequoia.processor.core.api.example;

import nl.buildforce.olingo.server.api.uri.UriInfo;
import nl.buildforce.olingo.server.api.uri.UriInfoResource;
import nl.buildforce.olingo.server.api.uri.UriResource;
import nl.buildforce.olingo.server.api.uri.UriResourceEntitySet;
import nl.buildforce.olingo.server.api.uri.UriResourceKind;
import nl.buildforce.olingo.server.api.uri.UriResourcePrimitiveProperty;
import nl.buildforce.olingo.server.api.uri.queryoption.OrderByItem;
import nl.buildforce.olingo.server.api.uri.queryoption.OrderByOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SkipOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SystemQueryOptionKind;
import nl.buildforce.olingo.server.api.uri.queryoption.TopOption;

import nl.buildforce.sequoia.processor.core.api.JPAODataPage;
import nl.buildforce.sequoia.processor.core.query.JPACountQuery;
import nl.buildforce.olingo.commons.api.edm.EdmEntitySet;
import nl.buildforce.olingo.commons.api.edm.EdmProperty;
import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.server.api.ODataApplicationException;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.Member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JPAExamplePagingProviderTest {
  private JPACountQuery countQuery;

  @BeforeEach
  public void setup() throws ODataApplicationException {
    countQuery = mock(JPACountQuery.class);
    when(countQuery.countResults()).thenReturn(10L);
  }

  @Test
  public void testReturnDefaultTopSkipPageSize2() throws ODataApplicationException {
    final UriInfo info = buildUriInfo();
    final JPAExamplePagingProvider cut = createOrgCut(2);
    final JPAODataPage act = cut.getFirstPage(info, null, countQuery /*, null*/);

    assertEquals(0, act.getSkip());
    assertEquals(2, act.getTop());
    assertNotNull(toODataString((String) act.getSkipToken()));
    assertEquals(info, act.getUriInfo());
  }

  @Test
  public void testReturnDefaultTopSkipPageSize5() throws ODataApplicationException {
    final UriInfo info = buildUriInfo();
    final JPAExamplePagingProvider cut = createOrgCut(5);
    final JPAODataPage act = cut.getFirstPage(info, null, countQuery /*, null*/);

    assertEquals(0, act.getSkip());
    assertEquals(5, act.getTop());
    assertNotNull(toODataString((String) act.getSkipToken()));
    assertEquals(info, act.getUriInfo());
  }

  @Test
  public void testReturnDefaultTopSkipPageSizeOther() throws ODataApplicationException {
    final UriInfo info = buildUriInfo("AdministrativeDivisions", "AdministrativeDivision");
    final JPAExamplePagingProvider cut = createOrgCut(5);
    when(countQuery.countResults()).thenReturn(12L);
    final JPAODataPage act = cut.getFirstPage(info, null, countQuery /*, null*/);

    assertEquals(0, act.getSkip());
    assertEquals(10, act.getTop());
    assertNotNull(toODataString((String) act.getSkipToken()));
    assertEquals(info, act.getUriInfo());
  }

  @Test
  public void testReturnDefaultTopSkipPageSize5NextPage() throws ODataApplicationException {
    final UriInfo info = buildUriInfo();
    final JPAExamplePagingProvider cut = createOrgCut(5);
    JPAODataPage act = cut.getFirstPage(info, null, countQuery /*, null*/);
    act = cut.getNextPage(toODataString((String) act.getSkipToken()));

    assertEquals(5, act.getSkip());
    assertEquals(5, act.getTop());
    assertEquals(info, act.getUriInfo());
  }

  @Test
  public void testReturnNullIfEntitySetIsUnknown() throws ODataApplicationException {
    final UriInfo info = buildUriInfo();
    final JPAExamplePagingProvider cut = createPersonCut(5);
    final JPAODataPage act = cut.getFirstPage(info, null, countQuery /*, null*/);

    assertNull(act);
  }

  @Test
  public void testReturnNullIfEntitySetIsUnknownButMaxPagesizeHeader() throws ODataApplicationException {
    final UriInfo info = buildUriInfo();
    final JPAExamplePagingProvider cut = createPersonCut(5);
    final JPAODataPage act = cut.getFirstPage(info, 3, countQuery /*, null*/);

    assertNull(act);
  }

  @Test
  public void testReturnRespectMaxPagesizeHeader() throws ODataApplicationException {
    final UriInfo info = buildUriInfo();
    final JPAExamplePagingProvider cut = createOrgCut(5);
    final JPAODataPage act = cut.getFirstPage(info, 3, countQuery /*, null*/);

    assertEquals(0, act.getSkip());
    assertEquals(3, act.getTop());
    assertNotNull(toODataString((String) act.getSkipToken()));
    assertEquals(info, act.getUriInfo());
  }

  @Test
  public void testReturnSkiptokenNullAtLastPage() throws ODataApplicationException {
    final UriInfo info = buildUriInfo();
    final JPAExamplePagingProvider cut = createOrgCut(5);
    JPAODataPage act = cut.getFirstPage(info, null, countQuery /*, null*/);
    act = cut.getNextPage(toODataString((String) act.getSkipToken()));

    assertNull(act.getSkipToken());
  }

  @Test
  public void testReturnSkiptokenNullOnlyOnePage() throws ODataApplicationException {
    final UriInfo info = buildUriInfo("AdministrativeDivisions", "AdministrativeDivision");
    final JPAExamplePagingProvider cut = createOrgCut(5);
    JPAODataPage act = cut.getFirstPage(info, null, countQuery /*, null*/);

    assertNull(act.getSkipToken());
  }

  @Test
  public void testReturnSkiptokenIfNotLastPage() throws ODataApplicationException {
    final UriInfo info = buildUriInfo();
    final JPAExamplePagingProvider cut = createOrgCut(2);
    JPAODataPage act = cut.getFirstPage(info, null, countQuery /*, null*/);
    act = cut.getNextPage(toODataString((String) act.getSkipToken()));

    assertNotNull(toODataString((String) act.getSkipToken()));
  }

  @Test
  public void testReturnThirdPage() throws ODataApplicationException {
    final UriInfo info = buildUriInfo();
    final JPAExamplePagingProvider cut = createOrgCut(2);
    JPAODataPage act = cut.getFirstPage(info, null, countQuery /*, null*/);
    act = cut.getNextPage(toODataString((String) act.getSkipToken()));
    act = cut.getNextPage(toODataString((String) act.getSkipToken()));

    assertNotNull(toODataString((String) act.getSkipToken()));
  }

  @Test
  public void testRespectTopSkipOfUriFirstPageLowerMaxSize() throws ODataApplicationException {
    final UriInfo info = buildUriInfo();
    addTopSkipToUri(info);
    final JPAExamplePagingProvider cut = createOrgCut(10);
    JPAODataPage act = cut.getFirstPage(info, null, countQuery /*, null*/);

    assertEquals(2, act.getSkip());
    assertEquals(7, act.getTop());
  }

  @Test
  public void testRespectTopSkipOfUriFirstPage() throws ODataApplicationException {
    final UriInfo info = buildUriInfo();
    addTopSkipToUri(info);
    final JPAExamplePagingProvider cut = createOrgCut(5);
    JPAODataPage act = cut.getFirstPage(info, null, countQuery /*, null*/);

    assertEquals(2, act.getSkip());
    assertEquals(5, act.getTop());
  }

  @Test
  public void testRespectTopSkipOfUriNextPage() throws ODataApplicationException {
    final UriInfo info = buildUriInfo();
    addTopSkipToUri(info);
    final JPAExamplePagingProvider cut = createOrgCut(5);
    JPAODataPage act = cut.getFirstPage(info, null, countQuery /*, null*/);
    act = cut.getNextPage(toODataString((String) act.getSkipToken()));

    assertEquals(7, act.getSkip());
    assertEquals(2, act.getTop());
  }

  @Test
  public void testBufferFilled() throws ODataApplicationException {
    final UriInfo info = buildUriInfo();
    final Map<String, Integer> sizes = new HashMap<>();
    sizes.put("Organizations", 2);

    final JPAExamplePagingProvider cut = new JPAExamplePagingProvider(sizes, 2);
    final JPAODataPage first = cut.getFirstPage(info, null, countQuery /*, null*/);
    assertNotNull(cut.getNextPage((String) first.getSkipToken()));
    final JPAODataPage second = cut.getNextPage((String) first.getSkipToken());
    assertNotNull(cut.getNextPage((String) second.getSkipToken()));
    final JPAODataPage third = cut.getNextPage((String) second.getSkipToken());
    assertNotNull(cut.getNextPage((String) third.getSkipToken()));
    assertNull(cut.getNextPage((String) first.getSkipToken()));
  }

  @Test
  public void testBufferNotFilled() throws ODataApplicationException {
    final UriInfo info = buildUriInfo();
    final Map<String, Integer> sizes = new HashMap<>();
    sizes.put("Organizations", 2);

    final JPAExamplePagingProvider cut = new JPAExamplePagingProvider(sizes, 10);
    final JPAODataPage first = cut.getFirstPage(info, null, countQuery /*, null*/);
    assertNotNull(cut.getNextPage((String) first.getSkipToken()));
    final JPAODataPage second = cut.getNextPage((String) first.getSkipToken());
    assertNotNull(cut.getNextPage((String) second.getSkipToken()));
    final JPAODataPage third = cut.getNextPage((String) second.getSkipToken());
    assertNotNull(cut.getNextPage((String) third.getSkipToken()));
    assertNotNull(cut.getNextPage((String) first.getSkipToken()));
  }

  private UriInfo buildUriInfo() {
    return buildUriInfo("Organizations", "Organization");
  }

  private UriInfo buildUriInfo(final String esName, final String etName) {
    final UriInfo uriInfo = mock(UriInfo.class);
    final UriResourceEntitySet uriEs = mock(UriResourceEntitySet.class);
    final EdmEntitySet es = mock(EdmEntitySet.class);
    final EdmType type = mock(EdmType.class);
    final OrderByOption order = mock(OrderByOption.class);
    final OrderByItem orderItem = mock(OrderByItem.class);
    final Member orderExpression = mock(Member.class);
    final UriInfoResource orderResourcePath = mock(UriInfoResource.class);
    final UriResourcePrimitiveProperty orderResourcePathItem = mock(UriResourcePrimitiveProperty.class);
    final EdmProperty orderProperty = mock(EdmProperty.class);
    final List<OrderByItem> orderItems = new ArrayList<>();
    final List<UriResource> orderResourcePathItems = new ArrayList<>();

    orderItems.add(orderItem);
    orderResourcePathItems.add(orderResourcePathItem);
    when(uriEs.getKind()).thenReturn(UriResourceKind.entitySet);
    when(uriEs.getEntitySet()).thenReturn(es);
    when(uriEs.getType()).thenReturn(type);
    when(es.getName()).thenReturn(esName);
    when(type.getNamespace()).thenReturn("nl.buildforce.sequoia");
    when(type.getName()).thenReturn(etName);
    when(order.getKind()).thenReturn(SystemQueryOptionKind.ORDERBY);
    when(orderItem.isDescending()).thenReturn(true);
    when(orderItem.getExpression()).thenReturn(orderExpression);
    when(orderExpression.getResourcePath()).thenReturn(orderResourcePath);
    when(orderResourcePath.getUriResourceParts()).thenReturn(orderResourcePathItems);
    when(orderResourcePathItem.getProperty()).thenReturn(orderProperty);
    when(orderProperty.getName()).thenReturn("ID");
    when(order.getOrders()).thenReturn(orderItems);
    final List<UriResource> resourceParts = new ArrayList<>();
    resourceParts.add(uriEs);
    when(uriInfo.getUriResourceParts()).thenReturn(resourceParts);
    when(uriInfo.getOrderByOption()).thenReturn(order);
    return uriInfo;
  }

  private void addTopSkipToUri(final UriInfo info) {
    final SkipOption skipOption = mock(SkipOption.class);
    final TopOption topOption = mock(TopOption.class);

    when(skipOption.getValue()).thenReturn(2);
    when(topOption.getValue()).thenReturn(7);
    when(info.getSkipOption()).thenReturn(skipOption);
    when(info.getTopOption()).thenReturn(topOption);

  }

  private JPAExamplePagingProvider createOrgCut(final int size) {
    final Map<String, Integer> sizes = new HashMap<>();
    sizes.put("Organizations", size);
    sizes.put("AdministrativeDivisions", 10);
    return new JPAExamplePagingProvider(sizes);
  }

  private JPAExamplePagingProvider createPersonCut(final int size) {
    final Map<String, Integer> sizes = new HashMap<>();
    sizes.put("Persons", size);
    return new JPAExamplePagingProvider(sizes);
  }

  private String toODataString(final String skiptoken) {
    return "'" + skiptoken + "'";
  }

}