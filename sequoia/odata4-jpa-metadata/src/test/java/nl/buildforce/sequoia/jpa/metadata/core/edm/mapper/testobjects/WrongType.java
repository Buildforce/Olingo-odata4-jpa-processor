package nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.testobjects;

import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmEnumeration;

import java.math.BigDecimal;

@EdmEnumeration(converter = WrongTypeConverter.class)
public enum WrongType {
  TEST(BigDecimal.valueOf(2L));

  private final BigDecimal value;

  WrongType(BigDecimal value) {
    this.value = value;
  }

  public BigDecimal getValue() {
    return value;
  }
}