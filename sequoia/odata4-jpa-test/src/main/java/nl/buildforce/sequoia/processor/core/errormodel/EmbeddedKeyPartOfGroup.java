/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
 */
package nl.buildforce.sequoia.processor.core.errormodel;

import nl.buildforce.sequoia.metadata.core.edm.annotation.EdmVisibleFor;
import nl.buildforce.sequoia.processor.core.testmodel.CountryKey;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Version;

@Entity
public class EmbeddedKeyPartOfGroup {

  @EdmVisibleFor("Person")
  @EmbeddedId
  private CountryKey key;

  @Version
  @Column(name = "\"ETag\"", nullable = false)
  protected long eTag;

}