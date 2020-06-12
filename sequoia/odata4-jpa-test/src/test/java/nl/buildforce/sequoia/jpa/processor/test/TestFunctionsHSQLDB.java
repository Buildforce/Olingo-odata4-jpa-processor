package nl.buildforce.sequoia.jpa.processor.test;

import nl.buildforce.sequoia.jpa.processor.core.testmodel.AdministrativeDivision;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.DataSourceHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Tuple;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.eclipse.persistence.config.EntityManagerProperties.NON_JTA_DATASOURCE;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestFunctionsHSQLDB {
  protected static final String PUNIT_NAME = "nl.buildforce.sequoia.jpa";
  private static EntityManagerFactory emf;

  @BeforeAll
  public static void setupClass() {

    Map<String, Object> properties = new HashMap<>();

    DataSource ds = DataSourceHelper.createDataSource(DataSourceHelper.DB_HSQLDB);

    properties.put(NON_JTA_DATASOURCE, ds);
    emf = Persistence.createEntityManagerFactory(PUNIT_NAME, properties);
  }

  private EntityManager em;

  private CriteriaBuilder cb;

  @BeforeEach
  public void setup() {
    em = emf.createEntityManager();
    cb = em.getCriteriaBuilder();
  }

  // @Ignore
  @Test
  public void TestScalarFunctionsWhere() {
    CreateUDFHSQLDB();

    CriteriaQuery<Tuple> count = cb.createTupleQuery();
    Root<?> adminDiv = count.from(AdministrativeDivision.class);
    count.multiselect(adminDiv);

    count.where(cb.and(cb.greaterThan(
        //
        cb.function("PopulationDensity", Integer.class, adminDiv.get("area"), adminDiv.get("population")),
            60)), cb.equal(adminDiv.get("countryCode"), cb.literal("BEL")));
    // cb.literal
    TypedQuery<Tuple> tq = em.createQuery(count);
    List<Tuple> act = tq.getResultList();
    assertNotNull(act);
    tq.getFirstResult();
  }

  private void CreateUDFHSQLDB() {
    EntityTransaction t = em.getTransaction();

    // StringBuffer dropString = new StringBuffer("DROP FUNCTION PopulationDensity");

    t.begin();
    // Query d = em.createNativeQuery(dropString.toString());

    String sqlString = "CREATE FUNCTION  PopulationDensity (area INT, population BIGINT ) " +
            "RETURNS INT " +
            "IF area <= 0 THEN RETURN 0;" +
            "ELSE RETURN population / area; " +
            "END IF";
    Query q = em.createNativeQuery(sqlString);
    // d.executeUpdate();
    q.executeUpdate();
    t.commit();
  }

}