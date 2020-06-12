package nl.buildforce.sequoia.jpa.processor.core.query;

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAAssociationPath;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAServiceDocument;

import java.util.List;

public final class JPACollectionItemInfo extends JPAInlineItemInfo {

  JPACollectionItemInfo(final JPAServiceDocument sd, final JPAExpandItem uriInfo,
      final JPAAssociationPath expandAssociation, final List<JPANavigationPropertyInfo> hops) {

    super(uriInfo, expandAssociation, hops);

    for (JPANavigationPropertyInfo predecessor : hops)
      this.hops.add(new JPANavigationPropertyInfo(predecessor));
    this.hops.get(this.hops.size() - 1).setAssociationPath(expandAssociation);
    this.hops.add(new JPANavigationPropertyInfo(sd, expandAssociation, uriInfo, uriInfo.getEntityType()));
  }
}