/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmAction;
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
import nl.buildforce.olingo.commons.api.edm.provider.CsdlAnnotation;

public abstract class AbstractEdm implements Edm {

  protected Map<String, EdmSchema> schemas;
  protected List<EdmSchema> schemaList;
  private boolean isEntityDerivedFromES;
  private boolean isComplexDerivedFromES;
  private boolean isPreviousES;

  private final Map<FullQualifiedName, EdmEntityContainer> entityContainers =
      Collections.synchronizedMap(new HashMap<>());

  private final Map<FullQualifiedName, EdmEnumType> enumTypes =
      Collections.synchronizedMap(new HashMap<>());

  private final Map<FullQualifiedName, EdmTypeDefinition> typeDefinitions =
      Collections.synchronizedMap(new HashMap<>());

  private final Map<FullQualifiedName, EdmEntityType> entityTypes =
      Collections.synchronizedMap(new HashMap<>());

  private final Map<FullQualifiedName, EdmComplexType> complexTypes =
      Collections.synchronizedMap(new HashMap<>());

  private final Map<FullQualifiedName, EdmAction> unboundActions =
      Collections.synchronizedMap(new HashMap<>());

  private final Map<FullQualifiedName, List<EdmFunction>> unboundFunctionsByName =
      Collections.synchronizedMap(new HashMap<>());

  private final Map<FunctionMapKey, EdmFunction> unboundFunctionsByKey =
      Collections.synchronizedMap(new HashMap<>());

  private final Map<ActionMapKey, EdmAction> boundActions =
      Collections.synchronizedMap(new HashMap<>());

  private final Map<FunctionMapKey, EdmFunction> boundFunctions =
      Collections.synchronizedMap(new HashMap<>());

  private final Map<FullQualifiedName, EdmTerm> terms =
      Collections.synchronizedMap(new HashMap<>());

  private final Map<TargetQualifierMapKey, EdmAnnotations> annotationGroups =
      Collections.synchronizedMap(new HashMap<>());

  private Map<String, String> aliasToNamespaceInfo;
  
  private final Map<FullQualifiedName, EdmEntityType> entityTypesWithAnnotations =
      Collections.synchronizedMap(new HashMap<>());
  
  private final Map<FullQualifiedName, EdmEntityType> entityTypesDerivedFromES =
      Collections.synchronizedMap(new HashMap<>());
  
  private final Map<FullQualifiedName, EdmComplexType> complexTypesWithAnnotations =
      Collections.synchronizedMap(new HashMap<>());
  
  private final Map<FullQualifiedName, EdmComplexType> complexTypesDerivedFromES =
      Collections.synchronizedMap(new HashMap<>());

  private final Map<String, List<CsdlAnnotation>> annotationMap =
          new HashMap<>();
  
  @Override
  public List<EdmSchema> getSchemas() {
    if (schemaList == null) {
      initSchemas();
    }
    return schemaList;
  }

  @Override
  public EdmSchema getSchema(String namespace) {
    if (schemas == null) {
      initSchemas();
    }

    EdmSchema schema = schemas.get(namespace);
    if (schema == null) {
      schema = schemas.get(aliasToNamespaceInfo.get(namespace));
    }
    return schema;
  }

  private void initSchemas() {
    loadAliasToNamespaceInfo();
    Map<String, EdmSchema> localSchemas = createSchemas();
    schemas = Collections.synchronizedMap(localSchemas);

    schemaList = List.copyOf(schemas.values());
  }

  private void loadAliasToNamespaceInfo() {
    Map<String, String> localAliasToNamespaceInfo = createAliasToNamespaceInfo();
    aliasToNamespaceInfo = Collections.synchronizedMap(localAliasToNamespaceInfo);
  }

  @Override
  public EdmEntityContainer getEntityContainer() {
    return getEntityContainer(null);
  }

  @Override
  public EdmEntityContainer getEntityContainer(FullQualifiedName namespaceOrAliasFQN) {
    FullQualifiedName fqn = resolvePossibleAlias(namespaceOrAliasFQN);
    EdmEntityContainer container = entityContainers.get(fqn);
    if (container == null) {
      container = createEntityContainer(fqn);
      if (container != null) {
        entityContainers.put(fqn, container);
        if (fqn == null) {
          entityContainers.put(new FullQualifiedName(container.getNamespace(), container.getName()), container);
        }
      }
    }
    return container;
  }

  @Override
  public EdmEnumType getEnumType(FullQualifiedName namespaceOrAliasFQN) {
    FullQualifiedName fqn = resolvePossibleAlias(namespaceOrAliasFQN);
    EdmEnumType enumType = enumTypes.get(fqn);
    if (enumType == null) {
      enumType = createEnumType(fqn);
      if (enumType != null) {
        enumTypes.put(fqn, enumType);
      }
    }
    return enumType;
  }

    @Override
    public EdmTypeDefinition getTypeDefinition(FullQualifiedName namespaceOrAliasFQN) {
    FullQualifiedName fqn = resolvePossibleAlias(namespaceOrAliasFQN);
    EdmTypeDefinition typeDefinition = typeDefinitions.get(fqn);
    if (typeDefinition == null) {
      typeDefinition = createTypeDefinition(fqn);
      if (typeDefinition != null) {
        typeDefinitions.put(fqn, typeDefinition);
      }
    }
    return typeDefinition;
  }

  @Override
  public EdmEntityType getEntityType(FullQualifiedName namespaceOrAliasFQN) {
    FullQualifiedName fqn = resolvePossibleAlias(namespaceOrAliasFQN);
    EdmEntityType entityType = entityTypes.get(fqn);
    if (entityType == null) {
      entityType = createEntityType(fqn);
      if (entityType != null) {
        entityTypes.put(fqn, entityType);
      }
    }
    return entityType;
  }

  @Override
  public EdmEntityType getEntityTypeWithAnnotations(FullQualifiedName namespaceOrAliasFQN) {
    FullQualifiedName fqn = resolvePossibleAlias(namespaceOrAliasFQN);
    EdmEntityType entityType = entityTypesWithAnnotations.get(fqn);
    if (entityType == null) {
      entityType = createEntityType(fqn);
      if (entityType != null) {
          entityTypesWithAnnotations.put(fqn, entityType);
      }
    }
    setIsPreviousES(false);
    return entityType;
  }

  protected EdmEntityType getEntityTypeWithAnnotations(FullQualifiedName namespaceOrAliasFQN,
                                                       boolean isEntityDerivedFromES) {
    this.isEntityDerivedFromES = isEntityDerivedFromES;
    FullQualifiedName fqn = resolvePossibleAlias(namespaceOrAliasFQN);
    if (!isPreviousES && getEntityContainer() != null) {
       getEntityContainer().getEntitySetsWithAnnotations();
    }
    EdmEntityType entityType = entityTypesDerivedFromES.get(fqn);
    if (entityType == null) {
      entityType = createEntityType(fqn);
      if (entityType != null) {
          entityTypesDerivedFromES.put(fqn, entityType);
      }
    }
    this.isEntityDerivedFromES = false;
    return entityType;
  }
  
  protected EdmComplexType getComplexTypeWithAnnotations(FullQualifiedName namespaceOrAliasFQN,
                                                         boolean isComplexDerivedFromES) {
    this.isComplexDerivedFromES = isComplexDerivedFromES;
    FullQualifiedName fqn = resolvePossibleAlias(namespaceOrAliasFQN);
    if (!isPreviousES && getEntityContainer() != null) {
       getEntityContainer().getEntitySetsWithAnnotations();
    }
    EdmComplexType complexType = complexTypesDerivedFromES.get(fqn);
    if (complexType == null) {
      complexType = createComplexType(fqn);
      if (complexType != null) {
          complexTypesDerivedFromES.put(fqn, complexType);
      }
    }
    this.isComplexDerivedFromES = false;
    return complexType;
  }

  @Override
  public EdmComplexType getComplexType(FullQualifiedName namespaceOrAliasFQN) {
    FullQualifiedName fqn = resolvePossibleAlias(namespaceOrAliasFQN);
    EdmComplexType complexType = complexTypes.get(fqn);
    if (complexType == null) {
      complexType = createComplexType(fqn);
      if (complexType != null) {
        complexTypes.put(fqn, complexType);
      }
    }
    return complexType;
  }

  @Override
  public EdmComplexType getComplexTypeWithAnnotations(FullQualifiedName namespaceOrAliasFQN) {
    FullQualifiedName fqn = resolvePossibleAlias(namespaceOrAliasFQN);
    EdmComplexType complexType = complexTypesWithAnnotations.get(fqn);
    if (complexType == null) {
      complexType = createComplexType(fqn);
      if (complexType != null) {
          complexTypesWithAnnotations.put(fqn, complexType);
      }
    }
    setIsPreviousES(false);
    return complexType;
  }

  @Override
  public EdmAction getUnboundAction(FullQualifiedName actionName) {
    FullQualifiedName fqn = resolvePossibleAlias(actionName);
    EdmAction action = unboundActions.get(fqn);
    if (action == null) {
      action = createUnboundAction(fqn);
      if (action != null) {
        unboundActions.put(actionName, action);
      }
    }

    return action;
  }

  @Override
  public EdmAction getBoundAction(FullQualifiedName actionName,
                                  FullQualifiedName bindingParameterTypeName, Boolean isBindingParameterCollection) {

    FullQualifiedName actionFqn = resolvePossibleAlias(actionName);
    FullQualifiedName bindingParameterTypeFqn = resolvePossibleAlias(bindingParameterTypeName);
    ActionMapKey key = new ActionMapKey(actionFqn, bindingParameterTypeFqn, isBindingParameterCollection);
    EdmAction action = boundActions.get(key);
    if (action == null) {
      action = createBoundAction(actionFqn, bindingParameterTypeFqn, isBindingParameterCollection);
      if (action != null) {
        boundActions.put(key, action);
      }
    }

    return action;
  }

  @Override
  public List<EdmFunction> getUnboundFunctions(FullQualifiedName functionName) {
    FullQualifiedName functionFqn = resolvePossibleAlias(functionName);

    List<EdmFunction> functions = unboundFunctionsByName.get(functionFqn);
    if (functions == null) {
      functions = createUnboundFunctions(functionFqn);
      if (functions != null) {
        unboundFunctionsByName.put(functionFqn, functions);

        for (EdmFunction unbound : functions) {
          FunctionMapKey key = new FunctionMapKey(
              new FullQualifiedName(unbound.getNamespace(), unbound.getName()),
              unbound.getBindingParameterTypeFqn(),
              unbound.isBindingParameterTypeCollection(),
              unbound.getParameterNames());
          unboundFunctionsByKey.put(key, unbound);
        }
      }
    }

    return functions;
  }

  @Override
  public EdmFunction getUnboundFunction(FullQualifiedName functionName, List<String> parameterNames) {
    FullQualifiedName functionFqn = resolvePossibleAlias(functionName);

    FunctionMapKey key = new FunctionMapKey(functionFqn, null, null, parameterNames);
    EdmFunction function = unboundFunctionsByKey.get(key);
    if (function == null) {
      function = createUnboundFunction(functionFqn, parameterNames);
      if (function != null) {
        unboundFunctionsByKey.put(key, function);
      }
    }

    return function;
  }

  @Override
  public EdmFunction getBoundFunction(FullQualifiedName functionName,
                                      FullQualifiedName bindingParameterTypeName,
                                      Boolean isBindingParameterCollection, List<String> parameterNames) {

    FullQualifiedName functionFqn = resolvePossibleAlias(functionName);
    FullQualifiedName bindingParameterTypeFqn = resolvePossibleAlias(bindingParameterTypeName);
    FunctionMapKey key =
        new FunctionMapKey(functionFqn, bindingParameterTypeFqn, isBindingParameterCollection, parameterNames);
    EdmFunction function = boundFunctions.get(key);
    if (function == null) {
      function = createBoundFunction(functionFqn, bindingParameterTypeFqn, isBindingParameterCollection,
          parameterNames);
      if (function != null) {
        boundFunctions.put(key, function);
      }
    }

    return function;
  }

  @Override
  public EdmTerm getTerm(FullQualifiedName termName) {
    FullQualifiedName fqn = resolvePossibleAlias(termName);
    EdmTerm term = terms.get(fqn);
    if (term == null) {
      term = createTerm(fqn);
      if (term != null) {
        terms.put(fqn, term);
      }
    }
    return term;
  }

  @Override
  public EdmAnnotations getAnnotationGroup(FullQualifiedName targetName, String qualifier) {
    FullQualifiedName fqn = resolvePossibleAlias(targetName);
    TargetQualifierMapKey key = new TargetQualifierMapKey(fqn, qualifier);
    EdmAnnotations _annotations = annotationGroups.get(key);
    if (_annotations == null) {
      _annotations = createAnnotationGroup(fqn, qualifier);
      if (_annotations != null) {
        annotationGroups.put(key, _annotations);
      }
    }
    return _annotations;
  }

  private FullQualifiedName resolvePossibleAlias(FullQualifiedName namespaceOrAliasFQN) {
    if (aliasToNamespaceInfo == null) {
      loadAliasToNamespaceInfo();
    }
    FullQualifiedName finalFQN = null;
    if (namespaceOrAliasFQN != null) {
      String namespace = aliasToNamespaceInfo.get(namespaceOrAliasFQN.getNamespace());
      // If not contained in info it must be a namespace
      if (namespace == null) {
        finalFQN = namespaceOrAliasFQN;
      } else {
        finalFQN = new FullQualifiedName(namespace, namespaceOrAliasFQN.getName());
      }
    }
    return finalFQN;
  }

  protected abstract Map<String, EdmSchema> createSchemas();

  protected abstract Map<String, String> createAliasToNamespaceInfo();

  public void cacheAliasNamespaceInfo(String alias, String namespace) {
    aliasToNamespaceInfo.put(alias, namespace);
  }

  protected abstract EdmEntityContainer createEntityContainer(FullQualifiedName containerName);

  public void cacheEntityContainer(FullQualifiedName containerFQN, EdmEntityContainer container) {
    entityContainers.put(containerFQN, container);
  }

  protected abstract EdmEnumType createEnumType(FullQualifiedName enumName);

  public void cacheEnumType(FullQualifiedName enumName, EdmEnumType enumType) {
    enumTypes.put(enumName, enumType);
  }

  protected abstract EdmTypeDefinition createTypeDefinition(FullQualifiedName typeDefinitionName);

  public void cacheTypeDefinition(FullQualifiedName typeDefName, EdmTypeDefinition typeDef) {
    typeDefinitions.put(typeDefName, typeDef);
  }

  protected abstract EdmEntityType createEntityType(FullQualifiedName entityTypeName);

  public void cacheEntityType(FullQualifiedName entityTypeName, EdmEntityType entityType) {
    entityTypes.put(entityTypeName, entityType);
  }

  protected abstract EdmComplexType createComplexType(FullQualifiedName complexTypeName);

  public void cacheComplexType(FullQualifiedName compelxTypeName, EdmComplexType complexType) {
    complexTypes.put(compelxTypeName, complexType);
  }

  protected abstract EdmAction createUnboundAction(FullQualifiedName actionName);

  protected abstract List<EdmFunction> createUnboundFunctions(FullQualifiedName functionName);

  protected abstract EdmFunction createUnboundFunction(FullQualifiedName functionName, List<String> parameterNames);

  protected abstract EdmAction createBoundAction(FullQualifiedName actionName,
      FullQualifiedName bindingParameterTypeName,
      Boolean isBindingParameterCollection);

  protected abstract EdmFunction createBoundFunction(FullQualifiedName functionName,
      FullQualifiedName bindingParameterTypeName, Boolean isBindingParameterCollection,
      List<String> parameterNames);

  public void cacheFunction(FullQualifiedName functionName, EdmFunction function) {
    FunctionMapKey key = new FunctionMapKey(functionName,
        function.getBindingParameterTypeFqn(), function.isBindingParameterTypeCollection(),
        function.getParameterNames());

    if (function.isBound()) {
      boundFunctions.put(key, function);
    } else {
      if (!unboundFunctionsByName.containsKey(functionName)) {
        unboundFunctionsByName.put(functionName, new ArrayList<>());
      }
      unboundFunctionsByName.get(functionName).add(function);

      unboundFunctionsByKey.put(key, function);
    }
  }

  public void cacheAction(FullQualifiedName actionName, EdmAction action) {
    if (action.isBound()) {
      ActionMapKey key = new ActionMapKey(actionName,
          action.getBindingParameterTypeFqn(), action.isBindingParameterTypeCollection());
      boundActions.put(key, action);
    } else {
      unboundActions.put(actionName, action);
    }
  }

  protected abstract EdmTerm createTerm(FullQualifiedName termName);

  public void cacheTerm(FullQualifiedName termName, EdmTerm term) {
    terms.put(termName, term);
  }

  protected abstract EdmAnnotations createAnnotationGroup(FullQualifiedName targetName, String qualifier);

  public void cacheAnnotationGroup(FullQualifiedName targetName,
                                   EdmAnnotations annotationsGroup) {
    TargetQualifierMapKey key = new TargetQualifierMapKey(targetName, annotationsGroup.getQualifier());
    annotationGroups.put(key, annotationsGroup);
  }
  
/*
  @Override
  public EdmAction getBoundActionWithBindingType(FullQualifiedName bindingParameterTypeName,
      Boolean isBindingParameterCollection) {
    for (EdmSchema schema:getSchemas()) {
      for (EdmAction action: schema.getActions()) {
        if (action.isBound()) {
          EdmParameter bindingParameter = action.getParameter(action.getParameterNames().get(0));
          if (bindingParameter.getType().getFullQualifiedName().equals(bindingParameterTypeName)
              && bindingParameter.isCollection() == isBindingParameterCollection) {
            return action;  
          }          
        }
      }
    }
    return null;
  }
  
  @Override
  public List<EdmFunction> getBoundFunctionsWithBindingType(FullQualifiedName bindingParameterTypeName,
      Boolean isBindingParameterCollection){
    List<EdmFunction> functions = new ArrayList<>();
    for (EdmSchema schema:getSchemas()) {
      for (EdmFunction function: schema.getFunctions()) {
        if (function.isBound()) {
          EdmParameter bindingParameter = function.getParameter(function.getParameterNames().get(0));
          if (bindingParameter.getType().getFullQualifiedName().equals(bindingParameterTypeName)
              && bindingParameter.isCollection() == isBindingParameterCollection) {
            functions.add(function);
          }
        }
      }
    }
    return functions;
  }
*/

  protected boolean isEntityDerivedFromES() {
    return isEntityDerivedFromES;
  }
  
  protected boolean isComplexDerivedFromES() {
    return isComplexDerivedFromES;
  }
  
  protected void setIsPreviousES(boolean isPreviousES) {
    this.isPreviousES = isPreviousES;
  }
  
  protected Map<String, List<CsdlAnnotation>> getAnnotationsMap() {
    return annotationMap;
  }

}