package nl.buildforce.sequoia.jpa.processor.core.query;

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAAssociationPath;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAEntityType;
import org.apache.olingo.server.api.uri.UriInfoResource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class JPAInlineItemInfo {

  protected final JPAExpandItem uriInfo;
  protected final JPAAssociationPath expandAssociation;
  protected final List<JPANavigationPropertyInfo> hops;
  protected final List<JPANavigationPropertyInfo> parentHops;

  public JPAInlineItemInfo(final JPAExpandItem uriInfo,
      final JPAAssociationPath expandAssociation, final List<JPANavigationPropertyInfo> parentHops) {

    this.uriInfo = uriInfo;
    this.expandAssociation = expandAssociation;
    this.parentHops = parentHops;
    this.hops = new ArrayList<>();
  }

  public UriInfoResource getUriInfo() {
    return uriInfo;
  }

  public JPAAssociationPath getExpandAssociation() {
    return expandAssociation;
  }

  public List<JPANavigationPropertyInfo> getHops() {
    return Collections.unmodifiableList(hops);
  }

  public JPAEntityType getEntityType() {
    return uriInfo.getEntityType();
  }

}