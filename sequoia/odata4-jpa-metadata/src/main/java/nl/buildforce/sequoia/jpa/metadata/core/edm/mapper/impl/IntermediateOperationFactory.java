package nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.impl;

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAEdmNameBuilder;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAModelException;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.extension.ODataOperation;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class IntermediateOperationFactory {

  abstract IntermediateOperation createOperation(final JPAEdmNameBuilder nameBuilder, final IntermediateSchema schema,
      final Method m, final Object functionDescription) throws ODataJPAModelException;

  Map<? extends String, ? extends IntermediateOperation> createOperationMap(final JPAEdmNameBuilder nameBuilder,
      final Reflections reflections, final IntermediateSchema schema, final Class<? extends ODataOperation> clazz,
      final Class<? extends Annotation> annotation)
      throws ODataJPAModelException {

    final Map<String, IntermediateOperation> funcList = new HashMap<>();
    if (reflections != null) {
      @SuppressWarnings("unchecked")
      final Set<Class<? extends ODataOperation>> operationClasses =
          (Set<Class<? extends ODataOperation>>) findJavaOperations(reflections, clazz);

      for (final Class<? extends ODataOperation> operationClass : operationClasses) {
        for (Method m : operationClass.getMethods()) {
          Object operationDescription = m.getAnnotation(annotation);
          if (operationDescription != null) {
            final IntermediateOperation func = createOperation(nameBuilder, schema, m, operationDescription);
            funcList.put(func.getInternalName(), createOperation(nameBuilder, schema, m, operationDescription));
          }
        }
      }
    }
    return funcList;
  }

  private Set<?> findJavaOperations(Reflections reflections, Class<? extends ODataOperation> clazz) {
    return reflections.getSubTypesOf(clazz);
  }

}