package nl.buildforce.sequoia.metadata.api;

import nl.buildforce.sequoia.metadata.core.edm.mapper.exception.ODataJPAModelException;
import nl.buildforce.sequoia.metadata.core.edm.mapper.extension.IntermediateEntityContainerAccess;
import nl.buildforce.sequoia.metadata.core.edm.mapper.extension.IntermediateEntitySetAccess;
import nl.buildforce.sequoia.metadata.core.edm.mapper.extension.IntermediateEntityTypeAccess;
import nl.buildforce.sequoia.metadata.core.edm.mapper.extension.IntermediateNavigationPropertyAccess;
import nl.buildforce.sequoia.metadata.core.edm.mapper.extension.IntermediatePropertyAccess;
import nl.buildforce.sequoia.metadata.core.edm.mapper.extension.IntermediateReferenceList;

public abstract class JPAEdmMetadataPostProcessor {

  public void processEntityContainer(final IntermediateEntityContainerAccess container) {}

  public abstract void processEntityType(final IntermediateEntityTypeAccess entityType);

  public void processEntitySet(final IntermediateEntitySetAccess entitySet) {}

  public abstract void processNavigationProperty(final IntermediateNavigationPropertyAccess property,
      final String jpaManagedTypeClassName);

  /**
   *

   * @param property
   * @param jpaManagedTypeClassName
   * @return
   */
  public abstract void processProperty(final IntermediatePropertyAccess property, final String jpaManagedTypeClassName);

}