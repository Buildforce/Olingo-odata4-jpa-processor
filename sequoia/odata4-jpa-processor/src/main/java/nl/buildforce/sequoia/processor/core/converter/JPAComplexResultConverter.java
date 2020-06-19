package nl.buildforce.sequoia.processor.core.converter;

import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAServiceDocument;
import nl.buildforce.olingo.commons.api.data.ComplexValue;
import nl.buildforce.olingo.commons.api.data.Property;
import nl.buildforce.olingo.commons.api.edm.EdmComplexType;
import nl.buildforce.olingo.server.api.ODataApplicationException;

import java.util.ArrayList;
import java.util.List;

public class JPAComplexResultConverter extends JPAStructuredResultConverter {

  public JPAComplexResultConverter(final JPAServiceDocument sd, final List<?> jpaQueryResult,
      final EdmComplexType edmComplexType) {

    super(jpaQueryResult, sd.getComplexType(edmComplexType));
  }

  @Override
  public List<ComplexValue> getResult() throws ODataApplicationException {
    List<ComplexValue> result = new ArrayList<>();

    for (Object row : this.jpaQueryResult) {
      final ComplexValue value = new ComplexValue();
      final List<Property> properties = value.getValue();
      convertProperties(row, properties, jpaTopLevelType);
      result.add(value);
    }
    return result;
  }

}