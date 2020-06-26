package nl.buildforce.sequoia.processor.core.processor;

import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAAssociationPath;
import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAEntityType;
import nl.buildforce.sequoia.processor.core.api.JPAODataClaimProvider;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Representing an entity that should be created, updated or deleted by a POST, PUT, PATCH or DELETE request
 * @author Oliver Grande
 *
 */
public interface JPARequestEntity {
  /**
   * Returns all OData request header
   * @return an unmodifiable Map of header names/values
   */
  Map<String, List<String>> getAllHeader();

  /**
   * For the creation of a dependent entity an instance of the requested entity (root entity) is provided. <br> The
   * instance must not be merged
   * @return
   */
  Optional<Object> getBeforeImage();

  /**
   * Provides the given claims of a user
   * @return
   */
  Optional<JPAODataClaimProvider> getClaims();

  /**
   * List of attributes with pojo attributes name converted into JAVA types. In case the entity contains embedded
   * attributes these are given as maps themselves.<p>
   * @return
   */
  Map<String, Object> getData();

  /**
   * Provides an instance of the entity metadata
   * @return
   */
  JPAEntityType getEntityType();

  /**
   * Returns a list of given filed groups
   * @return
   */
  List<String> getGroups();

  /**
   * Contains the key attributes of the entity to be update or deleted. Returns an empty Map in case of create.
   * @return
   */
  Map<String, Object> getKeys();

  /**
   * Returns an instance utility service
   * @return
   */
  JPAModifyUtil getModifyUtil();

  /**
   * <a href=
   * "https://docs.oasis-open.org/odata/odata/v4.0/errata03/os/complete/part1-protocol/odata-v4.0-errata03-os-part1-protocol-complete.html#_Toc453752299">
   * 11.4.2.2 Create Related Entities When Creating an Entity</a>
   * @return
   */
  Map<JPAAssociationPath, List<JPARequestEntity>> getRelatedEntities();

  /**
   * <a href=
   * "https://docs.oasis-open.org/odata/odata/v4.0/errata03/os/complete/part1-protocol/odata-v4.0-errata03-os-part1-protocol-complete.html#_Toc453752299">
   * 11.4.2.1 Link to Related Entities When Creating an Entity</a>
   * @return
   */
  Map<JPAAssociationPath, List<JPARequestLink>> getRelationLinks();

}