/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm;

/**
 * A CSDL ComplexType element.
 * <br/>
 * EdmComplexType holds a set of related information like {@link EdmPrimitiveType} properties and EdmComplexType
 * properties.
 */
public interface EdmComplexType extends EdmStructuredType {

  @Override
  EdmComplexType getBaseType();
}
