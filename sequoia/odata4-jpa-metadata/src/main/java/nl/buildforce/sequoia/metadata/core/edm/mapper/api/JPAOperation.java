package nl.buildforce.sequoia.metadata.core.edm.mapper.api;

import nl.buildforce.olingo.commons.api.edm.provider.CsdlReturnType;

public interface JPAOperation {
  /**
   *

   * @return The return or result parameter of the function
   */
  JPAOperationResultParameter getResultParameter();

  CsdlReturnType getReturnType();
}