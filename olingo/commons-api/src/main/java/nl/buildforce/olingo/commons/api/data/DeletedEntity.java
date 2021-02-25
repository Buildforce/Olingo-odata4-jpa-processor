/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.data;

import java.net.URI;

/**
 * A deleted entity contains the reason for deletion and the id.
 */
public class DeletedEntity extends Entity{
  
  /**
   * Reason of the removal from the list
   */
  public enum Reason {
    /** The entity was deleted. */
    deleted,
    /** The data of the entity has changed and is not any longer part of the response. */
    changed
  }

  private URI id;
  private Reason reason;

  /**
   * Get id.
   * @return id
   */
  public URI getId() {
    return id;
  }

  /**
   * Set id.
   * @param id id
   */
  public void setId(URI id) {
    this.id = id;
  }

  /**
   * Get reason for deletion.
   * @return reason for deletion
   */
  public Reason getReason() {
    return reason;
  }

  /**
   * Set reason for deletion.
   * @param reason for deletion
   */
  public void setReason(Reason reason) {
    this.reason = reason;
  }
}
