/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.provider;

import java.util.List;

/**
 * The type Csdl function.
 */
public class CsdlFunction extends CsdlOperation {

  private boolean isComposable;

  /**
   * Is composable.
   *
   * @return the boolean
   */
  public boolean isComposable() {
    return isComposable;
  }

  /**
   * Sets composable.
   *
   * @param isComposable the is composable
   * @return the composable
   */
  public CsdlFunction setComposable(boolean isComposable) {
    this.isComposable = isComposable;
    return this;
  }

  @Override
  public CsdlFunction setName(String name) {
    this.name = name;
    return this;
  }

  @Override
  public CsdlFunction setBound(boolean isBound) {
    this.isBound = isBound;
    return this;
  }

  @Override
  public CsdlFunction setEntitySetPath(String entitySetPath) {
    this.entitySetPath = entitySetPath;
    return this;
  }

  @Override
  public CsdlFunction setParameters(List<CsdlParameter> parameters) {
    this.parameters = parameters;
    return this;
  }

  @Override
  public CsdlFunction setReturnType(CsdlReturnType returnType) {
    this.returnType = returnType;
    return this;
  }

  @Override
  public CsdlFunction setAnnotations(List<CsdlAnnotation> annotations) {
    this.annotations = annotations;
    return this;
  }

}