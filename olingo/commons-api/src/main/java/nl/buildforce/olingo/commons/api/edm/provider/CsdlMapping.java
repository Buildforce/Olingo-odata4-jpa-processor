/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.provider;

import nl.buildforce.olingo.commons.api.edm.EdmMapping;

/**
 * Content of this class does not appear within the CSDL metadata document. This class is used to perform server
 * internal mapping for edm primitive types to java types.
 */
public class CsdlMapping implements EdmMapping {

  private String internalName;
  private Class<?> mappedJavaClass;

  
  /**
   * Sets the internal name for this mapped object. This name won`t be used by the Olingo library but can be used by 
   * applications to access their database easier.
   * @param internalName
   * @return this for method chaining
   */
  public CsdlMapping setInternalName(String internalName){
    this.internalName = internalName;
    return this;
  }
  
  /**
   * Sets the class to be used during deserialization to transform an EDM primitive type into this java class. To see
   * which classes work for which primitive type refer to {@link nl.buildforce.olingo.commons.api.edm.EdmPrimitiveType}.
   * @param mappedJavaClass class to which is mapped
   * @return this for method chaining
   */
  public CsdlMapping setMappedJavaClass(Class<?> mappedJavaClass) {
    this.mappedJavaClass = mappedJavaClass;
    return this;
  }

  /**
   * @see EdmMapping#getMappedJavaClass()
   */
  @Override
  public Class<?> getMappedJavaClass() {
    return mappedJavaClass;
  }

  /**
   * @see EdmMapping#getInternalName()
   */
  @Override
  public String getInternalName() {
    return internalName;
  }

}