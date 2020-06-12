package nl.buildforce.sequoia.jpa.processor.core.processor;

import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmFunctionType;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.*;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAException;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAModelException;
import nl.buildforce.sequoia.jpa.processor.core.api.JPAODataCRUDContextAccess;
import nl.buildforce.sequoia.jpa.processor.core.api.JPAODataDatabaseProcessor;
import nl.buildforce.sequoia.jpa.processor.core.api.JPAODataRequestContextAccess;
import nl.buildforce.sequoia.jpa.processor.core.exception.ODataJPADBAdaptorException;
import nl.buildforce.sequoia.jpa.processor.core.exception.ODataJPAProcessorException;
import org.apache.olingo.commons.api.data.Annotatable;
import org.apache.olingo.commons.api.edm.*;
import org.apache.olingo.commons.api.format.ContentType;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.*;
import org.apache.olingo.server.api.uri.UriParameter;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourceFunction;

import jakarta.persistence.EntityManager;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * Functions as User Defined Functions, Native Query, as Criteria Builder does not provide the option to used UDFs in
 * the From clause.
 * @author Oliver Grande
 *
 */
public final class JPAFunctionRequestProcessor extends JPAOperationRequestProcessor implements JPARequestProcessor {

  private final JPAODataDatabaseProcessor dbProcessor;

  public JPAFunctionRequestProcessor(final OData odata, final JPAODataCRUDContextAccess context,
      final JPAODataRequestContextAccess requestContext) throws ODataJPAException {
    super(odata, context, requestContext);
    this.dbProcessor = context.getDatabaseProcessor();
  }

  @Override
  public void retrieveData(final ODataRequest request, final ODataResponse response, final ContentType responseFormat)
      throws ODataApplicationException, ODataLibraryException {

    Object result = null;
    final UriResourceFunction uriResourceFunction =
        (UriResourceFunction) uriInfo.getUriResourceParts().get(uriInfo.getUriResourceParts().size() - 1);
    final JPAFunction jpaFunction = sd.getFunction(uriResourceFunction.getFunction());
    if (jpaFunction.getFunctionType() == EdmFunctionType.JavaClass) {
      result = processJavaFunction(uriResourceFunction, (JPAJavaFunction) jpaFunction, em);

    } else if (jpaFunction.getFunctionType() == EdmFunctionType.UserDefinedFunction)
      result = processJavaUDF(uriInfo.getUriResourceParts(), (JPADataBaseFunction) jpaFunction);

    final EdmType returnType = uriResourceFunction.getFunction().getReturnType().getType();
    final Annotatable annotatable = convertResult(result, returnType, jpaFunction);
    serializeResult(returnType, response, responseFormat, annotatable, request);
  }

  private Object getValue(final EdmFunction edmFunction, final JPAParameter parameter, final String uriValue)
      throws ODataApplicationException {
    final String value = uriValue.replaceAll("'", "");
    final EdmParameter edmParam = edmFunction.getParameter(parameter.getName());
    try {
      switch (edmParam.getType().getKind()) {
        case PRIMITIVE:
          return ((EdmPrimitiveType) edmParam.getType()).valueOfString(value, false, edmParam.getMaxLength(),
              edmParam.getPrecision(), edmParam.getScale(), true, parameter.getType());
        case ENUM:
          final JPAEnumerationAttribute enumeration = sd.getEnumType(parameter.getTypeFQN()
              .getFullQualifiedNameAsString());
          return enumeration.enumOf(value);
        default:
          throw new ODataJPADBAdaptorException(ODataJPADBAdaptorException.MessageKeys.PARAMETER_CONVERSION_ERROR,
              HttpStatusCode.NOT_IMPLEMENTED, uriValue, parameter.getName());
      }

    } catch (EdmPrimitiveTypeException | ODataJPAModelException e) {
      // Unable to convert value %1$s of parameter %2$s
      throw new ODataJPADBAdaptorException(ODataJPADBAdaptorException.MessageKeys.PARAMETER_CONVERSION_ERROR,
          HttpStatusCode.NOT_IMPLEMENTED, uriValue, parameter.getName());
    }
  }

  private Object processJavaFunction(final UriResourceFunction uriResourceFunction, final JPAJavaFunction jpaFunction,
      final EntityManager em) throws ODataApplicationException {

    final Constructor<?> c = jpaFunction.getConstructor();

    try {
      Object instance;
      if (c.getParameterCount() == 1)
        instance = c.newInstance(em);
      else
        instance = c.newInstance();
      final List<Object> parameter = new ArrayList<>();
      final Parameter[] methodParameter = jpaFunction.getMethod().getParameters();

      for (final Parameter declaredParameter : methodParameter) {
        for (final UriParameter providedParameter : uriResourceFunction.getParameters()) {
          JPAParameter jpaParameter = jpaFunction.getParameter(declaredParameter.getName());
          if (jpaParameter.getName().equals(providedParameter.getName())) {
            parameter.add(getValue(uriResourceFunction.getFunction(), jpaParameter, providedParameter.getText()));
            break;
          }
        }
      }

      return jpaFunction.getMethod().invoke(instance, parameter.toArray());
    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | ODataJPAModelException e) {
      throw new ODataJPAProcessorException(e, HttpStatusCode.INTERNAL_SERVER_ERROR);
    } catch (InvocationTargetException e) {
      Throwable cause = e.getCause();
      if (cause instanceof ODataApplicationException) {
        throw (ODataApplicationException) cause;
      } else {
        throw new ODataJPAProcessorException(e, HttpStatusCode.INTERNAL_SERVER_ERROR);
      }
    }
  }

  private Object processJavaUDF(final List<UriResource> uriResourceParts, final JPADataBaseFunction jpaFunction)
      throws ODataApplicationException {

    return dbProcessor.executeFunctionQuery(uriResourceParts, jpaFunction, em);
  }

}