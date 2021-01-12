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
package nl.buildforce.olingo.server.core;

import java.util.Collection;
import java.util.List;

import nl.buildforce.olingo.commons.api.constants.Constantsv00;
import nl.buildforce.olingo.commons.api.constants.Constantsv01;
import nl.buildforce.olingo.commons.api.IConstants;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveType;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlEdmProvider;
import nl.buildforce.olingo.commons.api.edmx.EdmxReference;
import nl.buildforce.olingo.commons.api.format.ContentType;
import nl.buildforce.olingo.commons.core.edm.primitivetype.EdmPrimitiveTypeFactory;
import nl.buildforce.olingo.server.api.OData;
import nl.buildforce.olingo.server.api.ServiceMetadata;
import nl.buildforce.olingo.server.api.ODataHandler;
import nl.buildforce.olingo.server.api.ODataHttpHandler;
// import nl.buildforce.olingo.server.api.debug.DebugResponseHelper;
import nl.buildforce.olingo.server.api.deserializer.DeserializerException;
import nl.buildforce.olingo.server.api.deserializer.FixedFormatDeserializer;
import nl.buildforce.olingo.server.api.deserializer.ODataDeserializer;
import nl.buildforce.olingo.server.api.etag.ETagHelper;
import nl.buildforce.olingo.server.api.etag.ServiceMetadataETagSupport;
import nl.buildforce.olingo.server.api.prefer.Preferences;
import nl.buildforce.olingo.server.api.serializer.EdmAssistedSerializer;
import nl.buildforce.olingo.server.api.serializer.EdmDeltaSerializer;
import nl.buildforce.olingo.server.api.serializer.FixedFormatSerializer;
import nl.buildforce.olingo.server.api.serializer.ODataSerializer;
import nl.buildforce.olingo.server.api.serializer.SerializerException;
import nl.buildforce.olingo.server.api.uri.UriHelper;
// import nl.buildforce.olingo.server.core.debug.DebugResponseHelperImpl;
// import nl.buildforce.olingo.server.core.debug.ServerCoreDebugger;
import nl.buildforce.olingo.server.core.deserializer.FixedFormatDeserializerImpl;
import nl.buildforce.olingo.server.core.deserializer.json.ODataJsonDeserializer;
import nl.buildforce.olingo.server.core.deserializer.xml.ODataXmlDeserializer;
import nl.buildforce.olingo.server.core.etag.ETagHelperImpl;
import nl.buildforce.olingo.server.core.prefer.PreferencesImpl;
import nl.buildforce.olingo.server.core.serializer.FixedFormatSerializerImpl;
import nl.buildforce.olingo.server.core.serializer.json.EdmAssistedJsonSerializer;
import nl.buildforce.olingo.server.core.serializer.json.ODataJsonSerializer;
import nl.buildforce.olingo.server.core.serializer.json.JsonDeltaSerializer;
import nl.buildforce.olingo.server.core.serializer.json.JsonDeltaSerializerWithNavigations;
import nl.buildforce.olingo.server.core.serializer.xml.ODataXmlSerializer;
import nl.buildforce.olingo.server.core.uri.UriHelperImpl;

public class ODataImpl extends OData {

  @Override
  public ODataSerializer createSerializer(ContentType contentType) throws SerializerException {
    ODataSerializer serializer = null;

    if (contentType != null && contentType.isCompatible(ContentType.APPLICATION_JSON)) {
      String metadata = contentType.getParameter(ContentType.PARAMETER_ODATA_METADATA);
      if (metadata == null
          || ContentType.VALUE_ODATA_METADATA_MINIMAL.equalsIgnoreCase(metadata)
          || ContentType.VALUE_ODATA_METADATA_NONE.equalsIgnoreCase(metadata)
          || ContentType.VALUE_ODATA_METADATA_FULL.equalsIgnoreCase(metadata)) {
        serializer = new ODataJsonSerializer(contentType, new Constantsv00());
      }
    } else if (contentType != null && (contentType.isCompatible(ContentType.APPLICATION_XML)
        || contentType.isCompatible(ContentType.APPLICATION_ATOM_XML))) {
      serializer = new ODataXmlSerializer();
    }

    if (serializer == null) {
      throw new SerializerException("Unsupported format: " + 
    ((contentType != null) ? contentType.toContentTypeString() : null),
          SerializerException.MessageKeys.UNSUPPORTED_FORMAT, 
          ((contentType != null) ? contentType.toContentTypeString() : null));
    } else {
      return serializer;
    }
  }
  
  @Override
  public ODataSerializer createSerializer(ContentType contentType,
                                          List<String> versions) throws SerializerException {
    ODataSerializer serializer = null;
    IConstants constants = new Constantsv00();
    if(versions!=null && !versions.isEmpty() && getMaxVersion(versions) > 4){
      constants = new Constantsv01() ;
    }
    if (contentType != null && contentType.isCompatible(ContentType.APPLICATION_JSON)) {
      String metadata = contentType.getParameter(ContentType.PARAMETER_ODATA_METADATA);
      if (metadata == null
          || ContentType.VALUE_ODATA_METADATA_MINIMAL.equalsIgnoreCase(metadata)
          || ContentType.VALUE_ODATA_METADATA_NONE.equalsIgnoreCase(metadata)
          || ContentType.VALUE_ODATA_METADATA_FULL.equalsIgnoreCase(metadata)) {
        serializer = new ODataJsonSerializer(contentType, constants);
      }
    } else if (contentType != null && (contentType.isCompatible(ContentType.APPLICATION_XML)
        || contentType.isCompatible(ContentType.APPLICATION_ATOM_XML))) {
      serializer = new ODataXmlSerializer();
    }

    if (serializer == null) {
      throw new SerializerException("Unsupported format: " + 
    ((contentType != null) ? contentType.toContentTypeString() : null),
          SerializerException.MessageKeys.UNSUPPORTED_FORMAT, 
          ((contentType != null) ? contentType.toContentTypeString() : null));
    } else {
      return serializer;
    }
  }

  @Override
  public FixedFormatSerializer createFixedFormatSerializer() {
    return new FixedFormatSerializerImpl();
  }

  @Override
  public EdmAssistedSerializer createEdmAssistedSerializer(ContentType contentType) throws SerializerException {
    if (contentType != null && contentType.isCompatible(ContentType.APPLICATION_JSON)) {
      return new EdmAssistedJsonSerializer(contentType);
    }
    throw new SerializerException("Unsupported format: " + 
    ((contentType != null) ? contentType.toContentTypeString() : null),
        SerializerException.MessageKeys.UNSUPPORTED_FORMAT, 
        ((contentType != null) ? contentType.toContentTypeString() : null));
  }
  
  
  @Override
  public EdmDeltaSerializer createEdmDeltaSerializer(ContentType contentType, List<String> versions)
      throws SerializerException {
    if (contentType != null && contentType.isCompatible(ContentType.APPLICATION_JSON)) {
      if(versions!=null && !versions.isEmpty()){
       return getMaxVersion(versions)>4 ?  new JsonDeltaSerializerWithNavigations(contentType):
         new JsonDeltaSerializer(contentType);
      }
      return new JsonDeltaSerializerWithNavigations(contentType);
    }
    throw new SerializerException("Unsupported format: " + 
    ((contentType != null) ? contentType.toContentTypeString() : null),
        SerializerException.MessageKeys.UNSUPPORTED_FORMAT, 
        ((contentType != null) ? contentType.toContentTypeString() : null));
  }

  private float getMaxVersion(List<String> versions) {
    Float[] versionValue = new Float [versions.size()];
    int i=0;
    Float max= (float) 0;
    for(String version:versions){
     Float ver = Float.valueOf(version);
     versionValue[i++] = ver;
     max = max > ver ? max : ver ;
   }
    return max;
  }

  @Override
  public ODataHttpHandler createHandler(ServiceMetadata serviceMetadata) {
    return new ODataHttpHandlerImpl(this, serviceMetadata);
  }

  @Override
  public ODataHandler createRawHandler(ServiceMetadata serviceMetadata) {
    return new ODataHandlerImpl(this, serviceMetadata/*, new ServerCoreDebugger(this)*/);
  }

  @Override
  public ServiceMetadata createServiceMetadata(CsdlEdmProvider edmProvider,
                                               List<EdmxReference> references) {
    return createServiceMetadata(edmProvider, references, null);
  }

  @Override
  public ServiceMetadata createServiceMetadata(CsdlEdmProvider edmProvider,
                                               List<EdmxReference> references, ServiceMetadataETagSupport serviceMetadataETagSupport) {
    return new ServiceMetadataImpl(edmProvider, references, serviceMetadataETagSupport);
  }

  @Override
  public FixedFormatDeserializer createFixedFormatDeserializer() {
    return new FixedFormatDeserializerImpl();
  }

  @Override
  public UriHelper createUriHelper() {
    return new UriHelperImpl();
  }

  @Override
  public ODataDeserializer createDeserializer(ContentType contentType) throws DeserializerException {
    if (contentType != null && contentType.isCompatible(ContentType.JSON)) {
      return new ODataJsonDeserializer(contentType);
    } else if (contentType != null && (contentType.isCompatible(ContentType.APPLICATION_XML)
        || contentType.isCompatible(ContentType.APPLICATION_ATOM_XML))) {
      return new ODataXmlDeserializer();
    } else {
      throw new DeserializerException("Unsupported format: " + 
    ((contentType != null) ? contentType.toContentTypeString() : null),
          DeserializerException.MessageKeys.UNSUPPORTED_FORMAT, 
          ((contentType != null) ? contentType.toContentTypeString() : null));
    }
  }  
  @Override
  public ODataDeserializer createDeserializer(ContentType contentType,
                                              ServiceMetadata metadata) throws DeserializerException {
    if (contentType != null && contentType.isCompatible(ContentType.JSON)) {
      return new ODataJsonDeserializer(contentType, metadata);
    } else if (contentType != null && (contentType.isCompatible(ContentType.APPLICATION_XML)
        || contentType.isCompatible(ContentType.APPLICATION_ATOM_XML))) {
      return new ODataXmlDeserializer(metadata);
    } else {
      throw new DeserializerException("Unsupported format: " + 
    ((contentType != null) ? contentType.toContentTypeString() : null),
          DeserializerException.MessageKeys.UNSUPPORTED_FORMAT, 
          ((contentType != null) ? contentType.toContentTypeString() : null));
    }
  }

  @Override
  public EdmPrimitiveType createPrimitiveTypeInstance(EdmPrimitiveTypeKind kind) {
    return EdmPrimitiveTypeFactory.getInstance(kind);
  }

  @Override
  public ETagHelper createETagHelper() {
    return new ETagHelperImpl();
  }

  @Override
  public Preferences createPreferences(Collection<String> preferHeaders) {
    return new PreferencesImpl(preferHeaders);
  }

  /*@Override
  public DebugResponseHelper createDebugResponseHelper(String debugFormat) {
    // TODO: What should we do with invalid formats?
    // TODO: Support more debug formats
    return new DebugResponseHelperImpl(debugFormat);
  }*/

  @Override
  public ODataDeserializer createDeserializer(ContentType contentType, List<String> versions)
      throws DeserializerException {
    IConstants constants = new Constantsv00();
    if(versions!=null && !versions.isEmpty() && getMaxVersion(versions)>4){
      constants = new Constantsv01() ;
    }
    if (contentType != null && contentType.isCompatible(ContentType.JSON)) {
      return new ODataJsonDeserializer(contentType, constants);
    } else if (contentType != null && (contentType.isCompatible(ContentType.APPLICATION_XML)
        || contentType.isCompatible(ContentType.APPLICATION_ATOM_XML))) {
      return new ODataXmlDeserializer();
    } else {
      throw new DeserializerException("Unsupported format: " + 
    ((contentType != null) ? contentType.toContentTypeString() : null),
          DeserializerException.MessageKeys.UNSUPPORTED_FORMAT, 
          ((contentType != null) ? contentType.toContentTypeString() : null));
    }
  
  }

  @Override
  public ODataDeserializer createDeserializer(ContentType contentType, ServiceMetadata metadata, List<String> versions)
      throws DeserializerException {
    IConstants constants = new Constantsv00();
    if(versions!=null && !versions.isEmpty() && getMaxVersion(versions)>4){
      constants = new Constantsv01() ;
    }
    if (contentType != null && contentType.isCompatible(ContentType.JSON)) {
      return new ODataJsonDeserializer(contentType, metadata, constants);
    } else if (contentType != null && (contentType.isCompatible(ContentType.APPLICATION_XML)
        || contentType.isCompatible(ContentType.APPLICATION_ATOM_XML))) {
      return new ODataXmlDeserializer(metadata);
    } else {
      throw new DeserializerException("Unsupported format: " + 
    ((contentType != null) ? contentType.toContentTypeString() : null),
          DeserializerException.MessageKeys.UNSUPPORTED_FORMAT, 
          ((contentType != null) ? contentType.toContentTypeString() : null));
    }
  }
}