/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.buildforce.olingo.commons.api.edm.EdmAction;
import nl.buildforce.olingo.commons.api.edm.EdmAnnotation;
import nl.buildforce.olingo.commons.api.edm.EdmAnnotations;
import nl.buildforce.olingo.commons.api.edm.EdmComplexType;
import nl.buildforce.olingo.commons.api.edm.EdmEntityContainer;
import nl.buildforce.olingo.commons.api.edm.EdmEntityType;
import nl.buildforce.olingo.commons.api.edm.EdmEnumType;
import nl.buildforce.olingo.commons.api.edm.EdmFunction;
import nl.buildforce.olingo.commons.api.edm.EdmSchema;
import nl.buildforce.olingo.commons.api.edm.EdmTerm;
import nl.buildforce.olingo.commons.api.edm.EdmTypeDefinition;
import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlAction;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlAnnotation;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlAnnotations;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlComplexType;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlEdmProvider;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlEntityType;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlEnumType;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlFunction;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlSchema;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlTerm;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlTypeDefinition;

public class EdmSchemaImpl extends AbstractEdmAnnotatable implements EdmSchema {

  private final CsdlSchema schema;
  private final EdmProviderImpl edm;
  private final CsdlEdmProvider provider;

  protected final String namespace;
  private final String alias;
  private final List<EdmEnumType> enumTypes;
  private final List<EdmEntityType> entityTypes;
  private final List<EdmComplexType> complexTypes;
  private final List<EdmAction> actions;
  private final List<EdmFunction> functions;
  private final List<EdmTypeDefinition> typeDefinitions;
  private final List<EdmTerm> terms;
  private final List<EdmAnnotations> annotationGroups;
  private final List<EdmAnnotation> annotations;
  private final EdmEntityContainer entityContainer;

  public EdmSchemaImpl(EdmProviderImpl edm, CsdlEdmProvider provider, CsdlSchema schema) {
    super(edm, schema);
    this.edm = edm;
    this.provider = provider;
    this.schema = schema;
    namespace = schema.getNamespace();
    alias = schema.getAlias();

    if (alias != null) {
      edm.cacheAliasNamespaceInfo(alias, namespace);
    }

    enumTypes = createEnumTypes();
    typeDefinitions = createTypeDefinitions();
    entityTypes = createEntityTypes();
    complexTypes = createComplexTypes();
    actions = createActions();
    functions = createFunctions();
    entityContainer = createEntityContainer();
    annotationGroups = createAnnotationGroups();
    annotations = createAnnotations();
    terms = createTerms();
  }

  @Override
  public List<EdmEnumType> getEnumTypes() {
    return Collections.unmodifiableList(enumTypes);
  }

  @Override
  public List<EdmEntityType> getEntityTypes() {
    return Collections.unmodifiableList(entityTypes);
  }

  @Override
  public List<EdmComplexType> getComplexTypes() {
    return Collections.unmodifiableList(complexTypes);
  }

  @Override
  public List<EdmAction> getActions() {
    return Collections.unmodifiableList(actions);
  }

  @Override
  public List<EdmFunction> getFunctions() {
    return Collections.unmodifiableList(functions);
  }

  @Override
  public List<EdmTypeDefinition> getTypeDefinitions() {
    return Collections.unmodifiableList(typeDefinitions);
  }

  @Override
  public List<EdmTerm> getTerms() {
    return Collections.unmodifiableList(terms);
  }

  @Override
  public List<EdmAnnotations> getAnnotationGroups() {
    return Collections.unmodifiableList(annotationGroups);
  }

  @Override
  public List<EdmAnnotation> getAnnotations() {
    return Collections.unmodifiableList(annotations);
  }

  @Override
  public EdmEntityContainer getEntityContainer() {
    return entityContainer;
  }

  @Override
  public String getNamespace() {
    return namespace;
  }

  @Override
  public String getAlias() {
    return alias;
  }

  protected EdmEntityContainer createEntityContainer() {
    if (schema.getEntityContainer() != null) {
      FullQualifiedName containerFQN = new FullQualifiedName(namespace, schema.getEntityContainer().getName());
      edm.addEntityContainerAnnotations(schema.getEntityContainer(), containerFQN);
      EdmEntityContainer impl = new EdmEntityContainerImpl(edm, provider, containerFQN, schema.getEntityContainer());
      edm.cacheEntityContainer(containerFQN, impl);
      edm.cacheEntityContainer(null, impl);
      return impl;
    }
    return null;
  }

  protected List<EdmTypeDefinition> createTypeDefinitions() {
    List<EdmTypeDefinition> typeDefns = new ArrayList<>();
    List<CsdlTypeDefinition> providerTypeDefinitions = schema.getTypeDefinitions();
    if (providerTypeDefinitions != null) {
      for (CsdlTypeDefinition def : providerTypeDefinitions) {
        FullQualifiedName typeDefName = new FullQualifiedName(namespace, def.getName());
        edm.addTypeDefnAnnotations(def, typeDefName);
        EdmTypeDefinitionImpl typeDefImpl = new EdmTypeDefinitionImpl(edm, typeDefName, def);
        typeDefns.add(typeDefImpl);
        edm.cacheTypeDefinition(typeDefName, typeDefImpl);
      }
    }
    return typeDefns;
  }

  protected List<EdmEnumType> createEnumTypes() {
    List<EdmEnumType> enumTyps = new ArrayList<>();
    List<CsdlEnumType> providerEnumTypes = schema.getEnumTypes();
    if (providerEnumTypes != null) {
      for (CsdlEnumType enumType : providerEnumTypes) {
        FullQualifiedName enumName = new FullQualifiedName(namespace, enumType.getName());
        edm.addEnumTypeAnnotations(enumType, enumName);
        EdmEnumType enumTypeImpl = new EdmEnumTypeImpl(edm, enumName, enumType);
        enumTyps.add(enumTypeImpl);
        edm.cacheEnumType(enumName, enumTypeImpl);
      }
    }
    return enumTyps;
  }

  protected List<EdmEntityType> createEntityTypes() {
    List<EdmEntityType> edmEntityTypes = new ArrayList<>();
    List<CsdlEntityType> providerEntityTypes = schema.getEntityTypes();
    if (providerEntityTypes != null) {
      for (CsdlEntityType entityType : providerEntityTypes) {
        FullQualifiedName entityTypeName = new FullQualifiedName(namespace, entityType.getName());
        edm.addStructuralTypeAnnotations(entityType, entityTypeName, schema.getEntityContainer());
        EdmEntityTypeImpl entityTypeImpl = new EdmEntityTypeImpl(edm, entityTypeName, entityType);
        edmEntityTypes.add(entityTypeImpl);
        edm.cacheEntityType(entityTypeName, entityTypeImpl);
      }
    }
    return edmEntityTypes;
  }

  protected List<EdmComplexType> createComplexTypes() {
    List<EdmComplexType> edmComplexTypes = new ArrayList<>();
    List<CsdlComplexType> providerComplexTypes = schema.getComplexTypes();
    if (providerComplexTypes != null) {
      for (CsdlComplexType complexType : providerComplexTypes) {
        FullQualifiedName comlexTypeName = new FullQualifiedName(namespace, complexType.getName());
        edm.addStructuralTypeAnnotations(complexType, comlexTypeName, schema.getEntityContainer());
        EdmComplexTypeImpl complexTypeImpl = new EdmComplexTypeImpl(edm, comlexTypeName, complexType);
        edmComplexTypes.add(complexTypeImpl);
        edm.cacheComplexType(comlexTypeName, complexTypeImpl);
      }
    }
    return edmComplexTypes;
  }

  protected List<EdmAction> createActions() {
    List<EdmAction> edmActions = new ArrayList<>();
    List<CsdlAction> providerActions = schema.getActions();
    if (providerActions != null) {
      for (CsdlAction action : providerActions) {
        FullQualifiedName actionName = new FullQualifiedName(namespace, action.getName());
        edm.addOperationsAnnotations(action, actionName);
        EdmActionImpl edmActionImpl = new EdmActionImpl(edm, actionName, action);
        edmActions.add(edmActionImpl);
        edm.cacheAction(actionName, edmActionImpl);
      }
    }
    return edmActions;
  }

  protected List<EdmFunction> createFunctions() {
    List<EdmFunction> edmFunctions = new ArrayList<>();
    List<CsdlFunction> providerFunctions = schema.getFunctions();
    if (providerFunctions != null) {
      for (CsdlFunction function : providerFunctions) {
        FullQualifiedName functionName = new FullQualifiedName(namespace, function.getName());
        edm.addOperationsAnnotations(function, functionName);
        EdmFunctionImpl functionImpl = new EdmFunctionImpl(edm, functionName, function);
        edmFunctions.add(functionImpl);
        edm.cacheFunction(functionName, functionImpl);
      }
    }
    return edmFunctions;
  }

  protected List<EdmTerm> createTerms() {
    List<EdmTerm> edmTerms = new ArrayList<>();
    List<CsdlTerm> providerTerms = schema.getTerms();
    if (providerTerms != null) {
      for (CsdlTerm term : providerTerms) {
        FullQualifiedName termName = new FullQualifiedName(namespace, term.getName());
        EdmTermImpl termImpl = new EdmTermImpl(edm, getNamespace(), term);
        edmTerms.add(termImpl);
        edm.cacheTerm(termName, termImpl);
      }
    }
    return edmTerms;
  }

  protected List<EdmAnnotations> createAnnotationGroups() {
    List<EdmAnnotations> edmAnnotationGroups = new ArrayList<>();
    List<CsdlAnnotations> providerAnnotations =
        schema.getAnnotationGroups();
    if (providerAnnotations != null) {
      for (CsdlAnnotations annotationGroup : providerAnnotations) {
        FullQualifiedName targetName;
        if (annotationGroup.getTarget().contains(".")) {
          targetName = new FullQualifiedName(annotationGroup.getTarget());
        } else {
          targetName = new FullQualifiedName(namespace, annotationGroup.getTarget());
        }
        EdmAnnotationsImpl annotationsImpl = new EdmAnnotationsImpl(edm, annotationGroup);
        edmAnnotationGroups.add(annotationsImpl);
        edm.cacheAnnotationGroup(targetName, annotationsImpl);
      }
    }
    return edmAnnotationGroups;
  }

  protected List<EdmAnnotation> createAnnotations() {
    List<EdmAnnotation> edmAnnotations = new ArrayList<>();
    List<CsdlAnnotation> providerAnnotations =
        schema.getAnnotations();
    if (providerAnnotations != null) {
      for (CsdlAnnotation annotation : providerAnnotations) {
        EdmAnnotationImpl annotationImpl = new EdmAnnotationImpl(edm, annotation);
        edmAnnotations.add(annotationImpl);
      }
    }
    return edmAnnotations;
  }
}