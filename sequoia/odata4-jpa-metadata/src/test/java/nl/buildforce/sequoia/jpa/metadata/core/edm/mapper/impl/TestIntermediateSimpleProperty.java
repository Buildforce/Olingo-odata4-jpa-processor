package nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.impl;

import nl.buildforce.sequoia.jpa.metadata.api.JPAEdmMetadataPostProcessor;
import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmProtectedBy;
import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmProtections;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAModelException;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.extension.IntermediateEntityTypeAccess;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.extension.IntermediateNavigationPropertyAccess;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.extension.IntermediatePropertyAccess;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.extension.IntermediateReferenceList;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.util.MemberDouble;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.AdministrativeDivision;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.BusinessPartner;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.BusinessPartnerProtected;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.Comment;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.DummyToBeIgnored;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.Organization;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.Person;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.PersonImage;
import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import jakarta.persistence.Column;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.Attribute.PersistentAttributeType;
import jakarta.persistence.metamodel.EmbeddableType;
import jakarta.persistence.metamodel.ManagedType;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

public class TestIntermediateSimpleProperty extends TestMappingRoot {
  private TestHelper helper;
  private JPAEdmMetadataPostProcessor processor;

  @BeforeEach
  public void setup() throws ODataJPAModelException {
    helper = new TestHelper(emf.getMetamodel(), PUNIT_NAME);
    processor = mock(JPAEdmMetadataPostProcessor.class);
  }

  @Test
  public void checkPropertyCanBeCreated() throws ODataJPAModelException {
    EmbeddableType<?> et = helper.getEmbeddableType("CommunicationData");
    Attribute<?, ?> jpaAttribute = helper.getAttribute(et, "landlinePhoneNumber");
    new IntermediateSimpleProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME), jpaAttribute, helper.schema);
  }

  @Test
  public void checkGetPropertyName() throws ODataJPAModelException {
    Attribute<?, ?> jpaAttribute = helper.getAttribute(helper.getEntityType(BusinessPartner.class), "type");
    IntermediateSimpleProperty property = new IntermediateSimpleProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        jpaAttribute,
        helper.schema);
    assertEquals("Type", property.getEdmItem().getName(), "Wrong name");
  }

  @Test
  public void checkGetPropertyDBFieldName() throws ODataJPAModelException {
    Attribute<?, ?> jpaAttribute = helper.getAttribute(helper.getEntityType(BusinessPartner.class), "type");
    IntermediateSimpleProperty property = new IntermediateSimpleProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        jpaAttribute,
        helper.schema);
    assertEquals("\"Type\"", property.getDBFieldName(), "Wrong name");
  }

  @Test
  public void checkGetPropertySimpleType() throws ODataJPAModelException {
    Attribute<?, ?> jpaAttribute = helper.getAttribute(helper.getEntityType(BusinessPartner.class), "type");
    IntermediateSimpleProperty property = new IntermediateSimpleProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        jpaAttribute, helper.schema);
    assertEquals(EdmPrimitiveTypeKind.String.getFullQualifiedName().getFullQualifiedNameAsString(),
        property.getEdmItem().getType(), "Wrong type");
  }

  @Test
  public void checkGetPropertyComplexType() throws ODataJPAModelException {
    Attribute<?, ?> jpaAttribute = helper.getAttribute(helper.getEntityType(BusinessPartner.class),
        "communicationData");
    IntermediateSimpleProperty property = new IntermediateSimpleProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        jpaAttribute,
        helper.schema);
    assertEquals(PUNIT_NAME + ".CommunicationData", property.getEdmItem().getType(), "Wrong type");
  }

  @Test
  public void checkGetPropertyEnumTypeWithoutConverter() throws ODataJPAModelException {
    Attribute<?, ?> jpaAttribute = helper.getAttribute(helper.getEntityType(Organization.class), "aBCClass");
    IntermediateSimpleProperty property = new IntermediateSimpleProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        jpaAttribute,
        helper.schema);
    assertEquals("nl.buildforce.sequoia.jpa.AbcClassification", property.getEdmItem().getType(), "Wrong type");
  }

  @Test
  public void checkGetPropertyEnumTypeWithoutConverterMustNotHaveMapper() throws ODataJPAModelException {
    Attribute<?, ?> jpaAttribute = helper.getAttribute(helper.getEntityType(Organization.class), "aBCClass");
    IntermediateSimpleProperty property = new IntermediateSimpleProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        jpaAttribute,
        helper.schema);
    assertNull(property.getEdmItem().getMapping());
  }

  @Test
  public void checkGetPropertyEnumTypeWithConverter() throws ODataJPAModelException {
    Attribute<?, ?> jpaAttribute = helper.getAttribute(helper.getEntityType(Person.class), "accessRights");
    IntermediateSimpleProperty property = new IntermediateSimpleProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        jpaAttribute,
        helper.schema);
    assertEquals("nl.buildforce.sequoia.jpa.AccessRights", property.getEdmItem().getType(), "Wrong type");
  }

  @Test
  public void checkGetPropertyIgnoreFalse() throws ODataJPAModelException {
    Attribute<?, ?> jpaAttribute = helper.getAttribute(helper.getEntityType(BusinessPartner.class), "type");
    IntermediatePropertyAccess property = new IntermediateSimpleProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        jpaAttribute,
        helper.schema);
    assertFalse(property.ignore());
  }

  @Test
  public void checkGetPropertyIgnoreTrue() throws ODataJPAModelException {
    Attribute<?, ?> jpaAttribute = helper.getAttribute(helper.getEntityType(BusinessPartner.class), "customString1");
    IntermediatePropertyAccess property = new IntermediateSimpleProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        jpaAttribute,
        helper.schema);
    assertTrue(property.ignore());
  }

  @Test
  public void checkGetPropertyFacetsNullableTrue() throws ODataJPAModelException {
    Attribute<?, ?> jpaAttribute = helper.getAttribute(helper.getEntityType(BusinessPartner.class), "customString1");
    IntermediateSimpleProperty property = new IntermediateSimpleProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        jpaAttribute,
        helper.schema);
    assertTrue(property.getEdmItem().isNullable());
  }

  @Test
  public void checkGetPropertyFacetsNullableTrueComplex() throws ODataJPAModelException {
    Attribute<?, ?> jpaAttribute = helper.getAttribute(helper.getEmbeddableType("PostalAddressData"), "pOBox");
    IntermediateSimpleProperty property = new IntermediateSimpleProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        jpaAttribute, helper.schema);
    assertTrue(property.getEdmItem().isNullable());
  }

  @Test
  public void checkGetPropertyFacetsNullableFalse() throws ODataJPAModelException {
    Attribute<?, ?> jpaAttribute = helper.getAttribute(helper.getEntityType(BusinessPartner.class), "eTag");
    IntermediateSimpleProperty property = new IntermediateSimpleProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        jpaAttribute,
        helper.schema);
    assertFalse(property.getEdmItem().isNullable());
  }

  @Test
  public void checkGetPropertyIsETagTrue() throws ODataJPAModelException {
    Attribute<?, ?> jpaAttribute = helper.getAttribute(helper.getEntityType(BusinessPartner.class), "eTag");
    IntermediateSimpleProperty property = new IntermediateSimpleProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        jpaAttribute,
        helper.schema);
    assertTrue(property.isEtag());
  }

  @Test
  public void checkGetPropertyIsETagFalse() throws ODataJPAModelException {
    Attribute<?, ?> jpaAttribute = helper.getAttribute(helper.getEntityType(BusinessPartner.class), "type");
    IntermediateSimpleProperty property = new IntermediateSimpleProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        jpaAttribute,
        helper.schema);
    assertFalse(property.isEtag());
  }

  @Test
  public void checkGetPropertyMaxLength() throws ODataJPAModelException {
    Attribute<?, ?> jpaAttribute = helper.getAttribute(helper.getEntityType(BusinessPartner.class), "type");
    IntermediateSimpleProperty property = new IntermediateSimpleProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        jpaAttribute,
        helper.schema);
    assertEquals(Integer.valueOf(1), property.getEdmItem().getMaxLength());
  }

  @Test
  public void checkGetPropertyMaxLengthNullForClob() throws ODataJPAModelException {
    Attribute<?, ?> jpaAttribute = helper.getAttribute(helper.getComplexType("DummyEmbeddedToIgnore"), "command");
    IntermediateSimpleProperty property = new IntermediateSimpleProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        jpaAttribute,
        helper.schema);
    assertNull(property.getEdmItem().getMaxLength());
  }

  @Test
  public void checkGetPropertyPrecisionDecimal() throws ODataJPAModelException {
    Attribute<?, ?> jpaAttribute = helper.getAttribute(helper.getEntityType(BusinessPartner.class), "customNum1");
    IntermediateSimpleProperty property = new IntermediateSimpleProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        jpaAttribute,
        helper.schema);
    assertEquals(Integer.valueOf(16), property.getEdmItem().getPrecision());
  }

  @Test
  public void checkGetPropertyScaleDecimal() throws ODataJPAModelException {
    Attribute<?, ?> jpaAttribute = helper.getAttribute(helper.getEntityType(BusinessPartner.class), "customNum1");
    IntermediateSimpleProperty property = new IntermediateSimpleProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        jpaAttribute,
        helper.schema);
    assertEquals(Integer.valueOf(5), property.getEdmItem().getScale());
  }

  @Test
  public void checkGetPropertyPrecisionTime() throws ODataJPAModelException {
    Attribute<?, ?> jpaAttribute = helper.getAttribute(helper.getEntityType(BusinessPartner.class), "creationDateTime");
    IntermediateSimpleProperty property = new IntermediateSimpleProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        jpaAttribute,
        helper.schema);
    assertEquals(Integer.valueOf(3), property.getEdmItem().getPrecision());
  }

  @Test
  public void checkGetPropertyMapper() throws ODataJPAModelException {
    Attribute<?, ?> jpaAttribute = helper.getAttribute(helper.getEntityType(BusinessPartner.class), "creationDateTime");
    IntermediateSimpleProperty property = new IntermediateSimpleProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        jpaAttribute,
        helper.schema);
    assertNotNull(property.getEdmItem().getMapping());
    assertEquals(Timestamp.class, property.getEdmItem().getMapping().getMappedJavaClass());
  }

  @Test
  public void checkGetPropertyMapperWithConverter() throws ODataJPAModelException {
    Attribute<?, ?> jpaAttribute = helper.getAttribute(helper.getEntityType(Person.class), "birthDay");
    IntermediateSimpleProperty property = new IntermediateSimpleProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        jpaAttribute,
        helper.schema);
    assertNotNull(property.getEdmItem().getMapping());
    assertEquals(Date.class, property.getEdmItem().getMapping().getMappedJavaClass());
  }

  @Test
  public void checkGetNoPropertyMapperForClob() throws ODataJPAModelException {
    Attribute<?, ?> jpaAttribute = helper.getAttribute(helper.getEntityType(Comment.class), "text");
    IntermediateSimpleProperty property = new IntermediateSimpleProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        jpaAttribute,
        helper.schema);
    assertNull(property.getEdmItem().getMapping());
  }

  @Test
  public void checkPostProcessorCalled() throws ODataJPAModelException {
    IntermediateSimpleProperty.setPostProcessor(processor);
    Attribute<?, ?> jpaAttribute = helper.getAttribute(helper.getEntityType(BusinessPartner.class), "creationDateTime");
    IntermediateSimpleProperty property = new IntermediateSimpleProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        jpaAttribute,
        helper.schema);

    property.getEdmItem();
    verify(processor, atLeastOnce()).processProperty(property, BUPA_CANONICAL_NAME);
  }

  @Test
  public void checkPostProcessorNameChanged() throws ODataJPAModelException {
    PostProcessorSetName pPDouble = new PostProcessorSetName();
    IntermediateSimpleProperty.setPostProcessor(pPDouble);

    Attribute<?, ?> jpaAttribute = helper.getAttribute(helper.getEntityType(BusinessPartner.class), "customString1");
    IntermediateSimpleProperty property = new IntermediateSimpleProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        jpaAttribute,
        helper.schema);

    assertEquals("ContactPersonName", property.getEdmItem().getName(), "Wrong name");
  }

  @Test
  public void checkPostProcessorExternalNameChanged() throws ODataJPAModelException {
    PostProcessorSetName pPDouble = new PostProcessorSetName();
    IntermediateModelElement.setPostProcessor(pPDouble);

    Attribute<?, ?> jpaAttribute = helper.getAttribute(helper.getEntityType(BusinessPartner.class), "customString1");
    IntermediatePropertyAccess property = new IntermediateSimpleProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        jpaAttribute,
        helper.schema);

    assertEquals("ContactPersonName", property.getExternalName(), "Wrong name");
  }

  @Test
  public void checkConverterGetConverterReturned() throws ODataJPAModelException {
    PostProcessorSetName pPDouble = new PostProcessorSetName();
    IntermediateModelElement.setPostProcessor(pPDouble);

    Attribute<?, ?> jpaAttribute = helper.getAttribute(helper.getEntityType(BusinessPartner.class), "creationDateTime");
    IntermediateSimpleProperty property = new IntermediateSimpleProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        jpaAttribute,
        helper.schema);

    assertNotNull(property.getConverter());
  }

  @Test
  public void checkConverterGetConverterNotReturned() throws ODataJPAModelException {
    PostProcessorSetName pPDouble = new PostProcessorSetName();
    IntermediateModelElement.setPostProcessor(pPDouble);

    Attribute<?, ?> jpaAttribute = helper.getAttribute(helper.getEntityType(Person.class), "customString1");
    IntermediateSimpleProperty property = new IntermediateSimpleProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        jpaAttribute,
        helper.schema);

    assertNull(property.getConverter());
  }

  @Test
  public void checkConverterGetConverterNotReturnedDifferent() throws ODataJPAModelException {
    PostProcessorSetName pPDouble = new PostProcessorSetName();
    IntermediateModelElement.setPostProcessor(pPDouble);

    Attribute<?, ?> jpaAttribute = helper.getAttribute(helper.getEntityType(DummyToBeIgnored.class), "uuid");
    IntermediateSimpleProperty property = new IntermediateSimpleProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        jpaAttribute,
        helper.schema);

    assertNull(property.getConverter());
  }

  @Test
  public void checkGetPropertyDefaultValue() throws ODataJPAModelException {
    Attribute<?, ?> jpaAttribute = helper.getAttribute(helper.getEmbeddableType("PostalAddressData"),
        "regionCodePublisher");
    IntermediateSimpleProperty property = new IntermediateSimpleProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        jpaAttribute,
        helper.schema);
    assertEquals("ISO", property.getEdmItem().getDefaultValue());
  }

  @Test
  public void checkGetPropertyIsStream() throws ODataJPAModelException {
    Attribute<?, ?> jpaAttribute = helper.getAttribute(helper.getEntityType(PersonImage.class),
        "image");
    IntermediateSimpleProperty property = new IntermediateSimpleProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        jpaAttribute,
        helper.schema);
    assertTrue(property.isStream());
  }

  @Test
  public void checkGetTypeBoxedForPrimitive() throws ODataJPAModelException {
    Attribute<?, ?> jpaAttribute = helper.getAttribute(helper.getEntityType(AdministrativeDivision.class),
        "population");
    IntermediateSimpleProperty property = new IntermediateSimpleProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        jpaAttribute,
        helper.schema);
    assertEquals(Long.class, property.getType());
  }

  @Test
  public void checkGetTypeBoxed() throws ODataJPAModelException {
    Attribute<?, ?> jpaAttribute = helper.getAttribute(helper.getEntityType(AdministrativeDivision.class),
        "area");
    IntermediateSimpleProperty property = new IntermediateSimpleProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        jpaAttribute,
        helper.schema);
    assertEquals(Integer.class, property.getType());
  }

  @Disabled("Not applicable according to OData spec 6.2.3 Attribute Precision.")
  @Test
  public void checkThrowsAnExceptionTimestampWithoutPrecision() throws ODataJPAModelException {
    // If Precision missing EdmDateTimeOffset.internalValueToString throws an exception => pre-check
    final Attribute<?, ?> jpaAttribute = mock(Attribute.class);
    final ManagedType<?> jpaManagedType = mock(ManagedType.class);
    when(jpaAttribute.getName()).thenReturn("start");
    when(jpaAttribute.getPersistentAttributeType()).thenReturn(PersistentAttributeType.BASIC);
    when(jpaAttribute.getDeclaringType()).thenAnswer((Answer<ManagedType<?>>) invocation -> jpaManagedType);
    when(jpaAttribute.getJavaType()).thenAnswer((Answer<Class<?>>) invocation -> Timestamp.class);
    when(jpaManagedType.getJavaType()).thenAnswer((Answer<Class<?>>) invocation -> DummyToBeIgnored.class);

    Column column = mock(Column.class);
    AnnotatedElement annotations = mock(AnnotatedElement.class, withSettings().extraInterfaces(Member.class));
    when(annotations.getAnnotation(Column.class)).thenReturn(column);
    when(jpaAttribute.getJavaMember()).thenReturn((Member) annotations);
    when(column.name()).thenReturn("Test");

    IntermediateSimpleProperty property = new IntermediateSimpleProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        jpaAttribute,
        helper.schema);

    assertThrows(ODataJPAModelException.class, property::getEdmItem);
  }

  @Test
  public void checkGetPropertyHasProtectionFalse() throws ODataJPAModelException {
    Attribute<?, ?> jpaAttribute = helper.getAttribute(helper.getEntityType(BusinessPartnerProtected.class), "eTag");
    IntermediatePropertyAccess property = new IntermediateSimpleProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        jpaAttribute, helper.schema);
    assertFalse(property.hasProtection());
  }

  @Test
  public void checkGetPropertyHasProtectionTrue() throws ODataJPAModelException {
    Attribute<?, ?> jpaAttribute = helper.getAttribute(helper.getEntityType(BusinessPartnerProtected.class),
        "username");
    IntermediatePropertyAccess property = new IntermediateSimpleProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        jpaAttribute, helper.schema);
    assertTrue(property.hasProtection());
  }

  @Test
  public void checkGetPropertyProtectionSupportsWildCardTrue() throws ODataJPAModelException {
    Attribute<?, ?> jpaAttribute = helper.getAttribute(helper.getEntityType(BusinessPartnerProtected.class),
        "username");
    IntermediateProperty property = new IntermediateSimpleProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        jpaAttribute, helper.schema);
    assertTrue(property.protectionWithWildcard("UserId", String.class));
  }

  //
  @Test
  public void checkGetPropertyProtectionSupportsWildCardFalse() throws ODataJPAModelException {
    Attribute<?, ?> jpaAttribute = helper.getAttribute(helper.getComplexType("InhouseAddressWithThreeProtections"),
        "building");
    IntermediateProperty property = new IntermediateSimpleProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        jpaAttribute, helper.schema);
    assertFalse(property.protectionWithWildcard("BuildingNumber", String.class));
  }

  @Test
  public void checkGetPropertyProtectionSupportsWildCardFalseNonString() throws ODataJPAModelException {
    Attribute<?, ?> jpaAttribute = helper.getAttribute(helper.getComplexType("InhouseAddressWithThreeProtections"),
        "roomNumber");
    IntermediateProperty property = new IntermediateSimpleProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        jpaAttribute, helper.schema);
    assertFalse(property.protectionWithWildcard("RoomNumber", Integer.class));
  }

  @Test
  public void checkGetPropertyProtectedAttributeClaimName() throws ODataJPAModelException {
    Attribute<?, ?> jpaAttribute = helper.getAttribute(helper.getEntityType(BusinessPartnerProtected.class),
        "username");
    IntermediateProperty property = new IntermediateSimpleProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        jpaAttribute, helper.schema);
    assertEquals("UserId", property.getProtectionClaimNames().toArray(new String[] {})[0]);
    assertNotNull(property.getProtectionPath("UserId"));
    List<String> actPath = property.getProtectionPath("UserId");
    assertEquals(1, actPath.size());
    assertEquals("Username", actPath.get(0));
  }

  @Test
  public void checkGetPropertyNotProtectedAttributeClaimName() throws ODataJPAModelException {
    Attribute<?, ?> jpaAttribute = helper.getAttribute(helper.getEntityType(BusinessPartnerProtected.class), "eTag");
    IntermediateProperty property = new IntermediateSimpleProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        jpaAttribute, helper.schema);
    assertTrue(property.getProtectionClaimNames().isEmpty());
    assertTrue(property.getProtectionPath("Username").isEmpty());
  }

  @Test
  public void checkGetComplexPropertyProtectedAttributeClaimName() throws ODataJPAModelException {
    Attribute<?, ?> jpaAttribute = helper.getAttribute(helper.getEntityType(BusinessPartnerProtected.class),
        "administrativeInformation");

    EdmProtectedBy annotation = Mockito.mock(EdmProtectedBy.class);
    when(annotation.name()).thenReturn("UserId");
    when(annotation.path()).thenReturn("created/by");

    MemberDouble memberSpy = new MemberDouble(jpaAttribute.getJavaMember());
    memberSpy.addAnnotation(EdmProtectedBy.class, annotation);
    Attribute<?, ?> attributeSpy = Mockito.spy(jpaAttribute);
    when(attributeSpy.getJavaMember()).thenReturn(memberSpy);

    IntermediateProperty property = new IntermediateSimpleProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        attributeSpy, helper.schema);
    assertEquals("UserId", property.getProtectionClaimNames().toArray(new String[] {})[0]);
    assertNotNull(property.getProtectionPath("UserId"));
    List<String> actPath = property.getProtectionPath("UserId");
    assertEquals(1, actPath.size());
    assertEquals("AdministrativeInformation/Created/By", actPath.get(0));
  }

  @Test
  public void checkGetComplexPropertyTwoProtectedAttributeClaimName() throws ODataJPAModelException {
    Attribute<?, ?> jpaAttribute = helper.getAttribute(helper.getEntityType(BusinessPartnerProtected.class),
        "administrativeInformation");

    EdmProtections protections = Mockito.mock(EdmProtections.class);
    EdmProtectedBy protectedBy1 = Mockito.mock(EdmProtectedBy.class);
    when(protectedBy1.name()).thenReturn("UserId");
    when(protectedBy1.path()).thenReturn("created/by");

    EdmProtectedBy protectedBy2 = Mockito.mock(EdmProtectedBy.class);
    when(protectedBy2.name()).thenReturn("UserId");
    when(protectedBy2.path()).thenReturn("updated/by");

    when(protections.value()).thenReturn(new EdmProtectedBy[] { protectedBy1, protectedBy2 });

    MemberDouble memberSpy = new MemberDouble(jpaAttribute.getJavaMember());
    memberSpy.addAnnotation(EdmProtections.class, protections);
    Attribute<?, ?> attributeSpy = Mockito.spy(jpaAttribute);
    when(attributeSpy.getJavaMember()).thenReturn(memberSpy);

    IntermediateProperty property = new IntermediateSimpleProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        attributeSpy, helper.schema);
    assertEquals("UserId", property.getProtectionClaimNames().toArray(new String[] {})[0]);
    assertNotNull(property.getProtectionPath("UserId"));

    List<String> actPath = property.getProtectionPath("UserId");
    assertEquals(2, actPath.size());
    assertEquals("AdministrativeInformation/Created/By", actPath.get(0));
    assertEquals("AdministrativeInformation/Updated/By", actPath.get(1));
  }

  @Test
  public void checkGetComplexPropertyTwoProtectedAttributeTwoClaimName() throws ODataJPAModelException {
    Attribute<?, ?> jpaAttribute = helper.getAttribute(helper.getEntityType(BusinessPartnerProtected.class),
        "administrativeInformation");

    EdmProtections protections = Mockito.mock(EdmProtections.class);
    EdmProtectedBy protectedBy1 = Mockito.mock(EdmProtectedBy.class);
    when(protectedBy1.name()).thenReturn("UserId");
    when(protectedBy1.path()).thenReturn("created/by");

    EdmProtectedBy protectedBy2 = Mockito.mock(EdmProtectedBy.class);
    when(protectedBy2.name()).thenReturn("Date");
    when(protectedBy2.path()).thenReturn("created/at");

    when(protections.value()).thenReturn(new EdmProtectedBy[] { protectedBy1, protectedBy2 });

    MemberDouble memberSpy = new MemberDouble(jpaAttribute.getJavaMember());
    memberSpy.addAnnotation(EdmProtections.class, protections);
    Attribute<?, ?> attributeSpy = Mockito.spy(jpaAttribute);
    when(attributeSpy.getJavaMember()).thenReturn(memberSpy);

    IntermediateProperty property = new IntermediateSimpleProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        attributeSpy, helper.schema);

    assertTrue(property.getProtectionClaimNames().contains("UserId"));
    List<String> actPath = property.getProtectionPath("UserId");
    assertEquals(1, actPath.size());
    assertEquals("AdministrativeInformation/Created/By", actPath.get(0));

    assertTrue(property.getProtectionClaimNames().contains("Date"));
    actPath = property.getProtectionPath("Date");
    assertEquals(1, actPath.size());
    assertEquals("AdministrativeInformation/Created/At", actPath.get(0));
  }

/*
  @Disabled
  @Test
  public void checkGetSRID() {
    // Test for spatial data missing
  }
*/

  private static class PostProcessorSetName extends JPAEdmMetadataPostProcessor {

    @Override
    public void processProperty(IntermediatePropertyAccess property, String jpaManagedTypeClassName) {
      if (jpaManagedTypeClassName.equals(
          "nl.buildforce.sequoia.jpa.processor.core.testmodel.BusinessPartner")) {
        if (property.getInternalName().equals("customString1")) {
          property.setExternalName("ContactPersonName");
        }
      }
    }

    @Override
    public void processNavigationProperty(IntermediateNavigationPropertyAccess property,
        String jpaManagedTypeClassName) {}

    @Override
    public void provideReferences(IntermediateReferenceList references) {}

    @Override
    public void processEntityType(IntermediateEntityTypeAccess entity) {}
  }

}