package nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.impl;

import nl.buildforce.sequoia.jpa.metadata.api.JPAEdmMetadataPostProcessor;
import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmEnumeration;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAEntityType;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAOnConditionItem;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAPath;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAProtectionInfo;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAStructuredType;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAModelException;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.extension.IntermediateEntityTypeAccess;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.extension.IntermediateNavigationPropertyAccess;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.extension.IntermediatePropertyAccess;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.extension.IntermediateReferenceList;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.AbcClassification;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.TestDataConstants;
import org.apache.olingo.commons.api.edm.provider.CsdlAnnotation;
import org.apache.olingo.commons.api.edm.provider.annotation.CsdlCollection;
import org.apache.olingo.commons.api.edm.provider.annotation.CsdlConstantExpression;
import org.apache.olingo.commons.api.edm.provider.annotation.CsdlConstantExpression.ConstantExpressionType;
import org.apache.olingo.commons.api.edm.provider.annotation.CsdlExpression;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import jakarta.persistence.metamodel.EntityType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestIntermediateEntityType extends TestMappingRoot {
  private Set<EntityType<?>> etList;
  private IntermediateSchema schema;

  @BeforeEach
  public void setup() throws ODataJPAModelException {
    IntermediateModelElement.setPostProcessor(new DefaultEdmPostProcessor());
    final Reflections r = mock(Reflections.class);
    when(r.getTypesAnnotatedWith(EdmEnumeration.class)).thenReturn(new HashSet<>(Collections.singletonList(AbcClassification.class)));

    etList = emf.getMetamodel().getEntities();
    schema = new IntermediateSchema(new JPADefaultEdmNameBuilder(PUNIT_NAME), emf.getMetamodel(), r);
  }

  @Test
  public void checkEntityTypeCanBeCreated() {

    new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType("BusinessPartner"), schema);
  }

  @Test
  public void checkEntityTypeIgnoreSet() throws ODataJPAModelException {

    IntermediateStructuredType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "DummyToBeIgnored"), schema);
    et.getEdmItem();
    assertTrue(et.ignore());
  }

  @Test
  public void checkGetAllProperties() throws ODataJPAModelException {
    IntermediateStructuredType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "BusinessPartner"), schema);
    assertEquals(TestDataConstants.NO_DEC_ATTRIBUTES_BUSINESS_PARTNER, et.getEdmItem()
        .getProperties()
        .size(), "Wrong number of entities");
  }

  @Test
  public void checkGetPropertyByNameNotNull() throws ODataJPAModelException {
    IntermediateStructuredType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "BusinessPartner"), schema);
    assertNotNull(et.getEdmItem().getProperty("Type"));
  }

  @Test
  public void checkGetPropertyByNameCorrectEntity() throws ODataJPAModelException {
    IntermediateStructuredType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "BusinessPartner"), schema);
    assertEquals("Type", et.getEdmItem().getProperty("Type").getName());
  }

  @Test
  public void checkGetPropertyByNameCorrectEntityID() throws ODataJPAModelException {
    IntermediateStructuredType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "BusinessPartner"), schema);
    assertEquals("ID", et.getEdmItem().getProperty("ID").getName());
  }

  @Test
  public void checkGetPathByNameCorrectEntityID() throws ODataJPAModelException {
    IntermediateStructuredType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "BusinessPartner"), schema);
    assertEquals("ID", et.getPath("ID").getLeaf().getExternalName());
  }

  @Test
  public void checkGetPathByNameIgnore() throws ODataJPAModelException {
    IntermediateStructuredType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "BusinessPartner"), schema);
    assertNull(et.getPath("CustomString2"));
  }

  @Test
  public void checkGetPathByNameIgnoreComplexType() throws ODataJPAModelException {
    IntermediateStructuredType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "BusinessPartner"), schema);
    assertNull(et.getPath("Address/RegionCodePublisher"));
  }

  @Test
  public void checkGetInheritedAttributeByNameCorrectEntityID() throws ODataJPAModelException {
    IntermediateStructuredType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "Person"), schema);
    assertEquals("ID", et.getPath("ID").getLeaf().getExternalName());
  }

  @Test
  public void checkGetAllNaviProperties() throws ODataJPAModelException {
    IntermediateStructuredType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "BusinessPartner"), schema);
    assertEquals(1, et.getEdmItem().getNavigationProperties().size(), "Wrong number of entities");
  }

  @Test
  public void checkGetNaviPropertyByNameNotNull() throws ODataJPAModelException {
    IntermediateStructuredType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "BusinessPartner"), schema);
    assertNotNull(et.getEdmItem().getNavigationProperty("Roles"));
  }

  @Test
  public void checkGetNaviPropertyByNameCorrectEntity() throws ODataJPAModelException {
    IntermediateStructuredType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "BusinessPartner"), schema);
    assertEquals("Roles", et.getEdmItem().getNavigationProperty("Roles").getName());
  }

  @Test
  public void checkGetAssociationOfComplexTypeByNameCorrectEntity() throws ODataJPAModelException {
    IntermediateStructuredType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "BusinessPartner"), schema);
    assertEquals("Address/AdministrativeDivision", et.getAssociationPath("Address/AdministrativeDivision").getAlias());
  }

  @Test
  public void checkGetAssociationOfComplexTypeByNameJoinColumns() throws ODataJPAModelException {
    int actCount = 0;
    IntermediateStructuredType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "BusinessPartner"), schema);
    for (JPAOnConditionItem item : et.getAssociationPath("Address/AdministrativeDivision").getJoinColumnsList()) {
      if (item.getLeftPath().getAlias().equals("Address/Region")) {
          assertEquals("DivisionCode", item.getRightPath().getAlias());
        actCount++;
      }
      if (item.getLeftPath().getAlias().equals("Address/RegionCodeID")) {
          assertEquals("CodeID", item.getRightPath().getAlias());
        actCount++;
      }
      if (item.getLeftPath().getAlias().equals("Address/RegionCodePublisher")) {
          assertEquals("CodePublisher", item.getRightPath().getAlias());
        actCount++;
      }
    }
    assertEquals(3, actCount, "Not all join columns found");
  }

  @Test
  public void checkGetPropertiesSkipIgnored() throws ODataJPAModelException {
    PostProcessorSetIgnore pPDouble = new PostProcessorSetIgnore();
    IntermediateModelElement.setPostProcessor(pPDouble);

    IntermediateStructuredType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "BusinessPartner"), schema);
    assertEquals(TestDataConstants.NO_DEC_ATTRIBUTES_BUSINESS_PARTNER - 1, et.getEdmItem()
        .getProperties().size(), "Wrong number of entities");
  }

  @Test
  public void checkGetIsAbstract() throws ODataJPAModelException {
    IntermediateStructuredType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "BusinessPartner"), schema);
    assertTrue(et.getEdmItem().isAbstract());
  }

  @Test
  public void checkGetIsNotAbstract() throws ODataJPAModelException {
    IntermediateStructuredType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "Organization"), schema);
    assertFalse(et.getEdmItem().isAbstract());
  }

  @Test
  public void checkGetHasBaseType() throws ODataJPAModelException {
    IntermediateStructuredType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "Organization"), schema);
    assertEquals(PUNIT_NAME + ".BusinessPartner", et.getEdmItem().getBaseType());
  }

  @Test
  public void checkGetKeyProperties() throws ODataJPAModelException {
    IntermediateEntityType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "BusinessPartnerRole"), schema);
    assertEquals(2, et.getEdmItem().getKey().size(), "Wrong number of key properties");
  }

  @Test
  public void checkGetAllAttributes() throws ODataJPAModelException {
    IntermediateStructuredType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "BusinessPartnerRole"), schema);
    assertEquals(2, et.getPathList().size(), "Wrong number of entities");
  }

  @Test
  public void checkGetAllAttributesWithBaseType() throws ODataJPAModelException {
    IntermediateStructuredType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "Organization"), schema);
    int exp = TestDataConstants.NO_ATTRIBUTES_BUSINESS_PARTNER
        + TestDataConstants.NO_ATTRIBUTES_POSTAL_ADDRESS
        + TestDataConstants.NO_ATTRIBUTES_COMMUNICATION_DATA
        + 2 * TestDataConstants.NO_ATTRIBUTES_CHANGE_INFO
        + TestDataConstants.NO_ATTRIBUTES_ORGANIZATION;
    assertEquals(exp, et.getPathList().size(), "Wrong number of entities");
  }

  @Test
  public void checkGetAllAttributesWithBaseTypeFields() throws ODataJPAModelException {
    IntermediateStructuredType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "Organization"), schema);

    assertNotNull(et.getPath("Type"));
    assertNotNull(et.getPath("Name1"));
    assertNotNull(et.getPath("Address" + JPAPath.PATH_SEPARATOR + "Region"));
    assertNotNull(et.getPath("AdministrativeInformation" + JPAPath.PATH_SEPARATOR
        + "Created" + JPAPath.PATH_SEPARATOR + "By"));
  }

  @Test
  public void checkGetAllAttributeIDWithBaseType() throws ODataJPAModelException {
    IntermediateStructuredType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "Organization"), schema);
    assertEquals("ID", et.getPath("ID").getAlias());
  }

  @Test
  public void checkGetKeyAttributeFromEmbeddedId() throws ODataJPAModelException {
    IntermediateEntityType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "AdministrativeDivisionDescription"), schema);

    assertNotNull(et.getAttribute("codePublisher"));
    assertEquals("CodePublisher", et.getAttribute("codePublisher").getExternalName());
  }

  @Test
  public void checkGetKeyWithBaseType() throws ODataJPAModelException {
    IntermediateEntityType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "Organization"), schema);
    assertEquals(1, et.getKey().size());
  }

  @Test
  public void checkEmbeddedIdResoledProperties() throws ODataJPAModelException {
    IntermediateEntityType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "AdministrativeDivisionDescription"), schema);
    assertEquals(5, et.getEdmItem().getProperties().size());
  }

  @Test
  public void checkEmbeddedIdResolvedKey() throws ODataJPAModelException {
    IntermediateEntityType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "AdministrativeDivisionDescription"), schema);
    assertEquals(4, et.getEdmItem().getKey().size());
  }

  @Test
  public void checkEmbeddedIdResolvedKeyInternal() throws ODataJPAModelException {
    IntermediateEntityType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "AdministrativeDivisionDescription"), schema);
    assertEquals(4, et.getKey().size());
  }

  @Test
  public void checkEmbeddedIdResoledKeyCorrectOrder() throws ODataJPAModelException {
    IntermediateEntityType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "AdministrativeDivisionDescription"), schema);
    assertEquals("Language", et.getKey().get(0).getExternalName());
    assertEquals("DivisionCode", et.getKey().get(1).getExternalName());
    assertEquals("CodeID", et.getKey().get(2).getExternalName());
    assertEquals("CodePublisher", et.getKey().get(3).getExternalName());
  }

  @Test
  public void checkCompoundResolvedKeyCorrectOrder() throws ODataJPAModelException {
    IntermediateEntityType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "AdministrativeDivision"), schema);
    assertEquals("DivisionCode", et.getKey().get(0).getExternalName());
    assertEquals("CodeID", et.getKey().get(1).getExternalName());
    assertEquals("CodePublisher", et.getKey().get(2).getExternalName());
  }

  @Test
  public void checkEmbeddedIdResolvedPath() throws ODataJPAModelException {
    JPAStructuredType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "AdministrativeDivisionDescription"), schema);
    assertEquals(5, et.getPathList().size());
  }

  @Test
  public void checkEmbeddedIdResolvedPathCodeId() throws ODataJPAModelException {
    JPAStructuredType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "AdministrativeDivisionDescription"), schema);
    assertEquals(2, et.getPath("CodeID").getPath().size());
  }

  @Test
  public void checkHasStreamNoProperties() throws ODataJPAModelException {
    IntermediateEntityType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "PersonImage"), schema);
    assertEquals(2, et.getEdmItem().getProperties().size());
  }

  @Test
  public void checkHasStreamTrue() throws ODataJPAModelException {
    IntermediateEntityType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "PersonImage"), schema);
    assertTrue(et.getEdmItem().hasStream());
  }

  @Test
  public void checkHasStreamFalse() throws ODataJPAModelException {
    IntermediateEntityType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "BusinessPartner"), schema);
    assertFalse(et.getEdmItem().hasStream());
  }

  @Test
  public void checkHasETagTrue() throws ODataJPAModelException {
    IntermediateEntityType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "BusinessPartner"), schema);
    assertTrue(et.hasEtag());
  }

  @Test
  public void checkHasETagTrueIfInherited() throws ODataJPAModelException {
    IntermediateEntityType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "Organization"), schema);
    assertTrue(et.hasEtag());
  }

  @Test
  public void checkHasETagFalse() throws ODataJPAModelException {
    IntermediateEntityType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "AdministrativeDivision"), schema);
    assertFalse(et.hasEtag());
  }

  @Test
  public void checkIgnoreIfAsEntitySet() {
    IntermediateEntityType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "BestOrganization"), schema);
    assertTrue(et.ignore());
  }

  @Test
  public void checkAnnotationSet() throws ODataJPAModelException {
    IntermediateModelElement.setPostProcessor(new PostProcessorSetIgnore());
    IntermediateEntityType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "PersonImage"), schema);
    List<CsdlAnnotation> act = et.getEdmItem().getAnnotations();
    assertEquals(1, act.size());
    assertEquals("Core.AcceptableMediaTypes", act.get(0).getTerm());
  }

  @Test
  public void checkGetPropertyByDBFieldName() throws ODataJPAModelException {
    IntermediateStructuredType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "BusinessPartner"), schema);
    assertEquals("Type", et.getPropertyByDBField("\"Type\"").getExternalName());
  }

  @Test
  public void checkGetPropertyByDBFieldNameFromSuperType() throws ODataJPAModelException {
    IntermediateStructuredType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "Organization"), schema);
    assertEquals("Type", et.getPropertyByDBField("\"Type\"").getExternalName());
  }

  @Test
  public void checkGetPropertyByDBFieldNameFromEmbedded() throws ODataJPAModelException {
    IntermediateStructuredType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "AdministrativeDivisionDescription"), schema);
    assertEquals("CodeID", et.getPropertyByDBField("\"CodeID\"").getExternalName());
  }

  @Test
  public void checkAllPathContainsComplexCollection() throws ODataJPAModelException {
    IntermediateStructuredType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "Collection"), schema);
    final List<JPAPath> act = et.getPathList();

    assertEquals(11, act.size());
    assertNotNull(et.getPath("Complex/Address"));
    assertTrue(et.getPath("Complex/Address").getLeaf().isCollection());
    final IntermediateCollectionProperty actIntermediate = (IntermediateCollectionProperty) et.getPath(
        "Complex/Address").getLeaf();
    assertTrue(actIntermediate.asAssociation().getSourceType() instanceof JPAEntityType);
    assertEquals(2, actIntermediate.asAssociation().getPath().size());

    for (JPAPath p : act) {
      if (p.getPath().size() > 1
          && p.getPath().get(0).getExternalName().equals("Complex")
          && p.getPath().get(1).getExternalName().equals("Address")) {
        assertTrue(p.getPath().get(1) instanceof IntermediateCollectionProperty);
        final IntermediateCollectionProperty actProperty = (IntermediateCollectionProperty) p.getPath().get(1);
        assertNotNull(actProperty.asAssociation());
        assertEquals(et, actProperty.asAssociation().getSourceType());
        break;
      }
    }
  }

  @Test
  public void checkAllPathContainsPrimitiveCollection() throws ODataJPAModelException {
    IntermediateStructuredType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "Collection"), schema);
    final List<JPAPath> act = et.getPathList();

    assertEquals(11, act.size());
    assertNotNull(et.getPath("Complex/Comment"));
    assertTrue(et.getPath("Complex/Comment").getLeaf().isCollection());
    final IntermediateCollectionProperty actIntermediate = (IntermediateCollectionProperty) et.getPath(
        "Complex/Comment").getLeaf();
    assertTrue(actIntermediate.asAssociation().getSourceType() instanceof JPAEntityType);
    assertEquals("Complex/Comment", actIntermediate.asAssociation().getAlias());

    for (JPAPath p : act) {
      if (p.getPath().size() > 1
          && p.getPath().get(0).getExternalName().equals("Complex")
          && p.getPath().get(1).getExternalName().equals("Comment")) {
        assertTrue(p.getPath().get(1) instanceof IntermediateCollectionProperty);
        final IntermediateCollectionProperty actProperty = (IntermediateCollectionProperty) p.getPath().get(1);
        assertNotNull(actProperty.asAssociation());
        assertEquals(et, actProperty.asAssociation().getSourceType());
        break;
      }
    }
  }

  @Test
  public void checkAllPathContainsDeepComplexWithPrimitiveCollection() throws ODataJPAModelException {
    IntermediateStructuredType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "CollectionDeep"), schema);
    final List<JPAPath> act = et.getPathList();

    assertEquals(8, act.size());
    assertNotNull(et.getPath("FirstLevel/SecondLevel/Comment"));
    assertTrue(et.getPath("FirstLevel/SecondLevel/Comment").getLeaf().isCollection());
    final IntermediateCollectionProperty actIntermediate = (IntermediateCollectionProperty) et.getPath(
        "FirstLevel/SecondLevel/Comment").getLeaf();
    assertTrue(actIntermediate.asAssociation().getSourceType() instanceof JPAEntityType);
    assertEquals(3, actIntermediate.asAssociation().getPath().size());
    assertEquals("FirstLevel/SecondLevel/Comment", actIntermediate.asAssociation().getAlias());
  }

  @Test
  public void checkAllPathContainsDeepComplexWithComplexCollection() throws ODataJPAModelException {
    IntermediateStructuredType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "CollectionDeep"), schema);

    assertNotNull(et.getPath("FirstLevel/SecondLevel/Address"));
    assertTrue(et.getPath("FirstLevel/SecondLevel/Address").getLeaf().isCollection());
    final IntermediateCollectionProperty actIntermediate = (IntermediateCollectionProperty) et.getPath(
        "FirstLevel/SecondLevel/Address").getLeaf();
    assertTrue(actIntermediate.asAssociation().getSourceType() instanceof JPAEntityType);
    assertEquals(3, actIntermediate.asAssociation().getPath().size());
    assertEquals("FirstLevel/SecondLevel/Address", actIntermediate.asAssociation().getAlias());
    for (JPAPath path : et.getPathList()) {
      String[] pathElements = path.getAlias().split("/");
      assertEquals(pathElements.length, path.getPath().size());
    }
  }

  @Test
  public void checkOneSimpleProtectedProperty() throws ODataJPAModelException {
    IntermediateStructuredType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "BusinessPartnerProtected"), schema);
    List<JPAProtectionInfo> act = et.getProtections();
    assertNotNull(act);
    assertEquals(1, act.size());
    assertEquals("Username", act.get(0).getAttribute().getExternalName());
    assertEquals("UserId", act.get(0).getClaimName());
  }

  @Test
  public void checkOneComplexProtectedProperty() throws ODataJPAModelException {
    IntermediateStructuredType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "DeepProtectedExample"), schema);
    List<JPAProtectionInfo> act = et.getProtections();
    assertNotNull(act);
    assertEquals(3, act.size());
  }

  @Test
  public void checkComplexAndInheritedProtectedProperty() throws ODataJPAModelException {
    IntermediateStructuredType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "PersonDeepProtectedHidden"), schema);

    List<JPAProtectionInfo> act = et.getProtections();
    assertNotNull(act);
    assertInherited(act);
    assertComplexAnnotated(act, "Creator", "Created");
    assertComplexAnnotated(act, "Updater", "Updated");
    assertComplexDeep(act);
    assertEquals(4, act.size());
  }

  @Test
  public void checkEmbeddedIdKeyIsCompound() {
    IntermediateEntityType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "AdministrativeDivisionDescription"), schema);
    assertTrue(et.hasCompoundKey());
  }

  @Test
  public void checkMultipleKeyIsCompound() {
    IntermediateEntityType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "AdministrativeDivision"), schema);
    assertTrue(et.hasCompoundKey());
  }

  @Test
  public void checkIdIsNotCompound() {
    IntermediateEntityType et = new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), getEntityType(
        "BusinessPartner"), schema);
    assertFalse(et.hasCompoundKey());
  }

  private void assertComplexDeep(List<JPAProtectionInfo> act) {
    for (final JPAProtectionInfo info : act) {
      if (info.getClaimName().equals("BuildingNumber")) {
        assertEquals("Building", info.getAttribute().getExternalName());
        assertEquals(3, info.getPath().getPath().size());
        assertEquals("InhouseAddress/InhouseAddress/Building", info.getPath().getAlias());
        return;
      }
    }
    fail("Deep protected complex attribute not found");

  }

  private void assertComplexAnnotated(List<JPAProtectionInfo> act, final String expClaimName,
      final String pathElement) {
    for (final JPAProtectionInfo info : act) {
      if (info.getClaimName().equals(expClaimName)) {
        assertEquals("By", info.getAttribute().getExternalName());
        assertEquals(3, info.getPath().getPath().size());
        assertEquals("ProtectedAdminInfo/" + pathElement + "/By", info.getPath().getAlias());
        return;
      }
    }
    fail("Complex attribute not found for: " + expClaimName);
  }

  private void assertInherited(List<JPAProtectionInfo> act) {
    for (final JPAProtectionInfo info : act) {
      if (info.getAttribute().getExternalName().equals("Username")) {
        assertEquals("UserId", info.getClaimName());
        assertEquals(1, info.getPath().getPath().size());
        assertEquals("Username", info.getPath().getAlias());
        return;
      }
    }
    fail("Inherited not found");
  }

  private static class PostProcessorSetIgnore extends JPAEdmMetadataPostProcessor {

    @Override
    public void processProperty(IntermediatePropertyAccess property, String jpaManagedTypeClassName) {
      if (jpaManagedTypeClassName.equals(
          "nl.buildforce.sequoia.jpa.processor.core.testmodel.BusinessPartner")) {
        if (property.getInternalName().equals("communicationData")) {
          property.setIgnore(true);
        }
      }
    }

    @Override
    public void processNavigationProperty(IntermediateNavigationPropertyAccess property,
        String jpaManagedTypeClassName) {}

    @Override
    public void processEntityType(IntermediateEntityTypeAccess entity) {
      if (entity.getExternalName().equals("PersonImage")) {
        List<CsdlExpression> items = new ArrayList<>();
        CsdlCollection exp = new CsdlCollection();
        exp.setItems(items);
        CsdlConstantExpression mimeType = new CsdlConstantExpression(ConstantExpressionType.String, "ogg");
        items.add(mimeType);
        CsdlAnnotation annotation = new CsdlAnnotation();
        annotation.setExpression(exp);
        annotation.setTerm("Core.AcceptableMediaTypes");
        List<CsdlAnnotation> annotations = new ArrayList<>();
        annotations.add(annotation);
        entity.addAnnotations(annotations);
      }
    }

    @Override
    public void provideReferences(IntermediateReferenceList references) {}
  }

  private EntityType<?> getEntityType(String typeName) {
    for (EntityType<?> entityType : etList) {
      if (entityType.getJavaType().getSimpleName().equals(typeName)) {
        return entityType;
      }
    }
    return null;
  }

}