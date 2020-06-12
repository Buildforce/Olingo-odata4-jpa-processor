package nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api;

/**
 * Provides information about a protected attribute
 * @author Oliver Grande
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