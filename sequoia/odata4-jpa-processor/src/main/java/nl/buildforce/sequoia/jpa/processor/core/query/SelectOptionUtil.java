package nl.buildforce.sequoia.jpa.processor.core.query;

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAPath;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAStructuredType;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAModelException;
import nl.buildforce.sequoia.jpa.processor.core.exception.ODataJPAQueryException;
import nl.buildforce.sequoia.jpa.processor.core.exception.ODataJPAQueryException.MessageKeys;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.queryoption.SelectItem;
import org.apache.olingo.server.api.uri.queryoption.SelectOption;

import java.util.stream.Collectors;

/**
 *

 *

 * @author Oliver Grande
 * Created: 01.11.2019
 *
 */
class SelectOptionUtil {

  private SelectOptionUtil() {}

  public static JPAPath selectItemAsPath(final JPAStructuredType jpaEntity, final String pathPrefix,
      final SelectItem sItem) throws ODataJPAQueryException {

    try {
      final String pathItem = sItem.getResourcePath().getUriResourceParts().stream().map(UriResource::getSegmentValue).collect(Collectors.joining(JPAPath.PATH_SEPARATOR));
      JPAPath selectItemPath;

      selectItemPath = jpaEntity.getPath(pathPrefix.isEmpty() ? pathItem : pathPrefix
          + JPAPath.PATH_SEPARATOR + pathItem);
      if (selectItemPath == null)
        throw new ODataJPAQueryException(MessageKeys.QUERY_PREPARATION_INVALID_SELECTION_PATH,
            HttpStatusCode.BAD_REQUEST);
      return selectItemPath;
    } catch (ODataJPAModelException e) {
      throw new ODataJPAQueryException(e, HttpStatusCode.INTERNAL_SERVER_ERROR);
    }
  }

  public static boolean selectAll(final SelectOption select) {
    return select == null || select.getSelectItems() == null || select.getSelectItems().isEmpty() || select
        .getSelectItems().get(0).isStar();
  }
}