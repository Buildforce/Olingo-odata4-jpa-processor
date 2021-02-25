/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm;

/**
 * EdmMapping holds custom mapping information which can be applied to a CSDL element.
 */
public interface EdmMapping {

  /**
   * Returns the internal name for this mapped object. This name won`t be used by the Olingo library but can be used by 
   * applications to access their database easier.
   * @return the internal name of this mapped object
   */
  String getInternalName();
  
  /**
   * The class which is returned here will be used to during deserialization to replace the default java class for a
   * primitive type.
   * @return class used during deserialization
   */
  Class<?> getMappedJavaClass();
}
