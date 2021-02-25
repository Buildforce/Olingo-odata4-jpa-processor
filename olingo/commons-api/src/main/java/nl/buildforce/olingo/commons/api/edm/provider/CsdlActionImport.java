/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.provider;

import java.util.List;

import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;

/**
 * Represents an action import CSDL item
 */
public class CsdlActionImport extends CsdlOperationImport {

  private FullQualifiedName action;

  @Override
  public CsdlActionImport setName(String name) {
    this.name = name;
    return this;
  }

  @Override
  public CsdlActionImport setEntitySet(String entitySet) {
    this.entitySet = entitySet;
    return this;
  }

  /**
   * Returns the full qualified name of the action as string
   * @return full qualified name
   */
  public String getAction() {
    return action.getFullQualifiedNameAsString();
  }

  /**
   * Returns the full qualified name of the action
   * @return full qualified name
   */
  public FullQualifiedName getActionFQN() {
    return action;
  }

  /**
   * Sets the full qualified name of the action as string
   * @param action full qualified name
   * @return this instance
   */
  public CsdlActionImport setAction(String action) {
    this.action = new FullQualifiedName(action);
    return this;
  }

  /**
   * Sets the full qualified name of the action
   * @param action full qualified name
   * @return this instance
   */
  public CsdlActionImport setAction(FullQualifiedName action) {
    this.action = action;
    return this;
  }

  @Override
  public CsdlActionImport setAnnotations(List<CsdlAnnotation> annotations) {
    this.annotations = annotations;
    return this;
  }

}