/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.provider;

import java.util.ArrayList;
import java.util.List;

import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;

/**
 * The type Csdl navigation property.
 */
public class CsdlNavigationProperty extends CsdlAbstractEdmItem implements CsdlNamed, CsdlAnnotatable {

  private String name;

  private FullQualifiedName type;

  private boolean isCollection;

  private String partner;

  private boolean containsTarget;

  private List<CsdlReferentialConstraint> referentialConstraints = new ArrayList<>();

  // Facets
  private boolean nullable = true;

  private CsdlOnDelete onDelete;

  private List<CsdlAnnotation> annotations = new ArrayList<>();

  @Override
  public String getName() {
    return name;
  }

  /**
   * Is collection.
   *
   * @return the boolean
   */
  public boolean isCollection() {
    return isCollection;
  }

  /**
   * Sets collection.
   *
   * @param isCollection the is collection
   * @return the collection
   */
  public CsdlNavigationProperty setCollection(boolean isCollection) {
    this.isCollection = isCollection;
    return this;
  }

  /**
   * Sets name.
   *
   * @param name the name
   * @return the name
   */
  public CsdlNavigationProperty setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Gets type fQN.
   *
   * @return the type fQN
   */
  public FullQualifiedName getTypeFQN() {
    return type;
  }

  /**
   * Gets type.
   *
   * @return the type
   */
  public String getType() {
    if (type != null) {
      return type.getFullQualifiedNameAsString();
    }
    return null;
  }

  /**
   * Sets type.
   *
   * @param type the type
   * @return the type
   */
  public CsdlNavigationProperty setType(FullQualifiedName type) {
    this.type = type;
    return this;
  }

  /**
   * Sets type.
   *
   * @param type the type
   * @return the type
   */
  public CsdlNavigationProperty setType(String type) {
    this.type = new FullQualifiedName(type);
    return this;
  }

  /**
   * Gets partner.
   *
   * @return the partner
   */
  public String getPartner() {
    return partner;
  }

  /**
   * Sets partner.
   *
   * @param partner the partner
   * @return the partner
   */
  public CsdlNavigationProperty setPartner(String partner) {
    this.partner = partner;
    return this;
  }

  /**
   * Is contains target.
   *
   * @return the boolean
   */
  public boolean isContainsTarget() {
    return containsTarget;
  }

  /**
   * Sets contains target.
   *
   * @param containsTarget the contains target
   * @return the contains target
   */
  public CsdlNavigationProperty setContainsTarget(boolean containsTarget) {
    this.containsTarget = containsTarget;
    return this;
  }

  /**
   * Gets referential constraints.
   *
   * @return the referential constraints
   */
  public List<CsdlReferentialConstraint> getReferentialConstraints() {
    return referentialConstraints;
  }

  /**
   * Sets referential constraints.
   *
   * @param referentialConstraints the referential constraints
   * @return the referential constraints
   */
  public CsdlNavigationProperty setReferentialConstraints(
      List<CsdlReferentialConstraint> referentialConstraints) {
    this.referentialConstraints = referentialConstraints;
    return this;
  }

  /**
   * Is nullable.
   *
   * @return the boolean
   */
  public Boolean isNullable() {
    return nullable;
  }

  /**
   * Sets nullable.
   *
   * @param nullable the nullable
   * @return the nullable
   */
  public CsdlNavigationProperty setNullable(Boolean nullable) {
    this.nullable = nullable;
    return this;
  }

  /**
   * Gets on delete.
   *
   * @return the on delete
   */
  public CsdlOnDelete getOnDelete() {
    return onDelete;
  }

  /**
   * Sets on delete.
   *
   * @param onDelete the on delete
   * @return the on delete
   */
  public CsdlNavigationProperty setOnDelete(CsdlOnDelete onDelete) {
    this.onDelete = onDelete;
    return this;
  }

  @Override
  public List<CsdlAnnotation> getAnnotations() {
    return annotations;
  }
  
  /**
   * Sets a list of annotations
   * @param annotations list of annotations
   * @return this instance
   */
  public CsdlNavigationProperty setAnnotations(List<CsdlAnnotation> annotations) {
    this.annotations = annotations;
    return this;
  }

}