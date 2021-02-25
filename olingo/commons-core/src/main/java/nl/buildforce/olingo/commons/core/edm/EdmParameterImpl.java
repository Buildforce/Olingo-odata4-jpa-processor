/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmException;
import nl.buildforce.olingo.commons.api.edm.EdmMapping;
import nl.buildforce.olingo.commons.api.edm.EdmParameter;
import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlParameter;

public class EdmParameterImpl extends AbstractEdmNamed implements EdmParameter {

  private final CsdlParameter parameter;
  private EdmType typeImpl;

  public EdmParameterImpl(Edm edm, CsdlParameter parameter) {
    super(edm, parameter.getName(), parameter);
    this.parameter = parameter;
  }

  @Override
  public boolean isCollection() {
    return parameter.isCollection();
  }

  @Override
  public EdmMapping getMapping() {
    return parameter.getMapping();
  }

  @Override
  public boolean isNullable() {
    return parameter.isNullable();
  }

  @Override
  public Integer getMaxLength() {
    return parameter.getMaxLength();
  }

  @Override
  public Integer getPrecision() {
    return parameter.getPrecision();
  }

  @Override
  public Integer getScale() {
    return parameter.getScale();
  }

  /*@Override
  public SRID getSrid() {
    return parameter.getSrid();
  }*/

  @Override
  public EdmType getType() {
    if (typeImpl == null) {
      if (parameter.getType() == null) {
        throw new EdmException("Parameter " + parameter.getName() + " must hava a full qualified type.");
      }
      typeImpl = new EdmTypeInfo.Builder().setEdm(edm).setTypeExpression(parameter.getType()).build().getType();
      if (typeImpl == null) {
        throw new EdmException("Cannot find type with name: " + parameter.getTypeFQN());
      }
    }

    return typeImpl;
  }
}