/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
 */
package nl.buildforce.olingo.server.api;

import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveType;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlEdmProvider;
import nl.buildforce.olingo.commons.api.edmx.EdmxReference;
import nl.buildforce.olingo.commons.api.ex.ODataRuntimeException;
import nl.buildforce.olingo.commons.api.format.ContentType;
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

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;

/**
 * Root object for serving factory tasks and support loose coupling of implementation (core) from the API.
 * This is not a singleton (static variables) to avoid issues with synchronization, OSGi, hot deployment and so on.
 * Each thread (request) should keep its own instance.
 */
public abstract class OData {

    private static final String IMPLEMENTATION = "nl.buildforce.olingo.server.core.ODataImpl";

    /**
     * Use this method to create a new OData instance. Each thread/request should keep its own instance.
     *
     * @return a new OData instance
     */
    public static OData newInstance() {
        try {
            Class<?> clazz = Class.forName(IMPLEMENTATION);

            /*
             * We explicitly do not use the singleton pattern to keep the server state free
             * and avoid class loading issues also during hot deployment.
             */
            return (OData) clazz.getConstructor().newInstance();

        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new ODataRuntimeException(e);
        }
    }

    /**
     * Creates a new serializer object for rendering content in the specified format.
     * Serializers are used in Processor implementations.
     *
     * @param contentType any format supported by Olingo (XML, JSON ...)
     */
    public abstract ODataSerializer createSerializer(ContentType contentType) throws SerializerException;

    /**
     * Creates a new serializer object for rendering content in the specified format.
     * Serializers are used in Processor implementations.
     *
     * @param contentType any format supported by Olingo (XML, JSON ...)
     * @param versions    any v4 version supported by Olingo (4.0, 4.01 ...)
     */
    public abstract ODataSerializer createSerializer(ContentType contentType,
                                                     List<String> versions) throws SerializerException;

    /**
     * Creates a new serializer object for rendering content in a fixed format, e.g., for binary output or multipart/mixed
     * outpu.
     * Serializers are used in Processor implementations.
     */
    public abstract FixedFormatSerializer createFixedFormatSerializer();

    /**
     * Creates a new deserializer object for reading content in a fixed format, e.g., for binary input.
     * Deserializers are used in Processor implementations.
     */
    public abstract FixedFormatDeserializer createFixedFormatDeserializer();

    /**
     * Creates a new ODataHttpHandler for handling OData requests in an HTTP context.
     *
     * @param serviceMetadata - metadata object required to handle an OData request
     */
    public abstract ODataHttpHandler createHandler(ServiceMetadata serviceMetadata);

// --Commented out by Inspection START (''21-03-09 16:29):
//  /**
//   * Creates a new ODataHandler for handling OData requests.
//   *
//   * @param serviceMetadata - metadata object required to handle an OData request
//   */
//  public abstract ODataHandler createRawHandler(ServiceMetadata serviceMetadata);
// --Commented out by Inspection STOP (''21-03-09 16:29)

    /**
     * Creates a metadata object for this service.
     *
     * @param edmProvider a custom or default implementation for creating metadata
     * @param references  list of edmx references
     * @return a service metadata implementation
     */
    public abstract ServiceMetadata createServiceMetadata(CsdlEdmProvider edmProvider, List<EdmxReference> references);

    /**
     * Creates a metadata object for this service.
     *
     * @param edmProvider                a custom or default implementation for creating metadata
     * @param references                 list of edmx references
     * @param serviceMetadataETagSupport
     * @return a service metadata implementation
     */
    public abstract ServiceMetadata createServiceMetadata(CsdlEdmProvider edmProvider, List<EdmxReference> references,
                                                          ServiceMetadataETagSupport serviceMetadataETagSupport);

    /**
     * Creates a new URI helper object for performing URI-related tasks.
     * It can be used in Processor implementations.
     */
    public abstract UriHelper createUriHelper();

    /**
     * Creates a new deserializer object for reading content in the specified format.
     * Deserializers are used in Processor implementations.
     *
     * @param contentType any content type supported by Olingo (XML, JSON ...)
     */
    public abstract ODataDeserializer createDeserializer(ContentType contentType) throws DeserializerException;

    /**
     * Creates a new deserializer object for reading content in the specified format.
     * Deserializers are used in Processor implementations.
     *
     * @param contentType any content type supported by Olingo (XML, JSON ...)
     * @param metadata    ServiceMetada of the service
     */
//    public abstract ODataDeserializer createDeserializer(ContentType contentType,
//                                                         ServiceMetadata metadata) throws DeserializerException;

    /**
     * Creates a new deserializer object for reading content in the specified format.
     * Deserializers are used in Processor implementations.
     *
     * @param contentType any content type supported by Olingo (XML, JSON ...)
     * @param versions    version
     */
    public abstract ODataDeserializer createDeserializer(ContentType contentType,
                                                         List<String> versions) throws DeserializerException;

    /**
     * Creates a new deserializer object for reading content in the specified format.
     * Deserializers are used in Processor implementations.
     *
     * @param contentType any content type supported by Olingo (XML, JSON ...)
     * @param metadata    ServiceMetada of the service
     * @param versions    version
     */
    // public abstract ODataDeserializer createDeserializer(ContentType contentType,
    //                                                     ServiceMetadata metadata, List<String> versions) throws DeserializerException;

    /**
     * Creates a primitive-type instance.
     *
     * @param kind the kind of the primitive type
     * @return an {@link EdmPrimitiveType} instance for the type kind
     */
    public abstract EdmPrimitiveType createPrimitiveTypeInstance(EdmPrimitiveTypeKind kind);

    /**
     * Creates a new ETag helper object for performing ETag-related tasks.
     * It can be used in Processor implementations.
     */
    public abstract ETagHelper createETagHelper();

    /**
     * Creates a new Preferences object out of Prefer HTTP request headers.
     * It can be used in Processor implementations.
     */
    public abstract Preferences createPreferences(Collection<String> preferHeaders);

    /*
     * Creates a DebugResponseHelper for the given debugFormat.
     * If the format is not supported no exception is thrown.
     * Instead we give back the implementation for the JSON format.
     * @param debugFormat format to be used
     * @return a debug-response helper
     */
    // public abstract DebugResponseHelper createDebugResponseHelper(String debugFormat);

    /**
     * Creates a new serializer object capable of working without EDM information
     * for rendering content in the specified format.
     *
     * @param contentType a content type supported by Olingo
     */
/*
    public abstract EdmAssistedSerializer createEdmAssistedSerializer(ContentType contentType)
            throws SerializerException;
*/

    /**
     * Creates a new serializer object capable of working without EDM information
     * for rendering delta content in the specified format.
     *
     * @param contentType a content type supported by Olingo
     * @param versions    versions supported by Olingo
     */
    // public abstract EdmDeltaSerializer createEdmDeltaSerializer(ContentType contentType,
    //                                                            List<String> versions) throws SerializerException;

}