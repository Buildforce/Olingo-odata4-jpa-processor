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
package nl.buildforce.olingo.server.api.uri.queryoption.expression;

import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.server.api.uri.UriInfoResource;

/**
 * Represents a member expression node in the expression tree. This expression is used to describe access paths
 * to properties and other EDM elements.
 */
public interface Member extends Expression {

  /**
   * @return UriInfoResource object describing the whole path used to access an data value
   * (this includes for example the usage of $root and $it inside the URI)
   */
  UriInfoResource getResourcePath();

  /**
   * @return Type
   */
  EdmType getType();

  /**
   * @return The used type filter ahead of the path
   */
  EdmType getStartTypeFilter();

  /**
   * @return true if the accessed data is a collection, otherwise false
   */
  boolean isCollection();

}