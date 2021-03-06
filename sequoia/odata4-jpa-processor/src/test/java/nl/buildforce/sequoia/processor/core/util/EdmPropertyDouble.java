package nl.buildforce.sequoia.processor.core.util;

import nl.buildforce.olingo.commons.api.edm.EdmAnnotation;
import nl.buildforce.olingo.commons.api.edm.EdmMapping;
import nl.buildforce.olingo.commons.api.edm.EdmProperty;
import nl.buildforce.olingo.commons.api.edm.EdmTerm;
import nl.buildforce.olingo.commons.api.edm.EdmType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

public class EdmPropertyDouble implements EdmProperty {
  private final String name;

  public EdmPropertyDouble(final String name) {
    this.name = name;
  }

  @Override
  public String getName() {

    return name;
  }

  @Override
  public EdmType getType() {
    fail();
    return null;
  }

  @Override
  public boolean isCollection() {
    fail();
    return false;
  }

  @Override
  public EdmMapping getMapping() {
    fail();
    return null;
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
  public String getMimeType() {
    fail();
    return null;
  }

  @Override
  public boolean isPrimitive() {
    fail();
    return false;
  }

  @Override
  public boolean isNullable() {
    fail();
    return false;
  }

  @Override
  public Integer getMaxLength() {
    fail();
    return null;
  }

  @Override
  public Integer getPrecision() {
    fail();
    return null;
  }

  @Override
  public Integer getScale() {
    fail();
    return null;
  }

  /*@Override
  public SRID getSrid() {
    fail();
    return null;
  }*/

  @Override
  public boolean isUnicode() {
    fail();
    return false;
  }

  @Override
  public String getDefaultValue() {
    fail();
    return null;
  }

  /*@Override
  public EdmType getTypeWithAnnotations() {
    fail();
    return null;
  }*/

}