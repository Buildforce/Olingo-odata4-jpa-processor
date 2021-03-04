/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.serializer.json;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.buildforce.olingo.server.api.ODataServerError;
import nl.buildforce.olingo.server.api.ServiceMetadata;
import nl.buildforce.olingo.server.api.uri.UriHelper;
import nl.buildforce.olingo.server.api.uri.queryoption.ExpandItem;
import nl.buildforce.olingo.server.core.ODataWritableContent;
import nl.buildforce.olingo.server.core.uri.UriHelperImpl;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import nl.buildforce.olingo.commons.api.Constants;
import nl.buildforce.olingo.commons.api.IConstants;
import nl.buildforce.olingo.commons.api.constants.Constantsv00;

import nl.buildforce.olingo.commons.api.data.AbstractEntityCollection;
import nl.buildforce.olingo.commons.api.data.ComplexValue;
import nl.buildforce.olingo.commons.api.data.ContextURL;
import nl.buildforce.olingo.commons.api.data.Entity;
import nl.buildforce.olingo.commons.api.data.EntityIterator;
import nl.buildforce.olingo.commons.api.data.Link;
import nl.buildforce.olingo.commons.api.data.Linked;
import nl.buildforce.olingo.commons.api.data.Operation;
import nl.buildforce.olingo.commons.api.data.Property;
import nl.buildforce.olingo.commons.api.data.ValueType;
import nl.buildforce.olingo.commons.api.edm.EdmComplexType;
import nl.buildforce.olingo.commons.api.edm.EdmEntitySet;
import nl.buildforce.olingo.commons.api.edm.EdmEntityType;
import nl.buildforce.olingo.commons.api.edm.EdmNavigationProperty;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveType;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeException;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import nl.buildforce.olingo.commons.api.edm.EdmProperty;
import nl.buildforce.olingo.commons.api.edm.EdmStructuredType;
import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;
import nl.buildforce.olingo.commons.api.edm.constants.EdmTypeKind;
import nl.buildforce.olingo.commons.api.format.ContentType;
import nl.buildforce.olingo.commons.core.edm.primitivetype.EdmPrimitiveTypeFactory;
import nl.buildforce.olingo.server.api.serializer.ComplexSerializerOptions;
import nl.buildforce.olingo.server.api.serializer.EntityCollectionSerializerOptions;
import nl.buildforce.olingo.server.api.serializer.EntitySerializerOptions;
import nl.buildforce.olingo.server.api.serializer.PrimitiveSerializerOptions;
import nl.buildforce.olingo.server.api.serializer.ReferenceCollectionSerializerOptions;
import nl.buildforce.olingo.server.api.serializer.ReferenceSerializerOptions;
import nl.buildforce.olingo.server.api.serializer.SerializerException;
import nl.buildforce.olingo.server.api.serializer.SerializerResult;
import nl.buildforce.olingo.server.api.serializer.SerializerStreamResult;
import nl.buildforce.olingo.server.api.uri.queryoption.CountOption;
import nl.buildforce.olingo.server.api.uri.queryoption.ExpandOption;
import nl.buildforce.olingo.server.api.uri.queryoption.LevelsExpandOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SelectOption;
import nl.buildforce.olingo.server.core.serializer.AbstractODataSerializer;
import nl.buildforce.olingo.server.core.serializer.SerializerResultImpl;
import nl.buildforce.olingo.server.core.serializer.utils.CircleStreamBuffer;
import nl.buildforce.olingo.server.core.serializer.utils.ContentTypeHelper;
import nl.buildforce.olingo.server.core.serializer.utils.ContextURLBuilder;
import nl.buildforce.olingo.server.core.serializer.utils.ExpandSelectHelper;
import nl.buildforce.olingo.server.core.uri.queryoption.ExpandOptionImpl;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

public class ODataJsonSerializer extends AbstractODataSerializer {

  private final boolean isIEEE754Compatible;
  private final boolean isODataMetadataNone;
  private final boolean isODataMetadataFull;
  private final IConstants constants;

  public ODataJsonSerializer(ContentType contentType, IConstants constants) {
    isIEEE754Compatible = ContentTypeHelper.isODataIEEE754Compatible(contentType);
    isODataMetadataNone = ContentTypeHelper.isODataMetadataNone(contentType);
    isODataMetadataFull = ContentTypeHelper.isODataMetadataFull(contentType);
    this.constants = constants;
  }

  public ODataJsonSerializer(ContentType contentType) {
    isIEEE754Compatible = ContentTypeHelper.isODataIEEE754Compatible(contentType);
    isODataMetadataNone = ContentTypeHelper.isODataMetadataNone(contentType);
    isODataMetadataFull = ContentTypeHelper.isODataMetadataFull(contentType);
      constants = new Constantsv00();
  }

  @Override
  public SerializerResult serviceDocument(ServiceMetadata metadata, String serviceRoot)
      throws SerializerException {
    OutputStream outputStream = null;
    SerializerException cachedException = null;

    CircleStreamBuffer buffer = new CircleStreamBuffer();
    outputStream = buffer.getOutputStream();
    try (JsonGenerator json = new JsonFactory().createGenerator(outputStream)) {
      new ServiceDocumentJsonSerializer(metadata, serviceRoot, isODataMetadataNone).writeServiceDocument(json);

      json.close();
      return SerializerResultImpl.with().content(buffer.getInputStream()).build();
    } catch (IOException e) {
    cachedException =
        new SerializerException(IO_EXCEPTION_TEXT, e, SerializerException.MessageKeys.IO_EXCEPTION);
    throw cachedException;
    } finally {
      closeCircleStreamBufferOutput(outputStream, cachedException);
    }
  }

  @Override
  public SerializerResult metadataDocument(ServiceMetadata serviceMetadata) throws SerializerException {
    OutputStream outputStream = null;
    SerializerException cachedException = null;

    
    CircleStreamBuffer buffer = new CircleStreamBuffer();
    outputStream = buffer.getOutputStream();
    try (JsonGenerator json = new JsonFactory().createGenerator(outputStream)) {
      new MetadataDocumentJsonSerializer(serviceMetadata).writeMetadataDocument(json);
      json.close();
      return SerializerResultImpl.with().content(buffer.getInputStream()).build();
    } catch (IOException e) {
      cachedException =
          new SerializerException(IO_EXCEPTION_TEXT, e, SerializerException.MessageKeys.IO_EXCEPTION);
      throw cachedException;
    } finally {
      closeCircleStreamBufferOutput(outputStream, cachedException);
    }
  }

  @Override
  public SerializerResult error(ODataServerError error) throws SerializerException {
    OutputStream outputStream = null;
    SerializerException cachedException = null;
    
    CircleStreamBuffer buffer = new CircleStreamBuffer();
    outputStream = buffer.getOutputStream();
    try (JsonGenerator json = new JsonFactory().createGenerator(outputStream)) {
      new ODataErrorSerializer().writeErrorDocument(json, error);

      json.close();
      return SerializerResultImpl.with().content(buffer.getInputStream()).build();
    } catch (IOException e) {
      cachedException =
          new SerializerException(IO_EXCEPTION_TEXT, e, SerializerException.MessageKeys.IO_EXCEPTION);
      throw cachedException;
    } finally {
      closeCircleStreamBufferOutput(outputStream, cachedException);
    }
  }

  @Override
  public SerializerResult entityCollection(ServiceMetadata metadata,
                                           EdmEntityType entityType, AbstractEntityCollection entitySet,
                                           EntityCollectionSerializerOptions options) throws SerializerException {
    OutputStream outputStream = null;
    SerializerException cachedException = null;
    // boolean pagination = false;
    
    CircleStreamBuffer buffer = new CircleStreamBuffer();
    outputStream = buffer.getOutputStream();
    try (JsonGenerator json = new JsonFactory().createGenerator(outputStream)) {
      json.writeStartObject();

      ContextURL contextURL = checkContextURL(options == null ? null : options.getContextURL());
      String name = contextURL == null ? null:contextURL.getEntitySetOrSingletonOrType();
      writeContextURL(contextURL, json);

      writeMetadataETag(metadata, json);

      if (options != null && options.getCount() != null && options.getCount().getValue()) {
        writeInlineCount("", entitySet.getCount(), json);
      }
      writeOperations(entitySet.getOperations(), json);
      json.writeFieldName(Constants.VALUE);
      if (options == null) {
        writeEntitySet(metadata, entityType, entitySet, null, null, null, false, null, name, json);
      } else {
        writeEntitySet(metadata, entityType, entitySet,
            options.getExpand(), null, options.getSelect(), options.getWriteOnlyReferences(), null, name, json);
      }
      writeNextLink(entitySet, json);
      writeDeltaLink(entitySet, json, false);

      json.close();
      return SerializerResultImpl.with().content(buffer.getInputStream()).build();
    } catch (IOException | DecoderException e) {
      cachedException =
          new SerializerException(IO_EXCEPTION_TEXT, e, SerializerException.MessageKeys.IO_EXCEPTION);
      throw cachedException;
    } finally {
      closeCircleStreamBufferOutput(outputStream, cachedException);
    }
  }

  @Override
  public SerializerStreamResult entityCollectionStreamed(ServiceMetadata metadata, EdmEntityType entityType,
      EntityIterator entities, EntityCollectionSerializerOptions options) {

    return ODataWritableContent.with(entities, entityType, this, metadata, options).build();
  }

  public void entityCollectionIntoStream(ServiceMetadata metadata,
                                         EdmEntityType entityType, EntityIterator entitySet,
                                         EntityCollectionSerializerOptions options, OutputStream outputStream)
      throws SerializerException {

    SerializerException cachedException;
    // boolean pagination = false;
    try {
      JsonGenerator json = new JsonFactory().createGenerator(outputStream);
      json.writeStartObject();

      ContextURL contextURL = checkContextURL(options == null ? null : options.getContextURL());
      writeContextURL(contextURL, json);

      writeMetadataETag(metadata, json);

      if (options != null && options.getCount() != null && options.getCount().getValue()) {
        writeInlineCount("", entitySet.getCount(), json);
      }
      json.writeFieldName(Constants.VALUE);
      String name =  contextURL == null ? null:contextURL.getEntitySetOrSingletonOrType() ;
      if (options == null) {
        writeEntitySet(metadata, entityType, entitySet, null, null, null, false, null, name, json);
      } else {
        writeEntitySet(metadata, entityType, entitySet,
            options.getExpand(), null, options.getSelect(), options.getWriteOnlyReferences(), null, name, json);
      }
      // next link support for streaming results
      writeNextLink(entitySet, json);

      json.close();
    } catch (IOException | DecoderException e) {
      cachedException =
          new SerializerException(IO_EXCEPTION_TEXT, e, SerializerException.MessageKeys.IO_EXCEPTION);
      throw cachedException;
    }
  }

  @Override
  public SerializerResult entity(ServiceMetadata metadata, EdmEntityType entityType,
                                 Entity entity, EntitySerializerOptions options) throws SerializerException {
    OutputStream outputStream = null;
    SerializerException cachedException = null;
    
    ContextURL contextURL = checkContextURL(options == null ? null : options.getContextURL());
    CircleStreamBuffer buffer = new CircleStreamBuffer();
    outputStream = buffer.getOutputStream();
    try (JsonGenerator json = new JsonFactory().createGenerator(outputStream)) {
      String name =  contextURL == null ? null:contextURL.getEntitySetOrSingletonOrType();
      writeEntity(metadata, entityType, entity, contextURL,
          options == null ? null : options.getExpand(),
          null,
          options == null ? null : options.getSelect(),
              options != null && options.getWriteOnlyReferences(),
          null, name,
          json);

      json.close();
      return SerializerResultImpl.with().content(buffer.getInputStream()).build();
    } catch (IOException | DecoderException e) {
      cachedException =
          new SerializerException(IO_EXCEPTION_TEXT, e, SerializerException.MessageKeys.IO_EXCEPTION);
      throw cachedException;
    } finally {
      closeCircleStreamBufferOutput(outputStream, cachedException);
    }
  }

  ContextURL checkContextURL(ContextURL contextURL) throws SerializerException {
    if (isODataMetadataNone) {
      return null;
    } else if (contextURL == null) {
      throw new SerializerException("ContextURL null!", SerializerException.MessageKeys.NO_CONTEXT_URL);
    }
    return contextURL;
  }

  protected void writeEntitySet(ServiceMetadata metadata, EdmEntityType entityType,
                                AbstractEntityCollection entitySet, ExpandOption expand, Integer toDepth, SelectOption select,
                                boolean onlyReference, Set<String> ancestors, String name, JsonGenerator json)
          throws IOException, SerializerException, DecoderException {
    json.writeStartArray();
    for (Entity entity : entitySet) {
      if (onlyReference) {
        json.writeStartObject();
        json.writeStringField(constants.getId(), getEntityId(entity, entityType, name));
        json.writeEndObject();
      } else {
        writeEntity(metadata, entityType, entity, null, expand, toDepth, select, false, ancestors, name, json);
      }
    }
    json.writeEndArray();
  }

  /**
   * Get the ascii representation of the entity id
   * or thrown an {@link SerializerException} if id is <code>null</code>.
   *
   * @param entity the entity
   * @param entityType 
   * @param name 
   * @return ascii representation of the entity id
   */
  private String getEntityId(Entity entity, EdmEntityType entityType, String name) throws SerializerException {
    if(entity != null && entity.getId() == null) {
      if(entityType == null || entityType.getKeyPredicateNames() == null 
          || name == null) {
        throw new SerializerException("Entity id is null.", SerializerException.MessageKeys.MISSING_ID);
      }else{
        UriHelper uriHelper = new UriHelperImpl();
        entity.setId(URI.create(name + '(' + uriHelper.buildKeyPredicate(entityType, entity) + ')'));
      }
    }
    return entity.getId().toASCIIString();
  }

  private boolean areKeyPredicateNamesSelected(SelectOption select, EdmEntityType type) {
    if (select == null || ExpandSelectHelper.isAll(select)) {
      return true;
    }
    Set<String> selected = ExpandSelectHelper.getSelectedPropertyNames(select.getSelectItems());
    for (String key : type.getKeyPredicateNames()) {
      if (!selected.contains(key)) {
        return false;
      }
    }
    return true;
  }

  protected void writeEntity(ServiceMetadata metadata, EdmEntityType entityType, Entity entity,
                             ContextURL contextURL, ExpandOption expand, Integer toDepth,
                             SelectOption select, boolean onlyReference, Set<String> ancestors,
                             String name, JsonGenerator json)
      throws IOException, SerializerException, DecoderException {
    boolean cycle = false;
    if (expand != null) {
      if (ancestors == null) {
        ancestors = new HashSet<>();
      }
      cycle = !ancestors.add(getEntityId(entity, entityType, name));
    }
    try {
      json.writeStartObject();
      if (!isODataMetadataNone) {
        // top-level entity
        if (contextURL != null) {
          writeContextURL(contextURL, json);
          writeMetadataETag(metadata, json);
        }
        if (entity.getETag() != null) {
          json.writeStringField(constants.getEtag(), entity.getETag());
        }
        if (entityType.hasStream()) {
          if (entity.getMediaETag() != null) {
            json.writeStringField(constants.getMediaEtag(), entity.getMediaETag());
          }
          if (entity.getMediaContentType() != null) {
            json.writeStringField(constants.getMediaContentType(), entity.getMediaContentType());
          }
          if (entity.getMediaContentSource() != null) {
            json.writeStringField(constants.getMediaReadLink(), entity.getMediaContentSource().toString());
          }
          if (entity.getMediaEditLinks() != null && !entity.getMediaEditLinks().isEmpty()) {
            json.writeStringField(constants.getMediaEditLink(), entity.getMediaEditLinks().get(0).getHref());
          }
        }
      }
      if (cycle || onlyReference) {
        json.writeStringField(constants.getId(), getEntityId(entity, entityType, name));
      } else {
        EdmEntityType resolvedType = resolveEntityType(metadata, entityType, entity.getType());
        if ((!isODataMetadataNone && !resolvedType.equals(entityType)) || isODataMetadataFull) {
          json.writeStringField(constants.getType(), "#" + entity.getType());
        }
        if ((!isODataMetadataNone && !areKeyPredicateNamesSelected(select, resolvedType)) || isODataMetadataFull) {
          json.writeStringField(constants.getId(), getEntityId(entity, resolvedType, name));
        }
        
        if (isODataMetadataFull) {
          if (entity.getSelfLink() != null) {
            json.writeStringField(constants.getReadLink(), entity.getSelfLink().getHref());
          }
          if (entity.getEditLink() != null) {
            json.writeStringField(constants.getEditLink(), entity.getEditLink().getHref());
          }
        }
        
        writeProperties(metadata, resolvedType, entity.getProperties(), select, json, entity, expand);
        writeNavigationProperties(metadata, resolvedType, entity, expand, toDepth, ancestors, name, json);
        writeOperations(entity.getOperations(), json);      
      }
      json.writeEndObject();
    } finally {
      if (expand != null && !cycle && ancestors != null) {
        ancestors.remove(getEntityId(entity, entityType, name));
      }
    }
  }

  private void writeOperations(List<Operation> operations, JsonGenerator json)
      throws IOException {
    if (isODataMetadataFull) {
      for (Operation operation : operations) {
        json.writeObjectFieldStart(operation.getMetadataAnchor());
        json.writeStringField(Constants.ATTR_TITLE, operation.getTitle());
        json.writeStringField(Constants.ATTR_TARGET, operation.getTarget().toASCIIString());
        json.writeEndObject();
      }
    }
  }

  protected EdmEntityType resolveEntityType(ServiceMetadata metadata, EdmEntityType baseType,
                                            String derivedTypeName) throws SerializerException {
    if (derivedTypeName == null ||
        baseType.getFullQualifiedName().getFullQualifiedNameAsString().equals(derivedTypeName)) {
      return baseType;
    }
    EdmEntityType derivedType = metadata.getEdm().getEntityType(new FullQualifiedName(derivedTypeName));
    if (derivedType == null) {
      throw new SerializerException("EntityType not found",
          SerializerException.MessageKeys.UNKNOWN_TYPE, derivedTypeName);
    }
    EdmEntityType type = derivedType.getBaseType();
    while (type != null) {
      if (type.getFullQualifiedName().equals(baseType.getFullQualifiedName())) {
        return derivedType;
      }
      type = type.getBaseType();
    }
    throw new SerializerException("Wrong base type",
        SerializerException.MessageKeys.WRONG_BASE_TYPE, derivedTypeName,
            baseType.getFullQualifiedName().getFullQualifiedNameAsString());
  }

  protected EdmComplexType resolveComplexType(ServiceMetadata metadata, EdmComplexType baseType,
                                              String derivedTypeName) throws SerializerException {
      
    String fullQualifiedName = baseType.getFullQualifiedName().getFullQualifiedNameAsString();
    if (derivedTypeName == null ||
      fullQualifiedName.equals(derivedTypeName)) {
      return baseType;
    }
    EdmComplexType derivedType = metadata.getEdm().getComplexType(new FullQualifiedName(derivedTypeName));
    if (derivedType == null) {
      throw new SerializerException("Complex Type not found",
          SerializerException.MessageKeys.UNKNOWN_TYPE, derivedTypeName);
    }
    EdmComplexType type = derivedType.getBaseType();
    while (type != null) {
      if (type.getFullQualifiedName().equals(baseType.getFullQualifiedName())) {
        return derivedType;
      }
      type = type.getBaseType();
    }
    throw new SerializerException("Wrong base type",
        SerializerException.MessageKeys.WRONG_BASE_TYPE, derivedTypeName,
            baseType.getFullQualifiedName().getFullQualifiedNameAsString());
  }

  protected void writeProperties(ServiceMetadata metadata, EdmStructuredType type,
                                 List<Property> properties,
                                 SelectOption select, JsonGenerator json, Linked linked, ExpandOption expand)
      throws IOException, SerializerException {
    boolean all = ExpandSelectHelper.isAll(select);
    Set<String> selected = all ? new HashSet<>() :
        ExpandSelectHelper.getSelectedPropertyNames(select.getSelectItems());
    addKeyPropertiesToSelected(selected, type);
    Set<List<String>> expandedPaths = ExpandSelectHelper.getExpandedItemsPath(expand);
    for (String propertyName : type.getPropertyNames()) {
      if (all || selected.contains(propertyName)) {
        EdmProperty edmProperty = type.getStructuralProperty(propertyName);
        Property property = findProperty(propertyName, properties);
        Set<List<String>> selectedPaths = all || edmProperty.isPrimitive() ? null :
            ExpandSelectHelper.getSelectedPaths(select.getSelectItems(), propertyName);
        writeProperty(metadata, edmProperty, property, selectedPaths, json, expandedPaths, linked, expand);
      }
    }
  }
  
  private void addKeyPropertiesToSelected(Set<String> selected, EdmStructuredType type) {
    if (!selected.isEmpty() && type instanceof EdmEntityType) {
      List<String> keyNames = ((EdmEntityType) type).getKeyPredicateNames();
        selected.addAll(keyNames);
    }
  }

  protected void writeNavigationProperties(ServiceMetadata metadata,
                                           EdmStructuredType type, Linked linked, ExpandOption expand, Integer toDepth,
                                           Set<String> ancestors, String name, JsonGenerator json)
          throws SerializerException, IOException, DecoderException {
    if (isODataMetadataFull) {
      for (String propertyName : type.getNavigationPropertyNames()) {
        Link navigationLink = linked.getNavigationLink(propertyName);
        if (navigationLink != null) {
          json.writeStringField(propertyName + constants.getNavigationLink(), navigationLink.getHref());  
        }
        Link associationLink = linked.getAssociationLink(propertyName);
        if (associationLink != null) {
          json.writeStringField(propertyName + constants.getAssociationLink(), associationLink.getHref());  
        }
      }
    }
    if ((toDepth != null && toDepth > 1) || (toDepth == null && ExpandSelectHelper.hasExpand(expand))) {
      ExpandItem expandAll = ExpandSelectHelper.getExpandAll(expand);
      for (String propertyName : type.getNavigationPropertyNames()) {
        ExpandItem innerOptions = ExpandSelectHelper.getExpandItemBasedOnType(expand.getExpandItems(),
            propertyName, type, name);
        if (innerOptions != null || expandAll != null || toDepth != null) {
          Integer levels = null;
          EdmNavigationProperty property = type.getNavigationProperty(propertyName);
          Link navigationLink = linked.getNavigationLink(property.getName());
          ExpandOption childExpand = null;
          LevelsExpandOption levelsOption = null;
          if (innerOptions != null) {
            levelsOption = innerOptions.getLevelsOption();
            childExpand = levelsOption == null ? innerOptions.getExpandOption() : new ExpandOptionImpl().addExpandItem(
                innerOptions);
          } else if (expandAll != null) {
            levels = 1;
            levelsOption = expandAll.getLevelsOption();
            childExpand = new ExpandOptionImpl().addExpandItem(expandAll);
          }

          if (levelsOption != null) { 
            levels = levelsOption.isMax() ? Integer.MAX_VALUE : levelsOption.getValue();
          }
          if (toDepth != null) {
            levels = toDepth - 1;
            childExpand = expand;
          }
                             
          writeExpandedNavigationProperty(metadata, property, navigationLink,
            childExpand, levels,
            innerOptions == null ? null : innerOptions.getSelectOption(),
            innerOptions == null ? null : innerOptions.getCountOption(),
                  innerOptions != null && innerOptions.hasCountPath(),
                  innerOptions != null && innerOptions.isRef(),
            ancestors, name,
            json);
        }
      }
    }
  }

  private void writeExpandedStreamProperty(ExpandOption expand, String propertyName, EdmProperty edmProperty, 
      Linked linked, ExpandItem expandAll, JsonGenerator json) throws SerializerException, 
      DecoderException, IOException {
    ExpandItem innerOptions = ExpandSelectHelper.getExpandItem(expand.getExpandItems(), propertyName);
    if (innerOptions != null || expandAll != null) {
      if(constants instanceof Constantsv00){
        throw new SerializerException("Expand not supported for Stream Property Type!",
            SerializerException.MessageKeys.UNSUPPORTED_OPERATION_TYPE, "expand", edmProperty.getName());
      }
      Property property = null;
      if (linked instanceof Entity) {
        Entity entity = (Entity) linked;
        property = entity.getProperty(propertyName);
      } else if (linked instanceof ComplexValue) {
        List<Property> properties = ((ComplexValue) linked).getValue();
        for (Property prop : properties) {
          if (prop.getName().equals(propertyName)) {
            property = prop;
            break;
          }
        }
      }
       
      if((property == null || property.isNull()) && edmProperty.isNullable() == Boolean.FALSE ){
        throw new SerializerException("Non-nullable property not present!",
            SerializerException.MessageKeys.MISSING_PROPERTY, edmProperty.getName());
      }
      Link link = (Link) property.getValue();
      Property stream = link.getInlineEntity().getProperty(propertyName);
      Base64 decoder = new Base64(true);
      byte[] decodedBytes = (byte[]) decoder.decode(stream.getValue());
      json.writeStringField(propertyName, new String(decodedBytes));     
    }
  }

  protected void writeExpandedNavigationProperty(
      ServiceMetadata metadata, EdmNavigationProperty property,
      Link navigationLink, ExpandOption innerExpand,
      Integer toDepth, SelectOption innerSelect, CountOption innerCount,
      boolean writeOnlyCount, boolean writeOnlyRef, Set<String> ancestors,
      String name, JsonGenerator json) throws IOException, SerializerException, DecoderException {

    if (property.isCollection()) {
      if (writeOnlyCount) {
        if (navigationLink == null || navigationLink.getInlineEntitySet() == null) {
          writeInlineCount(property.getName(), 0, json);
        } else {
          writeInlineCount(property.getName(), navigationLink.getInlineEntitySet().getCount(), json);
        }
      } else {
        if (navigationLink == null || navigationLink.getInlineEntitySet() == null) {
          if (innerCount != null && innerCount.getValue()) {
            writeInlineCount(property.getName(), 0, json);
          }
          json.writeFieldName(property.getName());
          json.writeStartArray();
          json.writeEndArray();
        } else {
          if (innerCount != null && innerCount.getValue()) {
            writeInlineCount(property.getName(), navigationLink.getInlineEntitySet().getCount(), json);
          }
          json.writeFieldName(property.getName());
          writeEntitySet(metadata, property.getType(), navigationLink.getInlineEntitySet(), innerExpand, toDepth,
              innerSelect, writeOnlyRef, ancestors, name, json);
        }
      }
    } else {
      json.writeFieldName(property.getName());
      if (navigationLink == null || navigationLink.getInlineEntity() == null) {
        json.writeNull();
      } else {
        writeEntity(metadata, property.getType(), navigationLink.getInlineEntity(), null,
            innerExpand, toDepth, innerSelect, writeOnlyRef, ancestors, name, json);
      }
    }
  }
  
  private boolean isStreamProperty(EdmProperty edmProperty) {
    EdmType type = edmProperty.getType();
    return (edmProperty.isPrimitive() && type == EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.Stream));    
  }

  protected void writeProperty(ServiceMetadata metadata,
                               EdmProperty edmProperty, Property property,
                               Set<List<String>> selectedPaths, JsonGenerator json,
                               Set<List<String>> expandedPaths, Linked linked, ExpandOption expand)
      throws IOException, SerializerException {
    boolean isStreamProperty = isStreamProperty(edmProperty);
    writePropertyType(edmProperty, json);
    if (!isStreamProperty) {
      json.writeFieldName(edmProperty.getName());
    }
    if (property == null || property.isNull()) {
      if (edmProperty.isNullable() == Boolean.FALSE && !isStreamProperty) {
        throw new SerializerException("Non-nullable property not present!",
            SerializerException.MessageKeys.MISSING_PROPERTY, edmProperty.getName());
      } else {
        if (!isStreamProperty) {
          if (edmProperty.isCollection()) {
            json.writeStartArray();
            json.writeEndArray();
          } else {
            json.writeNull();
          }
        }
      }
    } else {
      writePropertyValue(metadata, edmProperty, property, selectedPaths, json, 
          expandedPaths, linked, expand);
    }
  }
  
  private void writePropertyType(EdmProperty edmProperty, JsonGenerator json)
      throws SerializerException, IOException {
    if (!isODataMetadataFull) {
      return;
    }
    String typeName = edmProperty.getName() + constants.getType();
    EdmType type = edmProperty.getType();
    if (type.getKind() == EdmTypeKind.ENUM || type.getKind() == EdmTypeKind.DEFINITION) {
      if (edmProperty.isCollection()) {
        json.writeStringField(typeName, 
            "#Collection(" + type.getFullQualifiedName().getFullQualifiedNameAsString() + ")");
      } else {
        json.writeStringField(typeName, "#" + type.getFullQualifiedName().getFullQualifiedNameAsString());
      }
    } else if (edmProperty.isPrimitive()) {
      if (edmProperty.isCollection()) {
        json.writeStringField(typeName, "#Collection(" + type.getFullQualifiedName().getName() + ")");
      } else {
        // exclude the properties that can be heuristically determined
        if (type != EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.Boolean) &&
            type != EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.Double) &&
            type != EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.String)) {
          json.writeStringField(typeName, "#" + type.getFullQualifiedName().getName());                  
        }
      }
    } else if (type.getKind() == EdmTypeKind.COMPLEX) {
      // non-collection case written in writeComplex method directly.
      if (edmProperty.isCollection()) {
        json.writeStringField(typeName, 
            "#Collection(" + type.getFullQualifiedName().getFullQualifiedNameAsString() + ")");
      }
    } else {
      throw new SerializerException("Property type not yet supported!",
          SerializerException.MessageKeys.UNSUPPORTED_PROPERTY_TYPE, edmProperty.getName());
    }    
  }

  private void writePropertyValue(ServiceMetadata metadata, EdmProperty edmProperty,
                                  Property property, Set<List<String>> selectedPaths, JsonGenerator json,
                                  Set<List<String>> expandedPaths, Linked linked, ExpandOption expand)
      throws IOException, SerializerException {
    EdmType type = edmProperty.getType();
    try {
      if (edmProperty.isPrimitive()
          || type.getKind() == EdmTypeKind.ENUM || type.getKind() == EdmTypeKind.DEFINITION) {
        if (edmProperty.isCollection()) {
          writePrimitiveCollection((EdmPrimitiveType) type, property,
              edmProperty.isNullable(), edmProperty.getMaxLength(),
              edmProperty.getPrecision(), edmProperty.getScale(), edmProperty.isUnicode(), json);
        } else {
          writePrimitive((EdmPrimitiveType) type, property,
              edmProperty.isNullable(), edmProperty.getMaxLength(),
              edmProperty.getPrecision(), edmProperty.getScale(), edmProperty.isUnicode(), json);
          // If there is expand on a stream property
          if (isStreamProperty(edmProperty) && null != expand) {
            ExpandItem expandAll = ExpandSelectHelper.getExpandAll(expand);
            try {
              writeExpandedStreamProperty(expand, property.getName(), edmProperty, linked, expandAll, json);
            } catch (DecoderException e) {
              throw new SerializerException(IO_EXCEPTION_TEXT, e, SerializerException.MessageKeys.IO_EXCEPTION);
            }
          }
        }
      } else if (property.isComplex()) {
        if (edmProperty.isCollection()) {
          writeComplexCollection(metadata, (EdmComplexType) type, property, selectedPaths, 
              json, expandedPaths, linked, expand);
        } else {
         writeComplex(metadata, (EdmComplexType) type, property, selectedPaths, json, 
             expandedPaths, linked, expand);
        }
      } else {
        throw new SerializerException("Property type not yet supported!",
            SerializerException.MessageKeys.UNSUPPORTED_PROPERTY_TYPE, edmProperty.getName());
      }
    } catch (EdmPrimitiveTypeException e) {
      throw new SerializerException("Wrong value for property!", e,
          SerializerException.MessageKeys.WRONG_PROPERTY_VALUE,
          edmProperty.getName(), property.getValue().toString());
    }
  }

  private void writeComplex(ServiceMetadata metadata, EdmComplexType type,
                            Property property, Set<List<String>> selectedPaths, JsonGenerator json,
                            Set<List<String>> expandedPaths, Linked linked, ExpandOption expand)
          throws IOException, SerializerException{
        json.writeStartObject();        
        String derivedName = property.getType();
        EdmComplexType resolvedType = null;
        if (!type.getFullQualifiedName().getFullQualifiedNameAsString().
            equals(derivedName)) {
          if (type.getBaseType() != null && 
              type.getBaseType().getFullQualifiedName().getFullQualifiedNameAsString().
              equals(derivedName)) {
            resolvedType = resolveComplexType(metadata, type.getBaseType(), 
                type.getFullQualifiedName().getFullQualifiedNameAsString());
          } else {
            resolvedType = resolveComplexType(metadata, type, derivedName);
          }
        } else {
          resolvedType = resolveComplexType(metadata, type, derivedName);
        }
        if (!isODataMetadataNone && !resolvedType.equals(type) || isODataMetadataFull) {
           json.writeStringField(constants.getType(), "#" + 
        resolvedType.getFullQualifiedName().getFullQualifiedNameAsString());
        }
        
        if (null != linked) {
          if (linked instanceof Entity) {
            linked = ((Entity)linked).getProperty(property.getName()).asComplex();
          } else if (linked instanceof ComplexValue) {
            List<Property> complexProperties = ((ComplexValue)linked).getValue();
            for (Property prop : complexProperties) {
              if (prop.getName().equals(property.getName())) {
                linked = prop.asComplex();
                break;
              }
            }
          }
          expandedPaths = expandedPaths == null || expandedPaths.isEmpty() ? null :
            ExpandSelectHelper.getReducedExpandItemsPaths(expandedPaths, property.getName());
        }
        
        writeComplexValue(metadata, resolvedType, property.asComplex().getValue(), selectedPaths,
             json, expandedPaths, linked, expand, property.getName());
        json.writeEndObject();
  }

  private void writePrimitiveCollection(EdmPrimitiveType type, Property property,
                                        Boolean isNullable, Integer maxLength, Integer precision, Integer scale,
                                        Boolean isUnicode, JsonGenerator json)
      throws IOException, SerializerException {
    json.writeStartArray();
    for (Object value : property.asCollection()) {
      switch (property.getValueType()) {
      case COLLECTION_PRIMITIVE:
      case COLLECTION_ENUM:
        try {
          writePrimitiveValue(property.getName(), type, value, isNullable,
              maxLength, precision, scale, isUnicode, json);
        } catch (EdmPrimitiveTypeException e) {
          throw new SerializerException("Wrong value for property!", e,
              SerializerException.MessageKeys.WRONG_PROPERTY_VALUE,
              property.getName(), property.getValue().toString());
        }
        break;
      default:
        throw new SerializerException("Property type not yet supported!",
            SerializerException.MessageKeys.UNSUPPORTED_PROPERTY_TYPE, property.getName());
      }
    }
    json.writeEndArray();
  }

  private void writeComplexCollection(ServiceMetadata metadata, EdmComplexType type,
                                      Property property,
                                      Set<List<String>> selectedPaths, JsonGenerator json,
                                      Set<List<String>> expandedPaths, Linked linked, ExpandOption expand)
      throws IOException, SerializerException {
    json.writeStartArray();
    EdmComplexType derivedType = type;
    Set<List<String>> expandedPaths1 = expandedPaths != null && !expandedPaths.isEmpty() ? 
        expandedPaths : ExpandSelectHelper.getExpandedItemsPath(expand);
    for (Object value : property.asCollection()) {
      expandedPaths = expandedPaths1;
      derivedType = ((ComplexValue) value).getTypeName()!=null ? metadata.getEdm().getComplexType
          (new FullQualifiedName(((ComplexValue) value).getTypeName())): type;
      if (property.getValueType() == ValueType.COLLECTION_COMPLEX) {
        json.writeStartObject();
        if (isODataMetadataFull || (!isODataMetadataNone && !derivedType.equals(type))) {
          json.writeStringField(constants.getType(), "#" +
                  derivedType.getFullQualifiedName().getFullQualifiedNameAsString());
        }
        expandedPaths = expandedPaths.isEmpty() ? null :
                ExpandSelectHelper.getReducedExpandItemsPaths(expandedPaths, property.getName());
        writeComplexValue(metadata, derivedType, ((ComplexValue) value).getValue(),
                selectedPaths, json, expandedPaths, (ComplexValue) value, expand, property.getName());
        json.writeEndObject();
      } else {
        throw new SerializerException("Property type not yet supported!",
                SerializerException.MessageKeys.UNSUPPORTED_PROPERTY_TYPE, property.getName());
      }
    }
    json.writeEndArray();
  }

  private void writePrimitive(EdmPrimitiveType type, Property property,
                              Boolean isNullable, Integer maxLength, Integer precision, Integer scale,
                              Boolean isUnicode, JsonGenerator json)
      throws EdmPrimitiveTypeException, IOException, SerializerException {
    if (property.isPrimitive()) {
      writePrimitiveValue(property.getName(), type, property.asPrimitive(),
          isNullable, maxLength, precision, scale, isUnicode, json);
    /*} else if (property.isGeospatial()) {
      writeGeoValue(property.getName(), type, property.asGeospatial(), isNullable, json, null);
    */} else if (property.isEnum()) {
      writePrimitiveValue(property.getName(), type, property.asEnum(),
          isNullable, maxLength, precision, scale, isUnicode, json);
    } else {
      throw new SerializerException("Inconsistent property type!",
          SerializerException.MessageKeys.INCONSISTENT_PROPERTY_TYPE, property.getName());
    }
  }

  protected void writePrimitiveValue(String name, EdmPrimitiveType type, Object primitiveValue,
                                     Boolean isNullable, Integer maxLength, Integer precision, Integer scale,
                                     Boolean isUnicode, JsonGenerator json) throws EdmPrimitiveTypeException, IOException {
    String value = type.valueToString(primitiveValue,
        isNullable, maxLength, precision, scale, isUnicode);
    if (value == null) {
      json.writeNull();
    } else if (type == EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.Boolean)) {
      json.writeBoolean(Boolean.parseBoolean(value));
    } else if (type == EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.Byte)
        || type == EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.Double)
        || type == EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.Int16)
        || type == EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.Int32)
        || type == EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.SByte)
        || type == EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.Single)
        || (type == EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.Decimal)
        || type == EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.Int64))
        && !isIEEE754Compatible) {
      json.writeNumber(value);
    } else if (type == EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.Stream)) {
      if (primitiveValue instanceof Link) {
        Link stream = (Link)primitiveValue;
        if (!isODataMetadataNone) {
          if (stream.getMediaETag() != null) {
            json.writeStringField(name+constants.getMediaEtag(), stream.getMediaETag());
          }
          if (stream.getType() != null) {
            json.writeStringField(name+constants.getMediaContentType(), stream.getType());
          }
        }
        if (isODataMetadataFull) {
          if (stream.getRel() != null && stream.getRel().equals(Constants.NS_MEDIA_READ_LINK_REL)) {
            json.writeStringField(name+constants.getMediaReadLink(), stream.getHref());
          }
          if (stream.getRel() == null || stream.getRel().equals(Constants.NS_MEDIA_EDIT_LINK_REL)) {
            json.writeStringField(name+constants.getMediaEditLink(), stream.getHref());
          }
        }
      }
    } else {
      json.writeString(value);
    }
  }

  protected void writeComplexValue(ServiceMetadata metadata,
                                   EdmComplexType type, List<Property> properties,
                                   Set<List<String>> selectedPaths, JsonGenerator json,
                                   Set<List<String>> expandedPaths, Linked linked, ExpandOption expand, String complexPropName)
      throws IOException, SerializerException {

    if (null != expandedPaths) {
      for(List<String> paths : expandedPaths) {
        if (paths.size() == 1) {
          expandedPaths = ExpandSelectHelper.getReducedExpandItemsPaths(expandedPaths, paths.get(0));
        }
      }
    }
    
    for (String propertyName : type.getPropertyNames()) {
      Property property = findProperty(propertyName, properties);
      if (selectedPaths == null || ExpandSelectHelper.isSelected(selectedPaths, propertyName)) {
        writeProperty(metadata, (EdmProperty) type.getProperty(propertyName), property,
            selectedPaths == null ? null : ExpandSelectHelper.getReducedSelectedPaths(selectedPaths, propertyName),
            json, expandedPaths, linked, expand);
      }
    }
    try {
      writeNavigationProperties(metadata, type, linked, expand, null, null, complexPropName, json);
    } catch (DecoderException e) {
      throw new SerializerException(IO_EXCEPTION_TEXT, e, SerializerException.MessageKeys.IO_EXCEPTION);
    }
  }
    

  private Property findProperty(String propertyName, List<Property> properties) {
    for (Property property : properties) {
      if (propertyName.equals(property.getName())) {
        return property;
      }
    }
    return null;
  }

  @Override
  public SerializerResult primitive(ServiceMetadata metadata, EdmPrimitiveType type,
                                    Property property, PrimitiveSerializerOptions options) throws SerializerException {
    OutputStream outputStream = null;
    SerializerException cachedException = null;
    
    ContextURL contextURL = checkContextURL(options == null ? null : options.getContextURL());
    CircleStreamBuffer buffer = new CircleStreamBuffer();
    outputStream = buffer.getOutputStream();
    try (JsonGenerator json = new JsonFactory().createGenerator(outputStream)) {
      json.writeStartObject();
      writeContextURL(contextURL, json);
      writeMetadataETag(metadata, json);
      writeOperations(property.getOperations(), json);
      if (property.isNull() && options!=null && options.isNullable() != null && !options.isNullable()) {
        throw new SerializerException("Property value can not be null.", SerializerException.MessageKeys.NULL_INPUT);
      } else {
        json.writeFieldName(Constants.VALUE);
        writePrimitive(type, property,
            options == null ? null : options.isNullable(),
            options == null ? null : options.getMaxLength(),
            options == null ? null : options.getPrecision(),
            options == null ? null : options.getScale(),
            options == null ? null : options.isUnicode(), json);
      }
      json.writeEndObject();

      json.close();
      return SerializerResultImpl.with().content(buffer.getInputStream()).build();
    } catch (IOException e) {
      cachedException =
          new SerializerException(IO_EXCEPTION_TEXT, e, SerializerException.MessageKeys.IO_EXCEPTION);
      throw cachedException;
    } catch (EdmPrimitiveTypeException e) {
      cachedException = new SerializerException("Wrong value for property!", e,
          SerializerException.MessageKeys.WRONG_PROPERTY_VALUE,
          property.getName(), property.getValue().toString());
      throw cachedException;
    } finally {
      closeCircleStreamBufferOutput(outputStream, cachedException);
    }
  }

  @Override
  public SerializerResult complex(ServiceMetadata metadata, EdmComplexType type,
                                  Property property, ComplexSerializerOptions options) throws SerializerException {
    OutputStream outputStream = null;
    SerializerException cachedException = null;
    try {
      ContextURL contextURL = checkContextURL(options == null ? null : options.getContextURL());
      String name =  contextURL == null ? null:
        contextURL.getEntitySetOrSingletonOrType();
      CircleStreamBuffer buffer = new CircleStreamBuffer();
      outputStream = buffer.getOutputStream();
      JsonGenerator json = new JsonFactory().createGenerator(outputStream);
      json.writeStartObject();
      writeContextURL(contextURL, json);
      writeMetadataETag(metadata, json);      
      EdmComplexType resolvedType = null;
      if (!type.getFullQualifiedName().getFullQualifiedNameAsString().
          equals(property.getType())) {
        if (type.getBaseType() != null && 
            type.getBaseType().getFullQualifiedName().getFullQualifiedNameAsString().
            equals(property.getType())) {
          resolvedType = resolveComplexType(metadata, type.getBaseType(), 
              type.getFullQualifiedName().getFullQualifiedNameAsString());
        } else {
          resolvedType = resolveComplexType(metadata, type, property.getType());
        }
      } else {
        resolvedType = resolveComplexType(metadata, type, property.getType());
      }
      if (!isODataMetadataNone && !resolvedType.equals(type) || isODataMetadataFull) {
        json.writeStringField(constants.getType(), "#" + 
      resolvedType.getFullQualifiedName().getFullQualifiedNameAsString());
      }
      writeOperations(property.getOperations(), json);      
      List<Property> values =
          property.isNull() ? Collections.emptyList() : property.asComplex().getValue();
      writeProperties(metadata, type, values, options == null ? null : options.getSelect(),
          json, 
          property.asComplex(), options == null ? null : options.getExpand());
      if (!property.isNull() && property.isComplex()) {
        writeNavigationProperties(metadata, type, property.asComplex(),
            options == null ? null : options.getExpand(), null, null, name, json);
      }
      json.writeEndObject();

      json.close();
      outputStream.close();
      return SerializerResultImpl.with().content(buffer.getInputStream()).build();
    } catch (IOException | DecoderException e) {
      cachedException =
          new SerializerException(IO_EXCEPTION_TEXT, e, SerializerException.MessageKeys.IO_EXCEPTION);
      throw cachedException;
    } finally {
      closeCircleStreamBufferOutput(outputStream, cachedException);
    }
  }

  @Override
  public SerializerResult primitiveCollection(ServiceMetadata metadata, EdmPrimitiveType type,
                                              Property property, PrimitiveSerializerOptions options) throws SerializerException {
    OutputStream outputStream = null;
    SerializerException cachedException = null;
    
    ContextURL contextURL = checkContextURL(options == null ? null : options.getContextURL());
    CircleStreamBuffer buffer = new CircleStreamBuffer();
    outputStream = buffer.getOutputStream();
    try (JsonGenerator json = new JsonFactory().createGenerator(outputStream)) {
      json.writeStartObject();
      writeContextURL(contextURL, json);
      writeMetadataETag(metadata, json);
      if (isODataMetadataFull) {
        json.writeStringField(constants.getType(),  "#Collection("+type.getFullQualifiedName().getName()+")");
      }
      writeOperations(property.getOperations(), json);
      json.writeFieldName(Constants.VALUE);
      writePrimitiveCollection(type, property,
          options == null ? null : options.isNullable(),
          options == null ? null : options.getMaxLength(),
          options == null ? null : options.getPrecision(),
          options == null ? null : options.getScale(),
          options == null ? null : options.isUnicode(), json);
      json.writeEndObject();

      json.close();
      return SerializerResultImpl.with().content(buffer.getInputStream()).build();
    } catch (IOException e) {
      cachedException =
          new SerializerException(IO_EXCEPTION_TEXT, e, SerializerException.MessageKeys.IO_EXCEPTION);
      throw cachedException;
    } finally {
      closeCircleStreamBufferOutput(outputStream, cachedException);
    }
  }

  @Override
  public SerializerResult complexCollection(ServiceMetadata metadata, EdmComplexType type,
                                            Property property, ComplexSerializerOptions options) throws SerializerException {
    OutputStream outputStream = null;
    SerializerException cachedException = null;
    
    ContextURL contextURL = checkContextURL(options == null ? null : options.getContextURL());
    CircleStreamBuffer buffer = new CircleStreamBuffer();
    outputStream = buffer.getOutputStream();
    try (JsonGenerator json = new JsonFactory().createGenerator(outputStream)) {
      json.writeStartObject();
      writeContextURL(contextURL, json);
      writeMetadataETag(metadata, json);
      if (isODataMetadataFull) {
        json.writeStringField(constants.getType(), 
            "#Collection(" + type.getFullQualifiedName().getFullQualifiedNameAsString() + ")");                
      }
      writeOperations(property.getOperations(), json);
      json.writeFieldName(Constants.VALUE);
      Set<List<String>> selectedPaths = null;
      if (null != options && null != options.getSelect()) {
        boolean all = ExpandSelectHelper.isAll(options.getSelect());
        selectedPaths = all || property.isPrimitive() ? null : ExpandSelectHelper
            .getSelectedPaths(options.getSelect().getSelectItems());
      }
      Set<List<String>> expandPaths = null;
      if (null != options && null != options.getExpand()) {
        expandPaths = ExpandSelectHelper.getExpandedItemsPath(options.getExpand());
      }
      writeComplexCollection(metadata, type, property, selectedPaths, json, expandPaths, null, 
          options == null ? null : options.getExpand());
      json.writeEndObject();

      json.close();
      return SerializerResultImpl.with().content(buffer.getInputStream()).build();
    } catch (IOException e) {
      cachedException =
          new SerializerException(IO_EXCEPTION_TEXT, e, SerializerException.MessageKeys.IO_EXCEPTION);
      throw cachedException;
    } finally {
      closeCircleStreamBufferOutput(outputStream, cachedException);
    }
  }

  @Override
  public SerializerResult reference(ServiceMetadata metadata, EdmEntitySet edmEntitySet,
                                    Entity entity, ReferenceSerializerOptions options) throws SerializerException {
    OutputStream outputStream = null;
    SerializerException cachedException = null;

    
    ContextURL contextURL = checkContextURL(options == null ? null : options.getContextURL());
    CircleStreamBuffer buffer = new CircleStreamBuffer();
    UriHelper uriHelper = new UriHelperImpl();
    outputStream = buffer.getOutputStream();
    try (JsonGenerator json = new JsonFactory().createGenerator(outputStream)) {

      json.writeStartObject();
      writeContextURL(contextURL, json);
      json.writeStringField(constants.getId(), uriHelper.buildCanonicalURL(edmEntitySet, entity));
      json.writeEndObject();

      json.close();
      return SerializerResultImpl.with().content(buffer.getInputStream()).build();
    } catch (IOException e) {
      cachedException =
          new SerializerException(IO_EXCEPTION_TEXT, e, SerializerException.MessageKeys.IO_EXCEPTION);
      throw cachedException;
    } finally {
      closeCircleStreamBufferOutput(outputStream, cachedException);
    }
  }

  @Override
  public SerializerResult referenceCollection(ServiceMetadata metadata, EdmEntitySet edmEntitySet,
                                              AbstractEntityCollection entityCollection, ReferenceCollectionSerializerOptions options)
      throws SerializerException {
    // OutputStream outputStream = null;
    SerializerException cachedException = null;
    // boolean pagination = false ;

    ContextURL contextURL = checkContextURL(options == null ? null : options.getContextURL());
    CircleStreamBuffer buffer = new CircleStreamBuffer();
    UriHelper uriHelper = new UriHelperImpl();
    OutputStream outputStream = buffer.getOutputStream();
    try (JsonGenerator json = new JsonFactory().createGenerator(outputStream)) {
      json.writeStartObject();

      writeContextURL(contextURL, json);
      if (options != null && options.getCount() != null && options.getCount().getValue()) {
        writeInlineCount("", entityCollection.getCount(), json);
      }

      json.writeArrayFieldStart(Constants.VALUE);
      for (Entity entity : entityCollection) {
        json.writeStartObject();
        json.writeStringField(constants.getId(), uriHelper.buildCanonicalURL(edmEntitySet, entity));
        json.writeEndObject();
      }
      json.writeEndArray();

      writeNextLink(entityCollection, json);

      json.writeEndObject();

      json.close();
      return SerializerResultImpl.with().content(buffer.getInputStream()).build();
    } catch (IOException e) {
      cachedException =
          new SerializerException(IO_EXCEPTION_TEXT, e, SerializerException.MessageKeys.IO_EXCEPTION);
      throw cachedException;
    } finally {
      closeCircleStreamBufferOutput(outputStream, cachedException);
    }

  }

  void writeContextURL(ContextURL contextURL, JsonGenerator json) throws IOException {
    if (!isODataMetadataNone && contextURL != null) {
      json.writeStringField(constants.getContext(), ContextURLBuilder.create(contextURL).toASCIIString());
    }
  }

  void writeMetadataETag(ServiceMetadata metadata, JsonGenerator json) throws IOException {
    if (!isODataMetadataNone
        && metadata != null
        && metadata.getServiceMetadataETagSupport() != null
        && metadata.getServiceMetadataETagSupport().getMetadataETag() != null) {
      json.writeStringField(constants.getMetadataEtag(),
          metadata.getServiceMetadataETagSupport().getMetadataETag());
    }
  }

  void writeInlineCount(String propertyName, Integer count, JsonGenerator json)
      throws IOException {
    if (count != null) {
      if (isIEEE754Compatible) {
        json.writeStringField(propertyName + constants.getCount(), String.valueOf(count));
      } else {
        json.writeNumberField(propertyName + constants.getCount(), count);
      }
    }
  }

  void writeNextLink(AbstractEntityCollection entitySet, JsonGenerator json)
      throws IOException {
    if (entitySet.getNext() != null) {
      json.writeStringField(constants.getNextLink(), entitySet.getNext().toASCIIString());
    }
  }
  
  void writeDeltaLink(AbstractEntityCollection entitySet, JsonGenerator json, boolean pagination)
      throws IOException {
    if (entitySet.getDeltaLink() != null && !pagination) {
      json.writeStringField(constants.getDeltaLink(), entitySet.getDeltaLink().toASCIIString());
 }
  }

}