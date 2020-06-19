package nl.buildforce.sequoia.processor.core.util;

import nl.buildforce.olingo.commons.api.data.Entity;
import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmEntitySet;
import nl.buildforce.olingo.commons.api.edm.EdmEntityType;
import nl.buildforce.olingo.commons.api.edm.EdmStructuredType;
import nl.buildforce.olingo.server.api.uri.UriHelper;
import nl.buildforce.olingo.server.api.uri.UriParameter;
import nl.buildforce.olingo.server.api.uri.UriResourceEntitySet;
import nl.buildforce.olingo.server.api.uri.queryoption.ExpandOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SelectOption;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.fail;

public class UriHelperDouble implements UriHelper {
  private Map<String, String> keyPredicates;
  private String idPropertyName;

  @Override
  public String buildContextURLSelectList(EdmStructuredType type, ExpandOption expand, SelectOption select) {
    fail();
    return null;
  }

  @Override
  public String buildContextURLKeyPredicate(List<UriParameter> keys) {
    fail();
    return null;
  }

  @Override
  public String buildCanonicalURL(EdmEntitySet edmEntitySet, Entity entity) {
    fail();
    return null;
  }

  @Override
  public String buildKeyPredicate(EdmEntityType edmEntityType, Entity entity) {

    return keyPredicates.get(entity.getProperty(idPropertyName).getValue());
  }

  @Override
  public UriResourceEntitySet parseEntityId(Edm edm, String entityId, String rawServiceRoot) {
    fail();
    return null;
  }

  public Map<String, String> getKeyPredicates() {
    return keyPredicates;
  }

  public void setKeyPredicates(Map<String, String> keyPredicates, String idPropertyName) {
    this.keyPredicates = keyPredicates;
    this.idPropertyName = idPropertyName;
  }

}