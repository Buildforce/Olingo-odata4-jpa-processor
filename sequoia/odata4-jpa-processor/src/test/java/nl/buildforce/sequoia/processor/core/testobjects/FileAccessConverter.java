package nl.buildforce.sequoia.processor.core.testobjects;

import jakarta.persistence.AttributeConverter;
import java.util.ArrayList;
import java.util.List;

public class FileAccessConverter implements AttributeConverter<FileAccess[], Short> {

  @Override
  public Short convertToDatabaseColumn(FileAccess[] attribute) {
    return attribute[0].getValue();
  }

  @Override
  public FileAccess[] convertToEntityAttribute(Short dbData) {
    if (dbData == null)
      return null;
    final List<FileAccess> accesses = new ArrayList<>();
    for (FileAccess e : FileAccess.values()) {
      if (e.getValue() == dbData)
        accesses.add(e);
    }
    return accesses.toArray(new FileAccess[] {});
  }

}