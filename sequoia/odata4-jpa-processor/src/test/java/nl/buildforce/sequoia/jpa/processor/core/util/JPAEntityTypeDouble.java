package nl.buildforce.sequoia.jpa.processor.core.util;

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAAssociationAttribute;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAAssociationPath;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAAttribute;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPACollectionAttribute;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAEntityType;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAPath;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAProtectionInfo;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAModelException;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.server.api.uri.UriResourceProperty;

import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

public class JPAEntityTypeDouble implements JPAEntityType {
  private final JPAEntityType base;

  public JPAEntityTypeDouble(JPAEntityType base) {
    this.base = base;
  }

  @Override
  public JPAAssociationAttribute getAssociation(String internalName) throws ODataJPAModelException {
    return base.getAssociation(internalName);
  }

  @Override
  public JPAAssociationPath getAssociationPath(String externalName) throws ODataJPAModelException {
    return base.getAssociationPath(externalName);
  }

  @Override
  public List<JPAAssociationPath> getAssociationPathList() {
    fail();
    return null;
  }

  @Override
  public JPAAttribute getAttribute(UriResourceProperty uriResourceItem) {
    fail();
    return null;
  }

  @Override
  public JPAAttribute getAttribute(String internalName) throws ODataJPAModelException {
    return base.getAttribute(internalName);
  }

  @Override
  public List<JPAAttribute> getAttributes() throws ODataJPAModelException {
    return base.getAttributes();
  }

  @Override
  public List<JPAPath> getCollectionAttributesPath() {
    fail();
    return null;
  }

  @Override
  public JPAAssociationPath getDeclaredAssociation(JPAAssociationPath associationPath) {
    fail();
    return null;
  }

  @Override
  public JPAAssociationPath getDeclaredAssociation(String externalName) {
    fail();
    return null;
  }

  @Override
  public List<JPAAssociationAttribute> getDeclaredAssociations() {
    fail();
    return null;
  }

  @Override
  public List<JPAAttribute> getDeclaredAttributes() {
    fail();
    return null;
  }

  @Override
  public List<JPACollectionAttribute> getDeclaredCollectionAttributes() {
    fail();
    return null;
  }

  @Override
  public JPAPath getPath(String externalName) throws ODataJPAModelException {
    return base.getPath(externalName);
  }

  @Override
  public List<JPAPath> getPathList() throws ODataJPAModelException {
    return base.getPathList();
  }

  @Override
  public Class<?> getTypeClass() {
    return base.getTypeClass();
  }

  @Override
  public boolean isAbstract() {
    fail();
    return false;
  }

  @Override
  public FullQualifiedName getExternalFQN() {
    return base.getExternalFQN();
  }

  @Override
  public String getExternalName() {
    return base.getExternalName();
  }

  // @Override
  public String getInternalName() {
    fail();
    return null;
    // return getInternalName();
  }

  @Override
  public String getContentType() {
    fail();
    return null;
  }

  @Override
  public JPAPath getContentTypeAttributePath() {
    fail();
    return null;
  }

  @Override
  public List<JPAAttribute> getKey() {
    fail();
    return null;
  }

  @Override
  public List<JPAPath> getKeyPath() {
    fail();
    return null;
  }

  @Override
  public Class<?> getKeyType() {
    return base.getKeyType();
  }

  @Override
  public List<JPAPath> getSearchablePath() throws ODataJPAModelException {
    return base.getSearchablePath();
  }

  @Override
  public JPAPath getStreamAttributePath() throws ODataJPAModelException {
    return base.getStreamAttributePath();
  }

  @Override
  public String getTableName() {
    return base.getTableName();
  }

  @Override
  public boolean hasEtag() throws ODataJPAModelException {
    return base.hasEtag();
  }

  @Override
  public boolean hasStream() throws ODataJPAModelException {
    return base.hasStream();
  }

  @Override
  public List<JPAPath> searchChildPath(JPAPath selectItemPath) {
    return base.searchChildPath(selectItemPath);
  }

  @Override
  public JPACollectionAttribute getCollectionAttribute(String externalName) throws ODataJPAModelException {
    return base.getCollectionAttribute(externalName);
  }

  @Override
  public List<JPAProtectionInfo> getProtections() throws ODataJPAModelException {
    return base.getProtections();
  }

  @Override
  public JPAPath getEtagPath() {
    fail();
    return null;
  }

  @Override
  public boolean hasCompoundKey() {
    fail();
    return false;
  }

}