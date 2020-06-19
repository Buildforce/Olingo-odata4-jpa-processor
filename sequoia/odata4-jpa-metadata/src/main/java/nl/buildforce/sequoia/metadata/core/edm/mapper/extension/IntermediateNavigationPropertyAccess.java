package nl.buildforce.sequoia.metadata.core.edm.mapper.extension;

import nl.buildforce.olingo.commons.api.edm.provider.CsdlAnnotation;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlOnDelete;
import nl.buildforce.sequoia.metadata.core.edm.annotation.EdmAnnotation;

import java.util.List;

public interface IntermediateNavigationPropertyAccess extends IntermediateModelItemAccess {
  void setOnDelete(CsdlOnDelete onDelete);

  /**
   * Enables to add annotations to a property, e.g. because the type of annotation is not enabled via
   * {@link EdmAnnotation EdmAnnotation} or should be during runtime
   * @param annotations
   */
  void addAnnotations(final List<CsdlAnnotation> annotations);

}