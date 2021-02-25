/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm.primitivetype;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeException;

public class EdmDuration extends SingletonPrimitiveType {

  private static final Pattern PATTERN = Pattern.compile(
      "[-+]?P(?:(\\p{Digit}+)D)?(?:T(?:(\\p{Digit}+)H)?(?:(\\p{Digit}+)M)?"
          + "(?:(\\p{Digit}+(?:\\.\\p{Digit}+?0*)?)S)?)?");

  private static final EdmDuration INSTANCE = new EdmDuration();

  {
    uriPrefix = "duration'";
    uriSuffix = "'";
  }

  public static EdmDuration getInstance() {
    return INSTANCE;
  }

  @Override
  public Class<?> getDefaultType() {
    return BigDecimal.class;
  }

  @Override
  protected <T> T internalValueOfString(String value,
                                        Boolean isNullable, Integer maxLength, Integer precision,
                                        Integer scale, Boolean isUnicode, Class<T> returnType) throws EdmPrimitiveTypeException {

    Matcher matcher = PATTERN.matcher(value);
    if (!matcher.matches()
        || matcher.group(1) == null && matcher.group(2) == null && matcher.group(3) == null
        && matcher.group(4) == null) {
      throw new EdmPrimitiveTypeException("The literal '" + value + "' has illegal content.");
    }

    BigDecimal result = (matcher.group(1) == null ? BigDecimal.ZERO
        : new BigDecimal(matcher.group(1)).multiply(BigDecimal.valueOf(24 * 60 * 60))).
        add(matcher.group(2) == null ? BigDecimal.ZERO
            : new BigDecimal(matcher.group(2)).multiply(BigDecimal.valueOf(60 * 60))).
            add(matcher.group(3) == null ? BigDecimal.ZERO
                : new BigDecimal(matcher.group(3)).multiply(BigDecimal.valueOf(60))).
                add(matcher.group(4) == null ? BigDecimal.ZERO : new BigDecimal(matcher.group(4)));

    if (result.scale() <= (precision == null ? 0 : precision)) {
      result = value.charAt(0) == '-' ? result.negate() : result;
    } else {
      throw new EdmPrimitiveTypeException("The literal '" + value + "' does not match the facets' constraints.");
    }

    try {
      return EdmDecimal.convertDecimal(result, returnType);
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

    BigDecimal valueDecimal;
    if (value instanceof BigDecimal) {
      valueDecimal = (BigDecimal) value;
    } else if (value instanceof Byte || value instanceof Short || value instanceof Integer || value instanceof Long) {
      valueDecimal = BigDecimal.valueOf(((Number) value).longValue());
    } else if (value instanceof BigInteger) {
      valueDecimal = new BigDecimal((BigInteger) value);
    } else {
      throw new EdmPrimitiveTypeException("The value type " + value.getClass() + " is not supported.");
    }

    if (valueDecimal.scale() > (precision == null ? 0 : precision)) {
      throw new EdmPrimitiveTypeException("The value '" + value + "' does not match the facets' constraints.");
    }

    StringBuilder result = new StringBuilder();
    if (valueDecimal.signum() == -1) {
      result.append('-');
      valueDecimal = valueDecimal.negate();
    }
    result.append('P');
    BigInteger seconds = valueDecimal.toBigInteger();
    BigInteger days = seconds.divide(BigInteger.valueOf(24 * 60 * 60));
    if (!days.equals(BigInteger.ZERO)) {
      result.append(days);
      result.append('D');
    }
    result.append('T');
    seconds = seconds.subtract(days.multiply(BigInteger.valueOf(24 * 60 * 60)));
    BigInteger hours = seconds.divide(BigInteger.valueOf(60 * 60));
    if (!hours.equals(BigInteger.ZERO)) {
      result.append(hours);
      result.append('H');
    }
    seconds = seconds.subtract(hours.multiply(BigInteger.valueOf(60 * 60)));
    BigInteger minutes = seconds.divide(BigInteger.valueOf(60));
    if (!minutes.equals(BigInteger.ZERO)) {
      result.append(minutes);
      result.append('M');
    }
    result.append(valueDecimal.remainder(BigDecimal.valueOf(60)).toPlainString());
    result.append('S');

    return result.toString();
  }

}