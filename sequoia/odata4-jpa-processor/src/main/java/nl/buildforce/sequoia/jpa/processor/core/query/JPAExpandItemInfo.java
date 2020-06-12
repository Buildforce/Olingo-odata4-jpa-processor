package nl.buildforce.sequoia.jpa.processor.core.query;

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAAssociationPath;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAServiceDocument;
import org.apache.olingo.server.api.ODataApplicationException;

import java.util.List;

public final class JPAExpandItemInfo extends JPAInlineItemInfo {

  JPAExpandItemInfo(final JPAServiceDocument sd, final JPAExpandItem uriInfo,
      final JPAAssociationPath expandAssociation, final List<JPANavigationPropertyInfo> hops)
      throws ODataApplicationException {

    super(uriInfo, expandAssociation, hops);

    for (JPANavigationPropertyInfo predecessor : hops)
      this.hops.add(new JPANavigationPropertyInfo(predecessor));
    this.hops.get(this.hops.size() - 1).setAssociationPath(expandAssociation);
    if (!uriInfo.getUriResourceParts().isEmpty())
      this.hops.addAll(Util.determineNavigationPath(sd, uriInfo.getUriResourceParts(), uriInfo));
    else
      this.hops.add(new JPANavigationPropertyInfo(sd, null, uriInfo, uriInfo.getEntityType()));
  }
}