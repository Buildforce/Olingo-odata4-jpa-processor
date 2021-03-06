package nl.buildforce.sequoia.metadata.core.edm.mapper.impl;

import jakarta.persistence.Lob;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.metamodel.Attribute;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAAttribute;
import nl.buildforce.sequoia.metadata.core.edm.mapper.exception.ODataJPAModelException;

import java.lang.reflect.AnnotatedElement;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.UUID;

/**
 * This class holds utility methods for type conversions between JPA Java types and OData Types.
 */
public final class JPATypeConverter {

    private JPATypeConverter() {}

    public static EdmPrimitiveTypeKind convertToEdmSimpleType(final Class<?> type) throws ODataJPAModelException {
        return convertToEdmSimpleType(type, null);
    }

    /**
     * This utility method converts a given jpa Type to equivalent EdmPrimitiveTypeKind for maintaining compatibility
     * between Java and OData Types.
     *
     * @param jpaType The JPA Type input.
     * @return The corresponding EdmPrimitiveTypeKind.
     * @throws ODataJPAModelException
     * @see EdmPrimitiveTypeKind
     */

    public static <T> EdmPrimitiveTypeKind convertToEdmSimpleType(final Class<T> jpaType,
                                                                  final Attribute<?, ?> currentAttribute) throws ODataJPAModelException {

        if (jpaType.equals(String.class) || jpaType.equals(Character.class) || jpaType.equals(char.class) || jpaType.equals(
                char[].class) || jpaType.equals(Character[].class)) return EdmPrimitiveTypeKind.String;
        else if (jpaType.equals(Long.class) || jpaType.equals(long.class)) return EdmPrimitiveTypeKind.Int64;
        else if (jpaType.equals(Short.class) || jpaType.equals(short.class)) return EdmPrimitiveTypeKind.Int16;
        else if (jpaType.equals(Integer.class) || jpaType.equals(int.class)) return EdmPrimitiveTypeKind.Int32;
        else if (jpaType.equals(Double.class) || jpaType.equals(double.class)) return EdmPrimitiveTypeKind.Double;
        else if (jpaType.equals(Float.class) || jpaType.equals(float.class)) return EdmPrimitiveTypeKind.Single;
        else if (jpaType.equals(BigDecimal.class)) return EdmPrimitiveTypeKind.Decimal;
        else if (jpaType.equals(byte[].class)) return EdmPrimitiveTypeKind.Binary;
        else if (jpaType.equals(Byte.class) || jpaType.equals(byte.class)) return EdmPrimitiveTypeKind.SByte;
        else if (jpaType.equals(Boolean.class) || jpaType.equals(boolean.class)) return EdmPrimitiveTypeKind.Boolean;
        else if (jpaType.equals(java.time.LocalTime.class) || jpaType.equals(java.sql.Time.class))
            return EdmPrimitiveTypeKind.TimeOfDay;
        else if (jpaType.equals(java.time.Duration.class)) return EdmPrimitiveTypeKind.Duration;
        else if (jpaType.equals(java.time.LocalDate.class) || jpaType.equals(java.sql.Date.class))
            return EdmPrimitiveTypeKind.Date;
        else if (jpaType.equals(Calendar.class) || jpaType.equals(Timestamp.class) || jpaType.equals(java.util.Date.class))
            if ((currentAttribute != null) && (determineTemporalType(currentAttribute) == TemporalType.TIME)) {
                return EdmPrimitiveTypeKind.TimeOfDay;
            } else if ((currentAttribute != null) && (determineTemporalType(currentAttribute) == TemporalType.DATE))
                return EdmPrimitiveTypeKind.Date;
            else return EdmPrimitiveTypeKind.DateTimeOffset;
        else // Looks like Olingo does not support LocalDateTime or OffsetDateTime, which are supported by JPA 2.2. Olingo only
            // takes ZonedDateTime.
            if (jpaType.equals(ZonedDateTime.class) || jpaType.equals(LocalDateTime.class) || jpaType.equals(OffsetDateTime.class))
                return EdmPrimitiveTypeKind.DateTimeOffset;
            else if (jpaType.equals(UUID.class)) return EdmPrimitiveTypeKind.Guid;
            else if (jpaType.equals(Blob.class) && isBlob(currentAttribute)) return EdmPrimitiveTypeKind.Binary;
            else if (jpaType.equals(Clob.class) && isBlob(currentAttribute)) return EdmPrimitiveTypeKind.String;
            /*else if (isGeography(currentAttribute)) return convertGeography(jpaType, currentAttribute);
            else if (isGeometry(currentAttribute)) return convertGeometry(jpaType, currentAttribute);*/
        if (currentAttribute != null)
            // Type (%1$s) of attribute (%2$s) is not supported. Mapping not possible
            throw new ODataJPAModelException(ODataJPAModelException.MessageKeys.TYPE_NOT_SUPPORTED,
                    jpaType.getName(), currentAttribute.getName());
        else return null;
    }

    public static EdmPrimitiveTypeKind convertToEdmSimpleType(final JPAAttribute attribute)
            throws ODataJPAModelException {
        return convertToEdmSimpleType(attribute.getType(), null);
    }

    public static boolean isSimpleType(final Class<?> type, final Attribute<?, ?> currentAttribute) {
        return type != null
                && (isScalarType(type)
                || type.equals(Byte[].class)
                || type.equals(Blob.class)
                || type.equals(Clob.class));
    }

    public static boolean isScalarType(final Class<?> type) {
        return (type == String.class ||
                type == Character.class ||
                type == Long.class ||
                type == Short.class ||
                type == Integer.class ||
                type == Double.class ||
                type == Float.class ||
                type == BigDecimal.class ||
                type == Byte.class ||
                type == Boolean.class ||
                type == java.time.LocalTime.class ||
                type == java.sql.Time.class ||
                type == java.time.Duration.class ||
                type == java.time.LocalDate.class ||
                type == java.time.OffsetDateTime.class ||
                type == java.time.ZonedDateTime.class ||
                type == java.sql.Date.class ||
                type == Calendar.class ||
                type == Timestamp.class ||
                type == java.util.Date.class ||
                type == UUID.class);
    }

    /**
     * For supported java types see {@link nl.buildforce.olingo.commons.api.edm.EdmPrimitiveType}. In addition, since 4.7.1,
     * also some types from the java.time package are supported, see:
     * <ul>
     * <li>For EdmDate: LocalDate, see
     * {@link nl.buildforce.olingo.commons.core.edm.primitivetype.EdmDate#internalValueToString
     * EdmDate.internalValueToString}</li>
     * <li>For EdmTimeOfDay: LocalTime, see
     * {@link nl.buildforce.olingo.commons.core.edm.primitivetype.EdmTimeOfDay#internalValueToString
     * EdmTimeOfDay.internalValueToString}</li>
     * <li>For EdmDateTimeOffset: ZonedDateTime, see
     * {@link nl.buildforce.olingo.commons.core.edm.primitivetype.EdmDateTimeOffset#internalValueToString
     * EdmDateTimeOffset.internalValueToString}</li>
     * </ul>     * @param type
     * @return
     */
    public static boolean isSupportedByOlingo(final Class<?> type) {

        return (type == Boolean.class ||
                type == Byte.class ||
                type == Byte[].class ||
                type == Double.class ||
                type == Float.class ||
                type == Integer.class ||
                type == Long.class ||
                type == Short.class ||
                type == String.class ||
                type == byte[].class ||
                type == java.math.BigDecimal.class ||
                type == java.math.BigInteger.class ||
                type == java.sql.Time.class ||
                type == java.sql.Timestamp.class ||
                type == java.time.LocalDate.class ||
                type == java.time.LocalTime.class ||
                type == java.time.ZonedDateTime.class ||
                type == java.util.Calendar.class ||
                type == java.util.Date.class ||
                type == java.util.UUID.class);
    }

    private static TemporalType determineTemporalType(final Attribute<?, ?> currentAttribute) {
        if (currentAttribute != null) {
            final AnnotatedElement annotatedElement = (AnnotatedElement) currentAttribute.getJavaMember();
            if (annotatedElement != null && annotatedElement.getAnnotation(Temporal.class) != null) {
                return annotatedElement.getAnnotation(Temporal.class).value();
            }
        }
        return null;
    }

    private static boolean isBlob(final Attribute<?, ?> currentAttribute) {
        if (currentAttribute != null) {
            final AnnotatedElement annotatedElement = (AnnotatedElement) currentAttribute.getJavaMember();
            return annotatedElement != null && annotatedElement.getAnnotation(Lob.class) != null;
        }
        return false;
    }

}