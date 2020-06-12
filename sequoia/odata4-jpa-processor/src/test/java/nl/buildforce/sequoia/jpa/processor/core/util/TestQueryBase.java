package nl.buildforce.sequoia.jpa.processor.core.util;

import nl.buildforce.sequoia.jpa.metadata.api.JPAEdmProvider;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAEntityType;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAException;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.impl.JPADefaultEdmNameBuilder;
import nl.buildforce.sequoia.jpa.processor.core.api.JPAODataCRUDContextAccess;
import nl.buildforce.sequoia.jpa.processor.core.api.JPAODataContextAccessDouble;
import nl.buildforce.sequoia.jpa.processor.core.exception.JPAIllegalAccessException;
import nl.buildforce.sequoia.jpa.processor.core.processor.JPAODataRequestContextImpl;
import nl.buildforce.sequoia.jpa.processor.core.query.JPAAbstractJoinQuery;
import nl.buildforce.sequoia.jpa.processor.core.query.JPAJoinQuery;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.edm.EdmType;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriInfo;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourceEntitySet;
import org.apache.olingo.server.api.uri.UriResourceKind;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestQueryBase extends TestBase {

  protected JPAAbstractJoinQuery cut;
  protected JPAEntityType jpaEntityType;
  protected HashMap<String, From<?, ?>> joinTables;
  protected Root<?> root;
  protected JPAODataCRUDContextAccess context;
  protected UriInfo uriInfo;
  protected JPAODataRequestContextImpl requestContext;

  public TestQueryBase() {
    super();
  }

  @BeforeEach
  public void setup() throws ODataJPAException, JPAIllegalAccessException, ODataApplicationException {
    buildUriInfo("BusinessPartners", "BusinessPartner");

    helper = new TestHelper(emf, PUNIT_NAME);
    nameBuilder = new JPADefaultEdmNameBuilder(PUNIT_NAME);
    jpaEntityType = helper.getJPAEntityType("BusinessPartners");
    createHeaders();
    context = new JPAODataContextAccessDouble(new JPAEdmProvider(PUNIT_NAME, emf, null, TestBase.enumPackages), ds,
        null);

    requestContext = new JPAODataRequestContextImpl();
    requestContext.setEntityManager(emf.createEntityManager());
    requestContext.setUriInfo(uriInfo);
    cut = new JPAJoinQuery(null, context, headers, requestContext);

    root = emf.getCriteriaBuilder().createTupleQuery().from(jpaEntityType.getTypeClass());
    joinTables = new HashMap<>();
    joinTables.put(jpaEntityType.getExternalName(), root);
  }

  protected EdmType buildUriInfo(final String esName, final String etName) {
    uriInfo = Mockito.mock(UriInfo.class);
    final EdmEntitySet odataEs = Mockito.mock(EdmEntitySet.class);
    final EdmType odataType = Mockito.mock(EdmEntityType.class);
    final List<UriResource> resources = new ArrayList<>();
    final UriResourceEntitySet esResource = Mockito.mock(UriResourceEntitySet.class);
    Mockito.when(uriInfo.getUriResourceParts()).thenReturn(resources);
    Mockito.when(esResource.getKeyPredicates()).thenReturn(new ArrayList<>(0));
    Mockito.when(esResource.getEntitySet()).thenReturn(odataEs);
    Mockito.when(esResource.getKind()).thenReturn(UriResourceKind.entitySet);
    Mockito.when(esResource.getType()).thenReturn(odataType);
    Mockito.when(odataEs.getName()).thenReturn(esName);
    Mockito.when(odataType.getNamespace()).thenReturn(PUNIT_NAME);
    Mockito.when(odataType.getName()).thenReturn(etName);
    resources.add(esResource);
    return odataType;
  }

  protected /*EdmType*/ void buildRequestContext(final String esName, final String etName) throws JPAIllegalAccessException {
    final EdmType odataType = buildUriInfo(esName, etName);
    requestContext = new JPAODataRequestContextImpl();
    requestContext.setEntityManager(emf.createEntityManager());
    requestContext.setUriInfo(uriInfo);
    // return odataType;
  }

  protected void fillJoinTable(Root<?> joinRoot) {
    Join<?, ?> join = joinRoot.join("locationName", JoinType.LEFT);
    joinTables.put("locationName", join);
    join = joinRoot.join("address", JoinType.LEFT);
    join = join.join("countryName", JoinType.LEFT);
    joinTables.put("countryName", join);
    join = joinRoot.join("address", JoinType.LEFT);
    join = join.join("regionName", JoinType.LEFT);
    joinTables.put("regionName", join);
  }

}