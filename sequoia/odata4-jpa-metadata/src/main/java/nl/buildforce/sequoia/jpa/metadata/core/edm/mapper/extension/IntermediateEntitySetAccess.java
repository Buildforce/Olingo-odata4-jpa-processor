package nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.extension;

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAElement;
import org.apache.olingo.commons.api.edm.provider.CsdlAnnotation;

import java.util.List;

public interface IntermediateEntitySetAccess extends JPAElement {
  /**
   * Enables to add annotations to a property, e.g. because the type of annotation is not enabled via
   * {@link nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmAnnotation EdmAnnotation} or should be during runtime
   * @param annotations
   */
  void addAnnotations(final List<CsdlAnnotation> annotations);

  /**
   * Enables a rename external, OData, name of an entity set.
   * @param externalName
   */
  void setExternalName(String externalName);
}