package nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.impl;

import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmMediaStream;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAEdmNameBuilder;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAModelException;
import org.apache.olingo.commons.api.edm.FullQualifiedName;

import jakarta.persistence.Column;
import jakarta.persistence.Version;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.Attribute.PersistentAttributeType;
import jakarta.persistence.metamodel.SingularAttribute;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import static nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAModelException.MessageKeys.NOT_SUPPORTED_KEY_PART_OF_GROUP;
import static nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAModelException.MessageKeys.NOT_SUPPORTED_MANDATORY_PART_OF_GROUP;

/**
 * A Property is described on the one hand by its Name and Type and on the other
 * hand by its Property Facets. The type is a qualified name of either a
 * primitive type, a complex type or an enumeration type.
 * Primitive types mapped by {@link JPATypeConverter}.
 *

 * <p>
 * For details about Property metadata see: <a href=
 * "http://docs.oasis-open.org/odata/odata/v4.0/errata03/os/complete/part3-csdl/odata-v4.0-errata03-os-part3-csdl-complete.html#_Toc453752528"
 * >OData Version 4.0 Part 3 - 6 Structural Property </a>
 *
 *
 * @author Oliver Grande
 *
 */
class IntermediateSimpleProperty extends IntermediateProperty {
  private EdmMediaStream streamInfo;

  IntermediateSimpleProperty(final JPAEdmNameBuilder nameBuilder, final Attribute<?, ?> jpaAttribute,
      final IntermediateSchema schema) throws ODataJPAModelException {

    super(nameBuilder, jpaAttribute, schema);
  }

  @Override
  public boolean isAssociation() {
    return false;
  }

  @Override
  public boolean isCollection() {
    return false;
  }

  @Override
  public boolean isComplex() {
    return jpaAttribute.getPersistentAttributeType() == PersistentAttributeType.EMBEDDED;
  }

  @Override
  public boolean isKey() {
    if (jpaAttribute instanceof SingularAttribute<?, ?>)
      return ((SingularAttribute<?, ?>) jpaAttribute).isId();
    else
      return false;
  }

  @Override
  void checkConsistency() throws ODataJPAModelException {
    final Column jpaColumn = ((AnnotatedElement) jpaAttribute.getJavaMember()).getAnnotation(Column.class);
    if (jpaColumn != null && isPartOfGroup() && !jpaColumn.nullable())
      throw new ODataJPAModelException(NOT_SUPPORTED_MANDATORY_PART_OF_GROUP, jpaAttribute.getDeclaringType()
          .getJavaType().getCanonicalName(), jpaAttribute.getName());
    if (isPartOfGroup() && isKey())
      throw new ODataJPAModelException(NOT_SUPPORTED_KEY_PART_OF_GROUP, jpaAttribute.getDeclaringType()
          .getJavaType().getCanonicalName(), jpaAttribute.getName());
  }

  @Override
  Class<?> determineEntityType() {
    return jpaAttribute.getJavaType();
  }

  @Override
  void determineIsVersion() {
    final Version jpaVersion = ((AnnotatedElement) this.jpaAttribute.getJavaMember())
        .getAnnotation(Version.class);
    if (jpaVersion != null) {
      isVersion = true;
    }
  }

  @Override
  void determineStreamInfo() throws ODataJPAModelException {
    streamInfo = ((AnnotatedElement) jpaAttribute.getJavaMember()).getAnnotation(EdmMediaStream.class);
    if (streamInfo != null) {
      streamInfo.contentType();
      if (streamInfo.contentType().isEmpty()) {
        streamInfo.contentTypeAttribute();
        if (streamInfo.contentTypeAttribute().isEmpty()) throw new ODataJPAModelException(ODataJPAModelException.MessageKeys.ANNOTATION_STREAM_INCOMPLETE,
                internalName);
      }
    }
  }

  @Override
  void determineStructuredType() {
    type =
     jpaAttribute.getPersistentAttributeType() == PersistentAttributeType.EMBEDDED ? schema.getStructuredType(jpaAttribute) : null;
  }

  @Override
  FullQualifiedName determineType() throws ODataJPAModelException { return determineTypeByPersistenceType(jpaAttribute.getPersistentAttributeType()); }

  String getContentType() { return streamInfo.contentType(); }

  String getContentTypeProperty() { return streamInfo.contentTypeAttribute(); }

  @Override
  String getDefaultValue() throws ODataJPAModelException {
    String valueString = null;
    if (jpaAttribute.getJavaMember() instanceof Field
        && jpaAttribute.getPersistentAttributeType() == PersistentAttributeType.BASIC) {
      // It is not possible to get the default value directly from the
      // Field, only from an instance field.get(Object obj).toString(); //NOSONAR
      try {
        // Problem: In case of compound key, which is not referenced via @EmbeddedId Hibernate returns
        // a field of the key class, whereas Eclipselink returns a field of the entity class; which can
        // be checked via field.getDeclaringClass().
        final Field field = (Field) jpaAttribute.getJavaMember();
        Constructor<?> constructor;
        if (!field.getDeclaringClass().equals(jpaAttribute.getDeclaringType().getJavaType()))
          constructor = field.getDeclaringClass().getConstructor();
        else
          constructor = jpaAttribute.getDeclaringType().getJavaType().getConstructor();
        final Object pojo = constructor.newInstance();
        field.setAccessible(true);
        final Object value = field.get(pojo);
        if (value != null)
          valueString = value.toString();
      } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException
          | InvocationTargetException e) {
        throw new ODataJPAModelException(ODataJPAModelException.MessageKeys.PROPERTY_DEFAULT_ERROR, e,
            jpaAttribute.getName());
      } catch (InstantiationException e) {
        // Class could not be instantiated e.g. abstract class like
        // Business Partner=> default could not be determined and will be ignored
      }
    }
    return valueString;
  }

  @Override
  boolean isStream() { return streamInfo != null && streamInfo.stream(); }

}