/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm.annotation;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmException;
import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmExpression;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmIsOf;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlIsOf;
import nl.buildforce.olingo.commons.core.edm.EdmTypeInfo;

public class EdmIsOfImpl extends AbstractEdmAnnotatableDynamicExpression implements EdmIsOf {

  private final Edm edm;
  private final CsdlIsOf isOf;
  private EdmExpression value;
  private EdmType type;

  public EdmIsOfImpl(Edm edm, CsdlIsOf isOf) {
    super(edm, "IsOf", isOf);
    this.edm = edm;
    this.isOf = isOf;
  }

  @Override
  public Integer getMaxLength() {
    return isOf.getMaxLength();
  }

  @Override
  public Integer getPrecision() {
    return isOf.getPrecision();
  }

  @Override
  public Integer getScale() {
    return isOf.getScale();
  }

  /*@Override
  public SRID getSrid() {
    return isOf.getSrid();
  }*/

  @Override
  public EdmType getType() {
    if (type == null) {
      if(isOf.getType() == null){
        throw new EdmException("Must specify a type for an IsOf expression.");
      }
      EdmTypeInfo typeInfo = new EdmTypeInfo.Builder().setEdm(edm).setTypeExpression(isOf.getType()).build();
      type = typeInfo.getType();
    }
    return type;
  }

  @Override
  public EdmExpression getValue() {
    if(value == null){
      if(isOf.getValue() == null){
        throw new EdmException("IsOf expressions require an expression value.");
      }
      value = getExpression(edm, isOf.getValue());
    }
    return value;
  }

  @Override
  public EdmExpressionType getExpressionType() {
    return EdmExpressionType.IsOf;
  }
}