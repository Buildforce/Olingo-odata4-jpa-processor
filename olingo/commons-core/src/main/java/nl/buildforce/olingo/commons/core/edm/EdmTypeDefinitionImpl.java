/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmException;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveType;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeException;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import nl.buildforce.olingo.commons.api.edm.EdmTypeDefinition;
import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;
import nl.buildforce.olingo.commons.api.edm.constants.EdmTypeKind;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlTypeDefinition;
import nl.buildforce.olingo.commons.core.edm.primitivetype.EdmPrimitiveTypeFactory;

public class EdmTypeDefinitionImpl extends EdmTypeImpl implements EdmTypeDefinition {

  private final CsdlTypeDefinition typeDefinition;
  private EdmPrimitiveType edmPrimitiveTypeInstance;

  public EdmTypeDefinitionImpl(Edm edm, FullQualifiedName typeDefinitionName,
                               CsdlTypeDefinition typeDefinition) {
    super(edm, typeDefinitionName, EdmTypeKind.DEFINITION, typeDefinition);
    this.typeDefinition = typeDefinition;
  }

  @Override
  public EdmPrimitiveType getUnderlyingType() {
    if (edmPrimitiveTypeInstance == null) {
      try {
        if (typeDefinition.getUnderlyingType() == null) {
          throw new EdmException("Underlying Type for type definition: "
              + typeName.getFullQualifiedNameAsString() + " must not be null.");
        }
        edmPrimitiveTypeInstance = EdmPrimitiveTypeFactory.getInstance(
            EdmPrimitiveTypeKind.valueOfFQN(typeDefinition.getUnderlyingType()));
      } catch (IllegalArgumentException e) {
        throw new EdmException("Invalid underlying type: " + typeDefinition.getUnderlyingType(), e);
      }
    }
    return edmPrimitiveTypeInstance;
  }

  @Override
  public Integer getMaxLength() {
    return typeDefinition.getMaxLength();
  }

  @Override
  public Integer getPrecision() {
    return typeDefinition.getPrecision();
  }

  @Override
  public Integer getScale() {
    return typeDefinition.getScale();
  }

  /*@Override
  public SRID getSrid() {
    return typeDefinition.getSrid();
  }*/

  @Override
  public Boolean isUnicode() {
    return typeDefinition.isUnicode();
  }

  @Override
  public boolean isCompatible(EdmPrimitiveType primitiveType) {
    return this == primitiveType || getUnderlyingType().isCompatible(primitiveType);
  }

  @Override
  public Class<?> getDefaultType() {
    return getUnderlyingType().getDefaultType();
  }

  @Override
  public boolean validate(String value, Boolean isNullable, Integer maxLength,
                          Integer precision, Integer scale, Boolean isUnicode) {
    return getUnderlyingType().validate(value, isNullable,
        maxLength == null ? getMaxLength() : maxLength,
        precision == null ? getPrecision() : precision,
        scale == null ? getScale() : scale,
        isUnicode == null ? isUnicode() : isUnicode);
  }

  @Override
  public <T> T valueOfString(String value, Boolean isNullable, Integer maxLength,
                             Integer precision, Integer scale, Boolean isUnicode, Class<T> returnType)
      throws EdmPrimitiveTypeException {
    return getUnderlyingType().valueOfString(value, isNullable,
        maxLength == null ? getMaxLength() : maxLength,
        precision == null ? getPrecision() : precision,
        scale == null ? getScale() : scale,
        isUnicode == null ? isUnicode() : isUnicode,
        returnType);
  }

  @Override
  public String valueToString(Object value, Boolean isNullable, Integer maxLength,
                              Integer precision, Integer scale, Boolean isUnicode) throws EdmPrimitiveTypeException {
    return getUnderlyingType().valueToString(value, isNullable,
        maxLength == null ? getMaxLength() : maxLength,
        precision == null ? getPrecision() : precision,
        scale == null ? getScale() : scale,
        isUnicode == null ? isUnicode() : isUnicode);
  }

  @Override
  public String toUriLiteral(String literal) {
    return getUnderlyingType().toUriLiteral(literal);
  }

  @Override
  public String fromUriLiteral(String literal) throws EdmPrimitiveTypeException {
    return getUnderlyingType().fromUriLiteral(literal);
  }
}