package nl.buildforce.sequoia.processor.core.util;

import nl.buildforce.olingo.commons.api.edm.EdmAnnotation;
import nl.buildforce.olingo.commons.api.edm.EdmEntityType;
import nl.buildforce.olingo.commons.api.edm.EdmNavigationProperty;
import nl.buildforce.olingo.commons.api.edm.EdmOnDelete;
import nl.buildforce.olingo.commons.api.edm.EdmReferentialConstraint;
import nl.buildforce.olingo.commons.api.edm.EdmTerm;

import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

public class EdmNavigationPropertyDouble implements EdmNavigationProperty {
  private final String name;

  public EdmNavigationPropertyDouble(final String name) {
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean isCollection() {
    fail();
    return false;
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
  public EdmEntityType getType() {
    fail();
    return null;
  }

  @Override
  public boolean isNullable() {
    fail();
    return false;
  }

  @Override
  public boolean containsTarget() {
    fail();
    return false;
  }

  @Override
  public EdmNavigationProperty getPartner() {
    fail();
    return null;
  }

  @Override
  public String getReferencingPropertyName(final String referencedPropertyName) {
    fail();
    return null;
  }

  @Override
  public List<EdmReferentialConstraint> getReferentialConstraints() {
    fail();
    return null;
  }

  @Override
  public EdmOnDelete getOnDelete() {
    fail();
    return null;
  }

}