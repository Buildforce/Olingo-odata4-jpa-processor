/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
 */
package nl.buildforce.olingo.server.api.uri.queryoption.expression;

import nl.buildforce.olingo.server.api.ODataApplicationException;

/**
 * Marks an expression node a visitable by an expression visitor
 */
public interface VisitableExpression {

  /**
   * Method {@link #accept(ExpressionVisitor)} is called when traversing the expression tree. This method is invoked on
   * each expression used as node in an expression tree. The implementations should
   * behave as follows:
   * <li>Call accept on all sub nodes and store the returned Objects which are of the generic type T
   * <li>Call the appropriate method on the {@link ExpressionVisitor} instance and provide the stored return objects
   * to that instance
   * <li>Return the object which should be passed to the processing algorithm of the parent expression node
   * <br>
   * <br>
   * @param visitor
   * Visitor object (implementing {@link ExpressionVisitor}) whose methods are called during traversing a
   * expression node of the expression tree.
   * @return
   * Object of type T which should be passed to the processing algorithm of the parent expression node
   * @throws ExpressionVisitException
   * Exception occurred in the OData library while traversing the tree
   * @throws ODataApplicationException
   * Exception thrown by the application who implemented the visitor
   */
  <T> T accept(ExpressionVisitor<T> visitor) throws ExpressionVisitException, ODataApplicationException;

}