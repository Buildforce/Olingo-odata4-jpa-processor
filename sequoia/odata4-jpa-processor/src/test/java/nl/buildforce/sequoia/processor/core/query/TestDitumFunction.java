package nl.buildforce.sequoia.processor.core.query;

import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import nl.buildforce.olingo.commons.api.ex.ODataException;
import nl.buildforce.olingo.commons.api.http.HttpStatusCode;
import nl.buildforce.sequoia.processor.core.testmodel.DataSourceHelper;
import nl.buildforce.sequoia.processor.core.util.IntegrationTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.eclipse.persistence.config.EntityManagerProperties.NON_JTA_DATASOURCE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestDitumFunction {
  protected static final String PUNIT_NAME = "nl.buildforce.sequoia";
  protected static EntityManagerFactory emf;
  protected static DataSource ds = DataSourceHelper.createDataSource(DataSourceHelper.DB_HSQLDB);

  @BeforeEach
  public void setup() throws ODataException {

    Map<String, Object> properties = new HashMap<>();
    properties.put(NON_JTA_DATASOURCE, ds);
    emf = Persistence.createEntityManagerFactory(PUNIT_NAME, properties);

  }

  @Test
  public void testDitumFunction() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(emf, ds, "DitumFunction(A=10,B=50)",
            "nl.buildforce.sequoia.processor.core.testobjects");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());
    ObjectNode r = helper.getValue();
    assertNotNull(r);
    assertNotNull(r.get("value"));
    assertEquals(60, r.get("value").asInt());
  }

}
