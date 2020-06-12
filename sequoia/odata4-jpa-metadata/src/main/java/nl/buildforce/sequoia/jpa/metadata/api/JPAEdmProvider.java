package nl.buildforce.sequoia.jpa.metadata.api;

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAEdmNameBuilder;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAServiceDocument;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAException;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.impl.JPADefaultEdmNameBuilder;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.impl.JPAServiceDocumentFactory;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlAbstractEdmProvider;
import org.apache.olingo.commons.api.edm.provider.CsdlAction;
import org.apache.olingo.commons.api.edm.provider.CsdlActionImport;
import org.apache.olingo.commons.api.edm.provider.CsdlAnnotations;
import org.apache.olingo.commons.api.edm.provider.CsdlComplexType;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityContainer;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityContainerInfo;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlEnumType;
import org.apache.olingo.commons.api.edm.provider.CsdlFunction;
import org.apache.olingo.commons.api.edm.provider.CsdlFunctionImport;
import org.apache.olingo.commons.api.edm.provider.CsdlSchema;
import org.apache.olingo.commons.api.edm.provider.CsdlTerm;
import org.apache.olingo.commons.api.edm.provider.CsdlTypeDefinition;
import org.apache.olingo.commons.api.edmx.EdmxReference;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.metamodel.Metamodel;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

public class JPAEdmProvider extends CsdlAbstractEdmProvider {

  private final JPAEdmNameBuilder nameBuilder;
  private final JPAServiceDocument serviceDocument;

  // http://docs.oasis-open.org/odata/odata/v4.0/errata02/os/complete/part3-csdl/odata-v4.0-errata02-os-part3-csdl-complete.html#_Toc406397930
  public JPAEdmProvider(final String namespace,
                        final EntityManagerFactory emf,
                        final JPAEdmMetadataPostProcessor postProcessor,
                        final String[] packageName) throws ODataJPAException {
    this(namespace, emf.getMetamodel(), postProcessor, packageName);
  }

  public JPAEdmProvider(final String namespace,
                        final Metamodel jpaMetamodel,
                        final JPAEdmMetadataPostProcessor postProcessor,
                        final String[] packageName) throws ODataJPAException {
    nameBuilder = new JPADefaultEdmNameBuilder(namespace);
    // this(jpaMetamodel, postProcessor, packageName, new JPADefaultEdmNameBuilder(namespace));
    serviceDocument =
            new JPAServiceDocumentFactory(namespace, jpaMetamodel, postProcessor, packageName).getServiceDocument();
  }

/*  public JPAEdmProvider(final EntityManagerFactory emf,
      final JPAEdmMetadataPostProcessor postProcessor, final String[] packageName, final JPAEdmNameBuilder nameBuilder)
      throws ODataJPAException {
    this(emf.getMetamodel(), postProcessor, packageName, nameBuilder);
  }
  public JPAEdmProvider(final Metamodel jpaMetamodel,
                        final JPAEdmMetadataPostProcessor postProcessor,
                        final String[] packageName,
                        final JPAEdmNameBuilder nameBuilder) throws ODataJPAException {
    this.nameBuilder = nameBuilder;
    this.serviceDocument =
            new JPAServiceDocumentFactory(nameBuilder.getNamespace(), jpaMetamodel, postProcessor, packageName).getServiceDocument();
  }*/

  @Override
  public CsdlComplexType getComplexType(final FullQualifiedName complexTypeName) throws ODataJPAException {
    for (final CsdlSchema schema : serviceDocument.getAllSchemas()) {
      if (schema.getNamespace().equals(complexTypeName.getNamespace())
          || schema.getAlias() != null && schema.getAlias().equals(complexTypeName.getNamespace())) {
        return schema.getComplexType(complexTypeName.getName());
      }
    }
    return null;
  }

  @Override
  public CsdlEntityContainer getEntityContainer() throws ODataJPAException {
    return serviceDocument.getEdmEntityContainer();
  }

  @Override
  public CsdlEntityContainerInfo getEntityContainerInfo(final FullQualifiedName entityContainerName) {
    // This method is invoked when displaying the Service Document at e.g.
    // .../DemoService.svc
    if (entityContainerName == null
        || entityContainerName.equals(buildFQN(nameBuilder.buildContainerName()))) {
      final CsdlEntityContainerInfo entityContainerInfo = new CsdlEntityContainerInfo();
      entityContainerInfo.setContainerName(buildFQN(nameBuilder.buildContainerName()));
      return entityContainerInfo;
    }
    return null;
  }

  @Override
  public CsdlEntitySet getEntitySet(final FullQualifiedName entityContainerFQN, final String entitySetName)
      throws ODataJPAException {
    final CsdlEntityContainer container = serviceDocument.getEdmEntityContainer();
    if (entityContainerFQN.equals(buildFQN(container.getName()))) {
      return container.getEntitySet(entitySetName);
    }
    return null;
  }

  @Override
  public CsdlEntityType getEntityType(final FullQualifiedName entityTypeName) throws ODataJPAException {

    for (final CsdlSchema schema : serviceDocument.getEdmSchemas()) {
      if (schema.getNamespace().equals(entityTypeName.getNamespace())) {
        return schema.getEntityType(entityTypeName.getName());
      }
    }
    return null;
  }

  @Override
  public CsdlFunctionImport getFunctionImport(final FullQualifiedName entityContainerFQN,
      final String functionImportName) throws ODataJPAException {
    final CsdlEntityContainer container = serviceDocument.getEdmEntityContainer();
    if (entityContainerFQN.equals(buildFQN(container.getName()))) {
      return container.getFunctionImport(functionImportName);
    }
    return null;
  }

  @Override
  public List<CsdlFunction> getFunctions(final FullQualifiedName functionName) throws ODataJPAException {
    for (final CsdlSchema schema : serviceDocument.getEdmSchemas()) {
      if (schema.getNamespace().equals(functionName.getNamespace())) {
        return schema.getFunctions(functionName.getName());
      }
    }
    return null; // NOSONAR see documentation
  }

  @Override
  public List<CsdlAction> getActions(final FullQualifiedName actionName) throws ODataJPAException {
    for (final CsdlSchema schema : serviceDocument.getEdmSchemas()) {
      if (schema.getNamespace().equals(actionName.getNamespace())) {
        return schema.getActions(actionName.getName());
      }
    }
    return null; // NOSONAR see documentation
  }

  @Override
  public CsdlActionImport getActionImport(final FullQualifiedName entityContainerFQN, final String actionImportName)
      throws ODataJPAException {
    final CsdlEntityContainer container = serviceDocument.getEdmEntityContainer();
    if (entityContainerFQN.equals(buildFQN(container.getName()))) {
      return container.getActionImport(actionImportName);
    }
    return null;
  }

  @Override
  public CsdlEnumType getEnumType(final FullQualifiedName enumTypeNameFQN) throws ODataJPAException {

    for (final CsdlSchema schema : serviceDocument.getEdmSchemas()) {
      if (schema.getNamespace().equals(enumTypeNameFQN.getNamespace())) {
        return schema.getEnumType(enumTypeNameFQN.getName());
      }
    }
    return null;
  }

  @Override
  public CsdlAnnotations getAnnotationsGroup(final FullQualifiedName targetName, String qualifier) {
    return null;
  }

  @Override
  public CsdlTerm getTerm(final FullQualifiedName termName) {
    return serviceDocument.getTerm(termName);
  }

  @Override
  public CsdlTypeDefinition getTypeDefinition(final FullQualifiedName typeDefinitionName) throws ODataJPAException {
    for (final CsdlSchema schema : serviceDocument.getAllSchemas()) {
      if (schema.getNamespace().equals(typeDefinitionName.getNamespace())) {
        return schema.getTypeDefinition(typeDefinitionName.getName());
      }
    }
    return null;
  }

  @Override
  public List<CsdlSchema> getSchemas() throws ODataJPAException {
    return serviceDocument.getEdmSchemas();
  }

  public JPAServiceDocument getServiceDocument() {
    return serviceDocument;
  }

  public void setRequestLocales(final Enumeration<Locale> locales) {
    ODataJPAException.setLocales(locales);
  }

  public List<EdmxReference> getReferences() {
    return serviceDocument.getReferences();
  }

  public JPAEdmNameBuilder getEdmNameBuilder() {
    return nameBuilder;
  }

  protected final FullQualifiedName buildFQN(final String name) {
    return new FullQualifiedName(nameBuilder.getNamespace(), name);
  }

}