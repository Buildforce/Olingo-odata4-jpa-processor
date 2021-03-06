package nl.buildforce.sequoia.metadata.core.edm.mapper.api;

import nl.buildforce.sequoia.metadata.core.edm.mapper.exception.ODataJPAModelException;

//TODO remove extension
public interface JPAAssociationAttribute extends JPAAttribute {

  JPAStructuredType getTargetEntity() throws ODataJPAModelException;

  JPAAssociationAttribute getPartner();

  JPAAssociationPath getPath() throws ODataJPAModelException;

}