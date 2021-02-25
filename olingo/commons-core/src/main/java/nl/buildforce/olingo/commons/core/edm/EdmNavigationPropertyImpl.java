/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmEntityType;
import nl.buildforce.olingo.commons.api.edm.EdmException;
import nl.buildforce.olingo.commons.api.edm.EdmNavigationProperty;
import nl.buildforce.olingo.commons.api.edm.EdmOnDelete;
import nl.buildforce.olingo.commons.api.edm.EdmReferentialConstraint;
import nl.buildforce.olingo.commons.api.edm.EdmStructuredType;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlNavigationProperty;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlOnDelete;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlReferentialConstraint;

public class EdmNavigationPropertyImpl extends AbstractEdmNamed implements EdmNavigationProperty {

  private final CsdlNavigationProperty navigationProperty;
  private List<EdmReferentialConstraint> referentialConstraints;
  private EdmEntityType typeImpl;
  private EdmNavigationProperty partnerNavigationProperty;

  public EdmNavigationPropertyImpl(Edm edm, CsdlNavigationProperty navigationProperty) {
    super(edm, navigationProperty.getName(), navigationProperty);
    this.navigationProperty = navigationProperty;
  }

  @Override
  public boolean isCollection() {
    return navigationProperty.isCollection();
  }

  @Override
  public boolean isNullable() {
    return navigationProperty.isNullable();
  }

  @Override
  public boolean containsTarget() {
    return navigationProperty.isContainsTarget();
  }

  @Override
  public EdmEntityType getType() {
    if (typeImpl == null) {
      typeImpl = edm.getEntityType(navigationProperty.getTypeFQN());
      if (typeImpl == null) {
        throw new EdmException("Cannot find type with name: " + navigationProperty.getTypeFQN());
      }
    }
    return typeImpl;
  }

  @Override
  public EdmNavigationProperty getPartner() {
    if (partnerNavigationProperty == null) {
      String partner = navigationProperty.getPartner();
      if (partner != null) {
        EdmStructuredType type = getType();
        EdmNavigationProperty property = null;
        String[] split = partner.split("/");
        for (String element : split) {
          property = type.getNavigationProperty(element);
          if (property == null) {
            throw new EdmException("Cannot find navigation property with name: " + element
                + " at type " + type.getName());
          }
          type = property.getType();
        }
        partnerNavigationProperty = property;
      }
    }
    return partnerNavigationProperty;
  }

  @Override
  public String getReferencingPropertyName(String referencedPropertyName) {
    List<CsdlReferentialConstraint> refConstraints = navigationProperty.getReferentialConstraints();
    if (refConstraints != null) {
      for (CsdlReferentialConstraint constraint : refConstraints) {
        if (constraint.getReferencedProperty().equals(referencedPropertyName)) {
          return constraint.getProperty();
        }
      }
    }
    return null;
  }

  @Override
  public List<EdmReferentialConstraint> getReferentialConstraints() {
    if (referentialConstraints == null) {
      List<CsdlReferentialConstraint> providerConstraints = navigationProperty.getReferentialConstraints();
      List<EdmReferentialConstraint> referentialConstraintsLocal = new ArrayList<>();
      if (providerConstraints != null) {
        for (CsdlReferentialConstraint constraint : providerConstraints) {
          referentialConstraintsLocal.add(new EdmReferentialConstraintImpl(edm, constraint));
        }
      }

      referentialConstraints = Collections.unmodifiableList(referentialConstraintsLocal);
    }
    return referentialConstraints;
  }

  @Override
  public EdmOnDelete getOnDelete() {
    CsdlOnDelete csdlOnDelete = navigationProperty.getOnDelete();
    return csdlOnDelete != null ? new EdmOnDeleteImpl(edm, csdlOnDelete) : null;
  }
}
