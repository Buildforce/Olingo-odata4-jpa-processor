/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.provider;

import java.util.List;

import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;

/**
 * The type Csdl function import.
 */
public class CsdlFunctionImport extends CsdlOperationImport {

  private FullQualifiedName function;

  // Default include in service document is false for function imports
  private boolean includeInServiceDocument;

  /**
   * Humanreadable title
   */
  private String title;

  @Override
  public String getName() {
    return name;
  }

  @Override
  public CsdlFunctionImport setName(String name) {
    this.name = name;
    return this;
  }

  @Override
  public CsdlFunctionImport setEntitySet(String entitySet) {
    this.entitySet = entitySet;
    return this;
  }

  /**
   * Gets function.
   *
   * @return the function
   */
  public String getFunction() {
    return function.getFullQualifiedNameAsString();
  }

  /**
   * Gets function fQN.
   *
   * @return the function fQN
   */
  public FullQualifiedName getFunctionFQN() {
    return function;
  }

  /**
   * Sets function.
   *
   * @param function the function
   * @return the function
   */
  public CsdlFunctionImport setFunction(FullQualifiedName function) {
    this.function = function;
    return this;
  }

  /**
   * Sets function.
   *
   * @param function the function
   * @return the function
   */
  public CsdlFunctionImport setFunction(String function) {
    this.function = new FullQualifiedName(function);
    return this;
  }

  /**
   * Is include in service document.
   *
   * @return the boolean
   */
  public boolean isIncludeInServiceDocument() {
    return includeInServiceDocument;
  }

  /**
   * Sets include in service document.
   *
   * @param includeInServiceDocument the include in service document
   * @return the include in service document
   */
  public CsdlFunctionImport setIncludeInServiceDocument(boolean includeInServiceDocument) {
    this.includeInServiceDocument = includeInServiceDocument;
    return this;
  }

/*
  @Override
  public CsdlFunctionImport setAnnotations(List<CsdlAnnotation> annotations) {
    this.annotations = annotations;
    return this;
  }
*/

  public String getTitle() {
    return title;
  }

  /**
   * A human readable title for this instance
   * @param title
   * @return this instance
   */
  public CsdlFunctionImport setTitle(String title) {
    this.title = title;
    return this;
  }

}