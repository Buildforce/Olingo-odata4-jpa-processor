package nl.buildforce.sequoia.metadata.core.edm.mapper.api;

/**
 * Provides information about a protected attribute
 *
 */
public interface JPAProtectionInfo {
  /**
   * The protected attribute
   * @return
   */
  JPAAttribute getAttribute();

  /**
   * Path within the entity type to the attribute
   * @return
   */
  JPAPath getPath();

  /**
   * Claim names that shall be used to protect this attribute
   * @return
   */
  String getClaimName();

  /**
   * Returns the maintained wildcard setting.
   * @return
   */
  boolean supportsWildcards();

}