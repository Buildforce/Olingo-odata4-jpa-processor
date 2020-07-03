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
package nl.buildforce.olingo.commons.core.edm.primitivetype;

import java.math.BigInteger;

import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeException;

/**
 * Implementation of the EDM primitive type Byte.
 */
public final class EdmByte extends SingletonPrimitiveType {

  private static final EdmByte INSTANCE = new EdmByte();

  public static EdmByte getInstance() {
    return INSTANCE;
  }

  @Override
  public Class<?> getDefaultType() {
    return Short.class;
  }

  @Override
  protected <T> T internalValueOfString(String value,
                                        Boolean isNullable, Integer maxLength, Integer precision,
                                        Integer scale, Boolean isUnicode, Class<T> returnType) throws EdmPrimitiveTypeException {

    Short valueShort;
    try {
      valueShort = Short.parseShort(value);
    } catch (NumberFormatException e) {
      throw new EdmPrimitiveTypeException("The literal '" + value + "' has illegal content.", e);
    }
    if (valueShort < 0 || valueShort >= 1 << Byte.SIZE) {
      throw new EdmPrimitiveTypeException("The literal '" + value + "' has illegal content.");
    }

    try {
      return EdmInt64.convertNumber(valueShort, returnType);
    } catch (IllegalArgumentException e) {
      throw new EdmPrimitiveTypeException("The literal '" + value
          + "' cannot be converted to value type " + returnType + ".", e);
    } catch (ClassCastException e) {
      throw new EdmPrimitiveTypeException("The value type " + returnType + " is not supported.", e);
    }
  }

  @Override
  protected <T> String internalValueToString(T value,
                                             Boolean isNullable, Integer maxLength, Integer precision,
                                             Integer scale, Boolean isUnicode) throws EdmPrimitiveTypeException {

    if (value instanceof Byte || value instanceof Short || value instanceof Integer || value instanceof Long) {
      if (((Number) value).longValue() >= 0 && ((Number) value).longValue() < 1 << Byte.SIZE) {
        return value.toString();
      } else {
        throw new EdmPrimitiveTypeException("The value '" + value + "' is not valid.");
      }
    } else if (value instanceof BigInteger) {
      if (((BigInteger) value).compareTo(BigInteger.ZERO) >= 0
          && ((BigInteger) value).compareTo(BigInteger.valueOf(1 << Byte.SIZE)) < 0) {
        return value.toString();
      } else {
        throw new EdmPrimitiveTypeException("The value '" + value + "' is not valid.");
      }
    } else {
      throw new EdmPrimitiveTypeException("The value type " + value.getClass() + " is not supported.");
    }
  }
}