/**
 *
 */
package nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api;

import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;

import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.EmbeddableType;
import jakarta.persistence.metamodel.EntityType;

/**
 * A name builder creates, based on information from the JPA entity model names, the names of the corresponding element
 * of the OData entity data model (EDM)
 * @author Oliver Grande
 * Created: 15.09.2019
 *
 */
public interface JPAEdmNameBuilder {

  /**
   *

   * @param jpaEmbeddedType
   * @return
   */
  String buildComplexTypeName(final EmbeddableType<?> jpaEmbeddedType);

  /**
   * Container names are <a
   * href="http://docs.oasis-open.org/odata/odata/v4.0/errata02/os/complete/part3-csdl/odata-v4.0-errata02-os-part3-csdl-complete.html#_SimpleIdentifier">
   * Simple Identifier</a>,
   * so can contain only letters, digits and underscores.
   * @return non empty unique name of an Entity Set
   */
String buildContainerName();

  default String buildEntitySetName(final CsdlEntityType entityType) {
    return buildEntitySetName(entityType.getName());
  }

  /**
   * Create a name of an <a
   * href="http://docs.oasis-open.org/odata/odata/v4.0/errata02/os/complete/part3-csdl/odata-v4.0-errata02-os-part3-csdl-complete.html#_12.2_The_edm:EntitySet">
   * Entity Set</a> derived from the name of the corresponding entity type.
   * @param entityTypeName
   * @return non empty unique name of an Entity Set
   */
String buildEntitySetName(final String entityTypeName);

  /**
   * Creates the name of an <a
   * href="http://docs.oasis-open.org/odata/odata/v4.0/errata02/os/complete/part3-csdl/odata-v4.0-errata02-os-part3-csdl-complete.html#_Toc406397976">Entity
   * Type</a> derived from JPA Entity Type.
   * @param jpaEntityType
   * @return non empty unique name of an Entity Type
   */
String buildEntityTypeName(final EntityType<?> jpaEntityType);

  /**
   * Converts the internal java class name of an enumeration into the external entity data model <a
   * href="http://docs.oasis-open.org/odata/odata/v4.0/errata02/os/complete/part3-csdl/odata-v4.0-errata02-os-part3-csdl-complete.html#_Toc406397991">
   * Enumeration Type</a> name.
   * @param javaEnum
   * @return non empty unique name of an Enumeration
   */
String buildEnumerationTypeName(final Class<? extends Enum<?>> javaEnum);

  /**
   * Converts the name of an JPA association attribute into the name of an EDM navigation property
   * @param jpaAttribute
   * @return non empty unique name of a Navigation Property
   */
String buildNaviPropertyName(final Attribute<?, ?> jpaAttribute);

  /**
   * Convert the internal name of a java based operation into the external entity data model name.
   * @param internalOperationName
   * @return non empty unique name of an Operation (Function or Action)
   */
String buildOperationName(final String internalOperationName);

  /**
   * Converts the name of an JPA attribute into the name of an EDM property
   * @param jpaAttributeName
   * @return non empty unique name of a property
   */
String buildPropertyName(final String jpaAttributeName);

  /**
   * @return name space to a schema
   */
String getNamespace();

}