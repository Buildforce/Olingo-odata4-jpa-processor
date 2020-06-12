package nl.buildforce.sequoia.jpa.processor.test;

import nl.buildforce.sequoia.jpa.processor.core.testmodel.AdministrativeDivision;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.DataSourceHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.eclipse.persistence.config.EntityManagerProperties.NON_JTA_DATASOURCE;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestFunctions {
  protected static final String PUNIT_NAME = "nl.buildforce.sequoia.jpa";
  private static EntityManagerFactory emf;
  private static DataSource ds;

  @BeforeAll
  public static void setupClass() {

    Map<String, Object> properties = new HashMap<>();

    ds = DataSourceHelper.createDataSource(DataSourceHelper.DB_HSQLDB);

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

  @Disabled
  @Test
  public void TestProcedure() throws SQLException {
    StoredProcedureQuery pc = em.createStoredProcedureQuery("\"OLINGO\".\"org.apache.olingo.jpa::Siblings\"");

    pc.registerStoredProcedureParameter("CodePublisher", String.class, ParameterMode.IN);
    pc.setParameter("CodePublisher", "Eurostat");
    pc.registerStoredProcedureParameter("CodeID", String.class, ParameterMode.IN);
    pc.setParameter("CodeID", "NUTS2");
    pc.registerStoredProcedureParameter("DivisionCode", String.class, ParameterMode.IN);
    pc.setParameter("DivisionCode", "BE25");
//    pc.setParameter("CodePublisher", "Eurostat");
//    pc.setParameter("CodeID", "NUTS2");
//    pc.setParameter("DivisionCode", "BE25");

    Connection conn = ds.getConnection();
    DatabaseMetaData meta = conn.getMetaData();
    ResultSet metaR = meta.getProcedures(conn.getCatalog(), "OLINGO", "%");

    while (metaR.next()) {
      String procedureCatalog = metaR.getString(1);
      String procedureSchema = metaR.getString(2);
      String procedureName = metaR.getString(3);
//          reserved for future use
//          reserved for future use
//          reserved for future use
      String remarks = metaR.getString(7);
      short procedureTYpe = metaR.getShort(8);
//      String specificName = metaR.getString(9);

      System.out.println("procedureCatalog=" + procedureCatalog);
      System.out.println("procedureSchema=" + procedureSchema);
      System.out.println("procedureName=" + procedureName);
      System.out.println("remarks=" + remarks);
      System.out.println("procedureType=" + procedureTYpe);
//      System.out.println("specificName=" + specificName);
    }
    ResultSet rs = meta.getProcedureColumns(conn.getCatalog(),
        "OLINGO", "%", "%");

    while (rs.next()) {
      // get stored procedure metadata
      String procedureCatalog = rs.getString(1);
      String procedureSchema = rs.getString(2);
      String procedureName = rs.getString(3);
      String columnName = rs.getString(4);
      short columnReturn = rs.getShort(5);
      int columnDataType = rs.getInt(6);
      String columnReturnTypeName = rs.getString(7);
      int columnPrecision = rs.getInt(8);
      int columnByteLength = rs.getInt(9);
      short columnScale = rs.getShort(10);
      short columnRadix = rs.getShort(11);
      short columnNullable = rs.getShort(12);
      String columnRemarks = rs.getString(13);

      System.out.println("stored Procedure name=" + procedureName);
      System.out.println("procedureCatalog=" + procedureCatalog);
      System.out.println("procedureSchema=" + procedureSchema);
      System.out.println("procedureName=" + procedureName);
      System.out.println("columnName=" + columnName);
      System.out.println("columnReturn=" + columnReturn);
      System.out.println("columnDataType=" + columnDataType);
      System.out.println("columnReturnTypeName=" + columnReturnTypeName);
      System.out.println("columnPrecision=" + columnPrecision);
      System.out.println("columnByteLength=" + columnByteLength);
      System.out.println("columnScale=" + columnScale);
      System.out.println("columnRadix=" + columnRadix);
      System.out.println("columnNullable=" + columnNullable);
      System.out.println("columnRemarks=" + columnRemarks);
    }
    conn.close();
    pc.execute();
    List<?> r = pc.getResultList();

    Object[] one = (Object[]) r.get(0);
    assertNotNull(one);
  }

  @Disabled
  @Test
  public void TestScalarFunctionsWhere() {
    CreateUDFDerby();

    CriteriaQuery<Tuple> count = cb.createTupleQuery();
    Root<?> adminDiv = count.from(AdministrativeDivision.class);
    count.multiselect(adminDiv);
    count.where(cb.equal(cb.function("IS_PRIME", boolean.class, cb.literal(5)), Boolean.TRUE));
    // cb.literal
    TypedQuery<Tuple> tq = em.createQuery(count);
    List<Tuple> act = tq.getResultList();
    assertNotNull(act);
    tq.getFirstResult();
  }

  private void CreateUDFDerby() {
    EntityTransaction t = em.getTransaction();

    t.begin();
    Query d = em.createNativeQuery("DROP FUNCTION IS_PRIME");
    String sqlString = "CREATE FUNCTION IS_PRIME(number Integer) RETURNS Integer " +
            "PARAMETER STYLE JAVA NO SQL LANGUAGE JAVA " +
            "EXTERNAL NAME 'nl.buildforce.sequoia.jpa.processor.core.test_udf.isPrime'";
    Query q = em.createNativeQuery(sqlString);
    d.executeUpdate();
    q.executeUpdate();
    t.commit();
  }

}