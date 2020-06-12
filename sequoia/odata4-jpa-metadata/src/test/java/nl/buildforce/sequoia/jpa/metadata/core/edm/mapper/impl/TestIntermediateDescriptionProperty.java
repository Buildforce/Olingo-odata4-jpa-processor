package nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.impl;

import nl.buildforce.sequoia.jpa.metadata.api.JPAEdmMetadataPostProcessor;
import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmDescriptionAssociation;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAModelException;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.extension.IntermediateEntityTypeAccess;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.extension.IntermediateNavigationPropertyAccess;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.extension.IntermediatePropertyAccess;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.extension.IntermediateReferenceList;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.BusinessPartner;
import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.provider.CsdlAnnotation;
import org.apache.olingo.commons.api.edm.provider.annotation.CsdlConstantExpression.ConstantExpressionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;

import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.EmbeddableType;
import jakarta.persistence.metamodel.ManagedType;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TestIntermediateDescriptionProperty extends TestMappingRoot {
  private TestHelper helper;
  private IntermediateDescriptionProperty cut;
  private JPAEdmMetadataPostProcessor processor;

  @BeforeEach
  public void setup() throws ODataJPAModelException {
    helper = new TestHelper(emf.getMetamodel(), PUNIT_NAME);
    processor = mock(JPAEdmMetadataPostProcessor.class);
    IntermediateModelElement.setPostProcessor(new DefaultEdmPostProcessor());
  }

  @Test
  public void checkPropertyCanBeCreated() throws ODataJPAModelException {
    EmbeddableType<?> et = helper.getEmbeddableType("PostalAddressData");
    Attribute<?, ?> jpaAttribute = helper.getDeclaredAttribute(et, "countryName");
    new IntermediateDescriptionProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME), jpaAttribute, helper.schema);
  }

  @Test
  public void checkGetPropertyNameOneToMany() throws ODataJPAModelException {
    Attribute<?, ?> jpaAttribute = helper.getDeclaredAttribute(helper.getEmbeddableType("PostalAddressData"),
        "countryName");
    cut = new IntermediateDescriptionProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME), jpaAttribute,
        helper.schema);
    assertEquals("CountryName", cut.getEdmItem().getName(), "Wrong name");
  }

  @Test
  public void checkGetPropertyNameManyToMany() throws ODataJPAModelException {
    Attribute<?, ?> jpaAttribute = helper.getDeclaredAttribute(helper.getEmbeddableType("PostalAddressData"),
        "regionName");
    cut = new IntermediateDescriptionProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME), jpaAttribute,
        helper.schema);
    assertEquals("RegionName", cut.getEdmItem().getName(), "Wrong name");
  }

  @Test
  public void checkGetPropertyType() throws ODataJPAModelException {
    Attribute<?, ?> jpaAttribute = helper.getDeclaredAttribute(helper.getEmbeddableType("PostalAddressData"),
        "countryName");
    cut = new IntermediateDescriptionProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME), jpaAttribute,
        helper.schema);
    assertEquals(EdmPrimitiveTypeKind.String.getFullQualifiedName().getFullQualifiedNameAsString(),
        cut.getEdmItem().getType(), "Wrong type");
  }

  @Test
  public void checkGetPropertyIgnoreFalse() throws ODataJPAModelException {
    Attribute<?, ?> jpaAttribute = helper.getDeclaredAttribute(helper.getEmbeddableType("PostalAddressData"),
        "countryName");
    IntermediatePropertyAccess property = new IntermediateDescriptionProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        jpaAttribute,
        helper.schema);
    assertFalse(property.ignore());
  }

  @Test
  public void checkGetPropertyFacetsNullableTrue() throws ODataJPAModelException {
    Attribute<?, ?> jpaAttribute = helper.getDeclaredAttribute(helper.getEmbeddableType("PostalAddressData"),
        "countryName");
    cut = new IntermediateDescriptionProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME), jpaAttribute,
        helper.schema);
    assertTrue(cut.getEdmItem().isNullable());
  }

  @Test
  public void checkGetPropertyMaxLength() throws ODataJPAModelException {
    Attribute<?, ?> jpaAttribute = helper.getDeclaredAttribute(helper.getEmbeddableType("PostalAddressData"),
        "countryName");
    cut = new IntermediateDescriptionProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME), jpaAttribute,
        helper.schema);
    assertEquals(Integer.valueOf(100), cut.getEdmItem().getMaxLength());
  }

  @Test
  public void checkWrongPathElementThrowsException() {

    Attribute<?, ?> jpaAttribute = mock(Attribute.class);
    EdmDescriptionAssociation association = prepareCheckPath(jpaAttribute);

    EdmDescriptionAssociation.valueAssignment[] valueAssignments = new EdmDescriptionAssociation.valueAssignment[1];
    EdmDescriptionAssociation.valueAssignment valueAssignment = mock(EdmDescriptionAssociation.valueAssignment.class);
    valueAssignments[0] = valueAssignment;
    when(valueAssignment.attribute()).thenReturn("communicationData/dummy");
    when(association.valueAssignments()).thenReturn(valueAssignments);

    try {
      cut = new IntermediateDescriptionProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME), jpaAttribute,
          helper.schema);
      cut.getEdmItem();
    } catch (ODataJPAModelException e) {
      return;
    }
    fail();
  }

  @Test
  public void checkWrongPathStartThrowsException() {

    Attribute<?, ?> jpaAttribute = mock(Attribute.class);
    EdmDescriptionAssociation association = prepareCheckPath(jpaAttribute);

    EdmDescriptionAssociation.valueAssignment[] valueAssignments = new EdmDescriptionAssociation.valueAssignment[1];
    EdmDescriptionAssociation.valueAssignment valueAssignment = mock(EdmDescriptionAssociation.valueAssignment.class);
    valueAssignments[0] = valueAssignment;
    when(valueAssignment.attribute()).thenReturn("communicationDummy/dummy");
    when(association.valueAssignments()).thenReturn(valueAssignments);

    try {
      cut = new IntermediateDescriptionProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME), jpaAttribute,
          helper.schema);
      cut.getEdmItem();
    } catch (ODataJPAModelException e) {
      return;
    }
    fail();
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  private EdmDescriptionAssociation prepareCheckPath(Attribute<?, ?> jpaAttribute) {
    AnnotatedMember jpaField = mock(AnnotatedMember.class);
    ManagedType jpaManagedType = mock(ManagedType.class);
    EdmDescriptionAssociation association = mock(EdmDescriptionAssociation.class);

    when(jpaAttribute.getJavaType()).thenAnswer((Answer<Class<BusinessPartner>>) invocation -> BusinessPartner.class);
    when(jpaAttribute.getJavaMember()).thenReturn(jpaField);
    when(jpaAttribute.getName()).thenReturn("dummy");
    when(jpaAttribute.getDeclaringType()).thenReturn(jpaManagedType);
    when(jpaManagedType.getJavaType()).thenReturn(BusinessPartner.class);

    when(jpaField.getAnnotation(EdmDescriptionAssociation.class)).thenReturn(association);

    when(association.descriptionAttribute()).thenReturn("country");
    when(association.languageAttribute()).thenReturn("language");
    when(association.localeAttribute()).thenReturn("");
    return association;
  }

  @Test
  public void checkAnnotations() throws ODataJPAModelException {
    Attribute<?, ?> jpaAttribute = helper.getDeclaredAttribute(helper.getEntityType(BusinessPartner.class),
        "locationName");
    cut = new IntermediateDescriptionProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME), jpaAttribute,
        helper.schema);
    List<CsdlAnnotation> annotations = cut.getEdmItem().getAnnotations();
    assertEquals(1, annotations.size());
    assertEquals("Core.IsLanguageDependent", annotations.get(0).getTerm());
    assertEquals(ConstantExpressionType.Bool, annotations.get(0).getExpression().asConstant().getType());
    assertEquals("true", annotations.get(0).getExpression().asConstant().getValue());
    assertNull(annotations.get(0).getQualifier());
  }

  @Test
  public void checkPostProcessorCalled() throws ODataJPAModelException {
    // PostProcessorSpy spy = new PostProcessorSpy();
    IntermediateModelElement.setPostProcessor(processor);
    Attribute<?, ?> jpaAttribute = helper.getDeclaredAttribute(helper.getEmbeddableType("PostalAddressData"),
        "countryName");
    cut = new IntermediateDescriptionProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME), jpaAttribute,
        helper.schema);

    cut.getEdmItem();
    verify(processor, atLeastOnce()).processProperty(cut, ADDR_CANONICAL_NAME);
  }

  @Test
  public void checkPostProcessorNameChanged() throws ODataJPAModelException {
    PostProcessorSetName pPDouble = new PostProcessorSetName();
    IntermediateModelElement.setPostProcessor(pPDouble);

    Attribute<?, ?> jpaAttribute = helper.getDeclaredAttribute(helper.getEmbeddableType("PostalAddressData"),
        "countryName");
    cut = new IntermediateDescriptionProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME), jpaAttribute,
        helper.schema);

    assertEquals("CountryDescription", cut.getEdmItem().getName(), "Wrong name");
  }

  @Test
  public void checkPostProcessorExternalNameChanged() throws ODataJPAModelException {
    PostProcessorSetName pPDouble = new PostProcessorSetName();
    IntermediateModelElement.setPostProcessor(pPDouble);

    Attribute<?, ?> jpaAttribute = helper.getDeclaredAttribute(helper.getEmbeddableType("PostalAddressData"),
        "countryName");
    IntermediatePropertyAccess property = new IntermediateSimpleProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        jpaAttribute,
        helper.schema);

    assertEquals("CountryDescription", property.getExternalName(), "Wrong name");
  }

  private static class PostProcessorSetName extends JPAEdmMetadataPostProcessor {

    @Override
    public void processProperty(IntermediatePropertyAccess property, String jpaManagedTypeClassName) {
      if (jpaManagedTypeClassName.equals(ADDR_CANONICAL_NAME)) {
        if (property.getInternalName().equals("countryName")) {
          property.setExternalName("CountryDescription");
        }
      }
    }

    @Override
    public void processNavigationProperty(IntermediateNavigationPropertyAccess property,
        String jpaManagedTypeClassName) {}

    @Override
    public void processEntityType(IntermediateEntityTypeAccess entity) {}

    @Override
    public void provideReferences(IntermediateReferenceList references) {}
  }

  private interface AnnotatedMember extends Member, AnnotatedElement {}

}