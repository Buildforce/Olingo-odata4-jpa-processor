package nl.buildforce.sequoia.jpa.processor.core.converter;

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAServiceDocument;
import org.apache.olingo.commons.api.data.ComplexValue;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.edm.EdmComplexType;
import org.apache.olingo.server.api.ODataApplicationException;

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