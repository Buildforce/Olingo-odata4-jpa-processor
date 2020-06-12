package nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.extension;

import org.apache.olingo.commons.api.edm.provider.CsdlAnnotation;
import org.apache.olingo.commons.api.edm.provider.CsdlOnDelete;

import java.util.List;

public interface IntermediateNavigationPropertyAccess extends IntermediateModelItemAccess {
  void setOnDelete(CsdlOnDelete onDelete);

  /**
   * Enables to add annotations to a property, e.g. because the type of annotation is not enabled via
   * {@link nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmAnnotation EdmAnnotation} or should be during runtime
   * @param annotations
   */
  void addAnnotations(final List<CsdlAnnotation> annotations);

}