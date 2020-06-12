package nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api;

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAModelException;

import java.util.List;

public interface JPAEntityType extends JPAStructuredType {
  /**
   * Searches for a Collection Property defined by the name used in the OData metadata in all the collection properties
   * that are available for this type via the OData service. That is:
   * <ul>
   * <li> All not ignored collection properties of this type.
   * <li> All not ignored collection properties from super types.
   * <li> All not ignored collection properties from embedded types.
   * </ul>
   * @param externalName
   * @return
   * @throws ODataJPAModelException
   */
  JPACollectionAttribute getCollectionAttribute(final String externalName) throws ODataJPAModelException;

  /**
   *

   * @return Mime type of streaming content
   * @throws ODataJPAModelException
   */
  String getContentType() throws ODataJPAModelException;

  JPAPath getContentTypeAttributePath() throws ODataJPAModelException;

  JPAPath getEtagPath() throws ODataJPAModelException;

  /**
   * Returns a resolved list of all attributes that are marked as Id, so the attributes of an EmbeddedId are returned as
   * separate entries. For compound keys has the opposite order of the attributes in the entity or embedded id
   * respectively.
   * @return
   * @throws ODataJPAModelException
   */
  List<JPAAttribute> getKey() throws ODataJPAModelException;

  /**
   * Returns a list of path of all attributes annotated as Id. EmbeddedId are <b>not</b> resolved
   * @return
   * @throws ODataJPAModelException
   */
  List<JPAPath> getKeyPath() throws ODataJPAModelException;

  /**
   * Returns the class of the Key. This could by either a primitive type, the IdClass or the Embeddable of an EmbeddedId
   * @return
   */
  Class<?> getKeyType();

  /**
   * True in case the entity type has a compound key, so an EmbeddedId or multiple id properties
   * @return
   */
  boolean hasCompoundKey();

  /**
   *

   * @return
   * @throws ODataJPAModelException
   */
  List<JPAPath> getSearchablePath() throws ODataJPAModelException;

  JPAPath getStreamAttributePath() throws ODataJPAModelException;

  /**
   *

   * @return Name of the database table
   */
  String getTableName();

  boolean hasEtag() throws ODataJPAModelException;

  boolean hasStream() throws ODataJPAModelException;

  List<JPAPath> searchChildPath(final JPAPath selectItemPath);

}