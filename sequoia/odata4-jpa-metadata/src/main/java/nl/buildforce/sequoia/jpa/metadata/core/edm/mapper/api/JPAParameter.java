package nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api;

public interface JPAParameter extends JPAParameterFacet {

  /**
   * Name of the parameter at the UDF or the java method
   * @return
   */
  String getInternalName();

  /**
   * Externally used name
   * @return
   */
  String getName();

}