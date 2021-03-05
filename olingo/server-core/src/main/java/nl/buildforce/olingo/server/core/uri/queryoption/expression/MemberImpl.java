/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.queryoption.expression;

import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.server.api.ODataApplicationException;
import nl.buildforce.olingo.server.api.uri.UriResourcePartTyped;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.ExpressionVisitor;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.Member;
import nl.buildforce.olingo.server.core.uri.UriInfoImpl;
import nl.buildforce.olingo.server.core.uri.UriResourceActionImpl;
import nl.buildforce.olingo.server.core.uri.UriResourceTypedImpl;
import nl.buildforce.olingo.server.core.uri.UriResourceWithKeysImpl;
import nl.buildforce.olingo.server.api.uri.UriInfoResource;
import nl.buildforce.olingo.server.api.uri.UriResource;
import nl.buildforce.olingo.server.core.uri.UriResourceImpl;

public class MemberImpl implements Member {

  private final UriInfoResource path;
  private final EdmType startTypeFilter;

  public MemberImpl(UriInfoResource path, EdmType startTypeFilter) {
    this.path = path;
    this.startTypeFilter = startTypeFilter;
  }

  @Override
  public UriInfoResource getResourcePath() {
    return path;
  }

  @Override
  public EdmType getStartTypeFilter() {
    return startTypeFilter;
  }

  @Override
  public <T> T accept(ExpressionVisitor<T> visitor) throws ODataApplicationException {
    return visitor.visitMember(this);
  }

  @Override
  public EdmType getType() {
    UriInfoImpl uriInfo = (UriInfoImpl) path;
    UriResourceImpl lastResourcePart = (UriResourceImpl) uriInfo.getLastResourcePart();

    if (lastResourcePart instanceof UriResourceWithKeysImpl) {
      UriResourceWithKeysImpl lastKeyPred = (UriResourceWithKeysImpl) lastResourcePart;
      if (lastKeyPred.getTypeFilterOnEntry() != null) {
        return lastKeyPred.getTypeFilterOnEntry();
      } else if (lastKeyPred.getTypeFilterOnCollection() != null) {
        return lastKeyPred.getTypeFilterOnCollection();
      }
      return lastKeyPred.getType();
    } else if (lastResourcePart instanceof UriResourceTypedImpl) {
      UriResourceTypedImpl lastTyped = (UriResourceTypedImpl) lastResourcePart;
      EdmType type = lastTyped.getTypeFilter();
      if (type != null) {
        return type;
      }
      return lastTyped.getType();
    } else if (lastResourcePart instanceof UriResourceActionImpl) {
      return ((UriResourceActionImpl) lastResourcePart).getType();
    } else {
      return null;
    }
  }

  @Override
  public boolean isCollection() {
    UriInfoImpl uriInfo = (UriInfoImpl) path;
    UriResource lastResourcePart = uriInfo.getLastResourcePart();
    return lastResourcePart instanceof UriResourcePartTyped && ((UriResourcePartTyped) lastResourcePart).isCollection();
  }

  @Override
  public String toString() {
    return path.getUriResourceParts().toString() + (startTypeFilter == null ? "" : startTypeFilter);
  }

}