/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm.annotation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmException;
import nl.buildforce.olingo.commons.api.edm.EdmStructuredType;
import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmPropertyValue;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmRecord;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlPropertyValue;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlRecord;
import nl.buildforce.olingo.commons.core.edm.EdmTypeInfo;

public class EdmRecordImpl extends AbstractEdmAnnotatableDynamicExpression implements EdmRecord {

  private List<EdmPropertyValue> propertyValues;
  private EdmStructuredType type;
  private final CsdlRecord record;

  public EdmRecordImpl(Edm edm, CsdlRecord csdlExp) {
    super(edm, "Record", csdlExp);
      record = csdlExp;
  }

  @Override
  public List<EdmPropertyValue> getPropertyValues() {
    if (propertyValues == null) {
      List<EdmPropertyValue> localValues = new ArrayList<>();
      if (record.getPropertyValues() != null) {
        for (CsdlPropertyValue value : record.getPropertyValues()) {
          localValues.add(new EdmPropertyValueImpl(edm, value));
        }
      }
      propertyValues = Collections.unmodifiableList(localValues);
    }
    return propertyValues;
  }

  @Override
  public EdmStructuredType getType() {
    if (type == null && record.getType() != null) {
      // record MAY have a type information.
      EdmTypeInfo typeInfo = new EdmTypeInfo.Builder().setEdm(edm).setTypeExpression(record.getType()).build();
      if (typeInfo.isEntityType() || typeInfo.isComplexType()) {
        type = typeInfo.isEntityType() ? typeInfo.getEntityType() : typeInfo.getComplexType();
      } else {
        throw new EdmException("Record expressions must specify a complex or entity type.");
      }
    }
    return type;
  }

  @Override
  public EdmExpressionType getExpressionType() {
    return EdmExpressionType.Record;
  }

  @Override
  public FullQualifiedName getTypeFQN() {
    return record.getType() != null ? new FullQualifiedName(record.getType()) : null;
  }
}