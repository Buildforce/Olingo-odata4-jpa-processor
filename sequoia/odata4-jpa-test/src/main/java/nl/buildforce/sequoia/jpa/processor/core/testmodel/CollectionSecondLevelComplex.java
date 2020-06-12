package nl.buildforce.sequoia.jpa.processor.core.testmodel;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class CollectionSecondLevelComplex {

  @Column(name = "\"Number\"")
  private Long number;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(schema = "\"OLINGO\"", name = "\"InhouseAddress\"",
      joinColumns = @JoinColumn(name = "\"ID\"", referencedColumnName = "\"ID\""))
  private List<InhouseAddress> address = new ArrayList<>();

  @ElementCollection(fetch = FetchType.LAZY)
  @OrderColumn(name = "\"Order\"")
  @CollectionTable(schema = "\"OLINGO\"", name = "\"Comment\"",
      joinColumns = @JoinColumn(name = "\"BusinessPartnerID\"", referencedColumnName = "\"ID\""))
  @Column(name = "\"Text\"")
  private List<String> comment = new ArrayList<>();

  public void setNumber(Long number) {
    this.number = number;
  }

  public void setAddress(List<InhouseAddress> address) {
    this.address = address;
  }

  public void setComment(List<String> comment) {
    this.comment = comment;
  }

  public Long getNumber() {
    return number;
  }

  public List<InhouseAddress> getAddress() {
    return address;
  }

  public List<String> getComment() {
    return comment;
  }

  public void addInhouseAddress(InhouseAddress address) {
    this.address.add(address);
  }

}