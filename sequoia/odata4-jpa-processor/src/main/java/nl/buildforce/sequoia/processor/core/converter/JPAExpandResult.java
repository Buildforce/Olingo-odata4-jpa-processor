package nl.buildforce.sequoia.processor.core.converter;

import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAAssociationPath;
import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAEntityType;
import nl.buildforce.olingo.server.api.ODataApplicationException;

import jakarta.persistence.Tuple;
import java.util.List;
import java.util.Map;

public interface JPAExpandResult { // NOSONAR

  String ROOT_RESULT_KEY = "root";

  JPAExpandResult getChild(final JPAAssociationPath associationPath);

  Map<JPAAssociationPath, JPAExpandResult> getChildren();

  Long getCount(final String string);

  JPAEntityType getEntityType();

  List<Tuple> getResult(final String key);

  Map<String, List<Tuple>> getResults();

  boolean hasCount();

  void convert(final JPATupleChildConverter converter) throws ODataApplicationException;

}