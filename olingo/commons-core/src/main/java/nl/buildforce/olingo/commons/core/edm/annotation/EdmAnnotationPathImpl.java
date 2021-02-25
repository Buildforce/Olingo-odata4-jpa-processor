/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm.annotation;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmAnnotationPath;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlAnnotationPath;

public class EdmAnnotationPathImpl extends AbstractEdmDynamicExpression implements EdmAnnotationPath {

  private final CsdlAnnotationPath csdlExp;

  public EdmAnnotationPathImpl(Edm edm, CsdlAnnotationPath csdlExp) {
    super(edm, "AnnotationPath");
    this.csdlExp = csdlExp;
  }

  @Override
  public String getValue() {
    return csdlExp.getValue();
  }

  @Override
  public EdmExpressionType getExpressionType() {
    return EdmExpressionType.AnnotationPath;
  }
}
