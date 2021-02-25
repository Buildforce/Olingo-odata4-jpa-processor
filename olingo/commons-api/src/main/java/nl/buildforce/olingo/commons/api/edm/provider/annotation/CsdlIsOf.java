/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.provider.annotation;

import java.util.ArrayList;
import java.util.List;

import nl.buildforce.olingo.commons.api.edm.provider.CsdlAnnotatable;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlAnnotation;

/**
 * The edm:IsOf expression evaluates a child expression and returns a Boolean value indicating whether
 * the child expression returns the specified type
 */
public class CsdlIsOf extends CsdlDynamicExpression implements CsdlAnnotatable {

  private String type;
  private Integer maxLength;
  private Integer precision;
  private Integer scale;
  // private SRID srid;
  private CsdlExpression value;
  private List<CsdlAnnotation> annotations = new ArrayList<>();

  @Override
  public List<CsdlAnnotation> getAnnotations() {
    return annotations;
  }
  
  public CsdlIsOf setAnnotations(List<CsdlAnnotation> annotations) {
    this.annotations = annotations;
    return this;
  }
  /**
   * The type which is checked again the child expression
   * @return EdmType type
   */
  public String getType() {
    return type;
  }

  public CsdlIsOf setType(String type) {
    this.type = type;
    return this;
  }

  /**
   * Facet MaxLength
   * @return fact MaxLength
   */
  public Integer getMaxLength() {
    return maxLength;
  }

  public CsdlIsOf setMaxLength(Integer maxLength) {
    this.maxLength = maxLength;
    return this;
  }

  /**
   * Facet Precision
   * @return fact Precision
   */
  public Integer getPrecision() {
    return precision;
  }

  public CsdlIsOf setPrecision(Integer precision) {
    this.precision = precision;
return this;
  }

  /**
   * Facet Scale
   * @return facet Scale
   */
  public Integer getScale() {
    return scale;
  }

  public CsdlIsOf setScale(Integer scale) {
    this.scale = scale;
    return this;
  }

  /*
    Facet SRID
    @return facet SRID
   *
  public SRID getSrid() {
    return srid;
  }

  public CsdlIsOf setSrid(final SRID srid) {
    this.srid = srid;
    return this;
  }*/

  /**
   * Returns the child expression
   * @return Returns the child expression
   */
  public CsdlExpression getValue() {
    return value;
  }

  public CsdlIsOf setValue(CsdlExpression value) {
    this.value = value;
    return this;
  }

  @Override
  public boolean equals (Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof CsdlIsOf)) {
      return false;
    }
    CsdlIsOf csdlIsOf = (CsdlIsOf) obj;
    return (getType() == null ? csdlIsOf.getType() == null :
            getType().equals(csdlIsOf.getType()))
        && (getMaxLength() == null ? csdlIsOf.getMaxLength() == null :
            getMaxLength().equals(csdlIsOf.getMaxLength()))
        && (getPrecision() == null ? csdlIsOf.getPrecision() == null :
            getPrecision().equals(csdlIsOf.getPrecision()))
        && (getScale() == null ? csdlIsOf.getScale() == null :
            getScale().equals(csdlIsOf.getScale()))
        /*&& (this.getSrid() == null ? csdlIsOf.getSrid() == null :
          this.getSrid().equals(csdlIsOf.getSrid()))*/
        && (getValue() == null ? csdlIsOf.getValue() == null :
            getValue().equals(csdlIsOf.getValue()))
        && (getAnnotations() == null ? csdlIsOf.getAnnotations() == null :
            checkAnnotations(csdlIsOf.getAnnotations()));
  }
  
  private boolean checkAnnotations(List<CsdlAnnotation> csdlIsOfannot) {
    if (csdlIsOfannot == null) {
      return false;
    }
    if (getAnnotations().size() == csdlIsOfannot.size()) {
      for (int i = 0; i < getAnnotations().size(); i++) {
        if (!getAnnotations().get(i).equals(csdlIsOfannot.get(i))) {
          return false;
        }
      }
    } else {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((type == null) ? 0 : type.hashCode());
    result = prime * result + ((maxLength == null) ? 0 : maxLength.hashCode());
    result = prime * result + ((precision == null) ? 0 : precision.hashCode());
    result = prime * result + ((scale == null) ? 0 : scale.hashCode());
    /*result = prime * result + ((srid == null) ? 0 : srid.hashCode());*/
    result = prime * result + ((value == null) ? 0 : value.hashCode());
    result = prime * result + ((annotations == null) ? 0 : annotations.hashCode());
    return result;
  }
}