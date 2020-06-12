package nl.buildforce.sequoia.jpa.processor.core.database;

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAEntityType;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.queryoption.SearchOption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

public class TestJPA_DERBY_DatabaseProcessor extends TestJPA_XXX_DatabaseProcessor {

  @BeforeEach
  public void setup() {
    initEach();
    oneParameterResult = "SELECT * FROM TABLE (Example(?1))";
    twoParameterResult = "SELECT * FROM TABLE (Example(?1,?2))";
    countResult = "SELECT COUNT(*) FROM TABLE (Example(?1))";
    cut = new JPA_DERBY_DatabaseProcessor();
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testAbortsOnSearchRequest() {
    final CriteriaBuilder cb = mock(CriteriaBuilder.class);
    final CriteriaQuery<String> cq = mock(CriteriaQuery.class);
    final Root<String> root = mock(Root.class);
    final JPAEntityType entityType = mock(JPAEntityType.class);
    final SearchOption searchOption = mock(SearchOption.class);

    final ODataApplicationException act = assertThrows(ODataApplicationException.class,
        () -> cut.createSearchWhereClause(cb, cq, root, entityType, searchOption));
    assertEquals(HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), act.getStatusCode());

  }

}