package nl.buildforce.sequoia.jpa.processor.core.testmodel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(schema = "\"OLINGO\"", name = "\"User\"")
public class User {

  @Id
  @Column(name = "\"UserName\"", length = 60)
  private String username;

  @Column(name = "\"Password\"", length = 60)
  private String password;

  @Column(name = "\"Enabled\"", length = 60)
  private Boolean enabled;

  public String getUsername() {
    return username;
  }

  public void setId(String username) {
    this.username = username;
  }

  public String getId() {
    return this.username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public void setUsername(String username) {
    this.username = username;
  }

}