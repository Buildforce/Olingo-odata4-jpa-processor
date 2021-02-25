/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm;

import java.util.List;

/**
 * A CSDL EntityContainer element.
 *
 * <br/>
 * EdmEntityContainer hold the information of EntitySets, Singletons, ActionImports and FunctionImports contained
 */
public interface EdmEntityContainer extends EdmNamed, EdmAnnotatable {

  /**
   * @return namespace of this entity container
   */
  String getNamespace();

  /**
   * @return full qualified name of this entity container
   */
  FullQualifiedName getFullQualifiedName();

  /**
   * Get contained Singleton by name.
   *
   * @param name name of contained Singleton
   * @return {@link EdmSingleton}
   */
  EdmSingleton getSingleton(String name);

  /**
   * Get contained EntitySet by name.
   *
   * @param name name of contained EntitySet
   * @return {@link EdmEntitySet}
   */
  EdmEntitySet getEntitySet(String name);

  /**
   * Get contained ActionImport by name.
   *
   * @param name name of contained ActionImport
   * @return {@link EdmActionImport}
   */
  EdmActionImport getActionImport(String name);

  /**
   * Get contained FunctionImport by name.
   *
   * @param name name of contained FunctionImport
   * @return {@link EdmFunctionImport}
   */
  EdmFunctionImport getFunctionImport(String name);

  /**
   * This method <b>DOES NOT</b> support lazy loading
   *
   * @return returns all entity sets for this container.
   */
  List<EdmEntitySet> getEntitySets();
  
  /**
   * This method <b>DOES NOT</b> support lazy loading
   *
   * @return returns all entity sets for this container with 
   * annotations defined in external file.
   */
  List<EdmEntitySet> getEntitySetsWithAnnotations();

  /**
   * This method <b>DOES NOT</b> support lazy loading
   *
   * @return returns all function imports for this container.
   */
  List<EdmFunctionImport> getFunctionImports();

  /**
   * This method <b>DOES NOT</b> support lazy loading
   *
   * @return returns all singletons for this container.
   */
  List<EdmSingleton> getSingletons();

  /**
   * This method <b>DOES NOT</b> support lazy loading
   *
   * @return returns all action imports for this container.
   */
  List<EdmActionImport> getActionImports();

  /**
   * @return the {@link FullQualifiedName} of the parentContainer or null if no parent is specified
   */
  FullQualifiedName getParentContainerName();
}
