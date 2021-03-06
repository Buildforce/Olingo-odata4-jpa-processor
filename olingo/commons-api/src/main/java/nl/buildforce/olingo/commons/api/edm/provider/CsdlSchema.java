/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.provider;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Csdl schema.
 */
public class CsdlSchema extends CsdlAbstractEdmItem implements CsdlAnnotatable {

  private String namespace;

  private String alias;

  private List<CsdlEnumType> enumTypes = new ArrayList<>();

  private List<CsdlTypeDefinition> typeDefinitions = new ArrayList<>();

  private List<CsdlEntityType> entityTypes = new ArrayList<>();

  private List<CsdlComplexType> complexTypes = new ArrayList<>();

  private List<CsdlAction> actions = new ArrayList<>();

  private List<CsdlFunction> functions = new ArrayList<>();

  private CsdlEntityContainer entityContainer;

  private List<CsdlTerm> terms = new ArrayList<>();

  private List<CsdlAnnotations> annotationGroups = new ArrayList<>();

  private final List<CsdlAnnotation> annotations = new ArrayList<>();

  /**
   * Gets namespace.
   *
   * @return the namespace
   */
  public String getNamespace() {
    return namespace;
  }

  /**
   * Sets namespace.
   *
   * @param namespace the namespace
   * @return the namespace
   */
  public CsdlSchema setNamespace(String namespace) {
    this.namespace = namespace;
    return this;
  }

  /**
   * Gets alias.
   *
   * @return the alias
   */
  public String getAlias() {
    return alias;
  }

  /**
   * Sets alias.
   *
   * @param alias the alias
   * @return the alias
   */
  public CsdlSchema setAlias(String alias) {
    this.alias = alias;
    return this;
  }

  /**
   * Gets enum types.
   *
   * @return the enum types
   */
  public List<CsdlEnumType> getEnumTypes() {
    return enumTypes;
  }

  /**
   * Gets enum type.
   *
   * @param name the name
   * @return the enum type
   */
  public CsdlEnumType getEnumType(String name) {
    return getOneByName(name, getEnumTypes());
  }

  /**
   * Sets enum types.
   *
   * @param enumTypes the enum types
   * @return the enum types
   */
  public CsdlSchema setEnumTypes(List<CsdlEnumType> enumTypes) {
    this.enumTypes = enumTypes;
    return this;
  }

  /**
   * Gets type definitions.
   *
   * @return the type definitions
   */
  public List<CsdlTypeDefinition> getTypeDefinitions() {
    return typeDefinitions;
  }

  /**
   * Gets type definition.
   *
   * @param name the name
   * @return the type definition
   */
  public CsdlTypeDefinition getTypeDefinition(String name) {
    return getOneByName(name, getTypeDefinitions());
  }

  /**
   * Sets type definitions.
   *
   * @param typeDefinitions the type definitions
   * @return the type definitions
   */
  public CsdlSchema setTypeDefinitions(List<CsdlTypeDefinition> typeDefinitions) {
    this.typeDefinitions = typeDefinitions;
    return this;
  }

  /**
   * Gets entity types.
   *
   * @return the entity types
   */
  public List<CsdlEntityType> getEntityTypes() {
    return entityTypes;
  }

  /**
   * Gets entity type.
   *
   * @param name the name
   * @return the entity type
   */
  public CsdlEntityType getEntityType(String name) {
    return getOneByName(name, getEntityTypes());
  }

  /**
   * Sets entity types.
   *
   * @param entityTypes the entity types
   * @return the entity types
   */
  public CsdlSchema setEntityTypes(List<CsdlEntityType> entityTypes) {
    this.entityTypes = entityTypes;
    return this;
  }

  /**
   * Gets complex types.
   *
   * @return the complex types
   */
  public List<CsdlComplexType> getComplexTypes() {
    return complexTypes;
  }

  /**
   * Gets complex type.
   *
   * @param name the name
   * @return the complex type
   */
  public CsdlComplexType getComplexType(String name) {
    return getOneByName(name, getComplexTypes());
  }

  /**
   * Sets complex types.
   *
   * @param complexTypes the complex types
   * @return the complex types
   */
  public CsdlSchema setComplexTypes(List<CsdlComplexType> complexTypes) {
    this.complexTypes = complexTypes;
    return this;
  }

  /**
   * Gets actions.
   *
   * @return the actions
   */
  public List<CsdlAction> getActions() {
    return actions;
  }

  /**
   * All actions with the given name
   * @param name the name
   * @return a list of actions
   */
  public List<CsdlAction> getActions(String name) {
    return getAllByName(name, getActions());
  }

  /**
   * Sets actions.
   *
   * @param actions the actions
   * @return the actions
   */
  public CsdlSchema setActions(List<CsdlAction> actions) {
    this.actions = actions;
    return this;
  }

  /**
   * Gets functions.
   *
   * @return the functions
   */
  public List<CsdlFunction> getFunctions() {
    return functions;
  }

  /**
   * All functions with the given name
   * @param name the name
   * @return a list of functions
   */
  public List<CsdlFunction> getFunctions(String name) {
    return getAllByName(name, getFunctions());
  }

  /**
   * Sets functions.
   *
   * @param functions the functions
   * @return the functions
   */
  public CsdlSchema setFunctions(List<CsdlFunction> functions) {
    this.functions = functions;
    return this;
  }

  /**
   * Gets entity container.
   *
   * @return the entity container
   */
  public CsdlEntityContainer getEntityContainer() {
    return entityContainer;
  }

  /**
   * Sets entity container.
   *
   * @param entityContainer the entity container
   * @return the entity container
   */
  public CsdlSchema setEntityContainer(CsdlEntityContainer entityContainer) {
    this.entityContainer = entityContainer;
    return this;
  }

  /**
   * Gets terms.
   *
   * @return the terms
   */
  public List<CsdlTerm> getTerms() {
    return terms;
  }

  /**
   * Gets term.
   *
   * @param name the name
   * @return the term
   */
/*
  public CsdlTerm getTerm(String name) {
    return getOneByName(name, getTerms());
  }
*/

  /**
   * Sets terms.
   *
   * @param terms the terms
   * @return the terms
   */
  public CsdlSchema setTerms(List<CsdlTerm> terms) {
    this.terms = terms;
    return this;
  }

  /**
   * Gets annotation groups.
   *
   * @return the annotation groups
   */
  public List<CsdlAnnotations> getAnnotationGroups() {
    return annotationGroups;
  }
  
  /**
   * Sets a list of annotations
   * @param annotationGroups list of annotations
   * @return this instance
   */
  public CsdlSchema setAnnotationsGroup(List<CsdlAnnotations> annotationGroups) {
    this.annotationGroups = annotationGroups;
    return this;
  }

  /**
   * Gets annotation group.
   *
   * @param target the target
   * @return the annotation group
   */
  public CsdlAnnotations getAnnotationGroup(String target, String qualifier) {
    CsdlAnnotations result = null;
    for (CsdlAnnotations annots : getAnnotationGroups()) {
      if (target.equals(annots.getTarget())
              && qualifier != null && qualifier.equals(annots.getQualifier())) {
        result = annots;
      }
    }
    return result;
  }

  /**
   * Gets annotation.
   *
   * @param term the term
   * @return the annotation
   */
/*
  public CsdlAnnotation getAnnotation(String term) {
    CsdlAnnotation result = null;
    for (CsdlAnnotation annot : getAnnotations()) {
      if (term.equals(annot.getTerm())) {
        result = annot;
      }
    }
    return result;
  }
*/

  @Override
  public List<CsdlAnnotation> getAnnotations() {
    return annotations;
  }
  
  /**
   * Sets a list of annotations
   * @param annotations list of annotations
   * @return this instance
   */
/*
  public CsdlSchema setAnnotations(List<CsdlAnnotation> annotations) {
    this.annotations = annotations;
    return this;
  }
*/

}