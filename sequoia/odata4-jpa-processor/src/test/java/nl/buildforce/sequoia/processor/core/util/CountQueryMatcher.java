package nl.buildforce.sequoia.processor.core.util;

import nl.buildforce.sequoia.processor.core.query.JPACountQuery;
import nl.buildforce.olingo.server.api.ODataApplicationException;
import org.mockito.ArgumentMatcher;

public class CountQueryMatcher implements ArgumentMatcher<JPACountQuery> {

  private final long extCountResult;
  private boolean executed;

  public CountQueryMatcher(long exp) {
    extCountResult = exp;
  }

  @Override
  public boolean matches(JPACountQuery query) {
    if (query != null) {
      if (extCountResult != 0 && !executed) {
        try {
          executed = true; // Query can be used only once but matcher is called twice
          return extCountResult == query.countResults();
        } catch (ODataApplicationException e) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

}