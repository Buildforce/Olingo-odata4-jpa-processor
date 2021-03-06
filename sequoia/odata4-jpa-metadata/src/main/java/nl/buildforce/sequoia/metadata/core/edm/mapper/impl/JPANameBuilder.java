package nl.buildforce.sequoia.metadata.core.edm.mapper.impl;

import nl.buildforce.sequoia.metadata.core.edm.annotation.EdmAction;
import nl.buildforce.sequoia.metadata.core.edm.annotation.EdmFunction;
import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAEdmNameBuilder;
import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAStructuredType;
import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;

import jakarta.persistence.metamodel.Attribute;

/**
 * Build the internal name for Intermediate Model Elements
 *

 */
final class JPANameBuilder {
  static public String buildStructuredTypeName(final Class<?> clazz) {
    return clazz.getCanonicalName();
  }

  static public String buildAttributeName(final Attribute<?, ?> jpaAttribute) {
    return jpaAttribute.getName();
  }

  static public String buildAssociationName(final Attribute<?, ?> jpaAttribute) {
    return jpaAttribute.getName();
  }

  static public String buildFunctionName(final EdmFunction jpaFunction) {
    return jpaFunction.name();
  }

  static public String buildActionName(final EdmAction jpaAction) {
    return jpaAction.name();
  }

  static public String buildEntitySetName(final JPAEdmNameBuilder nameBuilder, final JPAStructuredType entityType) {
    return buildFQN(entityType.getInternalName(), nameBuilder).getFullQualifiedNameAsString();
  }

  static protected FullQualifiedName buildFQN(final String name, final JPAEdmNameBuilder nameBuilder) {
    return new FullQualifiedName(nameBuilder.getNamespace(), name);
  }

}