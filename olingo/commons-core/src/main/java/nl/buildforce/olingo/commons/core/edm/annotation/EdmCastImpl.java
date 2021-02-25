/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm.annotation;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmException;
import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmCast;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmExpression;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlCast;
import nl.buildforce.olingo.commons.core.edm.EdmTypeInfo;

public class EdmCastImpl extends AbstractEdmAnnotatableDynamicExpression implements EdmCast {

  private final CsdlCast cast;
  private EdmExpression value;
  private EdmType type;

  public EdmCastImpl(Edm edm, CsdlCast csdlExp) {
    super(edm, "Cast", csdlExp);
      cast = csdlExp;
  }

  @Override
  public Integer getMaxLength() {
    return cast.getMaxLength();
  }

  @Override
  public Integer getPrecision() {
    return cast.getPrecision();
  }

  @Override
  public Integer getScale() {
    return cast.getScale();
  }

  /*@Override
  public SRID getSrid() {
    return cast.getSrid();
  }*/

  @Override
  public EdmType getType() {
    if (type == null) {
      if (cast.getType() == null) {
        throw new EdmException("Must specify a type for a Cast expression.");
      }
      EdmTypeInfo typeInfo = new EdmTypeInfo.Builder().setEdm(edm).setTypeExpression(cast.getType()).build();
      type = typeInfo.getType();
    }
    return type;
  }

  @Override
  public EdmExpression getValue() {
    if (value == null) {
      if (cast.getValue() == null) {
        throw new EdmException("Cast expressions require an expression value.");
      }
      value = getExpression(edm, cast.getValue());
    }
    return value;
  }

  @Override
  public EdmExpressionType getExpressionType() {
    return EdmExpressionType.Cast;
  }
}