package nl.buildforce.sequoia.metadata.core.edm.mapper.extension;

import nl.buildforce.olingo.commons.api.edm.provider.CsdlAnnotation;
import nl.buildforce.sequoia.metadata.core.edm.annotation.EdmAnnotation;

import java.util.List;

/**
 * Override generated metadata for a property.
 * @author Oliver Grande
 *
 */
public interface IntermediatePropertyAccess extends IntermediateModelItemAccess {
  boolean isEtag();

  /**
   * Enables to add annotations to a property, e.g. because the type of annotation is not enabled via
   * {@link EdmAnnotation EdmAnnotation} or should changed be during
   * runtime
   * @param annotations
   */
  void addAnnotations(final List<CsdlAnnotation> annotations);

  boolean hasProtection();

}