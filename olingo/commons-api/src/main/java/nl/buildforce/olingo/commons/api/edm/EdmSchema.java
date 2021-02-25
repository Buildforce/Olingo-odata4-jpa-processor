/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm;

import java.util.List;

/**
 * A csdl schema element
 */
public interface EdmSchema extends EdmAnnotatable {

  /**
   * @return the namespace for this schema
   */
  String getNamespace();

  /**
   * @return the alias for this schema. May be null.
   */
  String getAlias();

  /**
   * @return all enum types for this schema
   */
  List<EdmEnumType> getEnumTypes();

  /**
   * @return all entity types for this schema
   */
  List<EdmEntityType> getEntityTypes();

  /**
   * @return all complex types for this schema
   */
  List<EdmComplexType> getComplexTypes();

  /**
   * @return all actions for this schema
   */
  List<EdmAction> getActions();

  /**
   * @return all functions for this schema
   */
  List<EdmFunction> getFunctions();

  /**
   * @return all {@link EdmTypeDefinition} for this schema.
   */
  List<EdmTypeDefinition> getTypeDefinitions();

  /**
   * @return all {@link EdmTerm} for this schema.
   */
  List<EdmTerm> getTerms();

  /**
   * @return all {@link EdmAnnotations} for this schema.
   */
  List<EdmAnnotations> getAnnotationGroups();

  /**
   * @return the entity container for this schema. May be null.
   */
  EdmEntityContainer getEntityContainer();

}