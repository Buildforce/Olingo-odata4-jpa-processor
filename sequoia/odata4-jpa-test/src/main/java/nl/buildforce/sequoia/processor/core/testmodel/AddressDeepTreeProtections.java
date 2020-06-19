package nl.buildforce.sequoia.processor.core.testmodel;

import nl.buildforce.sequoia.metadata.core.edm.annotation.EdmIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

//Only for Unit Tests
@EdmIgnore
@Embeddable
public class AddressDeepTreeProtections {

  @Column(name = "\"AddressType\"")
  private String type;

  @Embedded
  private InhouseAddressWithThreeProtections inhouseAddress;

}