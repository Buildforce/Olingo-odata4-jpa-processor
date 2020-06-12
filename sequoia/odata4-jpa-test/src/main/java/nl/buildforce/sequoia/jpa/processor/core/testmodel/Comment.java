package nl.buildforce.sequoia.jpa.processor.core.testmodel;

import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmIgnore;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.sql.Clob;

@EdmIgnore
@Entity
@Table(schema = "\"OLINGO\"", name = "\"Comment\"")
public class Comment implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "\"BusinessPartnerID\"")
  private String businessPartnerID;

  @Column(name = "\"Order\"")
  private String order;

  @Lob
  @Column(name = "\"Text\"")
  @Basic(fetch = FetchType.LAZY)
  private Clob text;

  public Comment() {
  }

  public String getBusinessPartnerID() {
    return this.businessPartnerID;
  }

  public String getOrder() {
    return order;
  }

  public void setOrder(String order) {
    this.order = order;
  }

  public Clob getText() {
    return text;
  }

  public void setText(Clob text) {
    this.text = text;
  }

  public void setBusinessPartnerID(String businessPartnerID) {
    this.businessPartnerID = businessPartnerID;
  }

}