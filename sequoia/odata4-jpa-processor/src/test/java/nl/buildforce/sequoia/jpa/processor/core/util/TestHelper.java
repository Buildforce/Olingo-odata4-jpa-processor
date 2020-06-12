package nl.buildforce.sequoia.jpa.processor.core.util;

import nl.buildforce.sequoia.jpa.metadata.api.JPAEdmProvider;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAAssociationPath;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAAttribute;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAEntityType;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAServiceDocument;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAException;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAModelException;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;
// import jakarta.persistence.metamodel.SingularAttribute;

// import java.lang.reflect.AnnotatedElement;

public class TestHelper {
  final private Metamodel jpaMetamodel;
  final public JPAServiceDocument sd;
  final public JPAEdmProvider edmProvider;

  public TestHelper(EntityManagerFactory emf, String namespace) throws ODataJPAException {
    this.jpaMetamodel = emf.getMetamodel();
    edmProvider = new JPAEdmProvider(namespace, emf, null, TestBase.enumPackages);
    sd = edmProvider.getServiceDocument();
    sd.getEdmEntityContainer();
  }

  public EntityType<?> getEntityType(String typeName) {
    for (EntityType<?> entityType : jpaMetamodel.getEntities()) {
      if (entityType.getJavaType().getSimpleName().equals(typeName)) {
        return entityType;
      }
    }
    return null;
  }

  public JPAEntityType getJPAEntityType(String entitySetName) throws ODataJPAModelException {
    return sd.getEntity(entitySetName);
  }

  public JPAAssociationPath getJPAAssociationPath(String entitySetName, String attributeExtName)
      throws ODataJPAModelException {
    JPAEntityType jpaEntity = sd.getEntity(entitySetName);
    return jpaEntity.getAssociationPath(attributeExtName);
  }

/*
  public JPAAttribute getJPAAssociation(String entitySetName, String attributeIntName) throws ODataJPAModelException {
    JPAEntityType jpaEntity = sd.getEntity(entitySetName);
    return jpaEntity.getAssociation(attributeIntName);
  }

  public EdmFunction getStoredProcedure(EntityType<?> jpaEntityType, String string) {
    if (jpaEntityType.getJavaType() != null) {
      EdmFunctions jpaStoredProcedureList = ((AnnotatedElement) jpaEntityType.getJavaType())
          .getAnnotation(EdmFunctions.class);
      if (jpaStoredProcedureList != null) {
        for (EdmFunction jpaStoredProcedure : jpaStoredProcedureList.value()) {
          if (jpaStoredProcedure.name().equals(string)) return jpaStoredProcedure;
        }
      }
    }
    return null;
  }

  public Attribute<?, ?> getAttribute(ManagedType<?> et, String attributeName) {
    for (SingularAttribute<?, ?> attribute : et.getSingularAttributes()) {
      if (attribute.getName().equals(attributeName))
        return attribute;
    }
    return null;
  }

  public EmbeddableType<?> getEmbeddableType(String typeName) {
    for (EmbeddableType<?> embeddableType : jpaMetamodel.getEmbeddables()) {
      if (embeddableType.getJavaType().getSimpleName().equals(typeName)) {
        return embeddableType;
      }
    }
    return null;
  }

  public Attribute<?, ?> getDeclaredAttribute(ManagedType<?> et, String attributeName) {
    for (Attribute<?, ?> attribute : et.getDeclaredAttributes()) {
      if (attribute.getName().equals(attributeName))
        return attribute;
    }
    return null;
  }
  */

  public JPAAttribute getJPAAttribute(String entitySetName, String attributeIntName) throws ODataJPAModelException {
    JPAEntityType jpaEntity = sd.getEntity(entitySetName);
    return jpaEntity.getAttribute(attributeIntName);
  }

}