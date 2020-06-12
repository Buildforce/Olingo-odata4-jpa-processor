package nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api;

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAModelException;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
// import org.apache.olingo.commons.api.edm.geo.SRID;

public interface JPAParameterFacet {

  Integer getMaxLength();

  Integer getPrecision();

  Integer getScale();

  //SRID getSrid();

  Class<?> getType();

  FullQualifiedName getTypeFQN() throws ODataJPAModelException;
}