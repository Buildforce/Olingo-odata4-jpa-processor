/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.provider;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Csdl operation.
 */
public abstract class CsdlOperation extends CsdlAbstractEdmItem implements CsdlNamed, CsdlAnnotatable {

  /**
   * The Name.
   */
  protected String name;

  /**
   * The Is bound.
   */
  protected boolean isBound;

  /**
   * The Entity set path.
   */
  protected String entitySetPath;

  /**
   * The Parameters.
   */
  protected List<CsdlParameter> parameters = new ArrayList<>();

  /**
   * The Return type.
   */
  protected CsdlReturnType returnType;

  /**
   * The Annotations.
   */
  protected List<CsdlAnnotation> annotations = new ArrayList<>();

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
  public CsdlOperation setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Is bound.
   *
   * @return the boolean
   */
  public boolean isBound() {
    return isBound;
  }

  /**
   * Sets as bound operation.
   *
   * @param isBound the is bound
   * @return the bound
   */
  public CsdlOperation setBound(boolean isBound) {
    this.isBound = isBound;
    return this;
  }

  /**
   * Gets entity set path.
   *
   * @return the entity set path
   */
  public String getEntitySetPath() {
    return entitySetPath;
  }

  /**
   * Sets entity set path.
   *
   * @param entitySetPath the entity set path
   * @return the entity set path
   */
  public CsdlOperation setEntitySetPath(String entitySetPath) {
    this.entitySetPath = entitySetPath;
    return this;
  }

  /**
   * Gets parameters.
   *
   * @return the parameters
   */
  public List<CsdlParameter> getParameters() {
    return parameters;
  }

  /**
   * Gets parameter.
   *
   * @param name the name
   * @return the parameter
   */
  public CsdlParameter getParameter(String name) {
    return getOneByName(name, getParameters());
  }

  /**
   * Sets parameters.
   *
   * @param parameters the parameters
   * @return the parameters
   */
  public CsdlOperation setParameters(List<CsdlParameter> parameters) {
    this.parameters = parameters;
    return this;
  }

  /**
   * Gets return type.
   *
   * @return the return type
   */
  public CsdlReturnType getReturnType() {
    return returnType;
  }

  /**
   * Sets return type.
   *
   * @param returnType the return type
   * @return the return type
   */
  public CsdlOperation setReturnType(CsdlReturnType returnType) {
    this.returnType = returnType;
    return this;
  }

  @Override
  public List<CsdlAnnotation> getAnnotations() {
    return annotations;
  }
  
  /**
   * Sets a list of annotations
   * @param annotations list of annotations
   * @return this instance
   */
  public CsdlOperation setAnnotations(List<CsdlAnnotation> annotations) {
    this.annotations = annotations;
    return this;
  }

}