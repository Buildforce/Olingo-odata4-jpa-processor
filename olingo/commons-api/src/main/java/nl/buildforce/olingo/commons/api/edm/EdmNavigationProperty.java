/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm;

import java.util.List;

/**
 * A CSDL NavigationProperty element
 * <br/>
 * EdmNavigationProperty allows navigation from one entity type to another via a relationship.
 */
public interface EdmNavigationProperty extends EdmElement, EdmAnnotatable {

  @Override
  EdmEntityType getType();

  /**
   * @return true if nullable or not specified
   */
  boolean isNullable();

  /**
   * @return true if containsTarget
   */
  boolean containsTarget();

  /**
   * @return the partner navigation property
   */
  EdmNavigationProperty getPartner();

  /**
   * Get property name for referenced property
   *
   * @param referencedPropertyName name of referenced property
   * @return propertyName for this referenced property
   */
  String getReferencingPropertyName(String referencedPropertyName);

  /**
   * @return all referential constraints for this navigation property.
   */
  List<EdmReferentialConstraint> getReferentialConstraints();
  
  EdmOnDelete getOnDelete();

}
