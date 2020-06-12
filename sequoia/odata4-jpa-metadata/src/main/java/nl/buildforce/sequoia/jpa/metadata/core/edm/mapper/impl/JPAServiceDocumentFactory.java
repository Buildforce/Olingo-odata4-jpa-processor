package nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.impl;

import nl.buildforce.sequoia.jpa.metadata.api.JPAEdmMetadataPostProcessor;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAServiceDocument;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAModelException;

import jakarta.persistence.metamodel.Metamodel;

public final class JPAServiceDocumentFactory {

  private final String namespace;
  private final Metamodel jpaMetamodel;
  private final JPAEdmMetadataPostProcessor postProcessor;
  private final String[] packageName;

  public JPAServiceDocumentFactory(final String namespace, final Metamodel jpaMetamodel,
      final JPAEdmMetadataPostProcessor postProcessor, final String[] packageName) {
    this.namespace = namespace;
    this.jpaMetamodel = jpaMetamodel;
    this.postProcessor = postProcessor;
    this.packageName = packageName;
  }

  public JPAServiceDocument getServiceDocument() throws ODataJPAModelException {
    return new IntermediateServiceDocument(namespace, jpaMetamodel, postProcessor, packageName);
  }

}