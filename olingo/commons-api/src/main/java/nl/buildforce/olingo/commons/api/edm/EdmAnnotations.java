/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm;


/**
 * This is used to apply a group of annotations to a single model element.
 */
public interface EdmAnnotations extends EdmAnnotatable{

  /**
   * @return a string allowing annotation authors a means of conditionally applying an annotation
   */
  String getQualifier();
  
  /**
   * Will return the full path to the target 
   * e.g. MySchema.MyEntityContainer/MyEntitySet/MySchema.MyEntityType/MyProperty
   * @return the path to the target
   */
  String getTargetPath();
}
