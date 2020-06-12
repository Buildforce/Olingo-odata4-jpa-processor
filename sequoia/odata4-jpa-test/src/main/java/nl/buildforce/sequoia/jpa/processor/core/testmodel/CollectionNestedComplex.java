package nl.buildforce.sequoia.jpa.processor.core.testmodel;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

@Embeddable
public class CollectionNestedComplex {

  @Column(name = "\"Number\"")
  private Long number;

  @Embedded
  private CollectionInnerComplex inner;

  public Long getNumber() {
    return number;
  }

  public void setNumber(Long number) {
    this.number = number;
  }

  public CollectionInnerComplex getInner() {
    return inner;
  }

  public void setInner(CollectionInnerComplex inner) {
    this.inner = inner;
  }

}