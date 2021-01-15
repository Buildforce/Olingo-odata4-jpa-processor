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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.buildforce.olingo.server.api.uri.UriInfo;
import nl.buildforce.olingo.server.api.uri.UriResource;
import nl.buildforce.olingo.server.api.uri.queryoption.apply.GroupByItem;

/**
 * Represents a grouping property.
 */
public class GroupByItemImpl implements GroupByItem {

  private UriInfo path;
  private boolean isRollupAll;
  private final List<GroupByItem> rollup = new ArrayList<>();

  @Override
  public List<UriResource> getPath() {
    return path == null ? Collections.emptyList() : path.getUriResourceParts();
  }

  public GroupByItemImpl setPath(UriInfo uriInfo) {
    path = uriInfo;
    return this;
  }

  @Override
  public List<GroupByItem> getRollup() {
    return rollup;
  }

  public GroupByItemImpl addRollupItem(GroupByItem groupByItem) {
    rollup.add(groupByItem);
    return this;
  }

  @Override
  public boolean isRollupAll() {
    return isRollupAll;
  }

  public GroupByItemImpl setIsRollupAll() {
      isRollupAll = true;
    return this;
  }
}