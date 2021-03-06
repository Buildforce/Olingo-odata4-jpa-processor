package nl.buildforce.sequoia.metadata.core.edm.mapper.impl;

import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAEdmNameBuilder;
import nl.buildforce.sequoia.metadata.core.edm.mapper.exception.ODataJPAModelException;

import jakarta.persistence.metamodel.Attribute;

final class IntermediateEmbeddedIdProperty extends IntermediateSimpleProperty {
  private final Attribute<?, ?> embeddable;

  /*IntermediateEmbeddedIdProperty(final JPAEdmNameBuilder nameBuilder, final Attribute<?, ?> jpaAttribute,
      final IntermediateSchema schema, final Attribute<?, ?> embeddable) throws ODataJPAModelException {
    super(nameBuilder, jpaAttribute, schema);
    this.embeddable = embeddable;
  }*/

  IntermediateEmbeddedIdProperty(final JPAEdmNameBuilder nameBuilder, final Attribute<?, ?> jpaAttribute,
      final IntermediateSchema schema) throws ODataJPAModelException {
    super(nameBuilder, jpaAttribute, schema);
    this.embeddable = null;
  }

  @Override
  public boolean isKey() {
    return true;
  }

  Attribute<?, ?> getEmbeddable() {
    return embeddable;
  }

}