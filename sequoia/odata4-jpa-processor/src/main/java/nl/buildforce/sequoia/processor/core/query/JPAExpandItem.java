package nl.buildforce.sequoia.processor.core.query;

import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAEntityType;
import nl.buildforce.olingo.server.api.uri.UriInfoResource;

public interface JPAExpandItem extends UriInfoResource {

  JPAEntityType getEntityType();

}