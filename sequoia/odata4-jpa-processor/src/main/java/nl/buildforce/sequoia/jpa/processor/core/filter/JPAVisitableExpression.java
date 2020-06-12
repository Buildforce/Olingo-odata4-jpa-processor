package nl.buildforce.sequoia.jpa.processor.core.filter;

import org.apache.olingo.server.api.uri.UriInfoResource;
import org.apache.olingo.server.api.uri.queryoption.expression.VisitableExpression;

public interface JPAVisitableExpression extends VisitableExpression {

  UriInfoResource getMember();
}