package nl.buildforce.sequoia.jpa.processor.core.modify;

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAEntityType;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAPath;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAModelException;
import nl.buildforce.sequoia.jpa.processor.core.converter.JPATupleChildConverter;
import nl.buildforce.sequoia.jpa.processor.core.processor.JPARequestEntity;
import org.apache.olingo.server.api.ODataApplicationException;

import jakarta.persistence.Tuple;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class JPAMapNavigationLinkResult extends JPACreateResult {
  private final List<Tuple> result;

  public JPAMapNavigationLinkResult(final JPAEntityType targetType, final List<JPARequestEntity> entities,
      final Map<String, List<String>> requestHeaders, final JPATupleChildConverter converter)
      throws ODataJPAModelException, ODataApplicationException {

    super(targetType, requestHeaders);
    result = new ArrayList<>();

    for (JPARequestEntity entity : entities) {
      result.add(new JPAMapResult(entity.getEntityType(), entity.getData(), requestHeaders, converter).getResult(
          ROOT_RESULT_KEY)
          .get(0));
    }
  }

  @Override
  public List<Tuple> getResult(String key) {
    return result;
  }

  @Override
  public Map<String, List<Tuple>> getResults() {
    final Map<String, List<Tuple>> results = new HashMap<>(1);
    results.put(ROOT_RESULT_KEY, result);
    return results;
  }

  @Override
  protected String determineLocale(final Map<String, Object> descGetterMap, final JPAPath localeAttribute,
      final int index) {
    // Not needed for JPAMapNavigationLinkResult
    return null;
  }

  @SuppressWarnings("unchecked")
  @Override
  protected Map<String, Object> entryAsMap(final Object entry) {
    return (Map<String, Object>) entry;
  }

}