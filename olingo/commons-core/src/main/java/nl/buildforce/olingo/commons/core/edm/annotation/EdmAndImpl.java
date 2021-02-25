/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm.annotation;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmAnd;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlLogicalOrComparisonExpression;

public class EdmAndImpl extends AbstractEdmLogicalOrComparisonExpression implements EdmAnd {

  public EdmAndImpl(Edm edm, CsdlLogicalOrComparisonExpression csdlExp) {
    super(edm, csdlExp);
  }
}
