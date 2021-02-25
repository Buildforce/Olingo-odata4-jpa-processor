/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.etag;

import nl.buildforce.olingo.commons.api.edm.EdmBindingTarget;
import nl.buildforce.olingo.commons.api.edm.EdmFunctionImport;
import nl.buildforce.olingo.commons.api.edm.EdmNavigationProperty;
import nl.buildforce.olingo.server.api.etag.CustomETagSupport;
import nl.buildforce.olingo.server.api.uri.UriResourceEntitySet;
import nl.buildforce.olingo.server.api.uri.UriResourceFunction;
import nl.buildforce.olingo.server.api.uri.UriResourceNavigation;
import nl.buildforce.olingo.server.api.uri.UriResourceSingleton;
import nl.buildforce.olingo.server.api.etag.PreconditionException;
import nl.buildforce.olingo.server.api.uri.UriInfo;
import nl.buildforce.olingo.server.api.uri.UriResource;

public class PreconditionsValidator {

  private final EdmBindingTarget affectedEntitySetOrSingleton;

  public PreconditionsValidator(UriInfo uriInfo) throws PreconditionException {
    affectedEntitySetOrSingleton = extractInformation(uriInfo);
  }

  public boolean mustValidatePreconditions(CustomETagSupport customETagSupport, boolean isMediaValue) {
    return affectedEntitySetOrSingleton != null
        && (isMediaValue ?
            customETagSupport.hasMediaETag(affectedEntitySetOrSingleton) :
              customETagSupport.hasETag(affectedEntitySetOrSingleton));
  }

  private EdmBindingTarget extractInformation(UriInfo uriInfo) throws PreconditionException {
    EdmBindingTarget lastFoundEntitySetOrSingleton = null;
    int counter = 0;
    for (UriResource uriResourcePart : uriInfo.getUriResourceParts()) {
      switch (uriResourcePart.getKind()) {
      case function:
        lastFoundEntitySetOrSingleton = getEntitySetFromFunctionImport((UriResourceFunction) uriResourcePart);
        break;
      case singleton:
        lastFoundEntitySetOrSingleton = ((UriResourceSingleton) uriResourcePart).getSingleton();
        break;
      case entitySet:
        lastFoundEntitySetOrSingleton = getEntitySet((UriResourceEntitySet) uriResourcePart);
        break;
      case navigationProperty:
        lastFoundEntitySetOrSingleton = getEntitySetFromNavigation(lastFoundEntitySetOrSingleton,
            (UriResourceNavigation) uriResourcePart);
        break;
      case primitiveProperty:
      case complexProperty:
        break;
      case value:
      case action:
        // This should not be possible since the URI Parser validates this but to be sure we throw an exception.
        if (counter != uriInfo.getUriResourceParts().size() - 1) {
          throw new PreconditionException("$value or Action must be the last segment in the URI.",
              PreconditionException.MessageKeys.INVALID_URI);
        }
        break;
      default:
        lastFoundEntitySetOrSingleton = null;
        break;
      }
      if (lastFoundEntitySetOrSingleton == null) {
        // Once we loose track of the entity set there is no way to retrieve it.
        break;
      }
      counter++;
    }
    return lastFoundEntitySetOrSingleton;
  }

  private EdmBindingTarget getEntitySetFromFunctionImport(UriResourceFunction uriResourceFunction) {
    EdmFunctionImport functionImport = uriResourceFunction.getFunctionImport();
    if (functionImport != null && functionImport.getReturnedEntitySet() != null
        && !uriResourceFunction.isCollection()) {
      return functionImport.getReturnedEntitySet();
    }
    return null;
  }

  private EdmBindingTarget getEntitySet(UriResourceEntitySet uriResourceEntitySet) {
    return uriResourceEntitySet.isCollection() ? null : uriResourceEntitySet.getEntitySet();
  }

  private EdmBindingTarget getEntitySetFromNavigation(EdmBindingTarget lastFoundEntitySetOrSingleton,
                                                      UriResourceNavigation uriResourceNavigation) {
    if (lastFoundEntitySetOrSingleton != null && !uriResourceNavigation.isCollection()) {
      EdmNavigationProperty navProp = uriResourceNavigation.getProperty();
      return lastFoundEntitySetOrSingleton.getRelatedBindingTarget(navProp.getName());
    }
    return null;
  }
}
