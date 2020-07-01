/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package nl.buildforce.olingo.commons.api.data;

import java.net.URI;
import java.util.Objects;

/**
 * Abstract OData object with basic values (<code>id</code>, <code>baseURI</code>, <code>title</code>).
 */
public abstract class AbstractODataObject extends Annotatable {

  private URI baseURI;
  private URI id;
  private String title;

  /**
   * Gets base URI.
   * @return base URI
   */
  public URI getBaseURI() {
    return baseURI;
  }

  /**
   * Sets base URI.
   * @param baseURI new base URI
   */
  public void setBaseURI(URI baseURI) {
    this.baseURI = baseURI;
  }

  /**
   * Gets ID.
   * @return ID.
   */
  public URI getId() {
    return id;
  }

  /**
   * Sets ID.
   * @param id new ID value
   */
  public void setId(URI id) {
    this.id = id;
  }

  /**
   * Gets title.
   * @return title
   */
  public String getTitle() {
    return title;
  }

  /**
   * Sets property with given key to given value.
   * @param key key of property
   * @param value new value for property
   */
  public void setCommonProperty(String key, String value) {
    if ("id".equals(key)) {
      id = URI.create(value);
    } else if ("title".equals(key)) {
      title = value;
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    AbstractODataObject other = (AbstractODataObject) o;
    return getAnnotations().equals(other.getAnnotations())
        && (Objects.equals(baseURI, other.baseURI))
        && (Objects.equals(id, other.id))
        && (Objects.equals(title, other.title));
  }

  @Override
  public int hashCode() {
    int result = getAnnotations().hashCode();
    result = 31 * result + (baseURI == null ? 0 : baseURI.hashCode());
    result = 31 * result + (id == null ? 0 : id.hashCode());
    result = 31 * result + (title == null ? 0 : title.hashCode());
    return result;
  }
}