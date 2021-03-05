/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.provider;

import java.util.List;

import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;
import nl.buildforce.olingo.commons.api.ex.ODataException;

/**
 * Dummy implementation of {@link CsdlEdmProvider}
 */
public abstract class CsdlAbstractEdmProvider implements CsdlEdmProvider {

  @Override
  public CsdlEnumType getEnumType(FullQualifiedName enumTypeName) throws ODataException {
    return null;
  }

  @Override
  public CsdlTypeDefinition getTypeDefinition(FullQualifiedName typeDefinitionName) throws ODataException {
    return null;
  }

  @Override
  public CsdlEntityType getEntityType(FullQualifiedName entityTypeName) throws ODataException {
    return null;
  }

  @Override
  public CsdlComplexType getComplexType(FullQualifiedName complexTypeName) throws ODataException {
    return null;
  }

  @Override
  public List<CsdlAction> getActions(FullQualifiedName actionName) throws ODataException {
    return null;
  }

  @Override
  public List<CsdlFunction> getFunctions(FullQualifiedName functionName) throws ODataException {
    return null;
  }

  @Override
  public CsdlTerm getTerm(FullQualifiedName termName) {
    return null;
  }

  @Override
  public CsdlEntitySet getEntitySet(FullQualifiedName entityContainer, String entitySetName)
      throws ODataException {
    return null;
  }

  @Override
  public CsdlSingleton getSingleton(FullQualifiedName entityContainer, String singletonName) {
    return null;
  }

  @Override
  public CsdlActionImport getActionImport(FullQualifiedName entityContainer, String actionImportName)
      throws ODataException {
    return null;
  }

  @Override
  public CsdlFunctionImport getFunctionImport(FullQualifiedName entityContainer, String functionImportName)
      throws ODataException {
    return null;
  }

  @Override
  public CsdlEntityContainerInfo getEntityContainerInfo(FullQualifiedName entityContainerName)
      throws ODataException {
    return null;
  }

  @Override
  public List<CsdlAliasInfo> getAliasInfos() {
    return null;
  }

  @Override
  public List<CsdlSchema> getSchemas() throws ODataException {
    return null;
  }

  @Override
  public CsdlEntityContainer getEntityContainer() throws ODataException {
    return null;
  }

  @Override
  public CsdlAnnotations getAnnotationsGroup(FullQualifiedName targetName, String qualifier) {
    return null;
  }

}