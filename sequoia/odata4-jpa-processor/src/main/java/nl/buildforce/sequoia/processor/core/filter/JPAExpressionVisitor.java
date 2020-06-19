package nl.buildforce.sequoia.processor.core.filter;

import nl.buildforce.olingo.server.api.OData;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.ExpressionVisitor;

import jakarta.persistence.criteria.From;

public interface JPAExpressionVisitor extends ExpressionVisitor<JPAOperator> {

  OData getOdata();

  From<?, ?> getRoot();

}