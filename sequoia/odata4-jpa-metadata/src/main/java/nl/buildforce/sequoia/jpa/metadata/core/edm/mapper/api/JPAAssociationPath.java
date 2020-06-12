package nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api;

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAModelException;

import java.util.List;

public interface JPAAssociationPath {

  String PATH_SEPARATOR = "/";

  String getAlias();

  List<JPAOnConditionItem> getJoinColumnsList() throws ODataJPAModelException;

  List<JPAPath> getLeftColumnsList() throws ODataJPAModelException;

  List<JPAPath> getRightColumnsList() throws ODataJPAModelException;

  JPAAssociationAttribute getLeaf();

  List<JPAElement> getPath();

  JPAStructuredType getTargetType();

  JPAStructuredType getSourceType();

  boolean isCollection();

  JPAAssociationAttribute getPartner();

  JPAJoinTable getJoinTable();

  /**
   * Only available if a Join Table was used
   * @return
   * @throws ODataJPAModelException
   */
  List<JPAPath> getInverseLeftJoinColumnsList() throws ODataJPAModelException;
}