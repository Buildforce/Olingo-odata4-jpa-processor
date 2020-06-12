package nl.buildforce.sequoia.jpa.processor.core.testmodel;

import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmMediaStream;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "PersonImage")
@Table(schema = "\"OLINGO\"", name = "\"PersonImage\"")
public class PersonImage {
  @Id
  @Column(name = "\"ID\"")
  private String ID;

  @Column(name = "\"Image\"")
  @EdmMediaStream(contentType = "image/png")
  private byte[] image;

  @Embedded
  private AdministrativeInformation administrativeInformation = new AdministrativeInformation();

  String getID() {
    return ID;
  }

  void setID(String iD) {
    ID = iD;
  }

  byte[] getImage() {
    return image;
  }

  void setImage(byte[] image) {
    this.image = image;
  }

  AdministrativeInformation getAdministrativeInformation() {
    return administrativeInformation;
  }

  void setAdministrativeInformation(AdministrativeInformation administrativeInformation) {
    this.administrativeInformation = administrativeInformation;
  }

}