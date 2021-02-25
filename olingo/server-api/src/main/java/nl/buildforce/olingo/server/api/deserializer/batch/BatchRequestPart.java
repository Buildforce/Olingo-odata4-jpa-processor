/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.deserializer.batch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.buildforce.olingo.server.api.ODataRequest;

/**
 * Represents a distinct MIME part of a Batch Request body - either a Change Set or a Query Operation
 */
public class BatchRequestPart {
  private final List<ODataRequest> requests;
  private final boolean isChangeSet;

  /**
   * Creates a new instance of BachRequestPart.
   * @param isChangeSet True, if this instance represents a change set
   * @param requests A list of {@link ODataRequest}
   */
  public BatchRequestPart(boolean isChangeSet, List<ODataRequest> requests) {
    this.isChangeSet = isChangeSet;
    this.requests = requests;
  }

  /**
   * Creates a new instance of BachRequestPart.
   * @param isChangeSet True, if this instance represents a change set
   * @param request A single {@link ODataRequest}
   */
  public BatchRequestPart(boolean isChangeSet, ODataRequest request) {
    this.isChangeSet = isChangeSet;
    requests = new ArrayList<>();
    requests.add(request);
  }

  /**
   * Gets the info if a BatchPart is a ChangeSet.
   * @return true or false
   */
  public boolean isChangeSet() {
    return isChangeSet;
  }

  /**
   * Gets all requests of this part. If a BatchPart is a Query Operation, the list contains one request.
   * @return a list of {@link ODataRequest}
   */
  public List<ODataRequest> getRequests() {
    return Collections.unmodifiableList(requests);
  }
}