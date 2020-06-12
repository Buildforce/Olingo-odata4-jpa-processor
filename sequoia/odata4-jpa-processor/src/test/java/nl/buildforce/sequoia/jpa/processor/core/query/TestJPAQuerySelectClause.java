package nl.buildforce.sequoia.jpa.processor.core.query;

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAException;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAModelException;
import nl.buildforce.sequoia.jpa.processor.core.exception.JPAIllegalAccessException;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.TestDataConstants;
import nl.buildforce.sequoia.jpa.processor.core.util.EdmEntityTypeDouble;
import nl.buildforce.sequoia.jpa.processor.core.util.EdmPropertyDouble;
import nl.buildforce.sequoia.jpa.processor.core.util.ExpandItemDouble;
import nl.buildforce.sequoia.jpa.processor.core.util.ExpandOptionDouble;
import nl.buildforce.sequoia.jpa.processor.core.util.SelectOptionDouble;
import nl.buildforce.sequoia.jpa.processor.core.util.TestQueryBase;
import nl.buildforce.sequoia.jpa.processor.core.util.UriInfoDouble;
import nl.buildforce.sequoia.jpa.processor.core.util.UriResourceNavigationDouble;
import nl.buildforce.sequoia.jpa.processor.core.util.UriResourcePropertyDouble;
import org.apache.olingo.commons.api.edm.EdmComplexType;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.edm.EdmProperty;
import org.apache.olingo.commons.api.edm.EdmType;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriParameter;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourceComplexProperty;
import org.apache.olingo.server.api.uri.UriResourceEntitySet;
import org.apache.olingo.server.api.uri.UriResourceKind;
import org.apache.olingo.server.api.uri.UriResourceValue;
import org.apache.olingo.server.api.uri.queryoption.ExpandItem;
import org.apache.olingo.server.api.uri.queryoption.ExpandOption;
import org.apache.olingo.server.api.uri.queryoption.SelectOption;
import org.junit.jupiter.api.Test;

import jakarta.persistence.criteria.Selection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestJPAQuerySelectClause extends TestQueryBase {

  @Test
  public void checkSelectAll() throws ODataApplicationException, ODataJPAModelException {
    fillJoinTable(root);

    List<Selection<?>> selectClause = cut.createSelectClause(joinTables, cut.buildSelectionPathList(
        new UriInfoDouble(new SelectOptionDouble("*"))), root, Collections.emptyList());
    assertEquals(jpaEntityType.getPathList().size(), selectClause.size());
  }

  @Test
  public void checkSelectAllWithSelectionNull() throws ODataApplicationException, ODataJPAModelException {
    fillJoinTable(root);
    final SelectOption selOpts = null;
    final List<Selection<?>> selectClause = cut.createSelectClause(joinTables, cut.buildSelectionPathList(
        new UriInfoDouble(selOpts)), root, Collections.emptyList());

    assertEquals(jpaEntityType.getPathList().size(), selectClause.size());
  }

  @Test
  public void checkSelectExpandViaIgnoredProperties() throws ODataApplicationException {
    // Organizations('3')/Address?$expand=AdministrativeDivision
    fillJoinTable(root);
    final List<ExpandItem> expItems = new ArrayList<>();
    final EdmEntityType startEntity = new EdmEntityTypeDouble(nameBuilder, "Organization");
    final EdmEntityType targetEntity = new EdmEntityTypeDouble(nameBuilder, "AdministrativeDivision");
    final SelectOption selOpts = null;

    ExpandOption expOps = new ExpandOptionDouble("AdministrativeDivision", expItems);
    expItems.add(new ExpandItemDouble(targetEntity));
    List<UriResource> startResources = new ArrayList<>();
    UriInfoDouble uriInfo = new UriInfoDouble(selOpts);
    uriInfo.setExpandOpts(expOps);
    uriInfo.setUriResources(startResources);

    startResources.add(new UriResourceNavigationDouble(startEntity));
    startResources.add(new UriResourcePropertyDouble(new EdmPropertyDouble("Address")));

    List<Selection<?>> selectClause = cut.createSelectClause(joinTables, cut.buildSelectionPathList(uriInfo), root,
        Collections.emptyList());

    assertContains(selectClause, "Address/RegionCodeID");
  }

  @Test
  public void checkSelectOnePropertyCreatedAt() throws ODataApplicationException {
    List<Selection<?>> selectClause = cut.createSelectClause(joinTables, cut.buildSelectionPathList(
        new UriInfoDouble(new SelectOptionDouble("CreationDateTime"))), root, Collections.emptyList());
    assertEquals(3, selectClause.size());
    assertContains(selectClause, "CreationDateTime");
    assertContains(selectClause, "ID");
    assertContains(selectClause, "ETag");
  }

  @Test
  public void checkSelectOnePropertyID() throws ODataApplicationException {
    List<Selection<?>> selectClause = cut.createSelectClause(joinTables, cut.buildSelectionPathList(
        new UriInfoDouble(new SelectOptionDouble("ID"))), root, Collections.emptyList());
    assertEquals(2, selectClause.size());
    assertContains(selectClause, "ID");
    assertContains(selectClause, "ETag");
  }

  @Test
  public void checkSelectOnePropertyPartKey() throws ODataJPAException, JPAIllegalAccessException, ODataApplicationException {
    jpaEntityType = helper.getJPAEntityType("AdministrativeDivisionDescriptions");
    buildRequestContext("AdministrativeDivisionDescriptions", "AdministrativeDivisionDescription");

    cut = new JPAJoinQuery(null, context, headers, requestContext);

    root = emf.getCriteriaBuilder().createTupleQuery().from(jpaEntityType.getTypeClass());
    joinTables.put(jpaEntityType.getInternalName(), root);

    List<Selection<?>> selectClause = cut.createSelectClause(joinTables, cut.buildSelectionPathList(
        new UriInfoDouble((new SelectOptionDouble("CodePublisher")))), root, Collections.emptyList());
    assertEquals(4, selectClause.size());
    assertContains(selectClause, "CodePublisher");
    assertContains(selectClause, "CodeID");
    assertContains(selectClause, "DivisionCode");
    assertContains(selectClause, "Language");
  }

  @Test
  public void checkSelectPropertyTypeCreatedAt() throws ODataApplicationException {
    List<Selection<?>> selectClause = cut.createSelectClause(joinTables, cut.buildSelectionPathList(
        new UriInfoDouble(new SelectOptionDouble("Type,CreationDateTime"))), root, Collections.emptyList());

    assertEquals(4, selectClause.size());
    assertContains(selectClause, "CreationDateTime");
    assertContains(selectClause, "Type");
    assertContains(selectClause, "ETag");
    assertContains(selectClause, "ID");
  }

  @Test
  public void checkSelectSupertypePropertyTypeName2() throws ODataJPAException, JPAIllegalAccessException, ODataApplicationException {
    jpaEntityType = helper.getJPAEntityType("Organizations");
    root = emf.getCriteriaBuilder().createTupleQuery().from(jpaEntityType.getTypeClass());
    joinTables.put(jpaEntityType.getInternalName(), root);
    buildRequestContext("Organizations", "Organization");

    cut = new JPAJoinQuery(null, context, headers, requestContext);

    List<Selection<?>> selectClause = cut.createSelectClause(joinTables, cut.buildSelectionPathList(
        new UriInfoDouble(new SelectOptionDouble("Type,Name2"))), root, Collections.emptyList());
    assertContains(selectClause, "Name2");
    assertContains(selectClause, "Type");
    assertContains(selectClause, "ID");
    assertEquals(4, selectClause.size());
  }

  @Test
  public void checkSelectCompleteComplexType() throws ODataJPAException, ODataApplicationException {
    // Organizations$select=Address
    jpaEntityType = helper.getJPAEntityType("Organizations");
    root = emf.getCriteriaBuilder().createTupleQuery().from(jpaEntityType.getTypeClass());
    joinTables.put(jpaEntityType.getInternalName(), root);
    fillJoinTable(root);

    cut = new JPAJoinQuery(null, context, headers, requestContext);

    List<Selection<?>> selectClause = cut.createSelectClause(joinTables, cut.buildSelectionPathList(
        new UriInfoDouble(new SelectOptionDouble("Address"))), root, Collections.emptyList());
    assertEquals(TestDataConstants.NO_ATTRIBUTES_POSTAL_ADDRESS + 2, selectClause.size());
  }

  @Test
  public void checkSelectCompleteNestedComplexTypeLowLevel() throws ODataJPAException, ODataApplicationException {
    // Organizations$select=Address
    jpaEntityType = helper.getJPAEntityType("Organizations");
    root = emf.getCriteriaBuilder().createTupleQuery().from(jpaEntityType.getTypeClass());
    joinTables.put(jpaEntityType.getInternalName(), root);

    List<Selection<?>> selectClause = cut.createSelectClause(joinTables, cut.buildSelectionPathList(
        new UriInfoDouble(new SelectOptionDouble("AdministrativeInformation/Created"))), root, Collections.emptyList());
    assertEquals(4, selectClause.size());
    assertContains(selectClause, "AdministrativeInformation/Created/By");
    assertContains(selectClause, "AdministrativeInformation/Created/At");
    assertContains(selectClause, "ETag");
    assertContains(selectClause, "ID");
  }

  @Test
  public void checkSelectCompleteNestedComplexTypeHighLevel() throws ODataJPAException, ODataApplicationException {
    // Organizations$select=Address
    jpaEntityType = helper.getJPAEntityType("Organizations");
    root = emf.getCriteriaBuilder().createTupleQuery().from(jpaEntityType.getTypeClass());
    joinTables.put(jpaEntityType.getInternalName(), root);

    List<Selection<?>> selectClause = cut.createSelectClause(joinTables, cut.buildSelectionPathList(
        new UriInfoDouble(new SelectOptionDouble("AdministrativeInformation"))), root, Collections.emptyList());
    assertEquals(6, selectClause.size());
    assertContains(selectClause, "AdministrativeInformation/Created/By");
    assertContains(selectClause, "AdministrativeInformation/Created/At");
    assertContains(selectClause, "AdministrativeInformation/Updated/By");
    assertContains(selectClause, "AdministrativeInformation/Updated/At");
    assertContains(selectClause, "ETag");
    assertContains(selectClause, "ID");
  }

  @Test
  public void checkSelectElementOfComplexType() throws ODataJPAException, ODataApplicationException {
    // Organizations$select=Address/Country
    jpaEntityType = helper.getJPAEntityType("Organizations");
    root = emf.getCriteriaBuilder().createTupleQuery().from(jpaEntityType.getTypeClass());
    joinTables.put(jpaEntityType.getInternalName(), root);

    // SELECT c.address.geocode FROM Company c WHERE c.name = 'Random House'
    List<Selection<?>> selectClause = cut.createSelectClause(joinTables, cut.buildSelectionPathList(
        new UriInfoDouble(new SelectOptionDouble("Address/Country"))), root, Collections.emptyList());
    assertContains(selectClause, "Address/Country");
    assertContains(selectClause, "ID");
    assertContains(selectClause, "ETag");
    assertEquals(3, selectClause.size());
  }

  @Test
  public void checkSelectTextJoinSingleAttribute() throws ODataJPAException, ODataApplicationException {
    jpaEntityType = helper.getJPAEntityType("Organizations");
    root = emf.getCriteriaBuilder().createTupleQuery().from(jpaEntityType.getTypeClass());
    joinTables.put(jpaEntityType.getInternalName(), root);
    fillJoinTable(root);

    List<Selection<?>> selectClause = cut.createSelectClause(joinTables, cut.buildSelectionPathList(
        new UriInfoDouble(new SelectOptionDouble("Address/CountryName"))), root, Collections.emptyList());
    assertContains(selectClause, "Address/CountryName");
    assertContains(selectClause, "ID");
    assertContains(selectClause, "ETag");
    assertEquals(3, selectClause.size());
  }

  @Test
  public void checkSelectTextJoinComplexType() throws ODataJPAException, ODataApplicationException {
    jpaEntityType = helper.getJPAEntityType("Organizations");
    root = emf.getCriteriaBuilder().createTupleQuery().from(jpaEntityType.getTypeClass());
    joinTables.put(jpaEntityType.getInternalName(), root);
    fillJoinTable(root);

    List<Selection<?>> selectClause = cut.createSelectClause(joinTables, cut.buildSelectionPathList(
        new UriInfoDouble(new SelectOptionDouble("Address"))), root, Collections.emptyList());
    assertEquals(TestDataConstants.NO_ATTRIBUTES_POSTAL_ADDRESS + 2, selectClause.size());
    assertContains(selectClause, "Address/CountryName");
    assertContains(selectClause, "ETag");
    assertContains(selectClause, "ID");
  }

  @Test
  public void checkSelectStreamValueStatic() throws ODataJPAException, JPAIllegalAccessException, ODataApplicationException {
    jpaEntityType = helper.getJPAEntityType("PersonImages");
    root = emf.getCriteriaBuilder().createTupleQuery().from(jpaEntityType.getTypeClass());
    buildRequestContext("PersonImages", "PersonImage");

    cut = new JPAJoinQuery(null, context, headers, requestContext);

    UriInfoDouble uriInfo = new UriInfoDouble(new SelectOptionDouble("Address"));
    List<UriResource> uriResources = new ArrayList<>();
    uriInfo.setUriResources(uriResources);
    uriResources.add(new UriResourceEntitySetDouble());
    uriResources.add(new UriResourceValueDouble());

    List<Selection<?>> selectClause = cut.createSelectClause(joinTables, cut.buildSelectionPathList(uriInfo), root,
        Collections.emptyList());
    assertNotNull(selectClause);
    assertContains(selectClause, "Image");
    assertContains(selectClause, "ID");
  }

  @Test
  public void checkSelectStreamValueDynamic() throws ODataJPAException, JPAIllegalAccessException, ODataApplicationException {
    jpaEntityType = helper.getJPAEntityType("OrganizationImages");
    root = emf.getCriteriaBuilder().createTupleQuery().from(jpaEntityType.getTypeClass());
    buildRequestContext("OrganizationImages", "OrganizationImage");

    cut = new JPAJoinQuery(null, context, headers, requestContext);

    UriInfoDouble uriInfo = new UriInfoDouble(new SelectOptionDouble("Address"));
    List<UriResource> uriResources = new ArrayList<>();
    uriInfo.setUriResources(uriResources);
    uriResources.add(new UriResourceEntitySetDouble());
    uriResources.add(new UriResourceValueDouble());

    List<Selection<?>> selectClause = cut.createSelectClause(joinTables, cut.buildSelectionPathList(uriInfo), root,
        Collections.emptyList());
    assertNotNull(selectClause);
    assertContains(selectClause, "Image");
    assertContains(selectClause, "MimeType");
    assertContains(selectClause, "ID");
  }

  @Test
  public void checkSelectPropertyValue() throws ODataJPAException, JPAIllegalAccessException, ODataApplicationException {
    jpaEntityType = helper.getJPAEntityType("PersonImages");
    root = emf.getCriteriaBuilder().createTupleQuery().from(jpaEntityType.getTypeClass());
    buildRequestContext("PersonImages", "PersonImage");
    cut = new JPAJoinQuery(null, context, headers, requestContext);

    final SelectOption selOpts = null;
    final UriInfoDouble uriInfo = new UriInfoDouble(selOpts);
    final List<UriResource> uriResources = new ArrayList<>();
    uriInfo.setUriResources(uriResources);
    // PersonImages('99')/AdministrativeInformation/Created/By/$value
    uriResources.add(new UriResourceEntitySetDouble());
    uriResources.add(new UriResourceComplexPropertyDouble(new EdmPropertyDouble("AdministrativeInformation")));
    uriResources.add(new UriResourceComplexPropertyDouble(new EdmPropertyDouble("Created")));
    uriResources.add(new UriResourcePropertyDouble(new EdmPropertyDouble("By")));
    uriResources.add(new UriResourceValueDouble());

    List<Selection<?>> selectClause = cut.createSelectClause(joinTables, cut.buildSelectionPathList(uriInfo), root,
        Collections.emptyList());
    assertNotNull(selectClause);
    assertContains(selectClause, "AdministrativeInformation/Created/By");
    assertContains(selectClause, "ID");
  }

  @Test
  public void checkSelectAllWithGroup() throws ODataApplicationException, ODataJPAModelException {

    fillJoinTable(root);
    List<Selection<?>> selectClause = cut.createSelectClause(joinTables, cut.buildSelectionPathList(
        new UriInfoDouble(new SelectOptionDouble("*"))), root, Collections.emptyList());
    assertEquals(jpaEntityType.getPathList().size(), selectClause.size());
  }

  private void assertContains(List<Selection<?>> selectClause, String alias) {
    for (Selection<?> selection : selectClause) {
      if (selection.getAlias().equals(alias))
        return;
    }
    fail(alias + " not found");
  }

  private static class UriResourceValueDouble implements UriResourceValue {

    @Override
    public UriResourceKind getKind() {
      return UriResourceKind.value;
    }

    @Override
    public String getSegmentValue() {
      return null;
    }
  }

  private static class UriResourceComplexPropertyDouble implements UriResourceComplexProperty {
    private final EdmProperty property;

    public UriResourceComplexPropertyDouble(EdmProperty property) {
      this.property = property;
    }

    @Override
    public EdmProperty getProperty() {
      return property;
    }

    @Override
    public EdmType getType() {
      fail();
      return null;
    }

    @Override
    public boolean isCollection() {
      fail();
      return false;
    }

    @Override
    public String getSegmentValue(boolean includeFilters) {
      fail();
      return null;
    }

    @Override
    public String toString(boolean includeFilters) {
      fail();
      return null;
    }

    @Override
    public UriResourceKind getKind() {
      fail();
      return null;
    }

    @Override
    public String getSegmentValue() {
      fail();
      return null;
    }

    @Override
    public EdmComplexType getComplexType() {
      fail();
      return null;
    }

    @Override
    public EdmComplexType getComplexTypeFilter() {
      fail();
      return null;
    }

  }

  private static class UriResourceEntitySetDouble implements UriResourceEntitySet {

    @Override
    public EdmType getType() {
      fail();
      return null;
    }

    @Override
    public boolean isCollection() {
      fail();
      return false;
    }

    @Override
    public String getSegmentValue(boolean includeFilters) {
      fail();
      return null;
    }

    @Override
    public String toString(boolean includeFilters) {
      fail();
      return null;
    }

    @Override
    public UriResourceKind getKind() {
      fail();
      return null;
    }

    @Override
    public String getSegmentValue() {
      fail();
      return null;
    }

    @Override
    public EdmEntitySet getEntitySet() {
      fail();
      return null;
    }

    @Override
    public EdmEntityType getEntityType() {
      fail();
      return null;
    }

    @Override
    public List<UriParameter> getKeyPredicates() {
      fail();
      return null;
    }

    @Override
    public EdmType getTypeFilterOnCollection() {
      fail();
      return null;
    }

    @Override
    public EdmType getTypeFilterOnEntry() {
      fail();
      return null;
    }
  }

}