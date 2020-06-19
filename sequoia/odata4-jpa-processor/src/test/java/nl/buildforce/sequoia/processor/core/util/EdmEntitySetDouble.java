package nl.buildforce.sequoia.processor.core.util;

import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAEdmNameBuilder;
import nl.buildforce.olingo.commons.api.edm.EdmAnnotation;
import nl.buildforce.olingo.commons.api.edm.EdmBindingTarget;
import nl.buildforce.olingo.commons.api.edm.EdmEntityContainer;
import nl.buildforce.olingo.commons.api.edm.EdmEntitySet;
import nl.buildforce.olingo.commons.api.edm.EdmEntityType;
import nl.buildforce.olingo.commons.api.edm.EdmMapping;
import nl.buildforce.olingo.commons.api.edm.EdmNavigationPropertyBinding;
import nl.buildforce.olingo.commons.api.edm.EdmTerm;

import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

public class EdmEntitySetDouble implements EdmEntitySet {
  private final String name;
  private final EdmEntityType type;
  // private final JPAEdmNameBuilder nameBuilder;

  public EdmEntitySetDouble(final JPAEdmNameBuilder nameBuilder, final String name) {
    this.name = name;
    this.type = new EdmEntityTypeDouble(nameBuilder, name.substring(0, name.length() - 1));
    // this.nameBuilder = nameBuilder;
  }

  @Override
  public String getTitle() {
    return null;
  }

  @Override
  public EdmBindingTarget getRelatedBindingTarget(final String path) {
    return null;
  }

  @Override
  public List<EdmNavigationPropertyBinding> getNavigationPropertyBindings() {
    return null;
  }

  @Override
  public EdmEntityContainer getEntityContainer() {
    return null;
  }

  @Override
  public EdmEntityType getEntityType() {
    return type;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public EdmAnnotation getAnnotation(final EdmTerm term, final String qualifier) {
    return null;
  }

  @Override
  public List<EdmAnnotation> getAnnotations() {
    return null;
  }

  @Override
  public boolean isIncludeInServiceDocument() {
    return false;
  }

  @Override
  public EdmMapping getMapping() {
    fail();
    return null;
  }

  @Override
  public EdmEntityType getEntityTypeWithAnnotations() {
    fail();
    return null;
  }
}