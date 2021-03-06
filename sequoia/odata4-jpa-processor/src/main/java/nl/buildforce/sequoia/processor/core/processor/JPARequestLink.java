package nl.buildforce.sequoia.processor.core.processor;

import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAEntityType;
import nl.buildforce.sequoia.processor.core.exception.ODataJPAProcessorException;

import java.util.Map;

public interface JPARequestLink {
  /**
   * Provides an instance of the target entity metadata
   * @return
   */
  JPAEntityType getEntityType();

  /**
   * Map of related keys
   * @return
   * @throws ODataJPAProcessorException
   */
  Map<String, Object> getRelatedKeys() throws ODataJPAProcessorException;

  Map<String, Object> getValues() throws ODataJPAProcessorException;
}