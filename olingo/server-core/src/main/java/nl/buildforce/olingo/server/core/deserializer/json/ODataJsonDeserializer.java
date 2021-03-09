package nl.buildforce.olingo.server.core.deserializer.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nl.buildforce.olingo.commons.api.Constants;
import nl.buildforce.olingo.commons.api.IConstants;
import nl.buildforce.olingo.commons.api.constants.Constantsv00;
import nl.buildforce.olingo.commons.api.constants.Constantsv01;
import nl.buildforce.olingo.commons.api.data.ComplexValue;
import nl.buildforce.olingo.commons.api.data.DeletedEntity;
import nl.buildforce.olingo.commons.api.data.DeletedEntity.Reason;
import nl.buildforce.olingo.commons.api.data.Delta;
import nl.buildforce.olingo.commons.api.data.Entity;
import nl.buildforce.olingo.commons.api.data.EntityCollection;
import nl.buildforce.olingo.commons.api.data.Link;
import nl.buildforce.olingo.commons.api.data.Parameter;
import nl.buildforce.olingo.commons.api.data.Property;
import nl.buildforce.olingo.commons.api.data.ValueType;
import nl.buildforce.olingo.commons.api.edm.EdmAction;
import nl.buildforce.olingo.commons.api.edm.EdmComplexType;
import nl.buildforce.olingo.commons.api.edm.EdmEntityType;
import nl.buildforce.olingo.commons.api.edm.EdmEnumType;
import nl.buildforce.olingo.commons.api.edm.EdmMapping;
import nl.buildforce.olingo.commons.api.edm.EdmNavigationProperty;
import nl.buildforce.olingo.commons.api.edm.EdmParameter;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveType;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeException;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import nl.buildforce.olingo.commons.api.edm.EdmProperty;
import nl.buildforce.olingo.commons.api.edm.EdmStructuredType;
import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.commons.api.edm.EdmTypeDefinition;
import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;
import nl.buildforce.olingo.commons.api.edm.constants.EdmTypeKind;
import nl.buildforce.olingo.commons.api.format.ContentType;
import nl.buildforce.olingo.server.api.ServiceMetadata;
import nl.buildforce.olingo.server.api.deserializer.DeserializerException;
import nl.buildforce.olingo.server.api.deserializer.DeserializerException.MessageKeys;
import nl.buildforce.olingo.server.api.deserializer.DeserializerResult;
import nl.buildforce.olingo.server.api.deserializer.ODataDeserializer;
import nl.buildforce.olingo.server.api.serializer.SerializerException;
import nl.buildforce.olingo.server.core.deserializer.DeserializerResultImpl;
import nl.buildforce.olingo.server.core.deserializer.helper.ExpandTreeBuilder;
import nl.buildforce.olingo.server.core.deserializer.helper.ExpandTreeBuilderImpl;
import nl.buildforce.olingo.server.core.serializer.utils.ContentTypeHelper;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Map;

public class ODataJsonDeserializer implements ODataDeserializer {

  private static final String ODATA_ANNOTATION_MARKER = "@";
  private static final String ODATA_CONTROL_INFORMATION_PREFIX = "@odata.";
  private static final String REASON = "reason";

  private final boolean isIEEE754Compatible;
  private ServiceMetadata serviceMetadata;
  private final IConstants constants;

  public ODataJsonDeserializer(ContentType contentType) {
    this(contentType, null, new Constantsv00());
  }

public ODataJsonDeserializer(ContentType contentType, ServiceMetadata serviceMetadata) {
    isIEEE754Compatible = ContentTypeHelper.isODataIEEE754Compatible(contentType);
    this.serviceMetadata = serviceMetadata;
    constants = new Constantsv00();
  }

  public ODataJsonDeserializer(ContentType contentType, ServiceMetadata serviceMetadata, IConstants constants) {
    isIEEE754Compatible = ContentTypeHelper.isODataIEEE754Compatible(contentType);
    this.serviceMetadata = serviceMetadata;
    this.constants = constants;
  }

  public ODataJsonDeserializer(ContentType contentType, IConstants constants) {
    isIEEE754Compatible = ContentTypeHelper.isODataIEEE754Compatible(contentType);
    this.constants = constants;
  }

/*
  @Override
  public DeserializerResult entityCollection(InputStream stream, EdmEntityType edmEntityType)
      throws DeserializerException {
    try {
      return DeserializerResultImpl.with().entityCollection(
          consumeEntityCollectionNode(edmEntityType, parseJsonTree(stream), null))
          .build();
    } catch (IOException e) {
      throw wrapParseException(e);
    }
  }
*/

  private EntityCollection consumeEntityCollectionNode(EdmEntityType edmEntityType, ObjectNode tree,
                                                       ExpandTreeBuilder expandBuilder) throws DeserializerException {
    EntityCollection entitySet = new EntityCollection();

    // Consume entities
    JsonNode jsonNode = tree.get(Constants.VALUE);
    if (jsonNode != null) {
      entitySet.getEntities().addAll(consumeEntitySetArray(edmEntityType, jsonNode, expandBuilder));
      tree.remove(Constants.VALUE);
    } else {
      throw new DeserializerException("Could not find value array.",
          DeserializerException.MessageKeys.VALUE_ARRAY_NOT_PRESENT);
    }

    if (tree.isObject()) {
      removeAnnotations(tree);
    }
    assertJsonNodeIsEmpty(tree);

    return entitySet;
  }

  private List<Entity> consumeEntitySetArray(EdmEntityType edmEntityType, JsonNode jsonNode,
                                             ExpandTreeBuilder expandBuilder) throws DeserializerException {
    if (jsonNode.isArray()) {
      List<Entity> entities = new ArrayList<>();
      for (JsonNode arrayElement : jsonNode) {
        if (arrayElement.isArray() || arrayElement.isValueNode()) {
          throw new DeserializerException("Nested Arrays and primitive values are not allowed for an entity value.",
              DeserializerException.MessageKeys.INVALID_ENTITY);
        }
        EdmEntityType derivedEdmEntityType = (EdmEntityType) getDerivedType(edmEntityType, arrayElement);
        entities.add(consumeEntityNode(derivedEdmEntityType, (ObjectNode) arrayElement, expandBuilder));
      }
      return entities;
    } else {
      throw new DeserializerException("The content of the value tag must be an Array but is not.",
          DeserializerException.MessageKeys.VALUE_TAG_MUST_BE_AN_ARRAY);
    }
  }

  @Override
  public DeserializerResult entity(InputStream stream, EdmEntityType edmEntityType)
      throws DeserializerException {
    try {
      ObjectNode tree = parseJsonTree(stream);
      ExpandTreeBuilder expandBuilder = ExpandTreeBuilderImpl.create();

      EdmEntityType derivedEdmEntityType = (EdmEntityType) getDerivedType(edmEntityType, tree);

      return DeserializerResultImpl.with().entity(consumeEntityNode(derivedEdmEntityType, tree, expandBuilder))
          .expandOption(expandBuilder.build())
          .build();
    } catch (IOException e) {
      throw wrapParseException(e);
    }
  }

  private Entity consumeEntityNode(EdmEntityType edmEntityType, ObjectNode tree,
                                   ExpandTreeBuilder expandBuilder) throws DeserializerException {
    Entity entity = new Entity();
    entity.setType(edmEntityType.getFullQualifiedName().getFullQualifiedNameAsString());
    
    // Check and consume @id for v4.01
    consumeId(tree, entity);

    // Check and consume all Properties
    consumeEntityProperties(edmEntityType, tree, entity);

    // Check and consume all expanded Navigation Properties
    consumeExpandedNavigationProperties(edmEntityType, tree, entity, expandBuilder);

    // consume delta json node fields for v4.01
    consumeDeltaJsonNodeFields(edmEntityType, tree, entity, expandBuilder);
    
    // consume remaining json node fields
    consumeRemainingJsonNodeFields(edmEntityType, tree, entity);

    assertJsonNodeIsEmpty(tree);

    return entity;
  }

  private void consumeDeltaJsonNodeFields(EdmEntityType edmEntityType, ObjectNode node,
      Entity entity, ExpandTreeBuilder expandBuilder) 
      throws DeserializerException {
    if (constants instanceof Constantsv01) {
      List<String> navigationPropertyNames = edmEntityType.getNavigationPropertyNames();
      for (String navigationPropertyName : navigationPropertyNames) {
        // read expanded navigation property for delta
        String delta = navigationPropertyName + Constants.AT + Constants.DELTAVALUE;
        JsonNode jsonNode = node.get(delta);
        EdmNavigationProperty edmNavigationProperty = edmEntityType.getNavigationProperty(navigationPropertyName);
        if (jsonNode != null && jsonNode.isArray() && edmNavigationProperty.isCollection()) {
          checkNotNullOrValidNull(jsonNode, edmNavigationProperty);
          Link link = new Link();
          link.setType(Constants.ENTITY_SET_NAVIGATION_LINK_TYPE);
          link.setTitle(navigationPropertyName);
          Delta deltaValue = new Delta();
          for (JsonNode arrayElement : jsonNode) {
            String removed = Constants.AT + Constants.REMOVED;
            if (arrayElement.get(removed) != null) {
              //if @removed is present create a DeletedEntity Object
              JsonNode reasonNode = arrayElement.get(removed);
              DeletedEntity deletedEntity = new DeletedEntity();
              Reason reason = null;
              if (reasonNode.get(REASON) != null) {
                if(reasonNode.get(REASON).asText().equals(Reason.changed.name())){
                  reason = Reason.changed;
                }else if(reasonNode.get(REASON).asText().equals(Reason.deleted.name())){
                  reason = Reason.deleted;
                }
              }else{
                throw new DeserializerException("DeletedEntity reason is null.",
                    SerializerException.MessageKeys.MISSING_DELTA_PROPERTY, Constants.REASON);
              }
              deletedEntity.setReason(reason);
              try {
                deletedEntity.setId(new URI(arrayElement.get(constants.getId()).asText()));
              } catch (URISyntaxException e) {
                throw new DeserializerException("Could not set Id for deleted Entity", e,
                    DeserializerException.MessageKeys.UNKNOWN_CONTENT);
              }
              deltaValue.getDeletedEntities().add(deletedEntity);
            } else {
              //For @id and properties create normal entity
              Entity inlineEntity = consumeEntityNode(edmEntityType, (ObjectNode) arrayElement, expandBuilder);
              deltaValue.getEntities().add(inlineEntity);
            }
          }
          link.setInlineEntitySet(deltaValue);
          entity.getNavigationLinks().add(link);
          node.remove(navigationPropertyName);
        }
      }
    }

  }

  private void consumeId(ObjectNode node, Entity entity) 
      throws DeserializerException {
    if (node.get(constants.getId()) != null && constants instanceof Constantsv01) {
      try {
        entity.setId(new URI(node.get(constants.getId()).textValue()));
        node.remove(constants.getId());
      } catch (URISyntaxException e) {
        throw new DeserializerException("Could not form Id", e,
            DeserializerException.MessageKeys.UNKNOWN_CONTENT);
      }
    }
  }

  @Override
  public DeserializerResult actionParameters(InputStream stream, EdmAction edmAction)
      throws DeserializerException {
	  Map<String, Parameter> parameters = new HashMap<>();
	  ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	  byte[] inputContent;
    try {
    	IOUtils.copy(stream, byteArrayOutputStream);
    	// copy the content of input stream to reuse it
      	  inputContent = byteArrayOutputStream.toByteArray();
      	  if (inputContent.length > 0) {
      		InputStream inputStream1 = new ByteArrayInputStream(inputContent);
    	      ObjectNode tree = parseJsonTree(inputStream1);
    	      parameters = consumeParameters(edmAction, tree);
    	
    	      if (tree.isObject()) {
    	        removeAnnotations(tree);
    	      }
    	      assertJsonNodeIsEmpty(tree);
      	  }
      return DeserializerResultImpl.with().actionParameters(parameters).build();

    } catch (IOException e) {
      throw wrapParseException(e);
    }
  }

  private ObjectNode parseJsonTree(InputStream stream) throws IOException, DeserializerException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY, true);
    JsonParser parser = new JsonFactory(objectMapper).createParser(stream);
    JsonNode tree = parser.getCodec().readTree(parser);
    if (tree == null || !tree.isObject()) {
      throw new DeserializerException("Invalid JSON syntax.",
          DeserializerException.MessageKeys.JSON_SYNTAX_EXCEPTION);
    }
    return (ObjectNode) tree;
  }

  private Map<String, Parameter> consumeParameters(EdmAction edmAction, ObjectNode node)
      throws DeserializerException {
    List<String> parameterNames = edmAction.getParameterNames();
    if (edmAction.isBound()) {
      // The binding parameter must not occur in the payload.
      parameterNames = parameterNames.subList(1, parameterNames.size());
    }
    Map<String, Parameter> parameters = new LinkedHashMap<>();
    for (String paramName : parameterNames) {
      EdmParameter edmParameter = edmAction.getParameter(paramName);

        switch (edmParameter.getType().getKind()) {
            case PRIMITIVE, DEFINITION, ENUM, COMPLEX, ENTITY -> {
                Parameter parameter = createParameter(node.get(paramName), paramName, edmParameter);
                parameters.put(paramName, parameter);
                node.remove(paramName);
            }
            default -> throw new DeserializerException(
                    "Invalid type kind " + edmParameter.getType().getKind() + " for action parameter: " + paramName,
                    MessageKeys.INVALID_ACTION_PARAMETER_TYPE, paramName);
        }
    }
    return parameters;
  }

  private Parameter createParameter(JsonNode node, String paramName, EdmParameter edmParameter)
      throws DeserializerException {
    Parameter parameter = new Parameter();
    parameter.setName(paramName);
    if (node == null || node.isNull()) {
      if (!edmParameter.isNullable()) {
        throw new DeserializerException("Non-nullable parameter not present or null: " + paramName,
            MessageKeys.INVALID_NULL_PARAMETER, paramName);
      }
      if (edmParameter.isCollection()) {
        throw new DeserializerException("Collection must not be null for parameter: " + paramName,
            MessageKeys.INVALID_NULL_PARAMETER, paramName);
      }
      parameter.setValue(ValueType.PRIMITIVE, null);
    } else if (edmParameter.getType().getKind() == EdmTypeKind.ENTITY) {
      if (edmParameter.isCollection()) {
        EntityCollection entityCollection = new EntityCollection();
        entityCollection.getEntities().addAll(
            consumeEntitySetArray((EdmEntityType) edmParameter.getType(), node, null));
        parameter.setValue(ValueType.COLLECTION_ENTITY, entityCollection);
      } else {
        Entity entity = consumeEntityNode((EdmEntityType) edmParameter.getType(), (ObjectNode) node, null);
        parameter.setValue(ValueType.ENTITY, entity);
      }
    } else {
      Property property =
          consumePropertyNode(edmParameter.getName(), edmParameter.getType(), edmParameter.isCollection(),
              edmParameter.isNullable(), edmParameter.getMaxLength(),
              edmParameter.getPrecision(), edmParameter.getScale(), true, edmParameter.getMapping(), node);
      parameter.setValue(property.getValueType(), property.getValue());
      parameter.setType(property.getType());
    }
    return parameter;
  }

  /** Reads a parameter value from a String. */
  public Parameter parameter(String content, EdmParameter parameter) throws DeserializerException {
    try {
      JsonParser parser = new JsonFactory(new ObjectMapper()
          .configure(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY, true))
              .createParser(content);
      JsonNode node = parser.getCodec().readTree(parser);
      if (node == null) {
        throw new DeserializerException("Invalid JSON syntax.",
            DeserializerException.MessageKeys.JSON_SYNTAX_EXCEPTION);
      }
      Parameter result = createParameter(node, parameter.getName(), parameter);
      if (node.isObject()) {
        removeAnnotations((ObjectNode) node);
        assertJsonNodeIsEmpty(node);
      }
      return result;
    } catch (IOException e) {
      throw wrapParseException(e);
    }
  }

  /**
   * Consumes all remaining fields of Json ObjectNode and tries to map found values
   * to according Entity fields and omits OData fields to be ignored (e.g., control information).
   *
   * @param edmEntityType edm entity type which for which the json node is consumed
   * @param node json node which is consumed
   * @param entity entity instance which is filled
   * @throws DeserializerException if an exception during digest occurs
   */
  private void consumeRemainingJsonNodeFields(EdmEntityType edmEntityType, ObjectNode node,
                                              Entity entity) throws DeserializerException {
    List<String> toRemove = new ArrayList<>();
    Iterator<Entry<String, JsonNode>> fieldsIterator = node.fields();
    while (fieldsIterator.hasNext()) {
      Entry<String, JsonNode> field = fieldsIterator.next();

      if (field.getKey().contains(constants.getBind())) {
        Link bindingLink = consumeBindingLink(field.getKey(), field.getValue(), edmEntityType);
        entity.getNavigationBindings().add(bindingLink);
        toRemove.add(field.getKey());
      }
    }
    // remove here to avoid iterator issues.
    node.remove(toRemove);

    removeAnnotations(node);
  }

  private void consumeEntityProperties(EdmEntityType edmEntityType, ObjectNode node,
                                       Entity entity) throws DeserializerException {
    List<String> propertyNames = edmEntityType.getPropertyNames();
    for (String propertyName : propertyNames) {
      JsonNode jsonNode = node.get(propertyName);
      if (jsonNode != null) {
        EdmProperty edmProperty = (EdmProperty) edmEntityType.getProperty(propertyName);
        if (jsonNode.isNull() && !edmProperty.isNullable()) {
          throw new DeserializerException("Property: " + propertyName + " must not be null.",
              DeserializerException.MessageKeys.INVALID_NULL_PROPERTY, propertyName);
        }
        Property property = consumePropertyNode(edmProperty.getName(), edmProperty.getType(),
            edmProperty.isCollection(), edmProperty.isNullable(), edmProperty.getMaxLength(),
            edmProperty.getPrecision(), edmProperty.getScale(), edmProperty.isUnicode(), edmProperty.getMapping(),
            jsonNode);
        entity.addProperty(property);
        node.remove(propertyName);
      }
    }
  }

  private void consumeExpandedNavigationProperties(EdmEntityType edmEntityType, ObjectNode node,
                                                   Entity entity, ExpandTreeBuilder expandBuilder) throws DeserializerException {
    List<String> navigationPropertyNames = edmEntityType.getNavigationPropertyNames();
    for (String navigationPropertyName : navigationPropertyNames) {
      // read expanded navigation property
      JsonNode jsonNode = node.get(navigationPropertyName);
      if (jsonNode != null) {
        EdmNavigationProperty edmNavigationProperty = edmEntityType.getNavigationProperty(navigationPropertyName);
        checkNotNullOrValidNull(jsonNode, edmNavigationProperty);

        Link link = createLink(expandBuilder, navigationPropertyName, jsonNode, edmNavigationProperty);
        entity.getNavigationLinks().add(link);
        node.remove(navigationPropertyName);
      }
    }
  }

  /**
   * Check if jsonNode is not null or if null but nullable or collection navigationProperty
   *
   * @param jsonNode related json node
   * @param edmNavigationProperty related navigation property
   * @throws DeserializerException if jsonNode is not null or if null but nullable or collection navigationProperty
   */
  private void checkNotNullOrValidNull(JsonNode jsonNode,
                                       EdmNavigationProperty edmNavigationProperty) throws DeserializerException {
    boolean isNullable = edmNavigationProperty.isNullable();
    if ((jsonNode.isNull() && !isNullable) || (jsonNode.isNull() && edmNavigationProperty.isCollection())) {
      throw new DeserializerException("Property: " + edmNavigationProperty.getName() + " must not be null.",
          MessageKeys.INVALID_NULL_PROPERTY, edmNavigationProperty.getName());
    }
  }

  private Link createLink(ExpandTreeBuilder expandBuilder, String navigationPropertyName,
                          JsonNode jsonNode,
                          EdmNavigationProperty edmNavigationProperty) throws DeserializerException {
    Link link = new Link();
    link.setTitle(navigationPropertyName);
    ExpandTreeBuilder childExpandBuilder = (expandBuilder != null) ? expandBuilder.expand(edmNavigationProperty)
        : null;
    EdmEntityType derivedEdmEntityType = (EdmEntityType) getDerivedType(
        edmNavigationProperty.getType(), jsonNode);
    if (jsonNode.isArray() && edmNavigationProperty.isCollection()) {
      link.setType(Constants.ENTITY_SET_NAVIGATION_LINK_TYPE);
      EntityCollection inlineEntitySet = new EntityCollection();
      inlineEntitySet.getEntities().addAll(
          consumeEntitySetArray(derivedEdmEntityType, jsonNode, childExpandBuilder));
      link.setInlineEntitySet(inlineEntitySet);
    } else if (!jsonNode.isArray() && (!jsonNode.isValueNode() || jsonNode.isNull())
        && !edmNavigationProperty.isCollection()) {
      link.setType(Constants.ENTITY_NAVIGATION_LINK_TYPE);
      if (!jsonNode.isNull()) {
        Entity inlineEntity = consumeEntityNode(derivedEdmEntityType, (ObjectNode) jsonNode, childExpandBuilder);
        link.setInlineEntity(inlineEntity);
      }
    } else {
      throw new DeserializerException("Invalid value: " + jsonNode.getNodeType()
          + " for expanded navigation property: " + navigationPropertyName,
          MessageKeys.INVALID_VALUE_FOR_NAVIGATION_PROPERTY, navigationPropertyName);
    }
    return link;
  }
  
  private Link consumeBindingLink(String key, JsonNode jsonNode, EdmEntityType edmEntityType)
      throws DeserializerException {
    String[] splitKey = key.split(ODATA_ANNOTATION_MARKER);
    String navigationPropertyName = splitKey[0];
    EdmNavigationProperty edmNavigationProperty = edmEntityType.getNavigationProperty(navigationPropertyName);
    if (edmNavigationProperty == null) {
      throw new DeserializerException("Invalid navigationPropertyName: " + navigationPropertyName,
          DeserializerException.MessageKeys.NAVIGATION_PROPERTY_NOT_FOUND, navigationPropertyName);
    }
    Link bindingLink = new Link();
    bindingLink.setTitle(navigationPropertyName);

    if (edmNavigationProperty.isCollection()) {
      assertIsNullNode(key, jsonNode);
      if (!jsonNode.isArray()) {
        throw new DeserializerException("Binding annotation: " + key + " must be an array.",
            DeserializerException.MessageKeys.INVALID_ANNOTATION_TYPE, key);
      }
      List<String> bindingLinkStrings = new ArrayList<>();
      for (JsonNode arrayValue : jsonNode) {
        assertIsNullNode(key, arrayValue);
        if (!arrayValue.isTextual()) {
          throw new DeserializerException("Binding annotation: " + key + " must have string valued array.",
              DeserializerException.MessageKeys.INVALID_ANNOTATION_TYPE, key);
        }
        bindingLinkStrings.add(arrayValue.asText());
      }
      bindingLink.setType(Constants.ENTITY_COLLECTION_BINDING_LINK_TYPE);
      bindingLink.setBindingLinks(bindingLinkStrings);
    } else {
      if (!jsonNode.isValueNode()) {
        throw new DeserializerException("Binding annotation: " + key + " must be a string value.",
            DeserializerException.MessageKeys.INVALID_ANNOTATION_TYPE, key);
      }
      if (edmNavigationProperty.isNullable() && jsonNode.isNull()) {
        bindingLink.setBindingLink(null);
      } else {
        assertIsNullNode(key, jsonNode);
        bindingLink.setBindingLink(jsonNode.asText());        
      }
      bindingLink.setType(Constants.ENTITY_BINDING_LINK_TYPE);
    }
    return bindingLink;
  }

  private void assertIsNullNode(String key, JsonNode jsonNode) throws DeserializerException {
    if (jsonNode.isNull()) {
      throw new DeserializerException("Annotation: " + key + "must not have a null value.",
          DeserializerException.MessageKeys.INVALID_NULL_ANNOTATION, key);
    }
  }

  private Property consumePropertyNode(String name, EdmType type, boolean isCollection,
                                       boolean isNullable, Integer maxLength, Integer precision, Integer scale,
                                       boolean isUnicode, EdmMapping mapping, JsonNode jsonNode) throws DeserializerException {
    Property property = new Property();
    property.setName(name);
    property.setType(type.getFullQualifiedName().getFullQualifiedNameAsString());
    if (isCollection) {
      consumePropertyCollectionNode(name, type, isNullable, maxLength, precision, scale, isUnicode, mapping, jsonNode,
          property);
    } else {
      consumePropertySingleNode(name, type, isNullable, maxLength, precision, scale, isUnicode, mapping, jsonNode,
          property);
    }
    return property;
  }

  private void consumePropertySingleNode(String name, EdmType type,
                                         boolean isNullable, Integer maxLength, Integer precision, Integer scale,
                                         boolean isUnicode, EdmMapping mapping, JsonNode jsonNode, Property property)
      throws DeserializerException {
    switch (type.getKind()) {
    case PRIMITIVE:
    case DEFINITION:
    case ENUM:
      Object value = readPrimitiveValue(name, (EdmPrimitiveType) type,
          isNullable, maxLength, precision, scale, isUnicode, mapping, jsonNode);
      property.setValue(type.getKind() == EdmTypeKind.ENUM ? ValueType.ENUM : ValueType.PRIMITIVE,
          value);
      break;
    case COMPLEX:
      EdmType derivedType = getDerivedType((EdmComplexType) type,
          jsonNode);
      property.setType(derivedType.getFullQualifiedName()
          .getFullQualifiedNameAsString());

      value = readComplexNode(name, derivedType, isNullable, jsonNode);
      property.setValue(ValueType.COMPLEX, value);
      break;
    default:
      throw new DeserializerException("Invalid Type Kind for a property found: " + type.getKind(),
          DeserializerException.MessageKeys.INVALID_JSON_TYPE_FOR_PROPERTY, name);
    }
  }

  private Object readComplexNode(String name, EdmType type, boolean isNullable,
                                 JsonNode jsonNode)
      throws DeserializerException {
    // read and add all complex properties
    ComplexValue value = readComplexValue(name, type, isNullable, jsonNode);

    if (jsonNode.isObject()) {
      removeAnnotations((ObjectNode) jsonNode);
    }
    // Afterwards the node must be empty
    assertJsonNodeIsEmpty(jsonNode);

    return value;
  }

  private void consumePropertyCollectionNode(String name, EdmType type,
                                             boolean isNullable, Integer maxLength, Integer precision, Integer scale,
                                             boolean isUnicode, EdmMapping mapping, JsonNode jsonNode, Property property)
      throws DeserializerException {
    if (!jsonNode.isArray()) {
      throw new DeserializerException("Value for property: " + name + " must be an array but is not.",
          DeserializerException.MessageKeys.INVALID_JSON_TYPE_FOR_PROPERTY, name);
    }
    List<Object> valueArray = new ArrayList<>();
    Iterator<JsonNode> iterator = jsonNode.iterator();
      switch (type.getKind()) {
          case PRIMITIVE, DEFINITION, ENUM -> {
              while (iterator.hasNext()) {
                  JsonNode arrayElement = iterator.next();
                  Object value = readPrimitiveValue(name, (EdmPrimitiveType) type,
                          isNullable, maxLength, precision, scale, isUnicode, mapping, arrayElement);
                  valueArray.add(value);
              }
              property.setValue(type.getKind() == EdmTypeKind.ENUM ? ValueType.COLLECTION_ENUM : ValueType.COLLECTION_PRIMITIVE,
                      valueArray);
          }
          case COMPLEX -> {
              while (iterator.hasNext()) {
                  // read and add all complex properties
                  Object value = readComplexNode(name, type, isNullable, iterator.next());
                  valueArray.add(value);
              }
              property.setValue(ValueType.COLLECTION_COMPLEX, valueArray);
          }
          default -> throw new DeserializerException("Invalid Type Kind for a property found: " + type.getKind(),
                  MessageKeys.INVALID_JSON_TYPE_FOR_PROPERTY, name);
      }
  }

  private ComplexValue readComplexValue(String name, EdmType type,
                                        boolean isNullable, JsonNode jsonNode) throws DeserializerException {
    if (isValidNull(name, isNullable, jsonNode)) {
      return null;
    }
    if (jsonNode.isArray() || !jsonNode.isContainerNode()) {
      throw new DeserializerException(
          "Invalid value for property: " + name + " must not be an array or primitive value.",
          DeserializerException.MessageKeys.INVALID_JSON_TYPE_FOR_PROPERTY, name);
    }
    // Even if there are no properties defined we have to give back an empty list
    ComplexValue complexValue = new ComplexValue();
    EdmComplexType edmType = (EdmComplexType) type;
    
    //Check if the properties are from derived type
    edmType = (EdmComplexType) getDerivedType(edmType, jsonNode);
    
    // Check and consume all Properties
    for (String propertyName : edmType.getPropertyNames()) {
      JsonNode subNode = jsonNode.get(propertyName);
      if (subNode != null) {
        EdmProperty edmProperty = (EdmProperty) edmType.getProperty(propertyName);
        if (subNode.isNull() && !edmProperty.isNullable()) {
          throw new DeserializerException("Property: " + propertyName + " must not be null.",
              DeserializerException.MessageKeys.INVALID_NULL_PROPERTY, propertyName);
        }
        Property property = consumePropertyNode(edmProperty.getName(), edmProperty.getType(),
            edmProperty.isCollection(),
            edmProperty.isNullable(), edmProperty.getMaxLength(), edmProperty.getPrecision(), edmProperty.getScale(),
            edmProperty.isUnicode(), edmProperty.getMapping(),
            subNode);
        complexValue.getValue().add(property);
        ((ObjectNode) jsonNode).remove(propertyName);
      }
    }
    complexValue.setTypeName(edmType.getFullQualifiedName().getFullQualifiedNameAsString());
    return complexValue;
  }

  private Object readPrimitiveValue(String name, EdmPrimitiveType type,
                                    boolean isNullable, Integer maxLength, Integer precision, Integer scale,
                                    boolean isUnicode, EdmMapping mapping, JsonNode jsonNode) throws DeserializerException {
    if (isValidNull(name, isNullable, jsonNode)) {
      return null;
    }
    checkForValueNode(name, jsonNode);
    checkJsonTypeBasedOnPrimitiveType(name, type, jsonNode);
    try {
      return type.valueOfString(jsonNode.asText(),
          isNullable, maxLength, precision, scale, isUnicode,
          getJavaClassForPrimitiveType(mapping, type));
    } catch (EdmPrimitiveTypeException e) {
      throw new DeserializerException(
          "Invalid value: " + jsonNode.asText() + " for property: " + name, e,
          DeserializerException.MessageKeys.INVALID_VALUE_FOR_PROPERTY, name);
    }
  }

  private boolean isValidNull(String name, boolean isNullable, JsonNode jsonNode)
      throws DeserializerException {
    if (jsonNode.isNull()) {
      if (isNullable) {
        return true;
      } else {
        throw new DeserializerException("Property: " + name + " must not be null.",
            DeserializerException.MessageKeys.INVALID_NULL_PROPERTY, name);
      }
    }
    return false;
  }

  /**
   * Returns the primitive type's default class or the manually mapped class if present.
   * @param mapping
   * @param primitiveType
   * @return the java class to be used during deserialization
   */
  private Class<?> getJavaClassForPrimitiveType(EdmMapping mapping, EdmPrimitiveType primitiveType) {
    return (mapping == null || mapping.getMappedJavaClass() == null) ?
            (primitiveType.getKind() == EdmTypeKind.ENUM ?
                    ((EdmEnumType) primitiveType).getUnderlyingType() : primitiveType.getKind() == EdmTypeKind.DEFINITION ?
                    ((EdmTypeDefinition) primitiveType).getUnderlyingType() : primitiveType).getDefaultType()
            : mapping.getMappedJavaClass();
  }

  /**
   * Check if JsonNode is a value node (<code>jsonNode.isValueNode()</code>) and if not throw
   * an DeserializerException.
   * @param name name of property which is checked
   * @param jsonNode node which is checked
   * @throws DeserializerException is thrown if json node is not a value node
   */
  private void checkForValueNode(String name, JsonNode jsonNode) throws DeserializerException {
    if (!jsonNode.isValueNode()) {
      throw new DeserializerException("Invalid value for property: " + name + " must not be an object or array.",
          DeserializerException.MessageKeys.INVALID_JSON_TYPE_FOR_PROPERTY, name);
    }
  }

  private void removeAnnotations(ObjectNode tree) throws DeserializerException {
    List<String> toRemove = new ArrayList<>();
    Iterator<Entry<String, JsonNode>> fieldsIterator = tree.fields();
    while (fieldsIterator.hasNext()) {
      Map.Entry<String, JsonNode> field = fieldsIterator.next();

      if (field.getKey().contains(ODATA_CONTROL_INFORMATION_PREFIX)) {
        // Control Information is ignored for requests as per specification chapter "4.5 Control Information"
        toRemove.add(field.getKey());
      } else if (field.getKey().contains(ODATA_ANNOTATION_MARKER)) {
        if(constants instanceof Constantsv01){
          toRemove.add(field.getKey());
        }else{
          throw new DeserializerException("Custom annotation with field name: " + field.getKey() + " not supported",
            DeserializerException.MessageKeys.NOT_IMPLEMENTED);
        }
      }
    }
    // remove here to avoid iterator issues.
    tree.remove(toRemove);
  }

  /**
   * Validates that node is empty (<code>node.size() == 0</code>).
   * @param node node to be checked
   * @throws DeserializerException if node is not empty
   */
  private void assertJsonNodeIsEmpty(JsonNode node) throws DeserializerException {
    if (node.size() != 0) {
      String unknownField = node.fieldNames().next();
      throw new DeserializerException("Tree should be empty but still has content left: " + unknownField,
          DeserializerException.MessageKeys.UNKNOWN_CONTENT, unknownField);
    }
  }

  private void checkJsonTypeBasedOnPrimitiveType(String propertyName, EdmPrimitiveType edmPrimitiveType,
                                                 JsonNode jsonNode) throws DeserializerException {
    boolean valid = true;
    if (edmPrimitiveType.getKind() == EdmTypeKind.DEFINITION) {
      checkJsonTypeBasedOnPrimitiveType(propertyName,
          ((EdmTypeDefinition) edmPrimitiveType).getUnderlyingType(), jsonNode);
    } else if (edmPrimitiveType.getKind() == EdmTypeKind.ENUM) {
      // Enum values must be strings.
      valid = jsonNode.isTextual();
    } else {
      String name = edmPrimitiveType.getName();
      EdmPrimitiveTypeKind primKind;
      try {
        primKind = EdmPrimitiveTypeKind.valueOf(name);
      } catch (IllegalArgumentException e) {
        throw new DeserializerException("Unknown Primitive Type: " + name, e,
            DeserializerException.MessageKeys.UNKNOWN_PRIMITIVE_TYPE, name, propertyName);
      }
      valid = matchTextualCase(jsonNode, primKind)
          || matchNumberCase(jsonNode, primKind)
          || matchBooleanCase(jsonNode, primKind)
          || matchIEEENumberCase(jsonNode, primKind);
    }
    if (!valid) {
      throw new DeserializerException(
          "Invalid json type: " + jsonNode.getNodeType() + " for " + edmPrimitiveType + " property: " + propertyName,
          DeserializerException.MessageKeys.INVALID_VALUE_FOR_PROPERTY, propertyName);
    }
  }

  private boolean matchIEEENumberCase(JsonNode node, EdmPrimitiveTypeKind primKind) {
    return (isIEEE754Compatible ? node.isTextual() : node.isNumber())
        && (primKind == EdmPrimitiveTypeKind.Int64 || primKind == EdmPrimitiveTypeKind.Decimal);
  }

  private boolean matchBooleanCase(JsonNode node, EdmPrimitiveTypeKind primKind) {
    return node.isBoolean() && primKind == EdmPrimitiveTypeKind.Boolean;
  }

  private boolean matchNumberCase(JsonNode node, EdmPrimitiveTypeKind primKind) {
    return node.isNumber() &&
        (primKind == EdmPrimitiveTypeKind.Int16
            || primKind == EdmPrimitiveTypeKind.Int32
            || primKind == EdmPrimitiveTypeKind.Byte
            || primKind == EdmPrimitiveTypeKind.SByte
            || primKind == EdmPrimitiveTypeKind.Single
            || primKind == EdmPrimitiveTypeKind.Double);
  }

  private boolean matchTextualCase(JsonNode node, EdmPrimitiveTypeKind primKind) {
    return node.isTextual() &&
        (primKind == EdmPrimitiveTypeKind.String
            || primKind == EdmPrimitiveTypeKind.Binary
            || primKind == EdmPrimitiveTypeKind.Date
            || primKind == EdmPrimitiveTypeKind.DateTimeOffset
            || primKind == EdmPrimitiveTypeKind.Duration
            || primKind == EdmPrimitiveTypeKind.Guid
            || primKind == EdmPrimitiveTypeKind.TimeOfDay);
  }

  @Override
  public DeserializerResult property(InputStream stream, EdmProperty edmProperty)
      throws DeserializerException {
    try {
      ObjectNode tree = parseJsonTree(stream);

      Property property;
      JsonNode jsonNode = tree.get(Constants.VALUE);
      if (jsonNode != null) {
        property = consumePropertyNode(edmProperty.getName(), edmProperty.getType(),
            edmProperty.isCollection(),
            edmProperty.isNullable(), edmProperty.getMaxLength(), edmProperty.getPrecision(), edmProperty.getScale(),
            edmProperty.isUnicode(), edmProperty.getMapping(),
            jsonNode);
        tree.remove(Constants.VALUE);
      } else {
        property = consumePropertyNode(edmProperty.getName(), edmProperty.getType(),
            edmProperty.isCollection(),
            edmProperty.isNullable(), edmProperty.getMaxLength(), edmProperty.getPrecision(), edmProperty.getScale(),
            edmProperty.isUnicode(), edmProperty.getMapping(),
            tree);
      }
      return DeserializerResultImpl.with().property(property).build();
    } catch (IOException e) {
      throw wrapParseException(e);
    }
  }

  @Override
  public DeserializerResult entityReferences(InputStream stream) throws DeserializerException {
    try {
      List<URI> parsedValues = new ArrayList<>();
      ObjectNode tree = parseJsonTree(stream);
      String key = constants.getId();
      JsonNode jsonNode = tree.get(Constants.VALUE);
      if (jsonNode != null) {
        if (jsonNode.isArray()) {
          ArrayNode arrayNode = (ArrayNode) jsonNode;
          for (JsonNode next : arrayNode) {
            if (next.has(key)) {
              parsedValues.add(new URI(next.get(key).asText()));
            }
          }
        } else {
          throw new DeserializerException("Value must be an array", DeserializerException.MessageKeys.UNKNOWN_CONTENT);
        }
        tree.remove(Constants.VALUE);
        return DeserializerResultImpl.with().entityReferences(parsedValues).build();
      }
      if (tree.get(key) != null) {
        parsedValues.add(new URI(tree.get(key).asText()));
      } else {
        throw new DeserializerException("Missing entity reference", DeserializerException.MessageKeys.UNKNOWN_CONTENT);
      }
      return DeserializerResultImpl.with().entityReferences(parsedValues).build();
    } catch (IOException e) {
      throw wrapParseException(e);
    } catch (URISyntaxException e) {
      throw new DeserializerException("failed to read @odata.id", e,
          DeserializerException.MessageKeys.UNKNOWN_CONTENT);
    }
  }

  private DeserializerException wrapParseException(IOException e) {
    if (e instanceof JsonParseException) {
      return new DeserializerException("A JsonParseException occurred.", e,
          DeserializerException.MessageKeys.JSON_SYNTAX_EXCEPTION);
    } else if (e instanceof JsonMappingException) {
      return new DeserializerException("Duplicate json property detected.", e,
          DeserializerException.MessageKeys.DUPLICATE_PROPERTY);
    } else {
      return new DeserializerException("An IOException occurred.", e,
          DeserializerException.MessageKeys.IO_EXCEPTION);
    }
  }

  private EdmType getDerivedType(EdmStructuredType edmType, JsonNode jsonNode)
      throws DeserializerException {
    JsonNode odataTypeNode = jsonNode.get(constants.getType());
    if (odataTypeNode != null) {
      String odataType = odataTypeNode.asText();
      if (!odataType.isEmpty()) {
        odataType = odataType.substring(1);

        if (odataType.equalsIgnoreCase(edmType.getFullQualifiedName().getFullQualifiedNameAsString())) {
          return edmType;
        } else if (serviceMetadata == null) {
          throw new DeserializerException(
              "Failed to resolve Odata type " + odataType + " due to metadata is not available",
              DeserializerException.MessageKeys.UNKNOWN_CONTENT);
        }

        EdmStructuredType currentEdmType = edmType.getKind() == EdmTypeKind.ENTITY ?
            serviceMetadata.getEdm().getEntityType(new FullQualifiedName(odataType)) :
            serviceMetadata.getEdm().getComplexType(new FullQualifiedName(odataType));
        if (!isAssignable(edmType, currentEdmType)) {
          throw new DeserializerException("Odata type " + odataType + " not allowed here",
              DeserializerException.MessageKeys.UNKNOWN_CONTENT);
        }

        return currentEdmType;
      }
    }
    return edmType;
  }

  private boolean isAssignable(EdmStructuredType edmStructuredType,
                               EdmStructuredType edmStructuredTypeToAssign) {
    return edmStructuredTypeToAssign != null
        && (edmStructuredType.getFullQualifiedName().equals(edmStructuredTypeToAssign.getFullQualifiedName())
            || isAssignable(edmStructuredType, edmStructuredTypeToAssign.getBaseType()));
  }

}