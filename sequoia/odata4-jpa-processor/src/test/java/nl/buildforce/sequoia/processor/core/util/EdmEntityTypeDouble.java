package nl.buildforce.sequoia.processor.core.util;

import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAEdmNameBuilder;
import nl.buildforce.olingo.commons.api.edm.EdmAnnotation;
import nl.buildforce.olingo.commons.api.edm.EdmElement;
import nl.buildforce.olingo.commons.api.edm.EdmEntityType;
import nl.buildforce.olingo.commons.api.edm.EdmKeyPropertyRef;
import nl.buildforce.olingo.commons.api.edm.EdmNavigationProperty;
import nl.buildforce.olingo.commons.api.edm.EdmProperty;
import nl.buildforce.olingo.commons.api.edm.EdmTerm;
import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;
import nl.buildforce.olingo.commons.api.edm.constants.EdmTypeKind;

import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

public class EdmEntityTypeDouble implements EdmEntityType {

  private final String name;
  private final JPAEdmNameBuilder nameBuilder;

  public EdmEntityTypeDouble(final JPAEdmNameBuilder nameBuilder, final String name) {
    this.name = name;
    this.nameBuilder = nameBuilder;
  }

  @Override
  public EdmElement getProperty(final String name) {
    fail();
    return null;
  }

  @Override
  public List<String> getPropertyNames() {
    fail();
    return null;
  }

  @Override
  public EdmProperty getStructuralProperty(final String name) {
    fail();
    return null;
  }

  @Override
  public EdmNavigationProperty getNavigationProperty(final String name) {
    fail();
    return null;
  }

  @Override
  public List<String> getNavigationPropertyNames() {
    fail();
    return null;
  }

  @Override
  public boolean compatibleTo(final EdmType targetType) {
    fail();
    return false;
  }

  @Override
  public boolean isOpenType() {
    fail();
    return false;
  }

  @Override
  public boolean isAbstract() {
    fail();
    return false;
  }

  @Override
  public FullQualifiedName getFullQualifiedName() {
    return new FullQualifiedName(nameBuilder.getNamespace(), name);
  }

  @Override
  public String getNamespace() {
    return nameBuilder.getNamespace();
  }

  @Override
  public EdmTypeKind getKind() {
    fail();
    return null;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public EdmAnnotation getAnnotation(final EdmTerm term, final String qualifier) {
    fail();
    return null;
  }

  @Override
  public List<EdmAnnotation> getAnnotations() {
    fail();
    return null;
  }

  @Override
  public List<String> getKeyPredicateNames() {
    fail();
    return null;
  }

  @Override
  public List<EdmKeyPropertyRef> getKeyPropertyRefs() {
    fail();
    return null;
  }

  @Override
  public EdmKeyPropertyRef getKeyPropertyRef(final String keyPredicateName) {
    fail();
    return null;
  }

  @Override
  public boolean hasStream() {
    fail();
    return false;
  }

  @Override
  public EdmEntityType getBaseType() {
    fail();
    return null;
  }

}