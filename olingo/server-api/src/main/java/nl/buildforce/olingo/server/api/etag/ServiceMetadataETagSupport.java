/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.etag;

/**
 * Register implementations for this interface in oder to support etags for the metadata document and service document.
 */
public interface ServiceMetadataETagSupport {

  /**
   * Since the Olingo library cannot generate a metadata document etag in a generic way we call this method to retrieve
   * an application specific etag for the metadata document. If this interface is registered applications can return an
   * etag or null here to provide caching support for clients. If a client sends a GET request to the metadata document
   * and this method delivers an etag we will match it to the request. If there has been no modification we will return
   * a 304 NOT MODIFIED status code. If this interface is not registered or delivers null we just send back the usual
   * metadata response.
   * @return the application generated etag for the metadata document
   */
  String getMetadataETag();

  /**
   * Since the Olingo library cannot generate a service document etag in a generic way we call this method to retrieve
   * an application specific etag for the service document. If this interface is registered applications can return an
   * etag or null here to provide caching support for clients. If a client sends a GET request to the service document
   * and this method delivers an etag we will match it to the request. If there has been no modification we will return
   * a 304 NOT MODIFIED status code. If this interface is not registered or delivers null we just send back the usual
   * service document response.
   * @return the application generated etag for the service document
   */
  String getServiceDocumentETag();

}
