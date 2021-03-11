package nl.buildforce.sequoia.processor.core.modify;

import jakarta.persistence.AttributeConverter;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.buildforce.olingo.commons.api.data.ComplexValue;
import nl.buildforce.olingo.commons.api.data.Entity;
import nl.buildforce.olingo.commons.api.data.Parameter;
import nl.buildforce.olingo.commons.api.data.Property;
import nl.buildforce.olingo.commons.api.data.ValueType;
import nl.buildforce.olingo.commons.api.edm.EdmEntitySet;
import nl.buildforce.olingo.commons.api.edm.EdmProperty;
import nl.buildforce.olingo.commons.api.format.ContentType;
import nl.buildforce.olingo.commons.api.http.HttpStatusCode;
import nl.buildforce.olingo.server.api.OData;
import nl.buildforce.olingo.server.api.ODataRequest;
import nl.buildforce.olingo.server.api.deserializer.DeserializerException;
import nl.buildforce.olingo.server.api.deserializer.ODataDeserializer;
import nl.buildforce.olingo.server.api.serializer.SerializerException;
import nl.buildforce.olingo.server.api.uri.UriParameter;
import nl.buildforce.olingo.server.api.uri.UriResource;
import nl.buildforce.olingo.server.api.uri.UriResourceProperty;

import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAAttribute;
import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAEntityType;
import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAEnumerationAttribute;
import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAPath;
import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAServiceDocument;
import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAStructuredType;
import nl.buildforce.sequoia.metadata.core.edm.mapper.exception.ODataJPAModelException;
import nl.buildforce.sequoia.processor.core.exception.ODataJPAFilterException;
import nl.buildforce.sequoia.processor.core.exception.ODataJPAProcessException;
import nl.buildforce.sequoia.processor.core.exception.ODataJPAProcessorException.MessageKeys;
import nl.buildforce.sequoia.processor.core.exception.ODataJPAProcessorException;
import nl.buildforce.sequoia.processor.core.query.EdmEntitySetInfo;
import nl.buildforce.sequoia.processor.core.query.ExpressionUtil;
import nl.buildforce.sequoia.processor.core.query.Util;

import static nl.buildforce.olingo.commons.api.http.HttpHeader.ODATA_VERSION;

/**
 * Helper method for modifying requests.<p>
 * Mainly created to increase testability
 */

public class JPAConversionHelper {

  private final Map<Object, Map<String, Object>> getterBuffer = new HashMap<>();

  public static Object convertParameter(final Parameter param, final JPAServiceDocument sd) throws ODataJPAModelException {
    if (param.getValueType() == ValueType.ENUM) {
      final JPAEnumerationAttribute enumType = sd.getEnumType(param.getType());
      return enumType.enumOf((Number) param.getValue());
    }
    return param.getValue();
  }

  /**
   * Creates a map of attribute name and the return value of there getter method. <p>
   * It is assumed that the method name is composed from <i>get</> and the
   * name of the attribute and that the attribute name starts with a lower case
   * letter.
   * @param instance
   * @return
   * @throws ODataJPAProcessorException
   */
  public Map<String, Object> buildGetterMap(final Object instance) throws ODataJPAProcessorException {

    if (instance != null) {
      final ODataJPAProcessorException[] exception = { null };
      final Map<String, Object> getterMap = getterBuffer.computeIfAbsent(instance, k -> {
        try {
          return this.determineGetter(instance);
        } catch (ODataJPAProcessorException e) {
          exception[0] = e;
          return new HashMap<>(1);
        }
      });
      if (exception[0] == null)
        return getterMap;
      else
        throw exception[0];
    } else {
      throw new ODataJPAProcessorException(MessageKeys.PARAMETER_NULL, HttpStatusCode.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Converts the payload of a request into the corresponding odata entity
   * @param odata
   * @param request
   * @param requestFormat
   * @param uriResourceParts
   * @return
   * @throws ODataJPAProcessorException
   */
  public Entity convertInputStream(final OData odata, final ODataRequest request, final ContentType requestFormat,
      final List<UriResource> uriResourceParts) throws ODataJPAProcessorException {

    final InputStream requestInputStream = request.getBody();
    final EdmEntitySetInfo targetEntityInfo = Util.determineModifyEntitySetAndKeys(uriResourceParts);
    try {
      final ODataDeserializer deserializer = createDeserializer(odata, requestFormat,
          request.getHeaders(ODATA_VERSION));
      final UriResource lastPart = uriResourceParts.get(uriResourceParts.size() - 1);
      if (lastPart instanceof UriResourceProperty) {
        // Convert requests on property level into request on entity level
        final Entity requestEntity = new Entity();
        final String startProperty = targetEntityInfo.getNavigationPath().split(JPAPath.PATH_SEPARATOR)[0];
        int i = uriResourceParts.size() - 1;
        for (; i > 0; i--) {
          if (uriResourceParts.get(i) instanceof UriResourceProperty
              && ((UriResourceProperty) uriResourceParts.get(i)).getProperty().getName().equals(startProperty)) {
            break;
          }
        }
        List<Property> properties = requestEntity.getProperties();
        for (int j = i; j < uriResourceParts.size() - 1; j++) {
          // NO $value supported yet
          if (!(uriResourceParts.get(i) instanceof UriResourceProperty)) {
            break;
          }
          final EdmProperty edmProperty = ((UriResourceProperty) uriResourceParts.get(i)).getProperty();
          final Property intermediateProperty = new Property();
          intermediateProperty.setType(edmProperty.getType().getFullQualifiedName().getFullQualifiedNameAsString());
          intermediateProperty.setName(edmProperty.getName());
          intermediateProperty.setValue(ValueType.COMPLEX, new ComplexValue());
          properties.add(intermediateProperty);
          properties = ((ComplexValue) intermediateProperty.getValue()).getValue();
        }
        properties.add(deserializer.property(requestInputStream, ((UriResourceProperty) lastPart).getProperty())
            .getProperty());
        return requestEntity;
      } else {
        return deserializer.entity(requestInputStream, targetEntityInfo.getTargetEdmEntitySet().getEntityType())
            .getEntity();
      }
    } catch (DeserializerException e) {
      throw new ODataJPAProcessorException(e, HttpStatusCode.BAD_REQUEST);
    }
  }

  /**
   *
   * @param odata
   * @param request
   * @param edmEntitySet
   * @param et
   * @param newPOJO
   * @return
   * @throws SerializerException
   * @throws ODataJPAProcessorException
   */

  @SuppressWarnings("unchecked")
  public String convertKeyToLocal(final OData odata, final ODataRequest request, final EdmEntitySet edmEntitySet,
      final JPAEntityType et, final Object newPOJO) throws SerializerException, ODataJPAProcessorException {

    if (newPOJO instanceof Map<?, ?>)
      return convertKeyToLocalMap(odata, request, edmEntitySet, et, (Map<String, Object>) newPOJO);
    else
      return convertKeyToLocalEntity(odata, request, edmEntitySet, et, newPOJO);
  }

  /**
   * Creates nested map of attributes and there (new) values. Primitive values are instances of e.g. Integer. Embedded
   * Types are returned as maps.
   *
   * @param odata
   * @param st
   * @param odataProperties
   * @return
   * @throws ODataJPAProcessException
   */
  @SuppressWarnings("unchecked")
  public Map<String, Object> convertProperties(final OData odata, final JPAStructuredType st,
      final List<Property> odataProperties) throws ODataJPAProcessException {

    final Map<String, Object> jpaAttributes = new HashMap<>();
    String internalName;
    Object jpaAttribute;
    JPAPath path;
    for (Property odataProperty : odataProperties) {
      try {
        path = st.getPath(odataProperty.getName());
      } catch (ODataJPAModelException e) {
        throw new ODataJPAProcessorException(e, HttpStatusCode.INTERNAL_SERVER_ERROR);
      }
      switch (odataProperty.getValueType()) {
        case COMPLEX -> {
          internalName = path.getPath().get(0).getInternalName();
          try {
            JPAStructuredType a = st.getAttribute(internalName).getStructuredType();
            jpaAttribute = convertProperties(odata, a, ((ComplexValue) odataProperty.getValue()).getValue());
          } catch (ODataJPAModelException e) {
            throw new ODataJPAProcessorException(e, HttpStatusCode.INTERNAL_SERVER_ERROR);
          }
        }
        case PRIMITIVE, ENUM -> {
          final JPAAttribute attribute = path.getLeaf();
          internalName = attribute.getInternalName();
          jpaAttribute = processAttributeConverter(odataProperty.getValue(), attribute);
        }
        case COLLECTION_PRIMITIVE, COLLECTION_ENUM -> {
          final JPAAttribute attribute2 = path.getLeaf();
          internalName = attribute2.getInternalName();
          jpaAttribute = new ArrayList<>();
          for (Object property : (List<?>) odataProperty.getValue())
            ((List<Object>) jpaAttribute).add(processAttributeConverter(property, attribute2));
        }
        case COLLECTION_COMPLEX -> {
          internalName = path.getPath().get(0).getInternalName();
          jpaAttribute = new ArrayList<>();
          try {
            JPAStructuredType a = st.getAttribute(internalName).getStructuredType();
            for (ComplexValue property : (List<ComplexValue>) odataProperty.getValue())
              ((List<Object>) jpaAttribute).add(convertProperties(odata, a, property.getValue()));
          } catch (ODataJPAModelException e) {
            throw new ODataJPAProcessorException(e, HttpStatusCode.INTERNAL_SERVER_ERROR);
          }
        }
        default -> throw new ODataJPAProcessorException(MessageKeys.NOT_SUPPORTED_PROP_TYPE, HttpStatusCode.NOT_IMPLEMENTED,
                odataProperty.getValueType().name());
      }
      jpaAttributes.put(internalName, jpaAttribute);
    }
    return jpaAttributes;
  }

  /**
   *

   * @param keyPredicates
   * @return
   * @throws ODataJPAFilterException
   * @throws ODataJPAProcessorException
   */
  public Map<String, Object> convertUriKeys(final OData odata, final JPAStructuredType st,
      final List<UriParameter> keyPredicates) throws ODataJPAFilterException, ODataJPAProcessorException {

    Map<String, Object> result = new HashMap<>(keyPredicates.size());
    String internalName;
    for (UriParameter key : keyPredicates) {
      try {
        final JPAAttribute attribute = st.getPath(key.getName()).getLeaf();
        internalName = attribute.getInternalName();
        Object jpaAttribute = ExpressionUtil.convertValueOnAttribute(odata, attribute, key.getText(), true);
        result.put(internalName, jpaAttribute);
      } catch (ODataJPAModelException e) {
        throw new ODataJPAProcessorException(e, HttpStatusCode.INTERNAL_SERVER_ERROR);
      }
    }
    return result;
  }

  /**
   * Like {@link #buildGetterMap}, but without buffer
   * @param instance
   * @return
   * @throws ODataJPAProcessorException
   */
  public Map<String, Object> determineGetter(final Object instance) throws ODataJPAProcessorException {
    Map<String, Object> getterMap = new HashMap<>();
    Method[] methods = instance.getClass().getMethods();

    for (Method meth : methods) {
      String methodName = meth.getName();
      if (methodName.startsWith("get") && methodName.length() > 3) {
        String attributeName = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
        try {
          Object value = meth.invoke(instance);
          getterMap.put(attributeName, value);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
          throw new ODataJPAProcessorException(MessageKeys.ATTRIBUTE_RETRIEVAL_FAILED,
              HttpStatusCode.INTERNAL_SERVER_ERROR, e, attributeName);
        }
      }
    }
    return getterMap;
  }

  @SuppressWarnings("unchecked")
  public <S, T> Object processAttributeConverter(Object value, final JPAAttribute attribute) {
    Object jpaAttribute;
    if (attribute.getConverter() != null) {
      AttributeConverter<T, S> converter = attribute.getConverter();
      jpaAttribute = converter.convertToEntityAttribute((S) value);
    } else if (attribute.isEnum()) {
      jpaAttribute = findEnumConstantsByOrdinal(attribute.getType().getEnumConstants(), value);
    } else {
      jpaAttribute = value;
    }
    return jpaAttribute;
  }

  private void collectKeyProperties(Map<String, Object> newPOJO, final List<JPAPath> keyPath, final List<Property> properties)
          throws ODataJPAModelException {

    if (keyPath.size() > 1) {

      for (JPAPath key : keyPath) {
        Object keyElement = newPOJO.get(key.getLeaf().getInternalName());
        final Property property = new Property(null, key.getLeaf().getExternalName());
        property.setValue(ValueType.PRIMITIVE, keyElement);
        properties.add(property);
      }
    } else {
      JPAPath key = keyPath.get(0);
      if (key.getLeaf().isComplex()) {
        // EmbeddedId
        @SuppressWarnings("unchecked")
        Map<String, Object> embeddedId = (Map<String, Object>) newPOJO.get(key.getLeaf().getInternalName());
        collectKeyProperties(embeddedId, key.getLeaf().getStructuredType().getPathList(), properties);
      } else {
        final Property property = new Property(null, key.getLeaf().getExternalName());
        property.setValue(ValueType.PRIMITIVE, newPOJO.get(key.getLeaf().getInternalName()));
        properties.add(property);
      }
    }
  }

  private void collectKeyProperties(Object newPOJO, final List<JPAPath> keyPath, final List<Property> properties)
      throws ODataJPAProcessorException, ODataJPAModelException {

    final Map<String, Object> getter = buildGetterMap(newPOJO);
    if (keyPath.size() > 1) {

      for (JPAPath key : keyPath) {
        final Property property = new Property(null, key.getLeaf().getExternalName());
        property.setValue(ValueType.PRIMITIVE, getter.get(key.getLeaf().getInternalName()));
        properties.add(property);
      }
    } else {
      JPAPath key = keyPath.get(0);
      if (key.getLeaf().isComplex()) {
        // EmbeddedId
        Object embeddedId = getter.get(key.getLeaf().getInternalName());
        collectKeyProperties(embeddedId, key.getLeaf().getStructuredType().getPathList(), properties);
      } else {
        final Property property = new Property(null, key.getLeaf().getExternalName());
        property.setValue(ValueType.PRIMITIVE, getter.get(key.getLeaf().getInternalName()));
        properties.add(property);
      }
    }
  }

  private String convertKeyToLocalEntity(final OData odata, final ODataRequest request, EdmEntitySet edmEntitySet,
      JPAEntityType et, Object newPOJO) throws SerializerException, ODataJPAProcessorException {

    final Entity createdEntity = new Entity();

    try {
      final List<JPAPath> keyPath = et.getKeyPath();
      final List<Property> properties = createdEntity.getProperties();

      collectKeyProperties(newPOJO, keyPath, properties);
    } catch (ODataJPAModelException e) {
      throw new ODataJPAProcessorException(e, HttpStatusCode.BAD_REQUEST);
    }

    return request.getRawBaseUri() + '/'
        + odata.createUriHelper().buildCanonicalURL(edmEntitySet, createdEntity);
  }

  private String convertKeyToLocalMap(final OData odata, final ODataRequest request, EdmEntitySet edmEntitySet,
      JPAEntityType et, Map<String, Object> newPOJO) throws SerializerException, ODataJPAProcessorException {

    final Entity createdEntity = new Entity();

    try {
      final List<Property> properties = createdEntity.getProperties();
      final List<JPAPath> keyPath = et.getKeyPath();
      collectKeyProperties(newPOJO, keyPath, properties);
    } catch (ODataJPAModelException e) {
      throw new ODataJPAProcessorException(e, HttpStatusCode.BAD_REQUEST);
    }

    return request.getRawBaseUri() + '/'
        + odata.createUriHelper().buildCanonicalURL(edmEntitySet, createdEntity);
  }

  private ODataDeserializer createDeserializer(final OData odata, final ContentType requestFormat,
      final List<String> version) throws DeserializerException {
    return odata.createDeserializer(requestFormat, version);
  }

  private <T> Object findEnumConstantsByOrdinal(T[] enumConstants, Object value) {
    for (T enumConstant : enumConstants) {
      if (((Enum<?>) enumConstant).ordinal() == (Integer) value)
        return enumConstant;
    }
    return null;
  }

}