/**
 *

 */
package nl.buildforce.sequoia.jpa.processor.core.errormodel;

import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmVisibleFor;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.CountryKey;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Version;

/**
 * @author Oliver Grande
 * Created: 29.06.2019
 *
 */
@Entity
public class EmbeddedKeyPartOfGroup {

  @EdmVisibleFor("Person")
  @EmbeddedId
  private CountryKey key;

  @Version
  @Column(name = "\"ETag\"", nullable = false)
  protected long eTag;

}