/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm.annotation;

import java.util.List;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmAnnotatable;
import nl.buildforce.olingo.commons.api.edm.EdmAnnotation;
import nl.buildforce.olingo.commons.api.edm.EdmTerm;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlAnnotatable;
import nl.buildforce.olingo.commons.core.edm.AbstractEdmAnnotatable;

public abstract class AbstractEdmAnnotatableDynamicExpression extends AbstractEdmDynamicExpression implements
    EdmAnnotatable {

  private final AnnotationHelper helper;

  public AbstractEdmAnnotatableDynamicExpression(Edm edm, String name, CsdlAnnotatable annotatable) {
    super(edm, name);
    helper = new AnnotationHelper(edm, annotatable);
  }

  @Override
  public EdmAnnotation getAnnotation(EdmTerm term, String qualifier) {
    return helper.getAnnotation(term, qualifier);
  }

  @Override
  public List<EdmAnnotation> getAnnotations() {
    return helper.getAnnotations();
  }

  private static class AnnotationHelper extends AbstractEdmAnnotatable {

    public AnnotationHelper(Edm edm, CsdlAnnotatable annotatable) {
      super(edm, annotatable);
    }
  }
}
