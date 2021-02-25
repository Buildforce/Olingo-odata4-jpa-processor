/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmAnnotation;
import nl.buildforce.olingo.commons.api.edm.EdmException;
import nl.buildforce.olingo.commons.api.edm.EdmTerm;
import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmExpression;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlAnnotation;
import nl.buildforce.olingo.commons.core.edm.annotation.AbstractEdmExpression;

public class EdmAnnotationImpl extends AbstractEdmAnnotatable implements EdmAnnotation {

  private final CsdlAnnotation annotation;
  private EdmTerm term;
  private EdmExpression expression;

  public EdmAnnotationImpl(Edm edm, CsdlAnnotation annotation) {
    super(edm, annotation);
    this.annotation = annotation;
  }

  @Override
  public EdmTerm getTerm() {
    if (term == null) {
      if (annotation.getTerm() == null) {
        throw new EdmException("Term must not be null for an annotation.");
      }
      term = edm.getTerm(new FullQualifiedName(annotation.getTerm()));
    }
    return term;
  }

  @Override
  public String getQualifier() {
    return annotation.getQualifier();
  }

 

  @Override
  public EdmExpression getExpression() {
    if (expression == null && annotation.getExpression() != null) {
      expression = AbstractEdmExpression.getExpression(edm, annotation.getExpression());
    }
    return expression;
  }
}
