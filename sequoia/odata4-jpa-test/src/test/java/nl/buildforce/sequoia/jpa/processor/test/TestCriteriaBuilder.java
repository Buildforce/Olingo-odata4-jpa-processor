package nl.buildforce.sequoia.jpa.processor.test;

import nl.buildforce.sequoia.processor.core.testmodel.AdministrativeDivision;
import nl.buildforce.sequoia.processor.core.testmodel.AdministrativeDivisionDescription;
import nl.buildforce.sequoia.processor.core.testmodel.AdministrativeDivisionDescriptionKey;
import nl.buildforce.sequoia.processor.core.testmodel.BusinessPartner;
import nl.buildforce.sequoia.processor.core.testmodel.BusinessPartnerRole;
import nl.buildforce.sequoia.processor.core.testmodel.DataSourceHelper;
import nl.buildforce.sequoia.processor.core.testmodel.Organization;
import nl.buildforce.sequoia.processor.core.testmodel.Person;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

import org.eclipse.persistence.internal.jpa.EJBQueryImpl;
import org.eclipse.persistence.queries.DatabaseQuery;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.eclipse.persistence.config.EntityManagerProperties.NON_JTA_DATASOURCE;

public class TestCriteriaBuilder {
  protected static final String PUNIT_NAME = "nl.buildforce.sequoia";
  private static EntityManagerFactory emf;
  private EntityManager em;
  private CriteriaBuilder cb;

  @BeforeAll
  public static void setupClass() {
    Map<String, Object> properties = new HashMap<>();
    properties.put(NON_JTA_DATASOURCE, DataSourceHelper.createDataSource(DataSourceHelper.DB_HSQLDB));
    emf = Persistence.createEntityManagerFactory(PUNIT_NAME, properties);
  }

  @BeforeEach
  public void setup() {
    em = emf.createEntityManager();
    assertNotNull(em);
    cb = em.getCriteriaBuilder();
    assertNotNull(cb);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testSubstringWithExpression() {
    CriteriaQuery<Tuple> adminQ = cb.createTupleQuery();
    Root<AdministrativeDivisionDescription> adminRoot1 = adminQ.from(AdministrativeDivisionDescription.class);
//    (Expression<T>) cb.sum(jpaOperator.getLeft(), jpaOperator.getRightAsNumber());
//    cb.substring((Expression<String>) (jpaFunction.getParameter(0).get()), start, length);
    Path<?> p = adminRoot1.get("name");

    Expression<Integer> sum = cb.sum(cb.literal(1), cb.literal(4));

    adminQ.where(cb.equal(cb.substring((Expression<String>) (p), cb.literal(1), sum), "North"));
    adminQ.multiselect(adminRoot1.get("name"));
    TypedQuery<Tuple> tq = em.createQuery(adminQ);
    tq.getResultList();
  }

  @Disabled // It's too time consuming
  @Test
  public void testSubSelect() {
    // https://stackoverflow.com/questions/29719321/combining-conditional-expressions-with-and-and-or-predicates-using-the-jpa-c
    CriteriaQuery<Tuple> adminQ1 = cb.createTupleQuery();
    Subquery<Long> adminQ2 = adminQ1.subquery(Long.class);
    Subquery<Long> adminQ3 = adminQ2.subquery(Long.class);
    Subquery<Long> org = adminQ3.subquery(Long.class);

    Root<AdministrativeDivision> adminRoot1 = adminQ1.from(AdministrativeDivision.class);
    Root<AdministrativeDivision> adminRoot2 = adminQ2.from(AdministrativeDivision.class);
    Root<AdministrativeDivision> adminRoot3 = adminQ3.from(AdministrativeDivision.class);
    Root<Organization> org1 = org.from(Organization.class);

    org.where(cb.and(cb.equal(org1.get("iD"), "3")), createParentOrg(org1, adminRoot3));
    org.select(cb.literal(1L));

    adminQ3.where(cb.and(createParentAdmin(adminRoot3, adminRoot2), cb.exists(org)));
    adminQ3.select(cb.literal(1L));

    adminQ2.where(cb.and(createParentAdmin(adminRoot2, adminRoot1), cb.exists(adminQ3)));
    adminQ2.select(cb.literal(1L));

    adminQ1.where(cb.exists(adminQ2));
    adminQ1.multiselect(adminRoot1.get("divisionCode"));

    TypedQuery<Tuple> tq = em.createQuery(adminQ1);
    tq.getResultList();
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testSubSelectTopOrderBy() {
    // https://stackoverflow.com/questions/9321916/jpa-criteriabuilder-how-to-use-in-comparison-operator
    // https://stackoverflow.com/questions/24109412/in-clause-with-a-composite-primary-key-in-jpa-criteria#24265131
    CriteriaQuery<Tuple> roleQ = cb.createTupleQuery();
    Root<BusinessPartnerRole> roleRoot = roleQ.from(BusinessPartnerRole.class);

    Subquery<BusinessPartner> bupaQ = roleQ.subquery(BusinessPartner.class);
    @SuppressWarnings("rawtypes")
    Root bupaRoot = roleQ.from(BusinessPartner.class);

    bupaQ.select(bupaRoot.get("iD"));
//    Expression<String> exp = scheduleRequest.get("createdBy");
//    Predicate predicate = exp.in(myList);
//    criteria.where(predicate);

    List<String> ids = new ArrayList<>();
    ids.add("1");
    ids.add("2");
    bupaQ.where(bupaRoot.get("iD").in(ids));
//    bupaQ.select(
//        (Expression<BusinessPartner>) cb.construct(
//            BusinessPartner.class,
//            bupaRoot.get("ID")));

    // roleQ.where(cb.in(roleRoot.get("businessPartnerID")).value(bupaQ));
    roleQ.where(cb.in(roleRoot.get("businessPartnerID")).value(bupaQ));
    roleQ.multiselect(roleRoot.get("businessPartnerID"));
    TypedQuery<Tuple> tq = em.createQuery(roleQ);
    tq.getResultList();
  }

  @Test
  public void testFilterOnPrimitiveCollectionAttribute() {
    CriteriaQuery<Tuple> orgQ = cb.createTupleQuery();
    Root<Organization> orgRoot = orgQ.from(Organization.class);
    orgQ.select(orgRoot.get("iD"));
    orgQ.where(cb.like(orgRoot.get("comment"), "%just%"));
    TypedQuery<Tuple> tq = em.createQuery(orgQ);
    List<Tuple> act = tq.getResultList();
    assertEquals(1, act.size());
  }

  @Test
  public void testFilterOnEmbeddedCollectionAttribute() {
    CriteriaQuery<Tuple> pQ = cb.createTupleQuery();
    Root<Person> pRoot = pQ.from(Person.class);
    pQ.select(pRoot.get("iD"));
    pQ.where(cb.equal(pRoot.get("inhouseAddress").get("taskID"), "MAIN"));
    TypedQuery<Tuple> tq = em.createQuery(pQ);
    List<Tuple> act = tq.getResultList();
    assertEquals(1, act.size());
  }

  @Test
  public void testExpandCount() {
    CriteriaQuery<Tuple> count = cb.createTupleQuery();
    Root<?> roles = count.from(BusinessPartnerRole.class);

    count.multiselect(roles.get("businessPartnerID"), cb.count(roles).alias("$count"));
    count.groupBy(roles.get("businessPartnerID"));
    count.orderBy(cb.desc(cb.count(roles)));
    TypedQuery<Tuple> tq = em.createQuery(count);
    tq.getResultList();
    tq.getFirstResult();
  }

  @Test
  public void testAnd() {
    CriteriaQuery<Tuple> count = cb.createTupleQuery();
    Root<?> adminDiv = count.from(AdministrativeDivision.class);

    count.multiselect(adminDiv);
    Predicate[] restrictions = new Predicate[3];
    restrictions[0] = cb.equal(adminDiv.get("codeID"), "NUTS2");
    restrictions[1] = cb.equal(adminDiv.get("divisionCode"), "BE34");
    restrictions[2] = cb.equal(adminDiv.get("codePublisher"), "Eurostat");
    count.where(cb.and(restrictions));
    TypedQuery<Tuple> tq = em.createQuery(count);
    tq.getResultList();
    tq.getFirstResult();
  }

  @Disabled
  @Test
  public void testSearchEmbeddedId() {
    CriteriaQuery<Tuple> cq = cb.createTupleQuery();
    Root<?> adminDiv = cq.from(AdministrativeDivisionDescription.class);
    cq.multiselect(adminDiv);

    Subquery<AdministrativeDivisionDescriptionKey> sq = cq.subquery(AdministrativeDivisionDescriptionKey.class);
    Root<AdministrativeDivisionDescription> text = sq.from(AdministrativeDivisionDescription.class);
    sq.where(cb.function("CONTAINS", Boolean.class, text.get("name"), cb.literal("luettich")));
    Expression<AdministrativeDivisionDescriptionKey> exp = text.get("key");
    sq.select(exp);

    cq.where(cb.and(cb.equal(adminDiv.get("key").get("codeID"), "NUTS2"),
        cb.in(sq).value(sq)));
    TypedQuery<Tuple> tq = em.createQuery(cq);
    List<Tuple> act = tq.getResultList();
    System.out.println(act.size());
  }

  @Disabled
  @Test
  public void testSearchNoSubquery() {
    CriteriaQuery<Tuple> cq = cb.createTupleQuery();
    Root<?> adminDiv = cq.from(AdministrativeDivisionDescription.class);
    cq.multiselect(adminDiv);

    // Predicate[] restrictions = new Predicate[2];
    cq.where(
        cb.and(cb.equal(cb.conjunction(),
            cb.function("CONTAINS", Boolean.class, adminDiv.get("name"), cb.literal("luettich"))),
            cb.equal(adminDiv.get("key").get("codeID"), "NUTS2")));

    TypedQuery<Tuple> tq = em.createQuery(cq);
    List<Tuple> act = tq.getResultList();
    System.out.println(act.size());
  }

  @Test
  public void testInClauseSimpleKey() {

    final CriteriaQuery<Tuple> cq = cb.createTupleQuery();
    final Root<?> bupa = cq.from(BusinessPartner.class);
    cq.select(bupa.get("iD"));

    cq.where(cb.in(bupa.get("iD")).value("3"));
    // (bupa.get("iD").in(Arrays.asList("3")));

    TypedQuery<Tuple> tq = em.createQuery(cq);
    DatabaseQuery dq = ((EJBQueryImpl<Tuple>) tq).getDatabaseQuery();
    System.out.println(dq.getSQLString());
    List<Tuple> act = tq.getResultList();
    System.out.println(dq.getSQLString());
    assertEquals(1, act.size());
  }

  @Test
  public void testEntityTransaction() {
    assertFalse(em.getTransaction().isActive());
    em.getTransaction().begin();
    assertTrue(em.getTransaction().isActive());
  }

  // @Disabled
  @Test
  public void testInClauseComplexKey() {

    final CriteriaQuery<Tuple> cq = cb.createTupleQuery();
    final Root<?> adminDiv = cq.from(AdministrativeDivisionDescription.class);
    final AdministrativeDivisionDescriptionKey key = new AdministrativeDivisionDescriptionKey();
    cq.multiselect(adminDiv);

    key.setCodeID("3166-1");
    key.setCodePublisher("ISO");
    key.setDivisionCode("DEU");
    key.setLanguage("de");
    // Create IN step by step
    In<Object> in = cb.in(adminDiv.get("key"));
    in.value(key);
    cq.where(in);
    // Execute query
    TypedQuery<Tuple> tq = em.createQuery(cq);
    // DatabaseQuery dq = ((EJBQueryImpl<Tuple>) tq).getDatabaseQuery();
    final List<Tuple> act = tq.getResultList();
    // Ensure EclipseLink problem still exists: ("WHERE ((NULL, NULL, NULL, NULL) IN "));
    assertEquals(0, act.size());
  }

  private Expression<Boolean> createParentAdmin(Root<AdministrativeDivision> subQuery,
      Root<AdministrativeDivision> query) {
    return cb.and(cb.equal(query.get("codePublisher"), subQuery.get("codePublisher")),
        cb.and(cb.equal(query.get("codeID"), subQuery.get("parentCodeID")),
            cb.equal(query.get("divisionCode"), subQuery.get("parentDivisionCode"))));
  }

  private Predicate createParentOrg(Root<Organization> org1, Root<AdministrativeDivision> adminRoot3) {
    return cb.and(cb.equal(adminRoot3.get("codePublisher"), org1.get("address").get("regionCodePublisher")),
        cb.and(cb.equal(adminRoot3.get("codeID"), org1.get("address").get("regionCodeID")),
            cb.equal(adminRoot3.get("divisionCode"), org1.get("address").get("region"))));
  }

}