package nl.buildforce.sequoia.jpa.processor.core.query;

import nl.buildforce.sequoia.jpa.processor.core.testmodel.DataSourceHelper;
import nl.buildforce.sequoia.jpa.processor.core.util.IntegrationTestHelper;
import nl.buildforce.sequoia.jpa.processor.core.util.TestHelper;
import org.apache.olingo.commons.api.ex.ODataException;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.eclipse.persistence.config.EntityManagerProperties.NON_JTA_DATASOURCE;

public class TestJPAFunction {
  protected static final String PUNIT_NAME = "nl.buildforce.sequoia.jpa";
  protected static EntityManagerFactory emf;
  protected static DataSource ds;

  protected TestHelper helper;
  protected Map<String, List<String>> headers;

  @BeforeEach
  public void setup() {
    ds = DataSourceHelper.createDataSource(DataSourceHelper.DB_HSQLDB);
    Map<String, Object> properties = new HashMap<>();
    properties.put(NON_JTA_DATASOURCE, ds);
    emf = Persistence.createEntityManagerFactory(PUNIT_NAME, properties);
    emf.getProperties();
  }

  @Disabled // TODO check is path is in general allowed
  @Test
  public void testNavigationAfterFunctionNotAllowed() throws IOException, ODataException {
    IntegrationTestHelper helper = new IntegrationTestHelper(emf, ds,
        "Siblings(DivisionCode='BE25',CodeID='NUTS2',CodePublisher='Eurostat')/Parent");
    helper.assertStatus(501);
  }

  @Test
  public void testFunctionGenerateQueryString() throws IOException, ODataException {
    createFunction();
    IntegrationTestHelper helper = new IntegrationTestHelper(emf, ds,
        "Siblings(DivisionCode='BE25',CodeID='NUTS2',CodePublisher='Eurostat')");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());
  }

  private void createFunction() {
    final EntityManager em = emf.createEntityManager();
    final EntityTransaction t = em.getTransaction();
    t.begin();
    String createSiblingsString = "CREATE FUNCTION  \"OLINGO\".\"Siblings\" (\"Publisher\" VARCHAR(10), \"ID\" VARCHAR(10), \"Division\" VARCHAR(10)) " +
            "RETURNS TABLE(\"CodePublisher\" VARCHAR(10),\"CodeID\" VARCHAR(10),\"DivisionCode\" VARCHAR(10)," +
            "\"CountryISOCode\" VARCHAR(4), \"ParentCodeID\" VARCHAR(10),\"ParentDivisionCode\" VARCHAR(10)," +
            "\"AlternativeCode\" VARCHAR(10),\"Area\" int, \"Population\" BIGINT) " +
            "READS SQL DATA " +
            "RETURN TABLE( SELECT * FROM \"AdministrativeDivision\" as a  WHERE " +
            "EXISTS (SELECT \"CodePublisher\" " +
            "FROM \"OLINGO\".\"AdministrativeDivision\" as b " +
            "WHERE b.\"CodeID\" = \"ID\" " +
            "AND   b.\"DivisionCode\" = \"Division\" " +
            "AND   b.\"CodePublisher\" = a.\"CodePublisher\" " +
            "AND   b.\"ParentCodeID\" = a.\"ParentCodeID\" " +
            "AND   b.\"ParentDivisionCode\" = a.\"ParentDivisionCode\") " +
            "AND NOT( a.\"CodePublisher\" = \"Publisher\" " +
            "AND  a.\"CodeID\" = \"ID\" " +
            "AND  a.\"DivisionCode\" = \"Division\" )); ";
    Query qP = em.createNativeQuery(createSiblingsString);
    qP.executeUpdate();
    t.commit();
  }

}