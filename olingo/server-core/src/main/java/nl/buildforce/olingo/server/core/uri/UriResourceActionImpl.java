/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri;

import nl.buildforce.olingo.commons.api.edm.EdmAction;
import nl.buildforce.olingo.commons.api.edm.EdmActionImport;
import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.server.api.uri.UriResourceAction;
import nl.buildforce.olingo.server.api.uri.UriResourceKind;

/**
 * Implementation of the {@link UriResourceAction} interface. This class does not extend
 * {@link UriResourceTypedImpl UriResourceTypedImpl} since that would allow type
 * filters and subsequent path segments.
 */
public class UriResourceActionImpl extends UriResourceImpl implements UriResourceAction {

  private final EdmActionImport actionImport;
  private final EdmAction action;

  public UriResourceActionImpl(EdmActionImport actionImport) {
    super(UriResourceKind.action);
    this.actionImport = actionImport;
      action = actionImport.getUnboundAction();
  }

  public UriResourceActionImpl(EdmAction action) {
    super(UriResourceKind.action);
      actionImport = null;
    this.action = action;
  }

  @Override
  public EdmAction getAction() {
    return action;
  }

  @Override
  public EdmActionImport getActionImport() {
    return actionImport;
  }

  @Override
  public boolean isCollection() {
    return action.getReturnType() != null && action.getReturnType().isCollection();
  }

  @Override
  public EdmType getType() {
    return action.getReturnType() == null ? null : action.getReturnType().getType();
  }

  @Override
  public String getSegmentValue(boolean includeFilters) {
    return actionImport == null ? (action == null ? "" : action.getName()) : actionImport.getName();
  }

  @Override
  public String getSegmentValue() {
    return getSegmentValue(false);
  }

  @Override
  public String toString(boolean includeFilters) {
    return getSegmentValue(includeFilters);
  }
}
