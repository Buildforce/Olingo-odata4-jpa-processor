/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.provider;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Csdl enum member.
 */
public class CsdlEnumMember extends CsdlAbstractEdmItem implements CsdlNamed, CsdlAnnotatable {

  private String name;

  private String value;

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
  public CsdlEnumMember setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Gets value.
   *
   * @return the value
   */
  public String getValue() {
    return value;
  }

  /**
   * Sets value.
   *
   * @param value the value
   * @return the value
   */
  public CsdlEnumMember setValue(String value) {
    this.value = value;
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
  public CsdlEnumMember setAnnotations(List<CsdlAnnotation> annotations) {
    this.annotations = annotations;
    return this;
  }

}