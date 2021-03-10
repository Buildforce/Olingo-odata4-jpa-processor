/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri;

import java.util.List;

import nl.buildforce.olingo.commons.api.data.Entity;
import nl.buildforce.olingo.commons.api.data.Property;
import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmEntitySet;
import nl.buildforce.olingo.commons.api.edm.EdmEntityType;
import nl.buildforce.olingo.commons.api.edm.EdmKeyPropertyRef;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveType;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeException;
import nl.buildforce.olingo.commons.api.edm.EdmProperty;
import nl.buildforce.olingo.commons.api.edm.EdmStructuredType;
import nl.buildforce.olingo.commons.core.UrlEncoder;
import nl.buildforce.olingo.server.api.ODataLibraryException;
import nl.buildforce.olingo.server.api.deserializer.DeserializerException.MessageKeys;
import nl.buildforce.olingo.server.api.deserializer.DeserializerException;
import nl.buildforce.olingo.server.api.serializer.SerializerException;
import nl.buildforce.olingo.server.api.uri.UriHelper;
import nl.buildforce.olingo.server.api.uri.UriParameter;
import nl.buildforce.olingo.server.api.uri.UriResource;
import nl.buildforce.olingo.server.api.uri.UriResourceEntitySet;
import nl.buildforce.olingo.server.api.uri.UriResourceKind;
import nl.buildforce.olingo.server.api.uri.queryoption.ExpandOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SelectOption;
import nl.buildforce.olingo.server.core.ODataImpl;
import nl.buildforce.olingo.server.core.serializer.utils.ContextURLHelper;
import nl.buildforce.olingo.server.core.uri.parser.Parser;

public class UriHelperImpl implements UriHelper {

  @Override
  public String buildContextURLSelectList(EdmStructuredType type,
                                          ExpandOption expand, SelectOption select) throws SerializerException {
    return ContextURLHelper.buildSelectList(type, expand, select);
  }

/*
  @Override
  public String buildContextURLKeyPredicate(List<UriParameter> keys) {
    return ContextURLHelper.buildKeyPredicate(keys);
  }
*/

  @Override
  public String buildCanonicalURL(EdmEntitySet edmEntitySet, Entity entity) throws SerializerException {
    return edmEntitySet.getName() + '(' + buildKeyPredicate(edmEntitySet.getEntityType(), entity) + ')';
  }

  @Override
  public String buildKeyPredicate(EdmEntityType edmEntityType, Entity entity) throws SerializerException {
    StringBuilder result = new StringBuilder();
    List<String> keyNames = edmEntityType.getKeyPredicateNames();
    boolean first = true;
    for (String keyName : keyNames) {
      EdmKeyPropertyRef refType = edmEntityType.getKeyPropertyRef(keyName);
      if (first) {
        first = false;
      } else {
        result.append(',');
      }
      if (keyNames.size() > 1) {
        result.append(UrlEncoder.encode(keyName)).append('=');
      }
      EdmProperty edmProperty =  refType.getProperty();
      if (edmProperty == null) {
        throw new SerializerException("Property not found (possibly an alias): " + keyName,
            SerializerException.MessageKeys.MISSING_PROPERTY, keyName);
      }
      EdmPrimitiveType type = (EdmPrimitiveType) edmProperty.getType();
      Object propertyValue = findPropertyRefValue(entity, refType);
      try {
        String value = type.toUriLiteral(
            type.valueToString(propertyValue,
                edmProperty.isNullable(), edmProperty.getMaxLength(),
                edmProperty.getPrecision(), edmProperty.getScale(), edmProperty.isUnicode()));
        result.append(UrlEncoder.encode(value));
      } catch (EdmPrimitiveTypeException e) {
        throw new SerializerException("Wrong key value!", e,
            SerializerException.MessageKeys.WRONG_PROPERTY_VALUE, edmProperty.getName(), 
            propertyValue != null ? propertyValue.toString(): null);
      }
    }
    return result.toString();
  }
  
  private Object findPropertyRefValue(Entity entity, EdmKeyPropertyRef refType) throws SerializerException {
    final int INDEX_ERROR_CODE = -1;
    String propertyPath = refType.getName();
    String tmpPropertyName;
    int lastIndex;
    int index = propertyPath.indexOf('/');
    if (index == INDEX_ERROR_CODE) {
        index  = propertyPath.length();
    }
    tmpPropertyName = propertyPath.substring(0, index);
    //get first property
    Property prop = entity.getProperty(tmpPropertyName);
    //get following properties
    while (index < propertyPath.length()) {
        lastIndex = ++index;
        index = propertyPath.indexOf('/', index+1);
        if (index == INDEX_ERROR_CODE) {
            index = propertyPath.length();
        }
        tmpPropertyName = propertyPath.substring(lastIndex, index);
        prop = findProperty(tmpPropertyName, prop.asComplex().getValue());
     }
    if (prop == null) {
      throw new SerializerException("Key Value Cannot be null for property: " + propertyPath, 
          SerializerException.MessageKeys.NULL_PROPERTY, propertyPath);
    }
    return prop.getValue();
  }

  private Property findProperty(String propertyName, List<Property> properties) {
    for (Property property : properties) {
      if (propertyName.equals(property.getName())) {
        return property;
      }
    }
    return null;
  }
  
/*  @Override
  public UriResourceEntitySet parseEntityId(Edm edm, String entityId, String rawServiceRoot)
      throws DeserializerException {

    String oDataPath = entityId;
    if (rawServiceRoot != null && entityId.startsWith(rawServiceRoot)) {
      oDataPath = entityId.substring(rawServiceRoot.length());
    }
    oDataPath = oDataPath.startsWith("/") ? oDataPath : "/" + oDataPath;

    try {
      List<UriResource> uriResourceParts =
          new Parser(edm, new ODataImpl()).parseUri(oDataPath, null, rawServiceRoot).getUriResourceParts();
      if (uriResourceParts.size() == 1 && uriResourceParts.get(0).getKind() == UriResourceKind.entitySet) {
        return (UriResourceEntitySet) uriResourceParts.get(0);
      }

      throw new DeserializerException("Invalid entity binding link", MessageKeys.INVALID_ENTITY_BINDING_LINK,
          entityId);
    } catch (ODataLibraryException e) {
      throw new DeserializerException("Invalid entity binding link", e, MessageKeys.INVALID_ENTITY_BINDING_LINK,
          entityId);
    }
  }*/

}