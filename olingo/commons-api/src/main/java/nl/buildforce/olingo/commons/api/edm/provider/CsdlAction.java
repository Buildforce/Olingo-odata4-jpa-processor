/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.provider;

import java.util.List;

/**
 * Represents an action CSDL item
 */
public class CsdlAction extends CsdlOperation {

  @Override
  public CsdlAction setName(String name) {
    this.name = name;
    return this;
  }

  @Override
  public CsdlAction setBound(boolean isBound) {
    this.isBound = isBound;
    return this;
  }

  @Override
  public CsdlAction setEntitySetPath(String entitySetPath) {
    this.entitySetPath = entitySetPath;
    return this;
  }

  @Override
  public CsdlAction setParameters(List<CsdlParameter> parameters) {
    this.parameters = parameters;
    return this;
  }

  @Override
  public CsdlAction setReturnType(CsdlReturnType returnType) {
    this.returnType = returnType;
    return this;
  }

  @Override
  public CsdlAction setAnnotations(List<CsdlAnnotation> annotations) {
    this.annotations = annotations;
    return this;
  }

}