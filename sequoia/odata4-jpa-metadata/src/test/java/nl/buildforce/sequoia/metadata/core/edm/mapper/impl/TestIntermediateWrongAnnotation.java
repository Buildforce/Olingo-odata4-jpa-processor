package nl.buildforce.sequoia.metadata.core.edm.mapper.impl;

import nl.buildforce.sequoia.metadata.api.JPAEntityManagerFactory;
import nl.buildforce.sequoia.metadata.core.edm.mapper.exception.ODataJPAModelException;
import nl.buildforce.sequoia.processor.core.errormodel.CollectionAttributeProtected;
import nl.buildforce.sequoia.processor.core.errormodel.ComplexProtectedNoPath;
import nl.buildforce.sequoia.processor.core.errormodel.ComplexProtectedWrongPath;
import nl.buildforce.sequoia.processor.core.errormodel.EmbeddedKeyPartOfGroup;
import nl.buildforce.sequoia.processor.core.errormodel.KeyPartOfGroup;
import nl.buildforce.sequoia.processor.core.errormodel.MandatoryPartOfGroup;
import nl.buildforce.sequoia.processor.core.errormodel.NavigationAttributeProtected;
import nl.buildforce.sequoia.processor.core.errormodel.NavigationPropertyPartOfGroup;
import nl.buildforce.sequoia.processor.core.errormodel.PersonDeepCollectionProtected;
import nl.buildforce.sequoia.processor.core.testmodel.DataSourceHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.PluralAttribute;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestIntermediateWrongAnnotation {
  private TestHelper helper;
  protected static final String PUNIT_NAME = "error";
  protected static EntityManagerFactory emf;

  @BeforeEach
  public void setup() throws ODataJPAModelException {
    emf = JPAEntityManagerFactory.getEntityManagerFactory(PUNIT_NAME, DataSourceHelper.createDataSource(DataSourceHelper.DB_HSQLDB));
    helper = new TestHelper(emf.getMetamodel(), PUNIT_NAME);
  }

  @Test
  public void checkErrorOnProtectedCollectionAttribute() {
    final PluralAttribute<?, ?, ?> jpaAttribute = helper.getCollectionAttribute(helper.getEntityType(
        CollectionAttributeProtected.class), "inhouseAddress");
    final IntermediateStructuredType entityType = helper.schema.getEntityType(CollectionAttributeProtected.class);

    final ODataJPAModelException act = assertThrows(ODataJPAModelException.class,
        () -> new IntermediateCollectionProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
            jpaAttribute, helper.schema, entityType));

    Assertions.assertEquals(ODataJPAModelException.MessageKeys.NOT_SUPPORTED_PROTECTED_COLLECTION.name(), act.getId());
    assertFalse(act.getMessage().isEmpty());

  }

  @Test
  public void checkErrorOnProtectedCollectionAttributeDeep() {
    final PluralAttribute<?, ?, ?> jpaAttribute = helper.getCollectionAttribute(helper.getEntityType(
        PersonDeepCollectionProtected.class), "inhouseAddress");
    final IntermediateStructuredType entityType = helper.schema.getEntityType(PersonDeepCollectionProtected.class);

    final ODataJPAModelException act = assertThrows(ODataJPAModelException.class,
        () -> new IntermediateCollectionProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
            jpaAttribute, helper.schema, entityType));

    Assertions.assertEquals(ODataJPAModelException.MessageKeys.NOT_SUPPORTED_PROTECTED_COLLECTION.name(), act.getId());
    assertFalse(act.getMessage().isEmpty());
  }

  @Test
  public void checkErrorOnProtectedNavigationAttribute() {
    final Attribute<?, ?> jpaAttribute = helper.getDeclaredAttribute(helper.getEntityType(
        NavigationAttributeProtected.class), "teams");

    final ODataJPAModelException act = assertThrows(ODataJPAModelException.class,
        () -> new IntermediateNavigationProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
            helper.schema.getEntityType(NavigationAttributeProtected.class), jpaAttribute, helper.schema));

    Assertions.assertEquals(ODataJPAModelException.MessageKeys.NOT_SUPPORTED_PROTECTED_NAVIGATION.name(), act.getId());
    assertFalse(act.getMessage().isEmpty());
  }

  @Test
  public void checkErrorOnProtectedComplexAttributeMissingPath() {
    final Attribute<?, ?> jpaAttribute = helper.getDeclaredAttribute(helper.getEntityType(
        ComplexProtectedNoPath.class),
        "administrativeInformation");

    final ODataJPAModelException act = assertThrows(ODataJPAModelException.class,
        () -> new IntermediateSimpleProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME), jpaAttribute, helper.schema));

    Assertions.assertEquals(ODataJPAModelException.MessageKeys.COMPLEX_PROPERTY_MISSING_PROTECTION_PATH.name(), act.getId());
    assertFalse(act.getMessage().isEmpty());
  }

  @Test
  public void checkErrorOnProtectedComplexAttributeWrongPath() throws ODataJPAModelException {
    // ComplexProtectedWrongPath
    final EntityType<?> jpaEt = helper.getEntityType(ComplexProtectedWrongPath.class);
    final IntermediateEntityType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), jpaEt,
        helper.schema);
    et.getEdmItem();

    final ODataJPAModelException act = assertThrows(ODataJPAModelException.class, et::getProtections);

    Assertions.assertEquals(ODataJPAModelException.MessageKeys.COMPLEX_PROPERTY_WRONG_PROTECTION_PATH.name(), act.getId());
    assertFalse(act.getMessage().isEmpty());

  }

  @Test
  public void checkErrorOnNavigationPropertyPartOfGroup() {
    final Attribute<?, ?> jpaAttribute = helper.getDeclaredAttribute(helper.getEntityType(
        NavigationPropertyPartOfGroup.class), "teams");
    final IntermediateStructuredType entityType = helper.schema.getEntityType(NavigationPropertyPartOfGroup.class);

    final ODataJPAModelException act = assertThrows(ODataJPAModelException.class,
        () -> new IntermediateNavigationProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME), entityType, jpaAttribute,
            helper.schema));

    Assertions.assertEquals(ODataJPAModelException.MessageKeys.NOT_SUPPORTED_NAVIGATION_PART_OF_GROUP.name(), act.getId());
    assertFalse(act.getMessage().isEmpty());
  }

  @Test
  public void checkErrorOnMandatoryPropertyPartOfGroup() {
    final Attribute<?, ?> jpaAttribute = helper.getDeclaredAttribute(helper.getEntityType(
        MandatoryPartOfGroup.class), "eTag");

    final ODataJPAModelException act = assertThrows(ODataJPAModelException.class,
        () -> new IntermediateSimpleProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME), jpaAttribute, helper.schema));

    Assertions.assertEquals(ODataJPAModelException.MessageKeys.NOT_SUPPORTED_MANDATORY_PART_OF_GROUP.name(), act.getId());
    assertFalse(act.getMessage().isEmpty());
  }

  @Test
  public void checkErrorOnKeyPropertyPartOfGroup() {
    final Attribute<?, ?> jpaAttribute = helper.getDeclaredAttribute(helper.getEntityType(
        KeyPartOfGroup.class), "iD");

    final ODataJPAModelException act = assertThrows(ODataJPAModelException.class,
        () -> new IntermediateSimpleProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME), jpaAttribute, helper.schema));

    Assertions.assertEquals(ODataJPAModelException.MessageKeys.NOT_SUPPORTED_KEY_PART_OF_GROUP.name(), act.getId());
    assertFalse(act.getMessage().isEmpty());
  }

  @Test
  public void checkErrorOnEmbeddedKeyPropertyPartOfGroup() {
    final Attribute<?, ?> jpaAttribute = helper.getDeclaredAttribute(helper.getEntityType(
        EmbeddedKeyPartOfGroup.class), "key");

    final ODataJPAModelException act = assertThrows(ODataJPAModelException.class,
        () -> new IntermediateSimpleProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME), jpaAttribute, helper.schema));

    Assertions.assertEquals(ODataJPAModelException.MessageKeys.NOT_SUPPORTED_KEY_PART_OF_GROUP.name(), act.getId());
    assertFalse(act.getMessage().isEmpty());
  }

}