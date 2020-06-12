/**
 *

 */
package nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.impl;

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAEdmNameBuilder;

import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.EmbeddableType;
import jakarta.persistence.metamodel.EntityType;
import java.util.stream.IntStream;

/**
 * @author Oliver Grande
 * Created: 19.09.2019
 *
 */
public class CustomJPANameBuilder implements JPAEdmNameBuilder {

  @Override
  public String buildComplexTypeName(EmbeddableType<?> jpaEmbeddedType) {
    return "T_" + jpaEmbeddedType.getJavaType().getSimpleName();
  }

  @Override
  public String buildContainerName() {
    return "service_container";
  }

  @Override
  public String buildEntitySetName(String entityTypeName) {
    return entityTypeName.toUpperCase();
  }

  @Override
  public String buildEntityTypeName(EntityType<?> jpaEntityType) {
    final StringBuilder externalName = new StringBuilder();
    final IntStream name = jpaEntityType.getName().chars();
    name.forEach(i -> this.appendChar(externalName, i));
    externalName.deleteCharAt(0);
    return externalName.toString();
  }

  @Override
  public String buildEnumerationTypeName(Class<? extends Enum<?>> javaEnum) {
    return "E_" + javaEnum.getSimpleName();
  }

  @Override
  public String buildNaviPropertyName(Attribute<?, ?> jpaAttribute) {
    return jpaAttribute.getName();
  }

  @Override
  public String buildOperationName(String internalOperationName) {
    return "O_" + internalOperationName;
  }

  @Override
  public String buildPropertyName(String jpaAttributeName) {
    return jpaAttributeName;
  }

  @Override
  public String getNamespace() { return "test";
  }

  private void appendChar(final StringBuilder builder, int i) {
    if (Character.isUpperCase(i))
      builder.append('_').append(Character.toChars(i)[0]);
    else
      builder.append(Character.toChars(i)[0]);
  }

}