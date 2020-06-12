package nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.impl;

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAEdmNameBuilder;

import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.EmbeddableType;
import jakarta.persistence.metamodel.EntityType;

public final class JPADefaultEdmNameBuilder implements JPAEdmNameBuilder {
  // V2 NameBuilder: package org.apache.olingo.odata2.jpa.processor.core.access.model
  private static final String ENTITY_CONTAINER_SUFFIX = "Container";
  private static final String ENTITY_SET_SUFFIX = "s";

/*
  public static String firstToLower(final String substring) {
    return Character.toLowerCase(substring.charAt(0)) + substring.substring(1);
  }
*/

  public static String capitalize(final String jpaAttributeName) {
    return Character.toUpperCase(jpaAttributeName.charAt(0)) + jpaAttributeName.substring(1);
  }

  private final String namespace;

  public JPADefaultEdmNameBuilder(final String namespace) {
    this.namespace = namespace;
  }

  /**
   * EDM Complex Type Name - RULE: <p>
   * Use JPA Embeddable Type Simple Name as Complex Type Name
   */
  @Override
  public final String buildComplexTypeName(final EmbeddableType<?> jpaEmbeddedType) {
    return jpaEmbeddedType.getJavaType().getSimpleName();
  }

  /**
   * EDM EntityContainer Name - RULE: <p>
   * The Entity Container Name is build of EDM Namespace + Literal "Container". Container names are simple identifiers,
   * so contain only letter, digits and underscores. However namespaces
   * can contain also dots => eliminate dots and convert to camel case.
   */
  @Override
  public String buildContainerName() {
    final StringBuilder containerName = new StringBuilder();
    final String[] elements = namespace.split("\\.");
    for (final String element : elements) {
      containerName.append(capitalize(element));
    }
    containerName.append(ENTITY_CONTAINER_SUFFIX);
    return containerName.toString();
  }

  /**
   * EDM EntitySet Name - RULE:<p>
   * Use plural of entity type name. The naming bases on the assumption that English nouns are used.<br>
   * Entity Set Name = JPA Entity Type Name + Literal "s"
   */
  @Override
  public final String buildEntitySetName(final String entityTypeName) {
    int length = entityTypeName.length();

    if (entityTypeName.charAt(length - 1) == 'y' && "aeiou".indexOf(entityTypeName.charAt(length - 2)) < 0) {
      return entityTypeName.substring(0, length - 1) + "ie" + ENTITY_SET_SUFFIX;
    }
    return entityTypeName + ENTITY_SET_SUFFIX;
  }

  /**
   * EDM EntityType Name - RULE:<p>
   * Use JPA Entity Name as EDM Entity Type Name
   */
  @Override
  public String buildEntityTypeName(final EntityType<?> jpaEntityType) {
    return jpaEntityType.getName();
  }

  @Override
  public final String getNamespace() {
    return namespace;
  }

  /**
   * EDM Navigation Property Name - RULE:<p>
   * OData requires: "The name of the navigation property MUST be unique
   * within the set of structural and navigation properties of the containing
   * structured type and any of its base types."
   * This is fulfilled by taking the property name it self.
   * @param jpaAttribute
   * @return
   */
  @Override
  public final String buildNaviPropertyName(final Attribute<?, ?> jpaAttribute) {
    return buildPropertyName(jpaAttribute.getName());
  }

  /**
   * EDM Property Name - RULE:<p>
   * OData Property Names are represented in Camel Case. The first character
   * of JPA Attribute Name is converted to an UpperCase Character.
   * @param jpaAttributeName
   * @return
   */
  @Override
  public final String buildPropertyName(final String jpaAttributeName) {
    return capitalize(jpaAttributeName);
  }

  /**
   * Convert the internal name of a java based operation into the external entity data model name.
   * @param internalOperationName
   * @return
   */
  @Override
  public final String buildOperationName(final String internalOperationName) {
    return capitalize(internalOperationName);
  }

  /**
   * Convert the internal java class name of an enumeration into the external entity data model name.
   * @param javaEnum
   * @return
   */
  @Override
  public final String buildEnumerationTypeName(final Class<? extends Enum<?>> javaEnum) {
    return javaEnum.getSimpleName();
  }

}