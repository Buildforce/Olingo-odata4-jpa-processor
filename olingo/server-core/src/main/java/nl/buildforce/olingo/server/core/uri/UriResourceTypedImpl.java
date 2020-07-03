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
package nl.buildforce.olingo.server.core.uri;

import nl.buildforce.olingo.commons.api.edm.EdmStructuredType;
import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.server.api.uri.UriResourcePartTyped;
import nl.buildforce.olingo.server.api.uri.UriResourceKind;

public abstract class UriResourceTypedImpl extends UriResourceImpl implements UriResourcePartTyped {

  private EdmType typeFilter = null;

  public UriResourceTypedImpl(UriResourceKind kind) {
    super(kind);
  }

  public EdmType getTypeFilter() {
    return typeFilter;
  }

  public UriResourceTypedImpl setTypeFilter(EdmStructuredType typeFilter) {
    this.typeFilter = typeFilter;
    return this;
  }

  @Override
  public String getSegmentValue(boolean includeFilters) {
    return includeFilters && typeFilter != null ?
        getSegmentValue() + "/" + typeFilter.getFullQualifiedName().getFullQualifiedNameAsString() :
        getSegmentValue();
  }

  @Override
  public String toString(boolean includeFilters) {
    return getSegmentValue(includeFilters);
  }
}
