/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm;

/**
 * An {@link EdmEnumType} member element.
 */
public interface EdmMember extends EdmNamed, EdmAnnotatable {

  /**
   * @return value of this member as string
   */
  String getValue();
}
