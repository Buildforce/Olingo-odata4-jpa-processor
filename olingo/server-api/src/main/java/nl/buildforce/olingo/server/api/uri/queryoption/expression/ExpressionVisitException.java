/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri.queryoption.expression;

import nl.buildforce.olingo.commons.api.ex.ODataException;

/**
 * Exception class used by the {@link ExpressionVisitor} to throw exceptions while traversing the expression tree
 */
public class ExpressionVisitException extends ODataException {

  //   private static final long serialVersionUID = 1L;

  public ExpressionVisitException(String msg) {
    super(msg);
  }

  public ExpressionVisitException(String msg, Throwable cause) {
    super(msg, cause);
  }

  public ExpressionVisitException(Throwable cause) {
    super(cause);
  }

}