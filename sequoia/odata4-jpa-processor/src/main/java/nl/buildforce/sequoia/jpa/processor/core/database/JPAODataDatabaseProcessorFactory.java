package nl.buildforce.sequoia.jpa.processor.core.database;

import nl.buildforce.sequoia.jpa.processor.core.api.JPAODataDatabaseProcessor;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public class JPAODataDatabaseProcessorFactory {
  private static final String PRODUCT_NAME_H2 = "H2";
  private static final String PRODUCT_NAME_HSQLDB = "HSQL Database Engine";
  private static final String PRODUCT_NAME_SAP_HANA = "HDB";

  public JPAODataDatabaseProcessor create(final DataSource ds) throws SQLException {
    if (ds != null) {
      try (Connection connection = ds.getConnection()) {
        final DatabaseMetaData dbMetadata = connection.getMetaData();
        switch (dbMetadata.getDatabaseProductName()) {
          case PRODUCT_NAME_SAP_HANA: return new JPA_HANA_DatabaseProcessor();
          case PRODUCT_NAME_HSQLDB:
          case PRODUCT_NAME_H2: return new JPA_HSQLDB_DatabaseProcessor();
          default: return new JPADefaultDatabaseProcessor();
        }
      }
    } else {
      return new JPADefaultDatabaseProcessor();
    }
  }

}