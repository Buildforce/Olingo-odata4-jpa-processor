/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmEntityType;
import nl.buildforce.olingo.commons.api.edm.EdmException;
import nl.buildforce.olingo.commons.api.edm.EdmKeyPropertyRef;
import nl.buildforce.olingo.commons.api.edm.EdmStructuredType;
import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;
import nl.buildforce.olingo.commons.api.edm.constants.EdmTypeKind;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlEntityType;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlPropertyRef;

public class EdmEntityTypeImpl extends AbstractEdmStructuredType implements EdmEntityType {

  private final CsdlEntityType entityType;
  private boolean baseTypeChecked;
  private final boolean hasStream;
  protected EdmEntityType entityBaseType;
  private final List<String> keyPredicateNames = Collections.synchronizedList(new ArrayList<>());
  private final Map<String, EdmKeyPropertyRef> keyPropertyRefs =
      Collections.synchronizedMap(new LinkedHashMap<>());
  private List<EdmKeyPropertyRef> keyPropertyRefsList;

  public EdmEntityTypeImpl(Edm edm, FullQualifiedName name, CsdlEntityType entityType) {
    super(edm, name, EdmTypeKind.ENTITY, entityType);
    this.entityType = entityType;
    hasStream = entityType.hasStream();
  }

  @Override
  protected void checkBaseType() {
    if (!baseTypeChecked) {
      if (baseTypeName != null) {
        baseType = buildBaseType(baseTypeName);
        entityBaseType = (EdmEntityType) baseType;
      }
      if (baseType == null
          || (baseType.isAbstract() && ((EdmEntityType) baseType).getKeyPropertyRefs().isEmpty())) {
        List<CsdlPropertyRef> key = entityType.getKey();
        if (key != null) {
          List<EdmKeyPropertyRef> edmKey = new ArrayList<>();
          for (CsdlPropertyRef ref : key) {
            edmKey.add(new EdmKeyPropertyRefImpl(this, ref));
          }
          setEdmKeyPropertyRef(edmKey);
        }
      }
      baseTypeChecked = true;
    }
  }

  protected void setEdmKeyPropertyRef(List<EdmKeyPropertyRef> edmKey) {
    for (EdmKeyPropertyRef ref : edmKey) {
      if (ref.getAlias() == null) {
        keyPredicateNames.add(ref.getName());
        keyPropertyRefs.put(ref.getName(), ref);
      } else {
        keyPredicateNames.add(ref.getAlias());
        keyPropertyRefs.put(ref.getAlias(), ref);
      }
    }
  }

  @Override
  protected EdmStructuredType buildBaseType(FullQualifiedName baseTypeName) {
    EdmEntityType baseType = null;
    if (baseTypeName != null) {
      baseType = edm.getEntityType(baseTypeName);
      if (baseType == null) {
        throw new EdmException("Cannot find base type with name: " + baseTypeName + " for entity type: " + getName());
      }
    }
    return baseType;
  }

  @Override
  public EdmEntityType getBaseType() {
    checkBaseType();
    return entityBaseType;
  }

  @Override
  public List<String> getKeyPredicateNames() {
    checkBaseType();
    if (keyPredicateNames.isEmpty() && baseType != null) {
      return entityBaseType.getKeyPredicateNames();
    }
    return Collections.unmodifiableList(keyPredicateNames);
  }

  @Override
  public List<EdmKeyPropertyRef> getKeyPropertyRefs() {
    checkBaseType();
    if (keyPropertyRefsList == null) {
      keyPropertyRefsList = new ArrayList<>(keyPropertyRefs.values());
    }
    if (keyPropertyRefsList.isEmpty() && entityBaseType != null) {
      return entityBaseType.getKeyPropertyRefs();
    }
    return Collections.unmodifiableList(keyPropertyRefsList);
  }

  @Override
  public EdmKeyPropertyRef getKeyPropertyRef(String keyPredicateName) {
    checkBaseType();
    EdmKeyPropertyRef edmKeyPropertyRef = keyPropertyRefs.get(keyPredicateName);
    if (edmKeyPropertyRef == null && entityBaseType != null) {
      return entityBaseType.getKeyPropertyRef(keyPredicateName);
    }
    return edmKeyPropertyRef;
  }

  @Override
  public boolean hasStream() {
    checkBaseType();

    return hasStream || entityBaseType != null && entityBaseType.hasStream();
  }

}