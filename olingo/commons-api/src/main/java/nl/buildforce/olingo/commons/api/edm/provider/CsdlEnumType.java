/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.provider;

import java.util.ArrayList;
import java.util.List;

import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;

/**
 * The type Csdl enum type.
 */
public class CsdlEnumType extends CsdlAbstractEdmItem implements CsdlNamed, CsdlAnnotatable {

  private String name;

  private boolean isFlags;

  private FullQualifiedName underlyingType;

  private List<CsdlEnumMember> members = new ArrayList<>();

  private List<CsdlAnnotation> annotations = new ArrayList<>();

  @Override
  public String getName() {
    return name;
  }

  /**
   * Sets name.
   *
   * @param name the name
   * @return the name
   */
  public CsdlEnumType setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Is flags.
   *
   * @return the boolean
   */
  public boolean isFlags() {
    return isFlags;
  }

  /**
   * Sets flags.
   *
   * @param isFlags the is flags
   * @return the flags
   */
  public CsdlEnumType setFlags(boolean isFlags) {
    this.isFlags = isFlags;
    return this;
  }

  /**
   * Gets underlying type.
   *
   * @return the underlying type
   */
  public String getUnderlyingType() {
    if (underlyingType != null) {
      return underlyingType.getFullQualifiedNameAsString();
    }
    return null;
  }

  /**
   * Sets underlying type.
   *
   * @param underlyingType the underlying type
   * @return the underlying type
   */
  public CsdlEnumType setUnderlyingType(String underlyingType) {
    this.underlyingType = new FullQualifiedName(underlyingType);
    return this;
  }

  /**
   * Sets underlying type.
   *
   * @param underlyingType the underlying type
   * @return the underlying type
   */
  public CsdlEnumType setUnderlyingType(FullQualifiedName underlyingType) {
    this.underlyingType = underlyingType;
    return this;
  }

  /**
   * Gets members.
   *
   * @return the members
   */
  public List<CsdlEnumMember> getMembers() {
    return members;
  }

  /**
   * Gets member.
   *
   * @param name the name
   * @return the member
   */
  public CsdlEnumMember getMember(String name) {
    CsdlEnumMember result = null;
    if (getMembers() != null) {
      for (CsdlEnumMember member : getMembers()) {
        if (name.equals(member.getName())) {
          result = member;
        }
      }
    }
    return result;
  }

  /**
   * Gets member.
   *
   * @param value the value
   * @return the member
   */
  public CsdlEnumMember getMember(Integer value) {
    CsdlEnumMember result = null;
    if (getMembers() != null) {
      for (CsdlEnumMember member : getMembers()) {
        if (String.valueOf(value).equals(member.getValue())) {
          result = member;
        }
      }
    }
    return result;
  }

  /**
   * Sets members.
   *
   * @param members the members
   * @return the members
   */
  public CsdlEnumType setMembers(List<CsdlEnumMember> members) {
    this.members = members;
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
  public CsdlEnumType setAnnotations(List<CsdlAnnotation> annotations) {
    this.annotations = annotations;
    return this;
  }

}