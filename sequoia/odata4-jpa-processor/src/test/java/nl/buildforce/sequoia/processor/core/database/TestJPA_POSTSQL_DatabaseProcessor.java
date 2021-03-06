package nl.buildforce.sequoia.processor.core.database;

import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAEntityType;
import nl.buildforce.olingo.commons.api.http.HttpStatusCode;
import nl.buildforce.olingo.server.api.ODataApplicationException;
import nl.buildforce.olingo.server.api.uri.queryoption.SearchOption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

public class TestJPA_POSTSQL_DatabaseProcessor extends TestJPA_XXX_DatabaseProcessor {
  @BeforeEach
  public void setup() {
    initEach();
    oneParameterResult = "SELECT * FROM Example(?1)";
    twoParameterResult = "SELECT * FROM Example(?1,?2)";
    countResult = "SELECT COUNT(*) FROM Example(?1)";
    cut = new JPA_POSTSQL_DatabaseProcessor();
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
        () -> cut.createSearchWhereClause());
    assertEquals(HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), act.getStatusCode());

  }

}