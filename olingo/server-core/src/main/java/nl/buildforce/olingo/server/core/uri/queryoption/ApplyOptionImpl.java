/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.queryoption;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.buildforce.olingo.commons.api.edm.EdmStructuredType;
import nl.buildforce.olingo.server.api.uri.queryoption.ApplyOption;
import nl.buildforce.olingo.server.api.uri.queryoption.ApplyItem;
import nl.buildforce.olingo.server.api.uri.queryoption.SystemQueryOptionKind;

public class ApplyOptionImpl extends SystemQueryOptionImpl implements ApplyOption {

  private final List<ApplyItem> transformations = new ArrayList<>();
  private EdmStructuredType edmStructuredType;

  public ApplyOptionImpl() {
    setKind(SystemQueryOptionKind.APPLY);
  }

  @Override
  public List<ApplyItem> getApplyItems() {
    return Collections.unmodifiableList(transformations);
  }

  public ApplyOptionImpl add(ApplyItem transformation) {
    transformations.add(transformation);
    return this;
  }

  public void setEdmStructuredType(EdmStructuredType referencedType) {
      edmStructuredType = referencedType;
  }
  
  @Override
  public EdmStructuredType getEdmStructuredType() {
    return edmStructuredType;
  }
}