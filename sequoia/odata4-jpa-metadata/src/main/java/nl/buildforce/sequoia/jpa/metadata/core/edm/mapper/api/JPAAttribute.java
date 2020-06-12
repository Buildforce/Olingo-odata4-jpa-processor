package nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api;

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAModelException;
import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.provider.CsdlAbstractEdmItem;

import jakarta.persistence.AttributeConverter;
import java.util.List;
import java.util.Set;

public interface JPAAttribute extends JPAElement {
  <X, Y> AttributeConverter<X, Y> getConverter();

  EdmPrimitiveTypeKind getEdmType() throws ODataJPAModelException;

  CsdlAbstractEdmItem getProperty() throws ODataJPAModelException;

  JPAStructuredType getStructuredType() throws ODataJPAModelException;

  /**
   * Returns a list of names of the claims that shall be matched with this property
   * @return
   */
  Set<String> getProtectionClaimNames();

  /**
   * Provides a List of path to the protected attributed
   * @return
   */
  List<String> getProtectionPath(String claimName);

  Class<?> getType();

  boolean isAssociation();

  /**
   * True is a to n association is involved
   * @return
   */
  boolean isCollection();

  boolean isComplex();

  boolean isEnum();

  boolean isEtag();

  boolean isKey();

  boolean isSearchable();

  boolean hasProtection();

}