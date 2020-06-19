package nl.buildforce.sequoia.metadata.core.edm.mapper.extension;

import nl.buildforce.olingo.commons.api.edm.provider.CsdlAnnotation;
import nl.buildforce.sequoia.metadata.core.edm.annotation.EdmAnnotation;

import java.util.List;

public interface IntermediateEntityTypeAccess extends IntermediateModelItemAccess {
  /**
   * Enables to add annotations to a property, e.g. because the type of annotation is not enabled via
   * {@link EdmAnnotation EdmAnnotation} or should be during runtime
   * @param annotations
   */
  void addAnnotations(final List<CsdlAnnotation> annotations);
}