/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri.queryoption;

import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.server.api.uri.UriInfoResource;

/**
 * Represents a single select item information
 * For example: http://.../Employees?select=name,age
 */
public interface SelectItem {

  /**
   * @return A star is used as select item
   */
  boolean isStar();

  /*
   * @return Namespace and star is used as select item in order to select operations
   */
  //boolean isAllOperationsInSchema();

  /*
   * @return Namespace when a star is used in combination with an namespace
   */
  //FullQualifiedName getAllOperationsInSchemaNameSpace();

  /**
   * @return A {@link UriInfoResource} object containing the resource path segments to be selected
   */
  UriInfoResource getResourcePath();

  /**
   * @return Before resource path segments which should be selected a type filter may be used.
   * For example: ...Suppliers?$select=Namespace.PreferredSupplier/AccountRepresentative
   */
  EdmType getStartTypeFilter();

}