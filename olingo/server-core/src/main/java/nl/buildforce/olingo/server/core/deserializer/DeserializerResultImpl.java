/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.deserializer;

import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import nl.buildforce.olingo.commons.api.data.Entity;
import nl.buildforce.olingo.commons.api.data.EntityCollection;
import nl.buildforce.olingo.commons.api.data.Parameter;
import nl.buildforce.olingo.commons.api.data.Property;
import nl.buildforce.olingo.server.api.deserializer.DeserializerResult;
import nl.buildforce.olingo.server.api.uri.queryoption.ExpandOption;

public class DeserializerResultImpl implements DeserializerResult {
  private Entity entity;
  private EntityCollection entitySet;
  private ExpandOption expandOption;
  private Property property;
  private Map<String, Parameter> actionParameters;
  private List<URI> entityReferences;

  private DeserializerResultImpl() {}

  @Override
  public Entity getEntity() {
    return entity;
  }

  @Override
  public EntityCollection getEntityCollection() {
    return entitySet;
  }

  @Override
  public ExpandOption getExpandTree() {
    return expandOption;
  }

  @Override
  public Map<String, Parameter> getActionParameters() {
    return actionParameters;
  }

  @Override
  public Property getProperty() {
    return property;
  }

  @Override
  public List<URI> getEntityReferences() {
    return entityReferences;
  }

  public static DeserializerResultBuilder with() {
    return new DeserializerResultBuilder();
  }

  public static class DeserializerResultBuilder {
    private Entity entity;
    private EntityCollection entitySet;
    private ExpandOption expandOption;
    private Property property;
    private Map<String, Parameter> actionParameters;
    private List<URI> entityReferences;

    public DeserializerResult build() {
      DeserializerResultImpl result = new DeserializerResultImpl();
      result.entity = entity;
      result.entitySet = entitySet;
      result.expandOption = expandOption;
      result.property = property;
      result.entityReferences = (entityReferences == null) ? new ArrayList<>() : entityReferences;
      result.actionParameters = (actionParameters == null) ? new LinkedHashMap<>() : actionParameters;

      return result;
    }

    public DeserializerResultBuilder entity(Entity entity) {
      this.entity = entity;
      return this;
    }

    public DeserializerResultBuilder entityCollection(EntityCollection entitySet) {
      this.entitySet = entitySet;
      return this;
    }

    public DeserializerResultBuilder expandOption(ExpandOption expandOption) {
      this.expandOption = expandOption;
      return this;
    }

    public DeserializerResultBuilder property(Property property) {
      this.property = property;
      return this;
    }

    public DeserializerResultBuilder entityReferences(List<URI> entityReferences) {
      this.entityReferences = entityReferences;
      return this;
    }

    public DeserializerResultBuilder actionParameters(Map<String, Parameter> actionParameters) {
      this.actionParameters = actionParameters;
      return this;
    }
  }
}
