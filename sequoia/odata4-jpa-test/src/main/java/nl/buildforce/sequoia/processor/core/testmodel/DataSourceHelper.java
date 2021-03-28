package nl.buildforce.sequoia.processor.core.testmodel;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.internal.jdbc.DriverDataSource;

import javax.sql.DataSource;

public enum  DataSourceHelper {
  DB_H2,
  DB_HSQLDB,
  DB_DERBY;

  private static final String DB_SCHEMA = "OLINGO";

  private static final String H2_URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
  private static final String H2_DRIVER_CLASS_NAME = "org.h2.Driver";

  private static final String HSQLDB_URL = "jdbc:hsqldb:mem:com.sample";
  private static final String HSQLDB_DRIVER_CLASS_NAME = "org.hsqldb.jdbcDriver";

  private static final String DERBY_URL =
      "jdbc:derby:testdb;create=true;traceFile=derby_trace.log;trace_level=0xFFFFFFFF";
  private static final String DERBY_DRIVER_CLASS_NAME = "org.apache.derby.jdbc.EmbeddedDriver";

  private static final String REMOTE_URL = "jdbc:$DBNAME$:$Host$:$Port$";

  public static DataSource createDataSource(DataSourceHelper database) {
    DriverDataSource ds;
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    ds = switch (database) {
      case DB_H2 ->
        new DriverDataSource(classLoader, H2_DRIVER_CLASS_NAME, H2_URL, null, null);
      case DB_HSQLDB ->
        new DriverDataSource(classLoader, HSQLDB_DRIVER_CLASS_NAME, HSQLDB_URL, null, null);
      case DB_DERBY ->
        new DriverDataSource(classLoader, DERBY_DRIVER_CLASS_NAME, DERBY_URL, null, null);

      default -> null;
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
    };

    Flyway flyway = Flyway.configure().dataSource(ds).schemas(DB_SCHEMA).load();
    flyway.migrate();
    return ds;
  }

}