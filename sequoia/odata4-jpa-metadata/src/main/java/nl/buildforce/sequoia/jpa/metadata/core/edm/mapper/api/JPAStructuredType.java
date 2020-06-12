package nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api;

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAModelException;
import org.apache.olingo.server.api.uri.UriResourceProperty;

import java.util.List;

/**
 * External view on an Intermediate Structured Type.
 *
 * @author Oliver Grande
 *
 */
public interface JPAStructuredType extends JPAElement {
  JPAAssociationAttribute getAssociation(final String internalName) throws ODataJPAModelException;

  /**
   * Searches for an AssociationPath defined by the name used in the OData metadata in all the navigation properties
   * that are available for this type via the OData service. That is:
   * <ul>
   * <li> All not ignored navigation properties of this type.
   * <li> All not ignored navigation properties from super types.
   * <li> All not ignored navigation properties from embedded types.
   * </ul>
   * @param externalName
   * @return
   * @throws ODataJPAModelException
   */
  JPAAssociationPath getAssociationPath(final String externalName) throws ODataJPAModelException;

  /**
   * Searches in the navigation properties that are available for this type via the OData service. That is:
   * <ul>
   * <li> All not ignored navigation properties of this type.
   * <li> All not ignored navigation properties from super types.
   * <li> All not ignored navigation properties from embedded types.
   * </ul>
   * @return null if no navigation property found.
   * @throws ODataJPAModelException
   */
  List<JPAAssociationPath> getAssociationPathList() throws ODataJPAModelException;

  JPAAttribute getAttribute(final String internalName) throws ODataJPAModelException;

  JPAAttribute getAttribute(final UriResourceProperty uriResourceItem) throws ODataJPAModelException;

  List<JPAAttribute> getAttributes() throws ODataJPAModelException;

  /**
   * List of the path to all collection properties of this type. That is:
   * <ul>
   * <li> All not ignored collection properties of this type.
   * <li> All not ignored collection properties from super types.
   * <li> All not ignored collection properties from embedded types.
   * </ul>
   * @return
   * @throws ODataJPAModelException
   */
  List<JPAPath> getCollectionAttributesPath() throws ODataJPAModelException;

  /**
   * In case the type is within the given association path, the sub-path is returned.
   * E.g. structured type is AdministrativeInformation and associationPath = AdministrativeInformation/Created/User
   * Created/User is returned.
   * @param associationPath
   * @return
   * @throws ODataJPAModelException
   */
  JPAAssociationPath getDeclaredAssociation(JPAAssociationPath associationPath) throws ODataJPAModelException;

  JPAAssociationPath getDeclaredAssociation(final String externalName) throws ODataJPAModelException;

  /**
   * List of all associations that are declared at this type. That is:
   * <ul>
   * <li> All navigation properties of this type.
   * <li> All navigation properties from super types.
   * </ul>
   * @return
   * @throws ODataJPAModelException
   */
  List<JPAAssociationAttribute> getDeclaredAssociations() throws ODataJPAModelException;

  /**
   * List of all associations that are declared at this type. That is:
   * <ul>
   * <li> All properties of this type.
   * <li> All properties from super types.
   * </ul>
   * @return
   * @throws ODataJPAModelException
   */
  List<JPAAttribute> getDeclaredAttributes() throws ODataJPAModelException;

  /**
   * List of all associations that are declared at this type. That is:
   * <ul>
   * <li> All collection properties of this type.
   * <li> All collection properties from super types.
   * </ul>
   * @return
   * @throws ODataJPAModelException
   */
  List<JPACollectionAttribute> getDeclaredCollectionAttributes() throws ODataJPAModelException;

  /**
   * List of all associations that are declared at this type. That is:
   * <ul>
   * <li> All not ignored collection properties of this type.
   * <li> All not ignored collection properties from super types.
   * </ul>
   * @return
   * @throws ODataJPAModelException
   */
  JPAPath getPath(final String externalName) throws ODataJPAModelException;

  /**
   * List of all attributes that are available for this type via the OData service. That is:
   * <ul>
   * <li> All not ignored properties of the type.
   * <li> All not ignored properties from super types.
   * <li> All not ignored properties from embedded types.
   * </ul>
   * @return List of all attributes that are available via the OData service.
   * @throws ODataJPAModelException
   */
  List<JPAPath> getPathList() throws ODataJPAModelException;

  /**
   * List of all protected Attributes including protection/claim information. That is:
   * <ul>
   * <li> All not ignored protected properties of the type.
   * <li> All not ignored protected properties from super types.
   * <li> All not ignored protected properties from embedded types.
   * </ul>
   * @return
   * @throws ODataJPAModelException
   */
  List<JPAProtectionInfo> getProtections() throws ODataJPAModelException;

  Class<?> getTypeClass();

  boolean isAbstract();
}