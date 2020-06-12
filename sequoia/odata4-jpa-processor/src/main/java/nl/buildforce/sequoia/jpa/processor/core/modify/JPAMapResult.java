package nl.buildforce.sequoia.jpa.processor.core.modify;

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.*;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAModelException;
import nl.buildforce.sequoia.jpa.processor.core.converter.JPAExpandResult;
import nl.buildforce.sequoia.jpa.processor.core.converter.JPATuple;
import nl.buildforce.sequoia.jpa.processor.core.converter.JPATupleChildConverter;
import nl.buildforce.sequoia.jpa.processor.core.exception.ODataJPAProcessorException;
import nl.buildforce.sequoia.jpa.processor.core.processor.JPARequestEntity;
import org.apache.olingo.server.api.ODataApplicationException;

import jakarta.persistence.Tuple;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

final class JPAMapResult extends JPAMapBaseResult {
  JPAMapResult(final JPAEntityType et, final Map<String, Object> jpaEntity,
      final Map<String, List<String>> requestHeaders, final JPATupleChildConverter converter)
      throws ODataJPAModelException, ODataApplicationException {

    super(et, requestHeaders);

    this.valuePairedResult = jpaEntity;
    this.result = createResult();
    createChildren(converter);
  }

  @SuppressWarnings("unchecked")
  private void createChildren(final JPATupleChildConverter converter) throws ODataJPAModelException,
      ODataApplicationException {
    for (JPAAssociationPath path : et.getAssociationPathList()) {
      String pathPropertyName = path.getPath().get(0).getInternalName();
      if (valuePairedResult.get(pathPropertyName) instanceof List) {
        children.put(path,
            new JPAMapNavigationLinkResult((JPAEntityType) path.getTargetType(),
                (List<JPARequestEntity>) valuePairedResult.get(
                    pathPropertyName), requestHeaders, converter));
      }
    }
    for (final JPAPath path : et.getCollectionAttributesPath()) {
      Map<String, Object> attributes = valuePairedResult;
      for (JPAElement e : path.getPath()) {
        final Object value = attributes.get(e.getInternalName());
        if (e instanceof JPAAttribute && ((JPAAttribute) e).isComplex() && !(((JPAAttribute) e).isCollection())
            && value != null) {
          attributes = (Map<String, Object>) value;
          continue;
        }
        if (e instanceof JPACollectionAttribute && value != null) {
          final JPAAssociationPath assPath = ((JPACollectionAttribute) e).asAssociation();
          final JPAExpandResult child = new JPAMapCollectionResult(et, (Collection<?>) value, requestHeaders,
              (JPACollectionAttribute) e);
          child.convert(converter);
          children.put(assPath, child);
        }
      }
    }

  }

  private List<Tuple> createResult() throws ODataJPAProcessorException {
    JPATuple tuple = new JPATuple();
    List<Tuple> tupleResult = new ArrayList<>();

    for (JPAPath path : pathList) {
      if (notContainsCollection(path))
        convertPathToTuple(tuple, valuePairedResult, path, 0);
    }
    tupleResult.add(tuple);
    return tupleResult;
  }

}