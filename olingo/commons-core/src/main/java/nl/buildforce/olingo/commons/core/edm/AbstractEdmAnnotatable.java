/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmAnnotatable;
import nl.buildforce.olingo.commons.api.edm.EdmAnnotation;
import nl.buildforce.olingo.commons.api.edm.EdmTerm;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlAnnotatable;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlAnnotation;

public abstract class AbstractEdmAnnotatable implements EdmAnnotatable {

  private final CsdlAnnotatable annotatable;
  private List<EdmAnnotation> annotations;
  protected final Edm edm;

  public AbstractEdmAnnotatable(Edm edm, CsdlAnnotatable annotatable) {
    this.edm = edm;
    this.annotatable = annotatable;
  }

  @Override
  public EdmAnnotation getAnnotation(EdmTerm term, String qualifier) {
    EdmAnnotation result = null;
    for (EdmAnnotation annotation : getAnnotations()) {
      if (term.getFullQualifiedName().equals(annotation.getTerm().getFullQualifiedName())
          && qualifierEqual(qualifier, annotation.getQualifier())) {
        result = annotation;
        break;
      }
    }
    return result;
  }

  private boolean qualifierEqual(String qualifier, String annotationQualifier) {
    return (qualifier == null && annotationQualifier == null)
        || (qualifier != null && qualifier.equals(annotationQualifier));
  }

  @Override
  public List<EdmAnnotation> getAnnotations() {
    if (annotations == null) {
      List<EdmAnnotation> annotationsLocal = new ArrayList<>();
      if (annotatable != null) {
        for (CsdlAnnotation annotation : annotatable.getAnnotations()) {
          annotationsLocal.add(new EdmAnnotationImpl(edm, annotation));
        }

        annotations = Collections.unmodifiableList(annotationsLocal);
      }
    }
    return annotations;
  }

}