package nl.buildforce.sequoia.processor.core.testmodel;

import nl.buildforce.sequoia.metadata.core.edm.annotation.EdmIgnore;

import jakarta.persistence.*;

@EdmIgnore
@Entity
@Table(schema = "\"OLINGO\"", name = "\"Membership\"")
@IdClass(MembershipKey.class)
public class Membership {
  @Id
  @Column(name = "\"PersonID\"", length = 32)
  private String personID;
  @Id
  @Column(name = "\"TeamID\"", length = 32)
  private String teamID;

}