/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri;

/**
 * Used to describe an lambda variable used within an resource path
 * For example: http://.../serviceroot/entityset/listofstring/any(d: 'string' eq d)
 */
public interface UriResourceLambdaVariable extends UriResourcePartTyped {

  /**
   * @return Name of the lambda variable
   */
  String getVariableName();
}
