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
package nl.buildforce.olingo.server.api.etag;

import nl.buildforce.olingo.server.api.ODataLibraryException;

/**
 * This exception is thrown for invalid precondition error cases.
 */
public class PreconditionException extends ODataLibraryException {
  private static final long serialVersionUID = -8112658467394158700L;

  public enum MessageKeys implements MessageKey {
    /** no parameter */
    MISSING_HEADER,
    /** no parameter */
    FAILED,
    /** no parameter */
    INVALID_URI;

    @Override
    public String getKey() {
      return name();
    }
  }

  public PreconditionException(String developmentMessage, MessageKey messageKey,
                               String... parameters) {
    super(developmentMessage, messageKey, parameters);
  }

  public PreconditionException(String developmentMessage, Throwable cause,
                               MessageKey messageKey, String... parameters) {
    super(developmentMessage, cause, messageKey, parameters);
  }

  @Override
  protected String getBundleName() {
    return DEFAULT_SERVER_BUNDLE_NAME;
  }
}