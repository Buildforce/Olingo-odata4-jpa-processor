package nl.buildforce.sequoia.jpa.processor.core.testmodel;

import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmIgnore;
import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmMediaStream;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "OrganizationImage")
@Table(schema = "\"OLINGO\"", name = "\"OrganizationImage\"")
public class OrganizationImage {
  @Id
  @Column(name = "\"ID\"")
  private String ID;

  @Column(name = "\"Image\"")
  @EdmMediaStream(contentTypeAttribute = "mimeType")
  private byte[] image;

  @EdmIgnore
  @Column(name = "\"MimeType\"")
  private String mimeType;

  @Embedded
  private AdministrativeInformation administrativeInformation = new AdministrativeInformation();

  public String getID() {
    return ID;
  }

  public void setID(final String iD) {
    ID = iD;
  }

  public byte[] getImage() {
    return image;
  }

  public void setImage(final byte[] image) {
    this.image = image;
  }

  public AdministrativeInformation getAdministrativeInformation() {
    return administrativeInformation;
  }

  public void setAdministrativeInformation(final AdministrativeInformation administrativeInformation) {
    this.administrativeInformation = administrativeInformation;
  }

  String getMimeType() {
    return mimeType;
  }

  void setMimeType(final String mimeType) {
    this.mimeType = mimeType;
  }

}