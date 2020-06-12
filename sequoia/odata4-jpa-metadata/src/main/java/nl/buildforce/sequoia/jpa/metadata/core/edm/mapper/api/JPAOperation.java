package nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api;

import org.apache.olingo.commons.api.edm.provider.CsdlReturnType;

public interface JPAOperation {
  /**
   *

   * @return The return or result parameter of the function
   */
  JPAOperationResultParameter getResultParameter();

  CsdlReturnType getReturnType();
}