package nl.buildforce.sequoia.processor.core.query;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nl.buildforce.olingo.commons.api.edm.EdmEntitySet;
import nl.buildforce.olingo.commons.api.edm.EdmEntityType;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveType;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeException;
import nl.buildforce.olingo.commons.api.edm.EdmProperty;
import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;
import nl.buildforce.olingo.commons.api.edm.constants.EdmTypeKind;
import nl.buildforce.olingo.commons.api.ex.ODataException;
import nl.buildforce.olingo.commons.api.http.HttpStatusCode;
import nl.buildforce.olingo.server.api.uri.UriInfo;
import nl.buildforce.olingo.server.api.uri.UriInfoResource;
import nl.buildforce.olingo.server.api.uri.UriResource;
import nl.buildforce.olingo.server.api.uri.UriResourceEntitySet;
import nl.buildforce.olingo.server.api.uri.UriResourceKind;
import nl.buildforce.olingo.server.api.uri.UriResourcePrimitiveProperty;
import nl.buildforce.olingo.server.api.uri.queryoption.OrderByItem;
import nl.buildforce.olingo.server.api.uri.queryoption.OrderByOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SelectItem;
import nl.buildforce.olingo.server.api.uri.queryoption.SelectOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SystemQueryOptionKind;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.Member;
import nl.buildforce.sequoia.processor.core.api.JPAClaimsPair;
import nl.buildforce.sequoia.processor.core.api.JPAODataClaimsProvider;
import nl.buildforce.sequoia.processor.core.api.JPAODataPage;
import nl.buildforce.sequoia.processor.core.api.JPAODataPagingProvider;
import nl.buildforce.sequoia.processor.core.util.CountQueryMatcher;
import nl.buildforce.sequoia.processor.core.util.IntegrationTestHelper;
import nl.buildforce.sequoia.processor.core.util.TestBase;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class TestJPAServerDrivenPaging extends TestBase {
  @Test
  public void testReturnsNotImplementedIfPagingProviderNotAvailable() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(emf, "Organizations?$skiptoken=xyz");
    helper.assertStatus(501);
  }

  @Test
  public void testReturnsGoneIfPagingProviderReturnsNullForSkiptoken() throws IOException, ODataException {
    final JPAODataPagingProvider provider = mock(JPAODataPagingProvider.class);
    when(provider.getNextPage("xyz")).thenReturn(null);
    IntegrationTestHelper helper = new IntegrationTestHelper(emf, "Organizations?$skiptoken=xyz", provider);
    helper.assertStatus(410);

  }

  @Test
  public void testReturnsFullResultIfProviderDoesNotReturnPage() throws IOException, ODataException {
    final JPAODataPagingProvider provider = mock(JPAODataPagingProvider.class);
    when(provider.getFirstPage(any(), any(), any(), any())).thenReturn(null);
    IntegrationTestHelper helper = new IntegrationTestHelper(emf, "Organizations", provider);
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());
    assertEquals(10, helper.getValues().size());
  }

  @Test
  public void testReturnsPartResultIfProviderPages() throws IOException, ODataException {

    final JPAODataPagingProvider provider = mock(JPAODataPagingProvider.class);
    when(provider.getFirstPage(any(), any(), any(), any())).thenAnswer(i -> new JPAODataPage((UriInfo) i
        .getArguments()[0], 0, 5, "Hugo"));
    IntegrationTestHelper helper = new IntegrationTestHelper(emf, "Organizations?$orderby=ID desc", provider);
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());
    assertEquals(5, helper.getValues().size());
  }

  @Test
  public void testReturnsNextLinkIfProviderPages() throws IOException, ODataException {

    final JPAODataPagingProvider provider = mock(JPAODataPagingProvider.class);
    when(provider.getFirstPage(any(), any(), any(), any())).thenAnswer(i -> new JPAODataPage((UriInfo) i
        .getArguments()[0], 0, 5, "Hugo"));

    IntegrationTestHelper helper = new IntegrationTestHelper(emf, "Organizations?$orderby=ID desc", provider);
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());
    assertEquals(5, helper.getValues().size());
    assertEquals("Organizations?$skiptoken='Hugo'", helper.getValue().get("@odata.nextLink").asText());
  }

  @Test
  public void testReturnsNextLinkNotAStringIfProviderPages() throws IOException, ODataException {

    final JPAODataPagingProvider provider = mock(JPAODataPagingProvider.class);
    when(provider.getFirstPage(any(), any(), any(), any())).thenAnswer(i -> new JPAODataPage((UriInfo) i
        .getArguments()[0], 0, 5, 123456789));

    IntegrationTestHelper helper = new IntegrationTestHelper(emf, "Organizations?$orderby=ID desc", provider);
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());
    assertEquals(5, helper.getValues().size());
    assertEquals("Organizations?$skiptoken=123456789", helper.getValue().get("@odata.nextLink").asText());
  }

  @Test
  public void testReturnsNextPagesRespectingFilter() throws IOException, ODataException {
    final UriInfo uriInfo = buildUriInfo();

    final JPAODataPagingProvider provider = mock(JPAODataPagingProvider.class);
    when(provider.getNextPage("xyz")).thenReturn(new JPAODataPage(uriInfo, 5, 5, null));

    IntegrationTestHelper helper = new IntegrationTestHelper(emf, "Organizations?$skiptoken=xyz", provider);
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());
    assertEquals(5, helper.getValues().size());
    ObjectNode org = (ObjectNode) helper.getValues().get(4);
    assertEquals("1", org.get("ID").asText());
  }

  @Test
  public void testEntityManagerProvided() throws IOException, ODataException {

    final JPAODataPagingProvider provider = mock(JPAODataPagingProvider.class);
    when(provider.getFirstPage(any(), any(), any(), any())).thenAnswer(i -> new JPAODataPage((UriInfo) i
        .getArguments()[0], 0, 5, "Hugo"));

    IntegrationTestHelper helper = new IntegrationTestHelper(emf, "Organizations?$orderby=ID desc", provider);
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    verify(provider).getFirstPage(any(), any(), any(), isNotNull());
  }

  @Test
  public void testCountQueryProvided() throws IOException, ODataException {

    final JPAODataPagingProvider provider = mock(JPAODataPagingProvider.class);
    when(provider.getFirstPage(any(), any(), any(), any())).thenAnswer(i -> new JPAODataPage((UriInfo) i
        .getArguments()[0], 0, 5, "Hugo"));
    IntegrationTestHelper helper = new IntegrationTestHelper(emf, "Organizations?$orderby=ID desc", provider);
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    verify(provider).getFirstPage(any(), any(), isNotNull(), any());
  }

  @Test
  public void testCountQueryProvidedWithProtection() throws IOException, ODataException {
    final JPAODataClaimsProvider claims = new JPAODataClaimsProvider();
    claims.add("UserId", new JPAClaimsPair<>("Willi"));
    final JPAODataPagingProvider provider = mock(JPAODataPagingProvider.class);
    when(provider.getFirstPage(any(), any(), any(), any())).thenAnswer(i -> new JPAODataPage((UriInfo) i
        .getArguments()[0], 0, 5, "Hugo"));
    IntegrationTestHelper helper = new IntegrationTestHelper(emf, "BusinessPartnerProtecteds", provider, claims);
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());
    final ArrayNode act = helper.getValues();
    assertEquals(3, act.size());
    verify(provider).getFirstPage(any(), any(), argThat(new CountQueryMatcher(3L)), any());
  }

  @Test
  public void testMaxPageSizeHeaderProvided() throws IOException, ODataException {

    headers = new HashMap<>();
    final List<String> headerValues = new ArrayList<>(0);
    final JPAODataPagingProvider provider = mock(JPAODataPagingProvider.class);

    when(provider.getFirstPage(any(), any(), any(), any())).thenAnswer(i -> new JPAODataPage((UriInfo) i
        .getArguments()[0], 0, 5, "Hugo"));
    headerValues.add("odata.maxpagesize=50");
    headers.put("Prefer", headerValues);

    IntegrationTestHelper helper = new IntegrationTestHelper(emf, "Organizations?$orderby=ID desc", provider, headers);
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    verify(provider).getFirstPage(any(), isNotNull(), any(), any());
  }

  @Test
  public void testMaxPageSizeHeaderProvidedInLowerCase() throws IOException, ODataException {
    headers = new HashMap<>();
    final List<String> headerValues = new ArrayList<>(0);
    final JPAODataPagingProvider provider = mock(JPAODataPagingProvider.class);
    when(provider.getFirstPage(any(), any(), any(), any())).thenAnswer(i -> new JPAODataPage((UriInfo) i
        .getArguments()[0], 0, 5, "Hugo"));
    headerValues.add("odata.maxpagesize=50");
    headers.put("prefer", headerValues);

    IntegrationTestHelper helper = new IntegrationTestHelper(emf, "Organizations?$orderby=ID desc", provider, headers);
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    verify(provider).getFirstPage(any(), isNotNull(), any(), any());
  }

  @Test
  public void testUriInfoProvided() throws IOException, ODataException {

    final JPAODataPagingProvider provider = mock(JPAODataPagingProvider.class);

    when(provider.getFirstPage(any(), any(), any(), any())).thenAnswer(i -> new JPAODataPage((UriInfo) i
        .getArguments()[0], 0, 5, "Hugo"));

    IntegrationTestHelper helper = new IntegrationTestHelper(emf, "Organizations?$orderby=ID desc", provider);
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());

    verify(provider).getFirstPage(isNotNull(), any(), any(), any());
  }

  @Test
  public void testMaxPageSiteHeaderNotANumber() throws IOException, ODataException {

    headers = new HashMap<>();
    final List<String> headerValues = new ArrayList<>(0);
    final JPAODataPagingProvider provider = mock(JPAODataPagingProvider.class);

    when(provider.getFirstPage(any(), any(), any(), any())).thenAnswer(i -> new JPAODataPage((UriInfo) i
        .getArguments()[0], 0, 5, "Hugo"));
    headerValues.add("odata.maxpagesize=Hugo");
    headers.put("Prefer", headerValues);

    IntegrationTestHelper helper = new IntegrationTestHelper(emf, "Organizations?$orderby=ID desc", provider, headers);
    helper.assertStatus(400);

  }

  @Test
  public void testSelectSubsetOfFields() throws IOException, ODataException {
    final UriInfo uriInfo = buildUriInfo();
    final JPAODataPagingProvider provider = mock(JPAODataPagingProvider.class);
    final SelectOption selOpt = mock(SelectOption.class);
    final List<SelectItem> selItems = new ArrayList<>();
    final SelectItem selItem = mock(SelectItem.class);

    when(uriInfo.getSelectOption()).thenReturn(selOpt);
    when(selOpt.getKind()).thenReturn(SystemQueryOptionKind.SELECT);
    when(selOpt.getSelectItems()).thenReturn(selItems);
    selItems.add(selItem);

    final UriInfoResource selectPath = mock(UriInfoResource.class);
    final List<UriResource> selectPathItems = new ArrayList<>(0);
    final UriResourcePrimitiveProperty selectResource = mock(UriResourcePrimitiveProperty.class);
    final EdmProperty selectProperty = mock(EdmProperty.class);
    selectPathItems.add(selectResource);
    when(selItem.getResourcePath()).thenReturn(selectPath);
    when(selectPath.getUriResourceParts()).thenReturn(selectPathItems);
    when(selectResource.getSegmentValue()).thenReturn("ID");
    when(selectResource.getProperty()).thenReturn(selectProperty);
    when(selectProperty.getName()).thenReturn("ID");

    when(provider.getFirstPage(any(), any(), any(), any())).thenAnswer(i -> new JPAODataPage((UriInfo) i
        .getArguments()[0], 0, 5, "Hugo"));

    when(provider.getNextPage("'Hugo'")).thenReturn(new JPAODataPage(uriInfo, 5, 5, "Willi"));
    IntegrationTestHelper helper = new IntegrationTestHelper(emf, "Organizations?$orderby=ID desc&$select=ID",
        provider);
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());
    assertNull(helper.getValues().get(0).get("Country"));
    IntegrationTestHelper act = new IntegrationTestHelper(emf, "Organizations?$skiptoken='Hugo'",
        provider);
    act.assertStatus(200);
    assertEquals(5, act.getValues().size());
    assertNull(act.getValues().get(0).get("Country"));

  }

  private UriInfo buildUriInfo() throws EdmPrimitiveTypeException {
    final UriInfo uriInfo = mock(UriInfo.class);
    final UriResourceEntitySet uriEs = mock(UriResourceEntitySet.class);
    final EdmEntitySet es = mock(EdmEntitySet.class);
    final EdmEntityType et = mock(EdmEntityType.class);
    final EdmType type = mock(EdmType.class);
    final OrderByOption order = mock(OrderByOption.class);
    final OrderByItem orderItem = mock(OrderByItem.class);
    final Member orderExpression = mock(Member.class);
    final UriInfoResource orderResourcePath = mock(UriInfoResource.class);
    final UriResourcePrimitiveProperty orderResourcePathItem = mock(UriResourcePrimitiveProperty.class);
    final EdmProperty orderProperty = mock(EdmProperty.class);
    final List<OrderByItem> orderItems = new ArrayList<>();
    final List<UriResource> orderResourcePathItems = new ArrayList<>();

    final EdmProperty propertyID = mock(EdmProperty.class); // type.getStructuralProperty(propertyName);
    final EdmProperty propertyCountry = mock(EdmProperty.class);
    final EdmPrimitiveType propertyType = mock(EdmPrimitiveType.class);

    orderItems.add(orderItem);
    orderResourcePathItems.add(orderResourcePathItem);
    when(uriEs.getKind()).thenReturn(UriResourceKind.entitySet);
    when(uriEs.getEntitySet()).thenReturn(es);
    when(uriEs.getType()).thenReturn(type);
    when(uriEs.isCollection()).thenReturn(true);
    when(es.getName()).thenReturn("Organizations");
    when(es.getEntityType()).thenReturn(et);
    when(type.getNamespace()).thenReturn("nl.buildforce.sequoia");
    when(type.getName()).thenReturn("Organization");
    when(et.getFullQualifiedName()).thenReturn(new FullQualifiedName("nl.buildforce.sequoia", "Organization"));
    when(et.getPropertyNames()).thenReturn(Arrays.asList("ID", "Country"));
    when(et.getStructuralProperty("ID")).thenReturn(propertyID);
    when(et.getStructuralProperty("Country")).thenReturn(propertyCountry);

    when(propertyID.getName()).thenReturn("ID");
    when(propertyID.isPrimitive()).thenReturn(true);
    when(propertyID.getType()).thenReturn(propertyType);
    when(propertyCountry.getName()).thenReturn("Country");
    when(propertyCountry.isPrimitive()).thenReturn(true);
    when(propertyCountry.getType()).thenReturn(propertyType);
    when(propertyType.getKind()).thenReturn(EdmTypeKind.PRIMITIVE);
    when(propertyType.valueToString(any(), any(), any(), any(), any(), any())).thenAnswer(i -> i.getArguments()[0]
        .toString());

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

}