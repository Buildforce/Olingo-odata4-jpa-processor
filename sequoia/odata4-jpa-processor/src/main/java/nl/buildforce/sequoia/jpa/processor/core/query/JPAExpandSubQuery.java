package nl.buildforce.sequoia.jpa.processor.core.query;

/**
 * A query to retrieve the expand entities.<p> According to
 * <a href=
 * "http://docs.oasis-open.org/odata/odata/v4.0/errata02/os/complete/part2-url-conventions/odata-v4.0-errata02-os-part2-url-conventions-complete.html#_Toc406398162"
 * >OData Version 4.0 Part 2 - 5.1.2 System Query Option $expand</a> the following query options are allowed:
 * <ul>
 * <li>expandCountOption = <b>filter</b>/ search<p>
 * <li>expandRefOption = expandCountOption/ <b>orderby</b> / <b>skip</b> / <b>top</b> / inlinecount
 * <li>expandOption = expandRefOption/ <b>select</b>/ <b>expand</b> / <b>levels</b> <p>
 * </ul>
 * As of now only the bold once are supported
 * <p>
 * @author Oliver Grande
 * @since 0.3.6
 */

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAAssociationPath;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAEntityType;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAException;
import nl.buildforce.sequoia.jpa.processor.core.api.JPAODataCRUDContextAccess;
import nl.buildforce.sequoia.jpa.processor.core.api.JPAODataRequestContextAccess;
import org.apache.olingo.server.api.OData;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

public class JPAExpandSubQuery extends JPAAbstractJoinQuery {

  private final JPAAssociationPath association;
  private final SortedSet<? extends Comparable<?>> rootKeys;

  public JPAExpandSubQuery(final OData odata, final JPAODataCRUDContextAccess sessionContext,
      final JPAInlineItemInfo item, final Map<String, List<String>> requestHeaders,
      final JPAODataRequestContextAccess requestContext, final SortedSet<? extends Comparable<?>> rootKeys)
      throws ODataJPAException {

    super(odata, sessionContext, item.getEntityType(), item.getUriInfo(), requestContext, requestHeaders,
        item.getHops());
    this.association = item.getExpandAssociation();
    this.rootKeys = rootKeys;
  }

  public <K extends Comparable<K>> JPAExpandSubQuery(final OData odata, final JPAODataCRUDContextAccess context,
      final JPAAssociationPath association, final JPAEntityType entityType,
      final Map<String, List<String>> requestHeaders, final JPAODataRequestContextAccess requestContext,
      final SortedSet<K> rootKeys)
      throws ODataJPAException {

    super(odata, context, entityType, requestContext, requestHeaders, Collections.emptyList());
    this.association = association;
    this.rootKeys = rootKeys;
  }

  @Override
  public JPAConvertibleResult execute() {
    // TODO Auto-generated method stub
    return null;
  }
}