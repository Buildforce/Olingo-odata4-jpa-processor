/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm;

/**
 * An EdmOperationImport can be an EdmActionImport or an EdmFunctionImport.
 */
public interface EdmOperationImport extends EdmNamed, EdmAnnotatable {

  /**
   * @return {@link FullQualifiedName} of this OperationImport
   */
  FullQualifiedName getFullQualifiedName();

  /**
   * @return {@link EdmEntitySet} of this OperationImport
   */
  EdmEntitySet getReturnedEntitySet();

  /**
   * @return {@link EdmEntityContainer} of this EdmOperationImport
   */
  EdmEntityContainer getEntityContainer();

}
