package nl.buildforce.sequoia.jpa.processor.core.modify;

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAEntityType;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAPath;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAModelException;
import nl.buildforce.sequoia.jpa.processor.core.exception.ODataJPAProcessorException;

import jakarta.persistence.Tuple;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class JPAEntityBasedResult extends JPACreateResult {

  protected List<Tuple> result;

  public JPAEntityBasedResult(JPAEntityType et, Map<String, List<String>> requestHeaders)
      throws ODataJPAModelException {
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
  protected String determineLocale(final Map<String, Object> descGetterMap, final JPAPath localeAttribute,
      final int index) throws ODataJPAProcessorException {
    Object value = descGetterMap.get(localeAttribute.getPath().get(index).getInternalName());
    if (localeAttribute.getPath().size() == index + 1 || value == null) {
      return (String) value;
    } else {
      final Map<String, Object> embeddedGetterMap = helper.buildGetterMap(value);
      return determineLocale(embeddedGetterMap, localeAttribute, index + 1);
    }
  }

  @Override
  protected Map<String, Object> entryAsMap(final Object entry) throws ODataJPAProcessorException {
    return helper.buildGetterMap(entry);
  }

}