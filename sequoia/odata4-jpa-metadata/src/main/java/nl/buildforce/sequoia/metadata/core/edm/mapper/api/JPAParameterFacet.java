package nl.buildforce.sequoia.metadata.core.edm.mapper.api;

import nl.buildforce.sequoia.metadata.core.edm.mapper.exception.ODataJPAModelException;
import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;

public interface JPAParameterFacet {

  Integer getMaxLength();

  Integer getPrecision();

  Integer getScale();

  //SRID getSrid();

  Class<?> getType();

  FullQualifiedName getTypeFQN() throws ODataJPAModelException;

}