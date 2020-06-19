package nl.buildforce.sequoia.processor.core.processor;

import nl.buildforce.sequoia.processor.core.api.JPAODataPage;
import nl.buildforce.sequoia.processor.core.exception.JPAIllegalAccessException;
import nl.buildforce.sequoia.processor.core.serializer.JPASerializer;
import nl.buildforce.olingo.server.api.uri.UriInfo;

interface JPARequestContext {

  /**
   *

   * @param uriInfo
   * @throws JPAIllegalAccessException In case UriInfo already exists e.g. because a page was provided
   */
  void setUriInfo(final UriInfo uriInfo) throws JPAIllegalAccessException;

  void setJPASerializer(final JPASerializer serializer);

  /**
   * In case a page is provided UriInfo has to be taken from there
   * @param page
   * @throws JPAIllegalAccessException In case UriInfo already exists
   */
  void setJPAODataPage(final JPAODataPage page) throws JPAIllegalAccessException;
}