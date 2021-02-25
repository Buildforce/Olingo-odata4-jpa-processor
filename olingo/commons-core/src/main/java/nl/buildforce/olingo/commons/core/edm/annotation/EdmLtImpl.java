/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm.annotation;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmLt;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlLogicalOrComparisonExpression;

public class EdmLtImpl extends AbstractEdmLogicalOrComparisonExpression implements EdmLt {

  public EdmLtImpl(Edm edm, CsdlLogicalOrComparisonExpression csdlExp) {
    super(edm, csdlExp);
  }
}
