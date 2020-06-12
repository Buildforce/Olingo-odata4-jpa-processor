package nl.buildforce.sequoia.jpa.processor.core.filter;

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAPath;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.queryoption.expression.ExpressionVisitException;

import jakarta.persistence.criteria.Expression;
import java.util.List;

public interface JPAFilterCompiler {

  Expression<Boolean> compile() throws ExpressionVisitException, ODataApplicationException;

  /**
   * Returns a list of all filter elements of type Member. This could be used e.g. to determine if a join is required
   * @throws ODataApplicationException
   */
  List<JPAPath> getMember() throws ODataApplicationException;

}