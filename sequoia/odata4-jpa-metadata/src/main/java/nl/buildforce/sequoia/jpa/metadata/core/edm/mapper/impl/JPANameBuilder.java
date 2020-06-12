package nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.impl;

import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmAction;
import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmFunction;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAEdmNameBuilder;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAStructuredType;
import org.apache.olingo.commons.api.edm.FullQualifiedName;

import jakarta.persistence.metamodel.Attribute;

/**
 * Build the internal name for Intermediate Model Elements
 *

 * @author Oliver Grande
 *
 */
final class JPANameBuilder {
  public String buildStructuredTypeName(final Class<?> clazz) {
    return clazz.getCanonicalName();
  }

  public String buildAttributeName(final Attribute<?, ?> jpaAttribute) {
    return jpaAttribute.getName();
  }

  public String buildAssociationName(final Attribute<?, ?> jpaAttribute) {
    return jpaAttribute.getName();
  }

  public String buildFunctionName(final EdmFunction jpaFunction) {
    return jpaFunction.name();
  }

  public String buildActionName(final EdmAction jpaAction) {
    return jpaAction.name();
  }

  public String buildEntitySetName(final JPAEdmNameBuilder nameBuilder, final JPAStructuredType entityType) {
    return buildFQN(entityType.getInternalName(), nameBuilder).getFullQualifiedNameAsString();
  }

  protected final FullQualifiedName buildFQN(final String name, final JPAEdmNameBuilder nameBuilder) {
    return new FullQualifiedName(nameBuilder.getNamespace(), name);
  }

}