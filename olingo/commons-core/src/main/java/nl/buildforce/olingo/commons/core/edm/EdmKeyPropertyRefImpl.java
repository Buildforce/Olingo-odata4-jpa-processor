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
package nl.buildforce.olingo.commons.core.edm;

import nl.buildforce.olingo.commons.api.edm.EdmEntityType;
import nl.buildforce.olingo.commons.api.edm.EdmException;
import nl.buildforce.olingo.commons.api.edm.EdmKeyPropertyRef;
import nl.buildforce.olingo.commons.api.edm.EdmProperty;
import nl.buildforce.olingo.commons.api.edm.EdmStructuredType;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlPropertyRef;

public class EdmKeyPropertyRefImpl implements EdmKeyPropertyRef {

  private final CsdlPropertyRef ref;
  private final EdmEntityType edmEntityType;
  private EdmProperty property;

  public EdmKeyPropertyRefImpl(EdmEntityType edmEntityType, CsdlPropertyRef ref) {
    this.edmEntityType = edmEntityType;
    this.ref = ref;
  }

  @Override
  public String getName() {
    return ref.getName();
  }

  @Override
  public String getAlias() {
    return ref.getAlias();
  }

  @Override
  public EdmProperty getProperty() {
    if (property == null) {
      if (getAlias() == null) {
        property = edmEntityType.getStructuralProperty(getName());
        if (property == null) {
          throw new EdmException("Invalid key property ref specified. Can´t find property with name: "
              + getName());
        }
      } else {
        if (getName() == null || getName().isEmpty()) {
          throw new EdmException("Alias but no path specified for propertyRef");
        }
        String[] splitPath = getName().split("/");
        EdmStructuredType structType = edmEntityType;
        for (int i = 0; i < splitPath.length - 1; i++) {
          EdmProperty _property = structType.getStructuralProperty(splitPath[i]);
          if (_property == null) {
            throw new EdmException("Invalid property ref specified. Can´t find property with name: " + splitPath[i]
                + " at type: " + structType.getNamespace() + "." + structType.getName());
          }
          structType = (EdmStructuredType) _property.getType();
        }
        property = structType.getStructuralProperty(splitPath[splitPath.length - 1]);
        if (property == null) {
          throw new EdmException("Invalid property ref specified. Can´t find property with name: "
              + splitPath[splitPath.length - 1] + " at type: " + structType.getNamespace() + "."
              + structType.getName());
        }
      }
    }

    return property;
  }
}