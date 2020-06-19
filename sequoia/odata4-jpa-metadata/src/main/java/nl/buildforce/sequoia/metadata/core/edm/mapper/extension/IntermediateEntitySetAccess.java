package nl.buildforce.sequoia.metadata.core.edm.mapper.extension;

import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAElement;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlAnnotation;
import nl.buildforce.sequoia.metadata.core.edm.annotation.EdmAnnotation;

import java.util.List;

public interface IntermediateEntitySetAccess extends JPAElement {
  /**
   * Enables to add annotations to a property, e.g. because the type of annotation is not enabled via
   * {@link EdmAnnotation EdmAnnotation} or should be during runtime
   * @param annotations
   */
  void addAnnotations(final List<CsdlAnnotation> annotations);

  /**
   * Enables a rename external, OData, name of an entity set.
   * @param externalName
   */
  void setExternalName(String externalName);
}