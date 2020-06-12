package nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.impl;

import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmAction;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAEdmNameBuilder;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAModelException;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.extension.ODataAction;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.Map;

class IntermediateActionFactory extends IntermediateOperationFactory {

  @Override
  IntermediateOperation createOperation(JPAEdmNameBuilder nameBuilder, IntermediateSchema schema, Method m,
      Object functionDescription) throws ODataJPAModelException {
    return new IntermediateJavaAction(nameBuilder, (EdmAction) functionDescription, m, schema);
  }

  @SuppressWarnings("unchecked")
  <F extends IntermediateJavaAction> Map<String, F> create(final JPAEdmNameBuilder nameBuilder,
      final Reflections reflections, final IntermediateSchema schema) throws ODataJPAModelException {

    return (Map<String, F>) createOperationMap(nameBuilder, reflections, schema, ODataAction.class, EdmAction.class);
  }

}