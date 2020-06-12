package nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.testobjects;

import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmEnumeration;

@EdmEnumeration(isFlags = true, converter = WrongMemberConverter.class)
public enum WrongMember {
  Right(1), Wrong(-2);
  private final int value;

  WrongMember(final int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

}