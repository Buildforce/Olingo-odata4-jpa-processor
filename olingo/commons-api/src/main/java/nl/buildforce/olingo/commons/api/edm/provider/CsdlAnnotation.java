/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.provider;

import java.util.ArrayList;
import java.util.List;

import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlExpression;

/**
 * Represents a CSDL annotation
 */
public class CsdlAnnotation extends CsdlAbstractEdmItem implements CsdlAnnotatable {

  private String term;

  private String qualifier;

  private List<CsdlAnnotation> annotations = new ArrayList<>();

  private CsdlExpression annotationExpression;

  /**
   * Returns the annotated expression
   * @return expression annotated expression
   */
  public CsdlExpression getExpression() {
    return annotationExpression;
  }

  /**
   * Sets the annotated expression
   * @param annotationExpression annotated expression
   */
  public CsdlAnnotation setExpression(CsdlExpression annotationExpression) {
    this.annotationExpression = annotationExpression;
    return this;
  }

  /**
   * Returns the annotated term
   * @return Term term
   */
  public String getTerm() {
    return term;
  }

  /**
   * Sets the annotated expression
   * @param term term
   * @return this instance
   */
  public CsdlAnnotation setTerm(String term) {
    this.term = term;
    return this;
  }

  /**
   * Returns the annotated qualifier
   * @return annotated qualifier
   */
  public String getQualifier() {
    return qualifier;
  }

  /**
   * Sets the annotated qualifier
   * @param qualifier annotated qualifier
   * @return this instance
   */
  public CsdlAnnotation setQualifier(String qualifier) {
    this.qualifier = qualifier;
    return this;
  }

  /**
   * Sets a list of annotations
   * @param annotations list of annotations
   * @return this instance
   */
  public CsdlAnnotation setAnnotations(List<CsdlAnnotation> annotations) {
    this.annotations = annotations;
    return this;
  }

  @Override
  public List<CsdlAnnotation> getAnnotations() {
    return annotations;
  }
  
  @Override
  public boolean equals (Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof CsdlAnnotation)) {
      return false;
    }
    CsdlAnnotation csdlAnnot = (CsdlAnnotation) obj;
    return (getTerm() == null ? csdlAnnot.getTerm() == null :
            getTerm().equals(csdlAnnot.getTerm()))
        && (getQualifier() == null ? csdlAnnot.getQualifier() == null :
            getQualifier().equals(csdlAnnot.getQualifier()))
        && (getExpression() == null ? csdlAnnot.getExpression() == null :
            getExpression().equals(csdlAnnot.getExpression()))
        && (getAnnotations() == null ? csdlAnnot.getAnnotations() == null :
          checkAnnotations(csdlAnnot.getAnnotations()));
  }
  
  private boolean checkAnnotations(List<CsdlAnnotation> csdlAnnots) {
    if (csdlAnnots == null) {
      return false;
    }
    if (getAnnotations().size() == csdlAnnots.size()) {
      for (int i = 0; i < getAnnotations().size(); i++) {
        if (!getAnnotations().get(i).equals(csdlAnnots.get(i))) {
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
    result = prime * result + ((term == null) ? 0 : term.hashCode());
    result = prime * result + ((qualifier == null) ? 0 : qualifier.hashCode());
    result = prime * result + ((annotationExpression == null) ? 0 : 
      annotationExpression.hashCode());
    result = prime * result + ((annotations == null) ? 0 : annotations.hashCode());
    return result;
  }

}