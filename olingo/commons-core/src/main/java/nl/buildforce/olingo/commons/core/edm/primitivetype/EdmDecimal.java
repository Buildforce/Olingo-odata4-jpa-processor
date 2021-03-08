/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
 */
package nl.buildforce.olingo.commons.core.edm.primitivetype;

import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveType;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeException;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementation of the EDM primitive type Decimal.
 */
public final class EdmDecimal extends SingletonPrimitiveType {

    private static final Pattern PATTERN = Pattern.compile(
            "[+-]?0*(\\p{Digit}+?)(?:\\.(\\p{Digit}+?)0*)?([Ee][+-]?\\p{Digit}+)?");

    private static final EdmDecimal INSTANCE = new EdmDecimal();

    public static EdmDecimal getInstance() {
        return INSTANCE;
    }

    private static boolean validateLiteral(String value) {
        return PATTERN.matcher(value).matches();
    }

    private static boolean validatePrecisionAndScale(String value, Integer precision, Integer scale) {

        var significantIntegerDigitsDecimals = significantIntegerDigits_decimals(value);
        int significantIntegerDigits = (int) significantIntegerDigitsDecimals.left;
        int decimals = (int) significantIntegerDigitsDecimals.right;

        return (precision == null || significantIntegerDigits <= precision - (scale == null ? 0 : scale)) && decimals <= (scale == null ? 0 : scale);
    }

    private static ImmutablePair<Integer, Integer> significantIntegerDigits_decimals(String value) {
        Matcher matcher = PATTERN.matcher(value);
        matcher.matches();
        if (matcher.group(3) != null) {
            String plainValue = new BigDecimal(value).toPlainString();
            matcher = PATTERN.matcher(plainValue);
            matcher.matches();
        }
        return new ImmutablePair<Integer, Integer>("0".equals(matcher.group(1)) ? 0 : matcher.group(1).length()
                , matcher.group(2) == null ? 0 : matcher.group(2).length());
    }

    /**
     * Converts a {@link BigDecimal} value into the requested return type if possible.
     *
     * @param value      the value
     * @param returnType the class of the returned value; it must be one of {@link BigDecimal}, {@link Double},
     *                   {@link Float}, {@link BigInteger}, {@link Long}, {@link Integer}, {@link Short}, or {@link Byte}
     * @return the converted value
     * @throws IllegalArgumentException if the conversion is not possible or would lead to loss of data
     * @throws ClassCastException       if the return type is not allowed
     */
    protected static <T> T convertDecimal(BigDecimal value, Class<T> returnType)
            throws IllegalArgumentException, ClassCastException {

        if (returnType.isAssignableFrom(BigDecimal.class)) return returnType.cast(value);
        else if (returnType.isAssignableFrom(Double.class)) {
            double doubleValue = value.doubleValue();
            if (BigDecimal.valueOf(doubleValue).compareTo(value) == 0) return returnType.cast(doubleValue);
            else throw new IllegalArgumentException();
        } else if (returnType.isAssignableFrom(Float.class)) {
            float floatValue = value.floatValue();
            if (BigDecimal.valueOf(floatValue).compareTo(value) == 0) return returnType.cast(floatValue);
            else throw new IllegalArgumentException();
        } else {
            try {
                if (returnType.isAssignableFrom(BigInteger.class)) return returnType.cast(value.toBigIntegerExact());
                else if (returnType.isAssignableFrom(Long.class)) return returnType.cast(value.longValueExact());
                else if (returnType.isAssignableFrom(Integer.class)) return returnType.cast(value.intValueExact());
                else if (returnType.isAssignableFrom(Short.class)) return returnType.cast(value.shortValueExact());
                else if (returnType.isAssignableFrom(Byte.class)) return returnType.cast(value.byteValueExact());
                else throw new ClassCastException("Unsupported return type " + returnType.getSimpleName());
            } catch (ArithmeticException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    @Override
    public boolean isCompatible(EdmPrimitiveType primitiveType) {
        return primitiveType instanceof EdmByte
                || primitiveType instanceof EdmSByte
                || primitiveType instanceof EdmInt16
                || primitiveType instanceof EdmInt32
                || primitiveType instanceof EdmInt64
                || primitiveType instanceof EdmSingle
                || primitiveType instanceof EdmDouble
                || primitiveType instanceof EdmDecimal;
    }

    @Override
    public Class<?> getDefaultType() {
        return BigDecimal.class;
    }

    @Override
    public boolean validate(String value,
                            Boolean isNullable, Integer maxLength, Integer precision,
                            Integer scale, Boolean isUnicode) {

        return value == null ? isNullable == null || isNullable
                : validateLiteral(value) && validatePrecisionAndScale(value, precision, scale);
    }

    @Override
    public boolean validateDecimals(String value,
                                    Boolean isNullable, Integer maxLength, Integer precision,
                                    String scale, Boolean isUnicode) {

        return value == null
                ? isNullable == null || isNullable
                : validateLiteral(value) && validatePrecisionAndScale(value, precision, scale);
    }

    private boolean validatePrecisionAndScale(String value, Integer precision, String scale) {
        var significantIntegerDigitsDecimals = significantIntegerDigits_decimals(value);
        int significantIntegerDigits = (int)significantIntegerDigitsDecimals.left;
        int decimals = (int) significantIntegerDigitsDecimals.right;


        try {
            int scaleValue = (scale == null) ? 0 : Integer.parseInt(scale);
            return (precision == null || significantIntegerDigits <= precision - scaleValue) && decimals <= scaleValue;
        } catch (NumberFormatException e) {
            if (scale.equals("variable")) {
                return (precision == null || significantIntegerDigits <= precision - decimals) && decimals <= (precision == null ? 0 : precision);
            } else if (scale.equals("floating")) {
                Matcher matcher1 = PATTERN.matcher(value);
                matcher1.matches();
                significantIntegerDigits = "0".equals(matcher1.group(1)) ? 0 : matcher1.group(1).length();
                decimals = matcher1.group(2) == null ? 0 : matcher1.group(2).length();
                int exponents;
                if (matcher1.group(3) != null) {
                    exponents = Integer.parseInt(matcher1.group(3).substring(1));
                    if (exponents < -95 || exponents > 96) {
                        if (String.valueOf(exponents).startsWith("-")) {
                            significantIntegerDigits += Integer.parseInt(String.valueOf(exponents + 95).substring(1));
                            exponents = -95;
                        }
                    }
                    return (significantIntegerDigits + decimals) <= 7 && (exponents >= -95 && exponents <= 96);
                }
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    protected <T> T internalValueOfString(String value,
                                          Boolean isNullable, Integer maxLength, Integer precision,
                                          Integer scale, Boolean isUnicode, Class<T> returnType) throws EdmPrimitiveTypeException {

        if (!validateLiteral(value)) {
            throw new EdmPrimitiveTypeException("The literal '" + value + "' has illegal content.");
        }
        if (!validatePrecisionAndScale(value, precision, scale)) {
            throw new EdmPrimitiveTypeException("The literal '" + value + "' does not match the facets' constraints.");
        }

        try {
            return convertDecimal(new BigDecimal(value), returnType);
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

        String result;
        if (value instanceof Long || value instanceof Integer || value instanceof Short
                || value instanceof Byte || value instanceof BigInteger) {
            result = value.toString();
            int digits = result.startsWith("-") ? result.length() - 1 : result.length();
            if (precision != null && precision < digits) {
                throw new EdmPrimitiveTypeException("The value '" + value + "' does not match the facets' constraints.");
            }

        } else if (value instanceof Double || value instanceof Float || value instanceof BigDecimal) {
            BigDecimal bigDecimalValue;
            try {
                bigDecimalValue = value instanceof Double ? BigDecimal.valueOf((Double) value)
                        : value instanceof Float ? BigDecimal.valueOf((Float) value) : (BigDecimal) value;
            } catch (NumberFormatException e) {
                throw new EdmPrimitiveTypeException("The value '" + value + "' is not valid.", e);
            }

            int digits = bigDecimalValue.scale() >= 0
                    ? Math.max(bigDecimalValue.precision(), bigDecimalValue.scale())
                    : bigDecimalValue.precision() - bigDecimalValue.scale();
            if ((precision == null || precision >= digits) && (bigDecimalValue.scale() <= (scale == null ? 0 : scale))) {
                result = bigDecimalValue.toPlainString();
            } else {
                throw new EdmPrimitiveTypeException("The value '" + value + "' does not match the facets' constraints.");
            }

        } else {
            throw new EdmPrimitiveTypeException("The value type " + value.getClass() + " is not supported.");
        }

        return result;
    }

}