package nl.buildforce.sequoia.jpa.processor.core.modify;

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAAssociationPath;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPACollectionAttribute;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAEntityType;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAPath;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAModelException;
import nl.buildforce.sequoia.jpa.processor.core.converter.JPACollectionResult;
import nl.buildforce.sequoia.jpa.processor.core.converter.JPATuple;
import nl.buildforce.sequoia.jpa.processor.core.converter.JPATupleChildConverter;
import nl.buildforce.sequoia.jpa.processor.core.exception.ODataJPAProcessorException;
import org.apache.olingo.server.api.ODataApplicationException;

import jakarta.persistence.Tuple;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

class JPAMapCollectionResult extends JPAMapBaseResult implements JPACollectionResult {
  private Map<String, List<Object>> converted;
  private final JPAAssociationPath path;

  public JPAMapCollectionResult(final JPAEntityType et, final Collection<?> values,
      final Map<String, List<String>> requestHeaders, final JPACollectionAttribute attribute)
      throws ODataJPAModelException, ODataJPAProcessorException {
    super(et, requestHeaders);
    this.path = attribute.asAssociation();
    result = convertToTuple(et, values, attribute);
  }

  @SuppressWarnings("unchecked")
  private List<Tuple> convertToTuple(final JPAEntityType et, final Collection<?> values,
      final JPACollectionAttribute attribute) throws ODataJPAProcessorException, ODataJPAModelException {

    final List<Tuple> tupleList = new ArrayList<>();
    for (final Object value : values) {
      final JPATuple tuple = new JPATuple();
      if (attribute.isComplex()) {
        for (JPAPath p : attribute.getStructuredType().getPathList())
          convertPathToTuple(tuple, (Map<String, Object>) value, et.getPath(this.path.getAlias()
              + JPAPath.PATH_SEPARATOR + p.getAlias()), 1);
      } else {
        tuple.addElement(path.getAlias(), attribute.getType(), value);
      }
      tupleList.add(tuple);
    }
    return tupleList;
  }

  @Override
  public void convert(final JPATupleChildConverter converter) throws ODataApplicationException {
    converted = converter.getCollectionResult(this, Collections.emptySet());
  }

  @Override
  public Collection<Object> getPropertyCollection(String key) {
    return converted.get(ROOT_RESULT_KEY);
  }

  @Override
  public JPAAssociationPath getAssociation() {
    return path;
  }

}