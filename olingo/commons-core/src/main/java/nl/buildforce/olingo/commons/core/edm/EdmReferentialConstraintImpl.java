/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmReferentialConstraint;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlReferentialConstraint;

public class EdmReferentialConstraintImpl extends AbstractEdmAnnotatable implements EdmReferentialConstraint {

  private final CsdlReferentialConstraint constraint;

  public EdmReferentialConstraintImpl(Edm edm, CsdlReferentialConstraint constraint) {
    super(edm, constraint);
    this.constraint = constraint;
  }

  @Override
  public String getPropertyName() {
    return constraint.getProperty();
  }

  @Override
  public String getReferencedPropertyName() {
    return constraint.getReferencedProperty();
  }
}
