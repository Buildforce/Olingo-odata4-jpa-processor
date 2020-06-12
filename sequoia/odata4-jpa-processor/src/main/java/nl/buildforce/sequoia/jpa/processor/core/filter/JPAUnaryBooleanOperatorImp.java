package nl.buildforce.sequoia.jpa.processor.core.filter;

import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.queryoption.expression.UnaryOperatorKind;

import jakarta.persistence.criteria.Expression;

final class JPAUnaryBooleanOperatorImp implements JPAUnaryBooleanOperator {

  private final JPAOperationConverter converter;
  private final UnaryOperatorKind operator;
  private final JPAExpressionOperator left;

  public JPAUnaryBooleanOperatorImp(final JPAOperationConverter converter, final UnaryOperatorKind operator,
      final JPAExpressionOperator left) {
    this.converter = converter;
    this.operator = operator;
    this.left = left;
  }

  /*
   * (non-Javadoc)
   *

   * @see nl.buildforce.sequoia.jpa.processor.core.filter.JPAUnaryBooleanOperator#get()
   */
  @Override
  public Expression<Boolean> get() throws ODataApplicationException {
    return converter.convert(this);
  }

  /*
   * (non-Javadoc)
   *

   * @see nl.buildforce.sequoia.jpa.processor.core.filter.JPAUnaryBooleanOperator#getLeft()
   */
  @Override
  public Expression<Boolean> getLeft() throws ODataApplicationException {
    return left.get();
  }

  /*
   * (non-Javadoc)
   *

   * @see nl.buildforce.sequoia.jpa.processor.core.filter.JPAUnaryBooleanOperator#getOperator()
   */
  @Override
  public UnaryOperatorKind getOperator() {
    return operator;
  }

  @Override
  public String getName() {
    return operator.name();
  }

}