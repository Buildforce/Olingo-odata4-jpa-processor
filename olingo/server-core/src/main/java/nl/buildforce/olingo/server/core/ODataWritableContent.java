/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;

import nl.buildforce.olingo.commons.api.data.EntityIterator;
import nl.buildforce.olingo.commons.api.data.EntityMediaObject;
import nl.buildforce.olingo.commons.api.edm.EdmEntityType;
import nl.buildforce.olingo.commons.api.ex.ODataRuntimeException;
import nl.buildforce.olingo.server.api.ODataLibraryException;
import nl.buildforce.olingo.server.api.ServiceMetadata;
import nl.buildforce.olingo.server.api.ODataContent;
import nl.buildforce.olingo.server.api.ODataContentWriteErrorCallback;
import nl.buildforce.olingo.server.api.ODataContentWriteErrorContext;
import nl.buildforce.olingo.server.api.serializer.EntityCollectionSerializerOptions;
import nl.buildforce.olingo.server.api.serializer.ODataSerializer;
import nl.buildforce.olingo.server.api.serializer.SerializerException;
import nl.buildforce.olingo.server.api.serializer.SerializerStreamResult;
import nl.buildforce.olingo.server.core.serializer.SerializerStreamResultImpl;
import nl.buildforce.olingo.server.core.serializer.FixedFormatSerializerImpl;
import nl.buildforce.olingo.server.core.serializer.json.ODataJsonSerializer;
import nl.buildforce.olingo.server.core.serializer.xml.ODataXmlSerializer;

/**
 * Stream supporting implementation of the ODataContent
 * and contains the response content for the OData request.
 * <p/>
 * If an error occur during a <code>write</code> method <b>NO</b> exception
 * will be thrown but if registered the
 * org.apache.olingo.server.api.ODataContentWriteErrorCallback is called.
 */
public class ODataWritableContent implements ODataContent {
  private final StreamContent streamContent;

  private static abstract class StreamContent {
    protected EntityIterator iterator;
    protected ServiceMetadata metadata;
    protected EdmEntityType entityType;
    protected EntityCollectionSerializerOptions options;
    protected EntityMediaObject mediaEntity;

    public StreamContent(EntityIterator iterator, EdmEntityType entityType, ServiceMetadata metadata,
        EntityCollectionSerializerOptions options) {
      this.iterator = iterator;
      this.entityType = entityType;
      this.metadata = metadata;
      this.options = options;
    }

    public StreamContent(EntityMediaObject mediaEntity) {
    	this.mediaEntity = mediaEntity;
    }
    
    protected abstract void writeEntity(EntityIterator entity, OutputStream outputStream) throws SerializerException;

    protected abstract void writeBinary(EntityMediaObject mediaEntity, OutputStream outputStream)
    		throws SerializerException;
    
    public void write(OutputStream out) {
      try {
    	  if (mediaEntity == null) {
    		  writeEntity(iterator, out);
    	  } else {
    		  writeBinary(mediaEntity, out);
    	  }
      } catch (SerializerException e) {
        ODataContentWriteErrorCallback errorCallback = options.getODataContentWriteErrorCallback();
        if (errorCallback != null) {
          WriteErrorContext errorContext = new WriteErrorContext(e);
          errorCallback.handleError(errorContext, Channels.newChannel(out));
        }
      }
    }
  }

  private static class StreamContentForJson extends StreamContent {
    private final ODataJsonSerializer jsonSerializer;

    public StreamContentForJson(EntityIterator iterator, EdmEntityType entityType,
        ODataJsonSerializer jsonSerializer, ServiceMetadata metadata,
        EntityCollectionSerializerOptions options) {
      super(iterator, entityType, metadata, options);

      this.jsonSerializer = jsonSerializer;
    }

    protected void writeEntity(EntityIterator entity, OutputStream outputStream) throws SerializerException {
      try {
        jsonSerializer.entityCollectionIntoStream(metadata, entityType, entity, options, outputStream);
        outputStream.flush();
      } catch (IOException e) {
        throw new ODataRuntimeException("Failed entity serialization", e);
      }
    }
    @Override
	protected void writeBinary(EntityMediaObject mediaEntity, 
			OutputStream outputStream) {
		throw new ODataRuntimeException("Not Implemented in Entity Handling");
	}
  }
  
  private static class StreamContentForMedia extends StreamContent {
	    private final FixedFormatSerializerImpl fixedFormatSerializer;

	    public StreamContentForMedia(EntityMediaObject mediaEntity, 
	    		FixedFormatSerializerImpl fixedFormatSerializer) {
	      super(mediaEntity);

	      this.fixedFormatSerializer = fixedFormatSerializer;
	    }

	    protected void writeEntity(EntityIterator entity, 
	        OutputStream outputStream) {
	    	throw new ODataRuntimeException("Not Implemented in Entity Handling");
	    }

		@Override
		protected void writeBinary(EntityMediaObject mediaEntity, 
				OutputStream outputStream) throws SerializerException {
			fixedFormatSerializer.binaryIntoStreamed(mediaEntity, outputStream);
		}
	  }

  private static class StreamContentForXml extends StreamContent {
    private final ODataXmlSerializer xmlSerializer;

    public StreamContentForXml(EntityIterator iterator, EdmEntityType entityType,
        ODataXmlSerializer xmlSerializer, ServiceMetadata metadata,
        EntityCollectionSerializerOptions options) {
      super(iterator, entityType, metadata, options);

      this.xmlSerializer = xmlSerializer;
    }

    protected void writeEntity(EntityIterator entity, OutputStream outputStream) throws SerializerException {
      try {
        xmlSerializer.entityCollectionIntoStream(metadata, entityType, entity, options, outputStream);
        outputStream.flush();
      } catch (IOException e) {
        throw new ODataRuntimeException("Failed entity serialization", e);
      }
    }
    
	protected void writeBinary(EntityMediaObject mediaEntity, 
			OutputStream outputStream) {
		throw new ODataRuntimeException("Not Implemented in XML Handling");
	}
  }

  @Override
  public void write(WritableByteChannel writeChannel) {
      streamContent.write(Channels.newOutputStream(writeChannel));
  }

/*
  @Override
  public void write(OutputStream stream) {
    write(Channels.newChannel(stream));
  }
*/

  private ODataWritableContent(StreamContent streamContent) {
    this.streamContent = streamContent;
  }

  public static ODataWritableContentBuilder with(EntityIterator iterator, EdmEntityType entityType,
      ODataSerializer serializer, ServiceMetadata metadata,
      EntityCollectionSerializerOptions options) {
    return new ODataWritableContentBuilder(iterator, entityType, serializer, metadata, options);
  }
  
  public static ODataWritableContentBuilder with(EntityMediaObject mediaEntity, 
		  FixedFormatSerializerImpl fixedFormatSerializer) {
	  return new ODataWritableContentBuilder(mediaEntity, fixedFormatSerializer);
  }

  public static class WriteErrorContext implements ODataContentWriteErrorContext {

      public WriteErrorContext(ODataLibraryException exception) {
      }

    /*@Override
    public Exception getException() {
      return exception;
    }

    @Override
    public ODataLibraryException getODataLibraryException() {
      return exception;
    }*/
  }

  public static class ODataWritableContentBuilder {
    private ODataSerializer serializer;
    private EntityIterator entities;
    private ServiceMetadata metadata;
    private EdmEntityType entityType;
    private EntityCollectionSerializerOptions options;
    private FixedFormatSerializerImpl fixedFormatSerializer;
    private EntityMediaObject mediaEntity;

    public ODataWritableContentBuilder(EntityIterator entities, EdmEntityType entityType,
        ODataSerializer serializer,
        ServiceMetadata metadata, EntityCollectionSerializerOptions options) {
      this.entities = entities;
      this.entityType = entityType;
      this.serializer = serializer;
      this.metadata = metadata;
      this.options = options;
    }

    public ODataWritableContentBuilder(EntityMediaObject mediaEntity, FixedFormatSerializerImpl fixedFormatSerializer) {
    	this.mediaEntity = mediaEntity;
    	this.fixedFormatSerializer = fixedFormatSerializer;
    }
    
    public ODataContent buildContent() {
      if (serializer instanceof ODataJsonSerializer) {
        StreamContent input = new StreamContentForJson(entities, entityType,
            (ODataJsonSerializer) serializer, metadata, options);
        return new ODataWritableContent(input);
      }else if (serializer instanceof ODataXmlSerializer) {
        StreamContentForXml input = new StreamContentForXml(entities, entityType,
            (ODataXmlSerializer) serializer, metadata, options);
        return new ODataWritableContent(input);
      } else if (fixedFormatSerializer != null) {
    	  StreamContent input = new StreamContentForMedia(mediaEntity, fixedFormatSerializer);
    	  return new ODataWritableContent(input);
      }
      throw new ODataRuntimeException("No suitable serializer found");
    }

    public SerializerStreamResult build() {
      return SerializerStreamResultImpl.with().content(buildContent()).build();
    }
  }

}