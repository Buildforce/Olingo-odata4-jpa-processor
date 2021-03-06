/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri.queryoption.expression;

import java.util.List;

import nl.buildforce.olingo.commons.api.edm.EdmEnumType;
import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.server.api.ODataApplicationException;

/**
 * Generic interface to define expression visitors with arbitrary return types.
 *
 * @param <T> Return type
 */
public interface ExpressionVisitor<T> {

  /**
   * Called for each traversed {@link Binary} expression
   * @param operator Operator kind
   * @param left Application return value of left sub tree
   * @param right Application return value of right sub tree
   * @return Application return value of type T
   * @throws ODataApplicationException Thrown by the application
   */
  T visitBinaryOperator(BinaryOperatorKind operator, T left, T right)
      throws ODataApplicationException;

  /**
   * Called for each traversed {@link Unary} expression
   * @param operator Operator kind
   * @param operand return value of sub tree
   * @return Application return value of type T
   * @throws ODataApplicationException Thrown by the application
   */
  T visitUnaryOperator(UnaryOperatorKind operator, T operand)
      throws ODataApplicationException;

  /**
   * Called for each traversed {@link Method} expression
   * @param methodCall Method
   * @param parameters List of application return values created by visiting each method parameter
   * @return Application return value of type T
   * @throws ODataApplicationException Thrown by the application
   */
  T visitMethodCall(MethodKind methodCall, List<T> parameters)
      throws ODataApplicationException;

  /**
   * Called for each traversed lambda expression
   * @param lambdaFunction "ALL" or "ANY"
   * @param lambdaVariable Variable name used lambda variable
   * @param expression Lambda expression
   * @return Application return value of type T
   * @throws ODataApplicationException Thrown by the application
   */
  T visitLambdaExpression(String lambdaFunction, String lambdaVariable, Expression expression)
      throws ODataApplicationException;

  /**
   * Called for each traversed {@link Literal} expression
   * @param literal Literal
   * @return Application return value of type T
   */
  T visitLiteral(Literal literal);

  /**
   * Called for each traversed {@link Member} expression
   * @param member UriInfoResource object describing the whole path used to access an data value
   * (this includes for example the usage of $root and $it inside the URI)
   * @return Application return value of type T
   * @throws ODataApplicationException Thrown by the application
   */
  T visitMember(Member member) throws ODataApplicationException;

  /**
   * Called for each traversed {@link Alias} expression
   * @param aliasName Name of the alias
   * @return Application return value of type T
   * @throws ODataApplicationException Thrown by the application
   */
  T visitAlias(String aliasName) throws ODataApplicationException;

  /**
   * Called for each traversed {@link TypeLiteral} expression
   * @param type EdmType
   * @return Application return value of type T
   * @throws ODataApplicationException Thrown by the application
   */
  T visitTypeLiteral(EdmType type) throws ODataApplicationException;

  /**
   * Called for each traversed {@link LambdaRef}
   * @param variableName Name of the used lambda variable
   * @return Application return value of type T
   * @throws ODataApplicationException Thrown by the application
   */
  T visitLambdaReference(String variableName) throws ODataApplicationException;

  /**
   * Called for each traversed {@link Enumeration} expression
   * @param type Type used in the URI before the enumeration values
   * @param enumValues List of enumeration values
   * @return Application return value of type T
   * @throws ODataApplicationException Thrown by the application
   */
  T visitEnum(EdmEnumType type, List<String> enumValues) throws ODataApplicationException;
  
  /**
   * Called for each traversed {@link Binary} expression
   * @param operator Operator kind
   * @param left Application return value of left sub tree
   * @param right Application return value of right sub tree
   * @return Application return value of type T
   * @throws ODataApplicationException Thrown by the application
   */
  T visitBinaryOperator(BinaryOperatorKind operator, T left, List<T> right)
      throws ODataApplicationException;

}