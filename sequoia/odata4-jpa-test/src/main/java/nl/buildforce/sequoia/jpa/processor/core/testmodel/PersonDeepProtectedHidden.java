package nl.buildforce.sequoia.jpa.processor.core.testmodel;

import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmIgnore;
import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmProtectedBy;
import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmProtections;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import java.time.LocalDate;

@EdmIgnore // Only
@Entity(name = "PersonDeepProtected")
@DiscriminatorValue(value = "1")
public class PersonDeepProtectedHidden extends BusinessPartnerProtected {// #NOSONAR use equal method from
                                                                         // BusinessPartner

  @Convert(converter = DateConverter.class)
  @Column(name = "\"BirthDay\"")
  private LocalDate birthDay;

  @Convert(converter = AccessRightsConverter.class)
  @Column(name = "\"AccessRights\"")
  private AccessRights[] accessRights;

  @Embedded
  private AddressDeepProtected inhouseAddress;

  @Embedded
  @EdmProtections({
      @EdmProtectedBy(name = "Creator", path = "created/by"),
      @EdmProtectedBy(name = "Updater", path = "updated/by")
  })
  private final AdministrativeInformation protectedAdminInfo = new AdministrativeInformation();

  public PersonDeepProtectedHidden() {
    type = "1";
  }

  public LocalDate getBirthDay() {
    return birthDay;
  }

  public void setBirthDay(LocalDate birthDay) {
    this.birthDay = birthDay;
  }

  public AccessRights[] getAccessRights() {
    return accessRights;
  }

}