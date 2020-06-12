package nl.buildforce.sequoia.jpa.processor.core.testmodel;

import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmEnumeration;

@EdmEnumeration(isFlags = true, converter = AccessRightsConverter.class)
public enum AccessRights {
  Read((short) 1), Write((short) 2), Create((short) 4), Delete((short) 8);

  private short value;

  AccessRights(short value) {
    this.setValue(value);
  }

  public short getValue() {
    return value;
  }

  private void setValue(short value) {
    this.value = value;
  }
}