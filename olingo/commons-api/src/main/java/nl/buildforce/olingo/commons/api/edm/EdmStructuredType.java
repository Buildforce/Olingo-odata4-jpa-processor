/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm;

import java.util.List;

/**
 * EdmStructuralType is the base for a complex type or an entity type.
 *
 * Complex types and entity types are described in the Conceptual Schema Definition of the OData protocol.
 */
public interface EdmStructuredType extends EdmType, EdmAnnotatable {

  /**
   * Get property by name
   *
   * @param name name of property
   * @return simple, complex or navigation property as {@link EdmTyped}
   */
  EdmElement getProperty(String name);

  /**
   * Get all simple and complex property names.
   *
   * @return property names as type List&lt;String&gt;
   */
  List<String> getPropertyNames();

  /**
   * Get structural property by name.
   *
   * @param name name of structural property
   * @return simple or complex property as {@link EdmTyped}
   */
  EdmProperty getStructuralProperty(String name);

  /**
   * Get navigation property by name.
   *
   * @param name name of navigation property
   * @return navigation property as {@link EdmTyped}
   */
  EdmNavigationProperty getNavigationProperty(String name);

  /**
   * Get all navigation property names.
   *
   * @return navigation property names as type List&lt;String&gt;
   */
  List<String> getNavigationPropertyNames();

  /**
   * Base types are described in the OData protocol specification.
   *
   * @return {@link EdmStructuredType}
   */
  EdmStructuredType getBaseType();

  /**
   * Checks if this type is convertible to parameter {@code targetType}
   *
   * @param targetType target type for which compatibility is checked
   * @return true if this type is compatible to the testType (i.e., this type is a subtype of targetType)
   */
  boolean compatibleTo(EdmType targetType);

  /**
   * Indicates if the structured type is an open type.
   *
   * @return <code>true</code> if the structured type is open
   */
  boolean isOpenType();

  /**
   * Indicates if the structured type is abstract.
   *
   * @return <code>true</code> if the structured type is abstract
   */
  boolean isAbstract();
}
