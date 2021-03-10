package nl.buildforce.sequoia.processor.core.util;

import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAEdmNameBuilder;
import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmAction;
import nl.buildforce.olingo.commons.api.edm.EdmAnnotations;
import nl.buildforce.olingo.commons.api.edm.EdmComplexType;
import nl.buildforce.olingo.commons.api.edm.EdmEntityContainer;
import nl.buildforce.olingo.commons.api.edm.EdmEntityType;
import nl.buildforce.olingo.commons.api.edm.EdmEnumType;
import nl.buildforce.olingo.commons.api.edm.EdmFunction;
import nl.buildforce.olingo.commons.api.edm.EdmSchema;
import nl.buildforce.olingo.commons.api.edm.EdmTerm;
import nl.buildforce.olingo.commons.api.edm.EdmTypeDefinition;
import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;
import nl.buildforce.olingo.commons.api.edm.constants.ODataServiceVersion;
import nl.buildforce.olingo.commons.api.edmx.EdmxReference;
import nl.buildforce.olingo.server.api.ServiceMetadata;
import nl.buildforce.olingo.server.api.etag.ServiceMetadataETagSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.fail;

public class ServiceMetadataDouble implements ServiceMetadata {
  private final Edm edm;
  private final JPAEdmNameBuilder nameBuilder;

  public ServiceMetadataDouble(JPAEdmNameBuilder nameBuilder, String typeName) {
    this.nameBuilder = nameBuilder;
    this.edm = new EdmDouble(typeName);
  }
  
  @Override
  public Edm getEdm() {
    return edm;
  }

/*
  @Override
  public ODataServiceVersion getDataServiceVersion() {
    fail();
    return null;
  }
*/

  @Override
  public List<EdmxReference> getReferences() {
    fail();
    return null;
  }

  @Override
  public ServiceMetadataETagSupport getServiceMetadataETagSupport() {
    fail();
    return null;
  }

  class EdmDouble implements Edm {
    private final Map<FullQualifiedName, EdmEntityType> typeMap;

    public EdmDouble() {
      typeMap = new HashMap<>();
    }

    public EdmDouble(String name) {
      typeMap = new HashMap<>();
      EdmEntityType edmType = new EdmEntityTypeDouble(nameBuilder, name);
      typeMap.put(edmType.getFullQualifiedName(), edmType);
    }

    @Override
    public List<EdmSchema> getSchemas() {
      fail();
      return null;
    }

    @Override
    public EdmSchema getSchema(String namespace) {
      fail();
      return null;
    }

    @Override
    public EdmEntityContainer getEntityContainer() {
      fail();
      return null;
    }

    @Override
    public EdmEntityContainer getEntityContainer(FullQualifiedName name) {
      fail();
      return null;
    }

    @Override
    public EdmEnumType getEnumType(FullQualifiedName name) {
      fail();
      return null;
    }

    @Override
    public EdmTypeDefinition getTypeDefinition(FullQualifiedName name) {
      fail();
      return null;
    }

    @Override
    public EdmEntityType getEntityType(FullQualifiedName name) {
      return typeMap.get(name);
    }

    @Override
    public EdmComplexType getComplexType(FullQualifiedName name) {
      fail();
      return null;
    }

    @Override
    public EdmAction getUnboundAction(FullQualifiedName actionName) {
      fail();
      return null;
    }

    @Override
    public EdmAction getBoundAction(FullQualifiedName actionName, FullQualifiedName bindingParameterTypeName,
        Boolean isBindingParameterCollection) {
      fail();
      return null;
    }

    @Override
    public List<EdmFunction> getUnboundFunctions(FullQualifiedName functionName) {
      fail();
      return null;
    }

    @Override
    public EdmFunction getUnboundFunction(FullQualifiedName functionName, List<String> parameterNames) {
      fail();
      return null;
    }

    @Override
    public EdmFunction getBoundFunction(FullQualifiedName functionName, FullQualifiedName bindingParameterTypeName,
        Boolean isBindingParameterCollection, List<String> parameterNames) {
      fail();
      return null;
    }

    @Override
    public EdmTerm getTerm(FullQualifiedName termName) {
      fail();
      return null;
    }

    @Override
    public EdmAnnotations getAnnotationGroup(FullQualifiedName targetName, String qualifier) {
      fail();
      return null;
    }

    @Override
    public EdmAction getBoundActionWithBindingType(FullQualifiedName bindingParameterTypeName,
        Boolean isBindingParameterCollection) {
      return null;
    }

    @Override
    public List<EdmFunction> getBoundFunctionsWithBindingType(FullQualifiedName bindingParameterTypeName,
        Boolean isBindingParameterCollection) {
      fail();
      return null;
    }

    @Override
    public EdmComplexType getComplexTypeWithAnnotations(FullQualifiedName arg0) {
      fail();
      return null;
    }

    @Override
    public EdmEntityType getEntityTypeWithAnnotations(FullQualifiedName arg0) {
      fail();
      return null;
    }

  }

}