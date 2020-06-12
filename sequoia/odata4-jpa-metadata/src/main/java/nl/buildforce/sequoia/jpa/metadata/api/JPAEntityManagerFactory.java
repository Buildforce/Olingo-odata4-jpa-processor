package nl.buildforce.sequoia.jpa.metadata.api;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class JPAEntityManagerFactory {
    public static final String NON_JTA_DATASOURCE0 = "javax.persistence.nonJtaDataSource";
    public static final String NON_JTA_DATASOURCE1 = "jakarta.persistence.nonJtaDataSource";

    private JPAEntityManagerFactory() { throw new IllegalStateException("JPAEntityManagerFactory class"); }

    public static EntityManagerFactory getEntityManagerFactory(final String pUnit, final DataSource ds) {
        final Map<String, Object> properties = new HashMap<>();

        properties.put(NON_JTA_DATASOURCE0, ds);
        properties.put(NON_JTA_DATASOURCE1, ds);
        return Persistence.createEntityManagerFactory(pUnit, properties);
    }

}