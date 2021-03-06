/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.provider;

import java.util.ArrayList;
import java.util.List;

import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;

/**
 * The type Csdl parameter.
 */
public class CsdlParameter extends CsdlAbstractEdmItem implements CsdlNamed, CsdlAnnotatable {

  private String name;

  private String type;

  private boolean isCollection;

  private CsdlMapping mapping;

  // Facets
  private boolean nullable = true;

  private Integer maxLength;

  private Integer precision;

  private Integer scale;

// private SRID srid;

  private final List<CsdlAnnotation> annotations = new ArrayList<>();

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
  public CsdlParameter setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Gets type.
   *
   * @return the type
   */
  public String getType() {
    return type;
  }

  /**
   * Gets type fQN.
   *
   * @return the type fQN
   */
  public FullQualifiedName getTypeFQN() {
    return new FullQualifiedName(type);
  }

  /**
   * Sets type.
   *
   * @param type the type
   * @return the type
   */
  public CsdlParameter setType(String type) {
    this.type = type;
    return this;
  }

  /**
   * Sets type.
   *
   * @param type the type
   * @return the type
   */
  public CsdlParameter setType(FullQualifiedName type) {
    this.type = type.getFullQualifiedNameAsString();
    return this;
  }

  /**
   * Is collection.
   *
   * @return the boolean
   */
  public boolean isCollection() {
    return isCollection;
  }

  /**
   * Sets collection.
   *
   * @param isCollection the is collection
   * @return the collection
   */
  public CsdlParameter setCollection(boolean isCollection) {
    this.isCollection = isCollection;
    return this;
  }

  /**
   * Is nullable.
   *
   * @return the boolean
   */
  public boolean isNullable() {
    return nullable;
  }

  /**
   * Sets nullable.
   *
   * @param nullable the nullable
   * @return the nullable
   */
  public CsdlParameter setNullable(boolean nullable) {
    this.nullable = nullable;
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
  public CsdlParameter setMaxLength(Integer maxLength) {
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
  public CsdlParameter setPrecision(Integer precision) {
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
  public CsdlParameter setScale(Integer scale) {
    this.scale = scale;
    return this;
  }

  /*
   * Gets srid.
   *
   * @return the srid
   *
  public SRID getSrid() {
    return srid;
  }

  /**
   * Sets srid.
   *
   * @param srid the srid
   * @return the srid
   *
  public CsdlParameter setSrid(final SRID srid) {
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
  public CsdlParameter setAnnotations(List<CsdlAnnotation> annotations) {
    this.annotations = annotations;
    return this;
  }
*/

  /**
   * Gets mapping.
   *
   * @return the mapping
   */
  public CsdlMapping getMapping() {
    return mapping;
  }

  /**
   * Sets mapping.
   *
   * @param mapping the mapping
   * @return the mapping
   */
  public CsdlParameter setMapping(CsdlMapping mapping) {
    this.mapping = mapping;
    return this;
  }

}