package nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.impl;

import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmEnumeration;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAModelException;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.AbcClassification;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.AccessRights;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.TestDataConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestIntermediateSchema extends TestMappingRoot {
  private Reflections r;

  @BeforeEach
  public void setup() {
    r = mock(Reflections.class);
    when(r.getTypesAnnotatedWith(EdmEnumeration.class)).thenReturn(new HashSet<>(Arrays.asList(AbcClassification.class, AccessRights.class)));
  }

  @Test
  public void checkSchemaCanBeCreated() throws ODataJPAModelException {

    new IntermediateSchema(new JPADefaultEdmNameBuilder(PUNIT_NAME), emf.getMetamodel(), r);
  }

  @Test
  public void checkSchemaGetAllEntityTypes() throws ODataJPAModelException {
    IntermediateSchema schema = new IntermediateSchema(new JPADefaultEdmNameBuilder(PUNIT_NAME), emf.getMetamodel(), r);
    assertEquals(TestDataConstants.NO_ENTITY_TYPES, schema.getEdmItem().getEntityTypes().size(),
        "Wrong number of entities");
  }

  @Test
  public void checkSchemaGetEntityTypeByNameNotNull() throws ODataJPAModelException {
    IntermediateSchema schema = new IntermediateSchema(new JPADefaultEdmNameBuilder(PUNIT_NAME), emf.getMetamodel(), r);
    assertNotNull(schema.getEdmItem().getEntityType("BusinessPartner"));
  }

  @Test
  public void checkSchemaGetEntityTypeByNameRightEntity() throws ODataJPAModelException {
    IntermediateSchema schema = new IntermediateSchema(new JPADefaultEdmNameBuilder(PUNIT_NAME), emf.getMetamodel(), r);
    assertEquals("BusinessPartner", schema.getEdmItem().getEntityType("BusinessPartner").getName());
  }

  @Test
  public void checkSchemaGetAllComplexTypes() throws ODataJPAModelException {
    IntermediateSchema schema = new IntermediateSchema(new JPADefaultEdmNameBuilder(PUNIT_NAME), emf.getMetamodel(), r);
    // ChangeInformation,CommunicationData,AdministrativeInformation,PostalAddressData
    assertEquals(TestDataConstants.NO_COMPLEXTYPES, schema.getEdmItem().getComplexTypes().size(), "Wrong number of complex types");
  }

  @Test
  public void checkSchemaGetComplexTypeByNameNotNull() throws ODataJPAModelException {
    IntermediateSchema schema = new IntermediateSchema(new JPADefaultEdmNameBuilder(PUNIT_NAME), emf.getMetamodel(), r);
    assertNotNull(schema.getEdmItem().getComplexType("CommunicationData"));
  }

  @Test
  public void checkSchemaGetComplexTypeByNameRightEntity() throws ODataJPAModelException {
    IntermediateSchema schema = new IntermediateSchema(new JPADefaultEdmNameBuilder(PUNIT_NAME), emf.getMetamodel(), r);
    assertEquals("CommunicationData", schema.getEdmItem().getComplexType("CommunicationData").getName());
  }

  @Test
  public void checkSchemaGetAllFunctions() throws ODataJPAModelException {
    IntermediateSchema schema = new IntermediateSchema(new JPADefaultEdmNameBuilder(PUNIT_NAME), emf.getMetamodel(), r);
    assertEquals(10, schema.getEdmItem().getFunctions().size(), "Wrong number of entities");
  }
}