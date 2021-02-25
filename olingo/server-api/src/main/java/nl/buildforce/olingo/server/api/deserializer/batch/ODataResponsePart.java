/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.deserializer.batch;

import java.util.Collections;
import java.util.List;

import nl.buildforce.olingo.server.api.ODataResponse;

/**
 * An ODataResponsePart represents a collection of ODataResponses.
 * A list of ODataResponseParts can be combined by the BatchSerializer to a single
 * OData batch response.
 */
public class ODataResponsePart {
  private final List<ODataResponse> responses;
  private final boolean isChangeSet;

  /**
   * Creates a new ODataResponsePart.
   *
   * An ODataResponsePart represents a collection of ODataResponses.
   * A list of ODataResponseParts can be combined by the BatchSerializer to a single
   * OData batch response.
   *
   * @param responses A list of {@link ODataResponse}
   * @param isChangeSet whether this ODataResponsePart represents a change set
   */
  public ODataResponsePart(List<ODataResponse> responses, boolean isChangeSet) {
    this.responses = responses;
    this.isChangeSet = isChangeSet;
  }

  /**
   * Creates a new ODataResponsePart.
   *
   * An ODataResponsePart represents a collection of ODataResponses.
   * A list of ODataResponseParts can be combined by the BatchSerializer to a single
   * OData batch response.
   *
   * @param response A single {@link ODataResponse}
   * @param isChangeSet whether this ODataResponsePart represents a change set
   */
  public ODataResponsePart(ODataResponse response, boolean isChangeSet) {
    responses = Collections.singletonList(response);
    this.isChangeSet = isChangeSet;
  }

  /**
   * Returns a collection of ODataResponses.
   * Each collection contains at least one {@link ODataResponse}.
   * If this instance represents a change set, there may be many ODataResponses.
   * @return a list of {@link ODataResponse}
   */
  public List<ODataResponse> getResponses() {
    return responses;
  }

  /**
   * Returns true if the current instance represents a change set.
   * @return true or false
   */
  public boolean isChangeSet() {
    return isChangeSet;
  }
}
