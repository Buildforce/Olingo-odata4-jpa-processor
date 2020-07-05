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
package nl.buildforce.olingo.server.core.uri.queryoption.apply;

import java.util.Collections;
import java.util.List;

import nl.buildforce.olingo.commons.api.edm.EdmFunction;
import nl.buildforce.olingo.server.api.uri.queryoption.apply.CustomFunction;
import nl.buildforce.olingo.server.api.uri.UriParameter;

/**
 * Represents a transformation with a custom function.
 */
public class CustomFunctionImpl implements CustomFunction {

  private EdmFunction function;
  private List<UriParameter> parameters;

  @Override
  public Kind getKind() {
    return Kind.CUSTOM_FUNCTION;
  }

  @Override
  public EdmFunction getFunction() {
    return function;
  }

  public CustomFunctionImpl setFunction(EdmFunction function) {
    this.function = function;
    return this;
  }

  @Override
  public List<UriParameter> getParameters() {
    return parameters == null ?
        Collections.emptyList() :
        Collections.unmodifiableList(parameters);
  }

  public CustomFunctionImpl setParameters(List<UriParameter> parameters) {
    this.parameters = parameters;
    return this;
  }
}
