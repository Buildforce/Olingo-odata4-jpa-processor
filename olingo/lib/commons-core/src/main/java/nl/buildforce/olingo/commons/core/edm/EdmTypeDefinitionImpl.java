/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
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
//import org.apache.olingo.commons.api.edm.geo.SRID;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlTypeDefinition;
import nl.buildforce.olingo.commons.core.edm.primitivetype.EdmPrimitiveTypeFactory;

public class EdmTypeDefinitionImpl extends EdmTypeImpl implements EdmTypeDefinition {

  private CsdlTypeDefinition typeDefinition;
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