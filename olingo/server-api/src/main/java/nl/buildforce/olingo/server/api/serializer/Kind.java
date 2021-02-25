/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.serializer;

/**
 * The different types of edm kinds that form the metadata
 * 
 */
public enum Kind {
  /** EntityType */
  EntityType,
  /** ComplexType */
  ComplexType,
  /** Function Import */
  FunctionImport,
  /** Action Import */
  ActionImport,
  /** Term */
  Term,
  /** Navigation Property */
  NavigationProperty,
  /** Enum Type **/
  EnumType,
  /** Singleton **/
  Singleton,
  /** Extending **/
  Extending,
  /** Entity Container **/
  EntityContainer,
  /** Entity Set **/
  EntitySet,
  /** Function **/
  Function,
  /** Action **/
  Action
}
