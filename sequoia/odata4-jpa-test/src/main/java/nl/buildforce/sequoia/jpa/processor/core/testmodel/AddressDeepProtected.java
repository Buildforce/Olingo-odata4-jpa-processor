package nl.buildforce.sequoia.jpa.processor.core.testmodel;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

//Only for Unit Tests
// @EdmIgnore
@Embeddable
public class AddressDeepProtected {

  @Column(name = "\"AddressType\"")
  private String type;

  @Embedded
  private InhouseAddressWithProtection inhouseAddress;

}