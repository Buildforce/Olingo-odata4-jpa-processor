package nl.buildforce.sequoia.metadata.core.edm.mapper.api;

import nl.buildforce.sequoia.metadata.core.edm.mapper.exception.ODataJPAModelException;
import nl.buildforce.olingo.commons.api.edm.EdmAction;
import nl.buildforce.olingo.commons.api.edm.EdmComplexType;
import nl.buildforce.olingo.commons.api.edm.EdmEnumType;
import nl.buildforce.olingo.commons.api.edm.EdmFunction;
import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlEntityContainer;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlSchema;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlTerm;
import nl.buildforce.olingo.commons.api.edmx.EdmxReference;
import nl.buildforce.olingo.server.api.etag.CustomETagSupport;

import java.util.List;

public interface JPAServiceDocument extends CustomETagSupport {

  CsdlEntityContainer getEdmEntityContainer() throws ODataJPAModelException;

  List<CsdlSchema> getEdmSchemas() throws ODataJPAModelException;

  List<CsdlSchema> getAllSchemas() throws ODataJPAModelException;

  /**
   *

   * @param edmType
   * @return
   */
  JPAEntityType getEntity(final EdmType edmType);

  JPAEntityType getEntity(final FullQualifiedName typeName);

  JPAEntityType getEntity(final String edmEntitySetName) throws ODataJPAModelException;

  JPAFunction getFunction(final EdmFunction function);

  JPAAction getAction(final EdmAction action);

  JPAEntitySet getEntitySet(final JPAEntityType entityType) throws ODataJPAModelException;

  List<EdmxReference> getReferences();

  CsdlTerm getTerm(final FullQualifiedName termName);

  JPAStructuredType getComplexType(final EdmComplexType edmComplexType);

  JPAEnumerationAttribute getEnumType(final EdmEnumType type);

  JPAEnumerationAttribute getEnumType(final String fqnAsString);

}