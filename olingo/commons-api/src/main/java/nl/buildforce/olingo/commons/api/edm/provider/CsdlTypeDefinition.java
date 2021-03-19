/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.provider;

import java.util.ArrayList;
import java.util.List;

import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;

/**
 * The type Csdl type definition.
 */
public class CsdlTypeDefinition extends CsdlAbstractEdmItem implements CsdlNamed, CsdlAnnotatable {

  private String name;

  private FullQualifiedName underlyingType;

  // Facets
  private Integer maxLength;

  private Integer precision;

  private Integer scale;

  private boolean unicode = true;

  // private SRID srid;

  private List<CsdlAnnotation> annotations = new ArrayList<>();

  @Override
  public String getName() {
    return name;
  }

  /**
   * Sets name.
   *
   * @param name the name
   * @return the name
   */
  public CsdlTypeDefinition setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Gets underlying type.
   *
   * @return the underlying type
   */
  public String getUnderlyingType() {
    if (underlyingType != null) {
      return underlyingType.getFullQualifiedNameAsString();
    }
    return null;
  }

  /**
   * Sets underlying type.
   *
   * @param underlyingType the underlying type
   * @return the underlying type
   */
  public CsdlTypeDefinition setUnderlyingType(String underlyingType) {
    this.underlyingType = new FullQualifiedName(underlyingType);
    return this;
  }

  /**
   * Sets underlying type.
   *
   * @param underlyingType the underlying type
   * @return the underlying type
   */
  public CsdlTypeDefinition setUnderlyingType(FullQualifiedName underlyingType) {
    this.underlyingType = underlyingType;
    return this;
  }

  /**
   * Gets max length.
   *
   * @return the max length
   */
  public Integer getMaxLength() {
    return maxLength;
  }

  /**
   * Sets max length.
   *
   * @param maxLength the max length
   * @return the max length
   */
  public CsdlTypeDefinition setMaxLength(Integer maxLength) {
    this.maxLength = maxLength;
    return this;
  }

  /**
   * Gets precision.
   *
   * @return the precision
   */
  public Integer getPrecision() {
    return precision;
  }

  /**
   * Sets precision.
   *
   * @param precision the precision
   * @return the precision
   */
  public CsdlTypeDefinition setPrecision(Integer precision) {
    this.precision = precision;
    return this;
  }

  /**
   * Gets scale.
   *
   * @return the scale
   */
  public Integer getScale() {
    return scale;
  }

  /**
   * Sets scale.
   *
   * @param scale the scale
   * @return the scale
   */
  public CsdlTypeDefinition setScale(Integer scale) {
    this.scale = scale;
    return this;
  }

  /**
   * Is unicode.
   *
   * @return the boolean
   */
  public boolean isUnicode() {
    return unicode;
  }

  /**
   * Sets unicode.
   *
   * @param unicode the unicode
   * @return the unicode
   */
  public CsdlTypeDefinition setUnicode(boolean unicode) {
    this.unicode = unicode;
    return this;
  }

  /*
    Gets srid.

    @return the srid
   */
  /*public SRID getSrid() {
    return srid;
  }*/

  /*
   * Sets srid.
   *
   * @param srid the srid
   * @return the srid
   */
  /*public CsdlTypeDefinition setSrid(final SRID srid) {
    this.srid = srid;
    return this;
  }*/

  @Override
  public List<CsdlAnnotation> getAnnotations() {
    return annotations;
  }
  
  /**
   * Sets a list of annotations
   * @param annotations list of annotations
   * @return this instance
   */
/*
  public CsdlTypeDefinition setAnnotations(List<CsdlAnnotation> annotations) {
    this.annotations = annotations;
    return this;
  }
*/

}