/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri.queryoption.expression;

/**
 * Used to within a lambda expression tree to define an access to the lambda variable
 */
public interface LambdaRef extends Expression {

  /**
   * @return Name of the lambda variable
   */
  String getVariableName();

}