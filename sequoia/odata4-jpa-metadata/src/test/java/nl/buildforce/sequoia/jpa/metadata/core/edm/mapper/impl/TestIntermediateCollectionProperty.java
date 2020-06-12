package nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.impl;

import nl.buildforce.sequoia.jpa.metadata.api.JPAEdmMetadataPostProcessor;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPACollectionAttribute;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAPath;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAModelException;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.Organization;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.Person;
import jakarta.persistence.metamodel.Type;
import org.apache.olingo.commons.api.edm.provider.CsdlAnnotation;
import org.apache.olingo.commons.api.edm.provider.annotation.CsdlConstantExpression.ConstantExpressionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;

import jakarta.persistence.metamodel.ManagedType;
import jakarta.persistence.metamodel.PluralAttribute;
import jakarta.persistence.metamodel.Type.PersistenceType;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestIntermediateCollectionProperty extends TestMappingRoot {
  private TestHelper helper;
  private JPAEdmMetadataPostProcessor processor;

  private JPADefaultEdmNameBuilder nameBuilder;
  private PluralAttribute<?, ?, ?> jpaAttribute;
  private ManagedType<?> managedType;

  @BeforeEach
  public void setup() throws ODataJPAModelException {
    helper = new TestHelper(emf.getMetamodel(), PUNIT_NAME);
    processor = mock(JPAEdmMetadataPostProcessor.class);
    nameBuilder = new JPADefaultEdmNameBuilder(PUNIT_NAME);
    jpaAttribute = mock(PluralAttribute.class);
    managedType = mock(ManagedType.class);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void checkSimpleCollectionPropertyType() throws ODataJPAModelException {
    when(jpaAttribute.getName()).thenReturn("Text");
    @SuppressWarnings("rawtypes")
    Type type = mock(Type.class);
    when(type.getPersistenceType()).thenReturn(PersistenceType.BASIC);
    when(type.getJavaType()).thenAnswer((Answer<Class<?>>) invocation -> String.class);
    when(jpaAttribute.getElementType()).thenReturn(type);
    when(jpaAttribute.getDeclaringType()).thenAnswer((Answer<ManagedType<?>>) invocation -> managedType);
    when(managedType.getJavaType()).thenAnswer((Answer<Class<?>>) invocation -> Person.class);
    when(jpaAttribute.getJavaType()).thenAnswer((Answer<Class<?>>) invocation -> List.class);
   IntermediateCollectionProperty cut =
     new IntermediateCollectionProperty(nameBuilder, jpaAttribute, helper.schema, helper.schema.getEntityType(Organization.class));
    assertEquals("Edm.String", cut.getEdmItem().getType());
    assertEquals(String.class, cut.getType());
  }

  @Test
  public void checkGetPropertyComplexType() throws ODataJPAModelException {

    PluralAttribute<?, ?, ?> jpaAttribute = helper.getCollectionAttribute(helper.getEntityType(
        Person.class), "inhouseAddress");
    IntermediateCollectionProperty property = new IntermediateCollectionProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        jpaAttribute, helper.schema, helper.schema.getEntityType(Person.class));
    assertEquals(PUNIT_NAME + ".InhouseAddress", property.getEdmItem().getType());
  }

  @Test
  public void checkGetPropertyIgnoreFalse() throws ODataJPAModelException {

    PluralAttribute<?, ?, ?> jpaAttribute = helper.getCollectionAttribute(helper.getEntityType(
        Person.class), "inhouseAddress");
    IntermediateCollectionProperty property = new IntermediateCollectionProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        jpaAttribute, helper.schema, helper.schema.getEntityType(Person.class));
    assertFalse(property.ignore());
  }

  @Test
  public void checkGetPropertyDBFieldName() throws ODataJPAModelException {

    PluralAttribute<?, ?, ?> jpaAttribute = helper.getCollectionAttribute(helper.getEntityType(
        Organization.class), "comment");
    IntermediateCollectionProperty property = new IntermediateCollectionProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        jpaAttribute, helper.schema, helper.schema.getEntityType(Organization.class));
    assertEquals("\"Text\"", property.getDBFieldName());
  }

  @Test
  public void checkPostProcessorCalled() throws ODataJPAModelException {

    IntermediateSimpleProperty.setPostProcessor(processor);
    PluralAttribute<?, ?, ?> jpaAttribute = helper.getCollectionAttribute(helper.getEntityType(
        Organization.class), "comment");
    IntermediateCollectionProperty property = new IntermediateCollectionProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        jpaAttribute, helper.schema, helper.schema.getEntityType(Organization.class));
    property.getEdmItem();
    verify(processor, atLeastOnce()).processProperty(property, ORG_CANONICAL_NAME);
  }

  @Test
  public void checkGetPropertyReturnsAnnotation() throws ODataJPAModelException {

    PluralAttribute<?, ?, ?> jpaAttribute = helper.getCollectionAttribute(helper.getEntityType(
        Person.class), "inhouseAddress");
    IntermediateCollectionProperty property = new IntermediateCollectionProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        jpaAttribute, helper.schema, helper.schema.getEntityType(Person.class));

    List<CsdlAnnotation> annotations = property.getEdmItem().getAnnotations();
    assertEquals(1, property.getEdmItem().getAnnotations().size());
    assertTrue(annotations.get(0).getExpression().isConstant());
  }

  @Test
  public void checkGetDeepComplexPropertyReturnsExternalName() throws ODataJPAModelException {

    final IntermediateStructuredType st = new IntermediateComplexType(new JPADefaultEdmNameBuilder(PUNIT_NAME), helper
        .getComplexType("CollectionSecondLevelComplex"), helper.schema);
    for (final JPACollectionAttribute collection : st.getDeclaredCollectionAttributes()) {
      if (collection.getInternalName().equals("comment")) {
        assertEquals("Comment", collection.asAssociation().getAlias());
      }
    }

    final IntermediateStructuredType structuredType = new IntermediateComplexType(new JPADefaultEdmNameBuilder(PUNIT_NAME), helper
        .getComplexType("CollectionFirstLevelComplex"), helper.schema);
    for (final JPAPath collection : structuredType.getCollectionAttributesPath()) {
      if (collection.getLeaf().getInternalName().equals("comment")) {
        assertEquals("SecondLevel/Comment", collection.getAlias());
      }
    }
  }

  @Test
  public void checkAnnotations() throws ODataJPAModelException {
    PluralAttribute<?, ?, ?> jpaAttribute = helper.getCollectionAttribute(helper.getEntityType(
        Person.class), "inhouseAddress");
    IntermediateCollectionProperty cut = new IntermediateCollectionProperty(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        jpaAttribute, helper.schema, helper.schema.getEntityType(Person.class));

    List<CsdlAnnotation> annotations = cut.getEdmItem().getAnnotations();
    assertEquals(1, annotations.size());
    assertEquals("Core.Description", annotations.get(0).getTerm());
    assertEquals(ConstantExpressionType.String, annotations.get(0).getExpression().asConstant().getType());
    assertEquals("Address for inhouse Mail", annotations.get(0).getExpression().asConstant().getValue());
    assertEquals("Address", annotations.get(0).getQualifier());
  }

}