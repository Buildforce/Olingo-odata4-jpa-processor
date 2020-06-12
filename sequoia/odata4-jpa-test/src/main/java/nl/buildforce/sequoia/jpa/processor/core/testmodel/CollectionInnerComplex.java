package nl.buildforce.sequoia.jpa.processor.core.testmodel;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class CollectionInnerComplex {

  @Column(name = "\"Figure1\"")
  private Long figure1;

  @Column(name = "\"Figure2\"")
  private Long figure2;

  @Column(name = "\"Figure3\"")
  private Long figure3;

  public Long getFigure1() {
    return figure1;
  }

  public void setFigure1(Long figure1) {
    this.figure1 = figure1;
  }

  public Long getFigure2() {
    return figure2;
  }

  public void setFigure2(Long figure2) {
    this.figure2 = figure2;
  }

  public Long getFigure3() {
    return figure3;
  }

  public void setFigure3(Long figure3) {
    this.figure3 = figure3;
  }

}