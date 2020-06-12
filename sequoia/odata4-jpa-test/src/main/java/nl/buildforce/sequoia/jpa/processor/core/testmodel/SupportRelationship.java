package nl.buildforce.sequoia.jpa.processor.core.testmodel;

import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@EdmIgnore
@Entity
@Table(schema = "\"OLINGO\"", name = "\"SupportRelationship\"")
public class SupportRelationship {
  @Id
  @Column(name = "\"ID\"")
  private Integer iD;

  @Column(name = "\"OrganizationID\"", length = 32)
  private String organizationID;

  @Column(name = "\"PersonID\"", length = 32)
  private String personID;

}