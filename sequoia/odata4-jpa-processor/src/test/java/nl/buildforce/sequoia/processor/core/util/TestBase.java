package nl.buildforce.sequoia.processor.core.util;

import nl.buildforce.sequoia.metadata.api.JPAEntityManagerFactory;
import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAEdmNameBuilder;
import nl.buildforce.sequoia.metadata.core.edm.mapper.impl.JPADefaultEdmNameBuilder;
import nl.buildforce.sequoia.processor.core.testmodel.DataSourceHelper;
import org.junit.jupiter.api.BeforeAll;

import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestBase {

  protected static final String uriPrefix = "http://localhost:8080/Test/Olingo.svc/";

  protected static final String PUNIT_NAME = "nl.buildforce.sequoia";
  public static final String[] enumPackages = { "nl.buildforce.sequoia.processor.core.testmodel" };
  protected static EntityManagerFactory emf;
  protected TestHelper helper;
  protected final HashMap<String, List<String>> headers = new HashMap<>();
  protected static JPAEdmNameBuilder nameBuilder;
  protected static DataSource ds;

  @BeforeAll
  public static void setupClass() {
    ds = DataSourceHelper.createDataSource(DataSourceHelper.DB_DERBY);
    emf = JPAEntityManagerFactory.getEntityManagerFactory(PUNIT_NAME, ds);
    nameBuilder = new JPADefaultEdmNameBuilder(PUNIT_NAME);
  }

  protected void addHeader(final String header, final String value) {
    final List<String> newHeader = new ArrayList<>();
    newHeader.add(value);
    headers.put(header, newHeader);
  }

}