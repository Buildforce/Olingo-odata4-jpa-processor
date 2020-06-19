package nl.buildforce.sequoia.processor.core.modify;

import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAEntityType;
import nl.buildforce.sequoia.metadata.core.edm.mapper.exception.ODataJPAModelException;
import nl.buildforce.sequoia.processor.core.converter.JPAExpandResult;
import nl.buildforce.sequoia.processor.core.converter.JPATupleChildConverter;
import nl.buildforce.olingo.server.api.ODataApplicationException;

import java.util.List;
import java.util.Map;

public final class JPACreateResultFactory {
  private final JPATupleChildConverter converter;

  public JPACreateResultFactory(JPATupleChildConverter converter) {
    this.converter = converter;
  }

  @SuppressWarnings("unchecked")
  public JPAExpandResult getJPACreateResult(JPAEntityType et, Object result, Map<String, List<String>> requestHeaders)
      throws ODataJPAModelException, ODataApplicationException {

    if (result instanceof Map<?, ?>)
      return new JPAMapResult(et, (Map<String, Object>) result, requestHeaders, converter);
    else
      return new JPAEntityResult(et, result, requestHeaders, converter);
  }

}