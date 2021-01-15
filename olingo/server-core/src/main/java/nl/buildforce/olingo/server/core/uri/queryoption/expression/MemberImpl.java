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
package nl.buildforce.olingo.server.core.uri.queryoption.expression;

import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.server.api.ODataApplicationException;
import nl.buildforce.olingo.server.api.uri.UriResourcePartTyped;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.ExpressionVisitException;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.ExpressionVisitor;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.Member;
import nl.buildforce.olingo.server.core.uri.UriInfoImpl;
import nl.buildforce.olingo.server.core.uri.UriResourceActionImpl;
import nl.buildforce.olingo.server.core.uri.UriResourceTypedImpl;
import nl.buildforce.olingo.server.core.uri.UriResourceWithKeysImpl;
import nl.buildforce.olingo.server.api.uri.UriInfoResource;
import nl.buildforce.olingo.server.api.uri.UriResource;
import nl.buildforce.olingo.server.core.uri.UriResourceImpl;

public class MemberImpl implements Member {

  private final UriInfoResource path;
  private final EdmType startTypeFilter;

  public MemberImpl(UriInfoResource path, EdmType startTypeFilter) {
    this.path = path;
    this.startTypeFilter = startTypeFilter;
  }

  @Override
  public UriInfoResource getResourcePath() {
    return path;
  }

  @Override
  public EdmType getStartTypeFilter() {
    return startTypeFilter;
  }

  @Override
  public <T> T accept(ExpressionVisitor<T> visitor) throws ExpressionVisitException, ODataApplicationException {
    return visitor.visitMember(this);
  }

  @Override
  public EdmType getType() {
    UriInfoImpl uriInfo = (UriInfoImpl) path;
    UriResourceImpl lastResourcePart = (UriResourceImpl) uriInfo.getLastResourcePart();

    if (lastResourcePart instanceof UriResourceWithKeysImpl) {
      UriResourceWithKeysImpl lastKeyPred = (UriResourceWithKeysImpl) lastResourcePart;
      if (lastKeyPred.getTypeFilterOnEntry() != null) {
        return lastKeyPred.getTypeFilterOnEntry();
      } else if (lastKeyPred.getTypeFilterOnCollection() != null) {
        return lastKeyPred.getTypeFilterOnCollection();
      }
      return lastKeyPred.getType();
    } else if (lastResourcePart instanceof UriResourceTypedImpl) {
      UriResourceTypedImpl lastTyped = (UriResourceTypedImpl) lastResourcePart;
      EdmType type = lastTyped.getTypeFilter();
      if (type != null) {
        return type;
      }
      return lastTyped.getType();
    } else if (lastResourcePart instanceof UriResourceActionImpl) {
      return ((UriResourceActionImpl) lastResourcePart).getType();
    } else {
      return null;
    }
  }

  @Override
  public boolean isCollection() {
    UriInfoImpl uriInfo = (UriInfoImpl) path;
    UriResource lastResourcePart = uriInfo.getLastResourcePart();
    return lastResourcePart instanceof UriResourcePartTyped && ((UriResourcePartTyped) lastResourcePart).isCollection();
  }

  @Override
  public String toString() {
    return path.getUriResourceParts().toString() + (startTypeFilter == null ? "" : startTypeFilter);
  }
}