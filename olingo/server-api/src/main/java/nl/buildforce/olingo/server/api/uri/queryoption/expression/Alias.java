/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri.queryoption.expression;

/**
 * Represents an alias expression node in the expression tree
 * <br>
 * A alias expression node is inserted in the expression tree for any valid alias<br>
 * E.g. $filter=name eq @alias
 */
public interface Alias extends Expression {

// --Commented out by Inspection START (''21-03-06 17:18):
//  /**
//   * @return Name of the used alias
//   */
//  String getParameterName();
// --Commented out by Inspection STOP (''21-03-06 17:18)

}