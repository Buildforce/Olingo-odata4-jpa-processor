package nl.buildforce.sequoia.processor.core.api;

import java.util.List;

/**
 * Container that provides field groups
 *
 */
public interface JPAODataGroupProvider {
  /**
   * Provides a list of all field groups to be taken into account
   * @return
   */
  List<String> getGroups();

}