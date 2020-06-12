package nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api;

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAModelException;

import java.util.List;

public interface JPAJoinTable {

  String getTableName();

  String getAlias(String dbFieldName);

  String getInverseAlias(String dbFieldName);

  JPAEntityType getEntityType();

  List<JPAOnConditionItem> getJoinColumns() throws ODataJPAModelException;

  /**
   * Returns the list of inverse join columns with exchanged left/right order.
   * @return
   * @throws ODataJPAModelException
   */
  List<JPAOnConditionItem> getInversJoinColumns() throws ODataJPAModelException;

}