/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmException;
import nl.buildforce.olingo.commons.api.edm.EdmReturnType;
import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlReturnType;

public class EdmReturnTypeImpl implements EdmReturnType {

  private final CsdlReturnType returnType;
  private final Edm edm;
  private EdmType typeImpl;

  public EdmReturnTypeImpl(Edm edm, CsdlReturnType returnType) {
    this.edm = edm;
    this.returnType = returnType;
  }

  @Override
  public boolean isCollection() {
    return returnType.isCollection();
  }

  @Override
  public boolean isNullable() {
    return returnType.isNullable();
  }

  @Override
  public Integer getMaxLength() {
    return returnType.getMaxLength();
  }

  @Override
  public Integer getPrecision() {
    return returnType.getPrecision();
  }

  @Override
  public Integer getScale() {
    return returnType.getScale();
  }

  /*@Override
  public SRID getSrid() {
    return returnType.getSrid();
  }*/

  @Override
  public EdmType getType() {
    if (typeImpl == null) {
      if (returnType.getType() == null) {
        throw new EdmException("Return types must hava a full qualified type.");
      }
      typeImpl = new EdmTypeInfo.Builder().setEdm(edm).setTypeExpression(returnType.getType()).build().getType();
      if (typeImpl == null) {
        throw new EdmException("Cannot find type with name: " + returnType.getType());
      }
    }

    return typeImpl;
  }
}