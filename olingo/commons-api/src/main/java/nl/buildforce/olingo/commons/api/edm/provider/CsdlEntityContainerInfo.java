/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.provider;

import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;

/**
 * The type Csdl entity container info.
 */
public class CsdlEntityContainerInfo {

  private FullQualifiedName containerName;

  private FullQualifiedName extendsContainer;

  /**
   * Gets container name.
   *
   * @return the container name
   */
  public FullQualifiedName getContainerName() {
    return containerName;
  }

  /**
   * Sets container name.
   *
   * @param containerName the container name
   * @return the container name
   */
  public CsdlEntityContainerInfo setContainerName(FullQualifiedName containerName) {
    this.containerName = containerName;
    return this;
  }

  /**
   * Gets extends container.
   *
   * @return the extends container
   */
  public FullQualifiedName getExtendsContainer() {
    return extendsContainer;
  }

  /**
   * Sets extends container.
   *
   * @param extendsContainer the extends container
   * @return the extends container
   */
  public CsdlEntityContainerInfo setExtendsContainer(FullQualifiedName extendsContainer) {
    this.extendsContainer = extendsContainer;
    return this;
  }

}
