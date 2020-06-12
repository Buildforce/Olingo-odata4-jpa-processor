package nl.buildforce.sequoia.jpa.processor.core.database;

import nl.buildforce.sequoia.jpa.processor.core.exception.ODataJPADBAdaptorException;
import nl.buildforce.sequoia.jpa.processor.core.exception.ODataJPAFilterException;
import nl.buildforce.sequoia.jpa.processor.core.filter.JPAAggregationOperation;
import nl.buildforce.sequoia.jpa.processor.core.filter.JPAArithmeticOperator;
import nl.buildforce.sequoia.jpa.processor.core.filter.JPABooleanOperator;
import nl.buildforce.sequoia.jpa.processor.core.filter.JPAComparisonOperator;
import nl.buildforce.sequoia.jpa.processor.core.filter.JPAEnumerationBasedOperator;
import nl.buildforce.sequoia.jpa.processor.core.filter.JPAMethodCall;
import nl.buildforce.sequoia.jpa.processor.core.filter.JPAUnaryBooleanOperator;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.queryoption.expression.BinaryOperatorKind;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestJPADefaultDatabaseProcessor extends TestJPA_XXX_DatabaseProcessor {

  @BeforeEach
  public void setup() {
    initEach();
    oneParameterResult = "SELECT * FROM Example(?1)";
    twoParameterResult = "SELECT * FROM Example(?1,?2)";
    countResult = "SELECT COUNT(*) FROM Example(?1)";
    cut = new JPADefaultDatabaseProcessor();
  }

  @Test
  public void testNotSupportedConvertBooleanOperator() {
    final JPABooleanOperator operator = mock(JPABooleanOperator.class);
    assertThrows(ODataJPAFilterException.class, () -> ((JPAODataDatabaseOperations) cut).convert(operator));
  }

  @Test
  public void testNotSupportedConvertAggregationOperator() {
    final JPAAggregationOperation operator = mock(JPAAggregationOperation.class);
    assertThrows(ODataJPAFilterException.class, () -> ((JPAODataDatabaseOperations) cut).convert(operator));
  }

  @Test
  public void testNotSupportedConvertArithmeticOperator() {
    final JPAArithmeticOperator operator = mock(JPAArithmeticOperator.class);
    assertThrows(ODataJPAFilterException.class, () -> ((JPAODataDatabaseOperations) cut).convert(operator));
  }

  @Test
  public void testNotSupportedConvertMethodCall() {
    final JPAMethodCall operator = mock(JPAMethodCall.class);
    assertThrows(ODataJPAFilterException.class, () -> ((JPAODataDatabaseOperations) cut).convert(operator));
  }

  @Test
  public void testNotSupportedConvertUnaryBooleanOperator() {
    final JPAUnaryBooleanOperator operator = mock(JPAUnaryBooleanOperator.class);
    assertThrows(ODataJPAFilterException.class, () -> ((JPAODataDatabaseOperations) cut).convert(operator));
  }

  @Test
  public void testNotSupportedConvertComparisonOperatorOthersThenHAS() {
    @SuppressWarnings("unchecked")
    final JPAComparisonOperator<String> operator = mock(JPAComparisonOperator.class);
    when(operator.getOperator()).then((Answer<BinaryOperatorKind>) invocation -> BinaryOperatorKind.SUB);
    assertThrows(ODataJPAFilterException.class, () -> ((JPAODataDatabaseOperations) cut).convert(operator));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testSupportedConvertComparisonOperatorOperatorHAS() throws ODataApplicationException {
    final CriteriaBuilder cb = mock(CriteriaBuilder.class);
    Expression<Integer> cbResult = mock(Expression.class);
    Predicate cbPredicate = mock(Predicate.class);
    final JPAComparisonOperator<Long> operator = mock(JPAComparisonOperator.class);
    final Expression<Long> left = mock(Expression.class);
    final JPAEnumerationBasedOperator right = mock(JPAEnumerationBasedOperator.class);

    when(operator.getOperator()).then((Answer<BinaryOperatorKind>) invocation -> BinaryOperatorKind.HAS);
    when(operator.getRight()).thenReturn(right);
    when(right.getValue()).thenReturn(5L);
    when(operator.getLeft()).thenReturn(left);

    when(cb.quot(left, 5L)).thenAnswer((Answer<Expression<Integer>>) invocation -> cbResult);
    when(cb.mod(cbResult, 2)).thenReturn(cbResult);
    when(cb.equal(cbResult, 1)).thenReturn(cbPredicate);
    ((JPAODataDatabaseOperations) cut).setCriterialBuilder(cb);
    final Expression<Boolean> act = ((JPAODataDatabaseOperations) cut).convert(operator);
    assertNotNull(act);
  }

  @Test
  public void testNotSupportedSearch() {
    assertThrows(ODataJPADBAdaptorException.class, () -> cut.createSearchWhereClause(null, null, null, null, null));
  }

}