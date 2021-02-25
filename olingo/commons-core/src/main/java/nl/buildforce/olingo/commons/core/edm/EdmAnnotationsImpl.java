/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmAnnotations;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlAnnotations;

public class EdmAnnotationsImpl extends AbstractEdmAnnotatable implements EdmAnnotations {

  private final CsdlAnnotations annotationGroup;

  public EdmAnnotationsImpl(Edm edm, CsdlAnnotations annotationGroup) {
    super(edm, annotationGroup);
    this.annotationGroup = annotationGroup;
  }

  @Override
  public String getQualifier() {
    return annotationGroup.getQualifier();
  }

  @Override
  public String getTargetPath() {
    return annotationGroup.getTarget();
  }
}
