package nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.impl;

import nl.buildforce.sequoia.jpa.metadata.api.JPAEntityManagerFactory;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.DataSourceHelper;
import org.junit.jupiter.api.BeforeAll;

import jakarta.persistence.EntityManagerFactory;

public class TestMappingRoot {
  protected static final String PUNIT_NAME = "nl.buildforce.sequoia.jpa";
  protected static EntityManagerFactory emf;
  public static final String BUPA_CANONICAL_NAME = "nl.buildforce.sequoia.jpa.processor.core.testmodel.BusinessPartner";
  public static final String ORG_CANONICAL_NAME = "nl.buildforce.sequoia.jpa.processor.core.testmodel.Organization";
  public static final String ADDR_CANONICAL_NAME = "nl.buildforce.sequoia.jpa.processor.core.testmodel.PostalAddressData";
  public static final String COMM_CANONICAL_NAME = "nl.buildforce.sequoia.jpa.processor.core.testmodel.CommunicationData";
  public static final String ADMIN_CANONICAL_NAME =
      "nl.buildforce.sequoia.jpa.processor.core.testmodel.AdministrativeDivision";

  @BeforeAll
  public static void setupClass() {
    emf = JPAEntityManagerFactory.getEntityManagerFactory(PUNIT_NAME, DataSourceHelper.createDataSource(DataSourceHelper.DB_HSQLDB));
  }

}