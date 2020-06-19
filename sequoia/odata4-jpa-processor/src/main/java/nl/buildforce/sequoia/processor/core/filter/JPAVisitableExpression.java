package nl.buildforce.sequoia.processor.core.filter;

import nl.buildforce.olingo.server.api.uri.UriInfoResource;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.VisitableExpression;

public interface JPAVisitableExpression extends VisitableExpression {

  UriInfoResource getMember();
}