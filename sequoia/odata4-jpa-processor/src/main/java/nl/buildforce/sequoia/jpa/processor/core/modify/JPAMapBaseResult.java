package nl.buildforce.sequoia.jpa.processor.core.modify;

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAEntityType;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAPath;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAModelException;

import jakarta.persistence.Tuple;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class JPAMapBaseResult extends JPACreateResult {

  protected Map<String, Object> valuePairedResult;
  protected List<Tuple> result;

  public JPAMapBaseResult(JPAEntityType et, Map<String, List<String>> requestHeaders) throws ODataJPAModelException {
    super(et, requestHeaders);
  }

  @Override
  public List<Tuple> getResult(final String key) {
    return result;
  }

  @Override
  public Map<String, List<Tuple>> getResults() {
    final Map<String, List<Tuple>> results = new HashMap<>(1);
    results.put(ROOT_RESULT_KEY, result);
    return results;
  }

  @Override
  @SuppressWarnings("unchecked")
  protected String determineLocale(final Map<String, Object> descGetterMap, final JPAPath localeAttribute,
      final int index) {

    final Object value = descGetterMap.get(localeAttribute.getPath().get(index).getInternalName());
    if (localeAttribute.getPath().size() == index + 1 || value == null) {
      return (String) value;
    } else {
      return determineLocale((Map<String, Object>) value, localeAttribute, index + 1);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  protected Map<String, Object> entryAsMap(final Object entry) {
    return (Map<String, Object>) entry;
  }

}