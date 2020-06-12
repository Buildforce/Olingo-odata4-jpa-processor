package nl.buildforce.sequoia.jpa.processor.core.testmodel;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.internal.jdbc.DriverDataSource;

import javax.sql.DataSource;

public class DataSourceHelper {
  private static final String DB_SCHEMA = "OLINGO";

  private static final String H2_URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
  private static final String H2_DRIVER_CLASS_NAME = "org.h2.Driver";

  private static final String HSQLDB_URL = "jdbc:hsqldb:mem:com.sample";
  private static final String HSQLDB_DRIVER_CLASS_NAME = "org.hsqldb.jdbcDriver";

  private static final String DERBY_URL =
      "jdbc:derby:testdb;create=true;traceFile=derby_trace.log;trace_level=0xFFFFFFFF";
  private static final String DERBY_DRIVER_CLASS_NAME = "org.apache.derby.jdbc.EmbeddedDriver";

  private static final String REMOTE_URL = "jdbc:$DBNAME$:$Host$:$Port$";

  public static final int DB_H2 = 1;
  public static final int DB_HSQLDB = 2;
  public static final int DB_REMOTE = 3;
  public static final int DB_DERBY = 4;

  public static DataSource createDataSource(int database) {
    DriverDataSource ds;
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    switch (database) {
      case DB_H2:
        ds = new DriverDataSource(classLoader, H2_DRIVER_CLASS_NAME, H2_URL, null, null);
        break;
      case DB_HSQLDB:
        ds = new DriverDataSource(classLoader, HSQLDB_DRIVER_CLASS_NAME, HSQLDB_URL, null, null);
        break;
      case DB_DERBY:
        ds = new DriverDataSource(classLoader, DERBY_DRIVER_CLASS_NAME, DERBY_URL, null, null);
        break;
/*      case DB_REMOTE:
        String env = System.getenv().get("REMOTE_DB_LOGON");
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode connectionInfo;
        try {
          connectionInfo = (ObjectNode) mapper.readTree(env);
        } catch (IOException e) {
          return null;
        }
        String url = REMOTE_URL;
        url = url.replace("$Host$", connectionInfo.get("hostname").asText());
        url = url.replace("$Port$", connectionInfo.get("port").asText());
        url = url.replace("$DBNAME$", connectionInfo.get("dbname").asText());
        String driver = connectionInfo.get("driver").asText();

        ds = new DriverDataSource(classLoader, driver, url, connectionInfo.get("username").asText(),
                connectionInfo.get("password").asText(), new Properties());
        break;*/
      default:
        return null;
    }

    System.out.println("Selected DB " + ds.getUrl());
    Flyway flyway = Flyway.configure().dataSource(ds).schemas(DB_SCHEMA).load();
    flyway.migrate();
    return ds;
  }
}