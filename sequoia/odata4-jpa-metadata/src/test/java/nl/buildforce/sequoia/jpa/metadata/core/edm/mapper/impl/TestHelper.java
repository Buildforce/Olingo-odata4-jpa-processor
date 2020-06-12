package nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.impl;

import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmEnumeration;
import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmFunction;
import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmFunctions;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAAttribute;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAModelException;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.AbcClassification;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.AccessRights;

import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.EmbeddableType;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.ManagedType;
import jakarta.persistence.metamodel.Metamodel;
import jakarta.persistence.metamodel.PluralAttribute;
import jakarta.persistence.metamodel.SingularAttribute;

import org.reflections.Reflections;

import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestHelper {
  final private Metamodel jpaMetamodel;
  final public IntermediateSchema schema;

  public TestHelper(final Metamodel metamodel, final String namespace) throws ODataJPAModelException {
    final Reflections r = mock(Reflections.class);
    when(r.getTypesAnnotatedWith(EdmEnumeration.class)).thenReturn(new HashSet<>(Arrays.asList(AbcClassification.class, AccessRights.class)));

    this.jpaMetamodel = metamodel;
    this.schema = new IntermediateSchema(new JPADefaultEdmNameBuilder(namespace), jpaMetamodel, r);
  }

  public Object findAttribute(final List<? extends JPAAttribute> attributes, final String searchItem) {
    for (final JPAAttribute attribute : attributes) {
      if (attribute.getExternalName().equals(searchItem))
        return attribute;
    }
    return null;
  }

  public Attribute<?, ?> getAttribute(final ManagedType<?> et, final String attributeName) {
    for (final SingularAttribute<?, ?> attribute : et.getSingularAttributes()) {
      if (attribute.getName().equals(attributeName))
        return attribute;
    }
    return null;
  }

  public PluralAttribute<?, ?, ?> getCollectionAttribute(final ManagedType<?> et, final String attributeName) {
    for (final PluralAttribute<?, ?, ?> attribute : et.getPluralAttributes()) {
      if (attribute.getName().equals(attributeName))
        return attribute;
    }
    return null;
  }

  public EmbeddableType<?> getComplexType(final String typeName) {
    for (final EmbeddableType<?> embeddableType : jpaMetamodel.getEmbeddables()) {
      if (embeddableType.getJavaType().getSimpleName().equals(typeName)) {
        return embeddableType;
      }
    }
    return null;
  }

  public Attribute<?, ?> getDeclaredAttribute(final ManagedType<?> et, final String attributeName) {
    for (final Attribute<?, ?> attribute : et.getDeclaredAttributes()) {
      if (attribute.getName().equals(attributeName))
        return attribute;
    }
    return null;
  }

  public EmbeddableType<?> getEmbeddableType(String typeName) {
    for (final EmbeddableType<?> embeddableType : jpaMetamodel.getEmbeddables()) {
      if (embeddableType.getJavaType().getSimpleName().equals(typeName)) {
        return embeddableType;
      }
    }
    return null;
  }

  public EntityType<?> getEntityType(final Class<?> clazz) {
    for (final EntityType<?> entityType : jpaMetamodel.getEntities()) {
      if (entityType.getJavaType().getSimpleName().equals(clazz.getSimpleName())) {
        return entityType;
      }
    }
    return null;
  }

  public EdmFunction getStoredProcedure(EntityType<?> jpaEntityType, String string) {
    if (jpaEntityType.getJavaType() != null) {
      final EdmFunctions jpaStoredProcedureList = ((AnnotatedElement) jpaEntityType.getJavaType())
          .getAnnotation(EdmFunctions.class);
      if (jpaStoredProcedureList != null) {
        for (final EdmFunction jpaStoredProcedure : jpaStoredProcedureList.value()) {
          if (jpaStoredProcedure.name().equals(string)) return jpaStoredProcedure;
        }
      }
    }
    return null;
  }

}