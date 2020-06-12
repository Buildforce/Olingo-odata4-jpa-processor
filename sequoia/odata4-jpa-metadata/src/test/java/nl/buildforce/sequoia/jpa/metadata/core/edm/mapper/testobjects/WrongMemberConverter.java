package nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.testobjects;

import jakarta.persistence.AttributeConverter;
import java.util.ArrayList;
import java.util.List;

public class WrongMemberConverter implements AttributeConverter<WrongMember[], Integer> {

  @Override
  public Integer convertToDatabaseColumn(WrongMember[] attributes) {
    return attributes[0].getValue();
  }

  @Override
  public WrongMember[] convertToEntityAttribute(Integer dbData) {
    if (dbData == null)
      return null;
    final List<WrongMember> accesses = new ArrayList<>();
    for (WrongMember e : WrongMember.values()) {
      if (e.getValue() == dbData)
        accesses.add(e);
    }
    return accesses.toArray(new WrongMember[] {});
  }

}