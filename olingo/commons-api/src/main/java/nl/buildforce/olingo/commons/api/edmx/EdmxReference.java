/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edmx;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.buildforce.olingo.commons.api.edm.provider.CsdlAnnotatable;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlAnnotation;

/**
 * POJO for Edmx Reference.
 */
public class EdmxReference implements CsdlAnnotatable{

  private final URI uri;
  private final List<EdmxReferenceInclude> edmxIncludes;
  private final List<EdmxReferenceIncludeAnnotation> edmxIncludeAnnotations;
  private List<CsdlAnnotation> annotations = new ArrayList<>();
  /**
   * Create reference with given uri
   *
   * @param uri of reference
   */
  public EdmxReference(URI uri) {
    this.uri = uri;
    edmxIncludes = new ArrayList<>();
    edmxIncludeAnnotations = new ArrayList<>();
  }

  /**
   * Get URI for the reference
   * @return uri for the reference
   */
  public URI getUri() {
    return uri;
  }

  /**
   * edmx:Include elements that specify the schemas to include from the target document
   *
   * @return list of {@link EdmxReferenceInclude} in reference or null if none specified
   */
  public List<EdmxReferenceInclude> getIncludes() {
    return Collections.unmodifiableList(edmxIncludes);
  }

  /**
   * Add include element to current list.
   *
   * @param include to be added
   * @return this EdmxReference object
   */
  public EdmxReference addInclude(EdmxReferenceInclude include) {
    edmxIncludes.add(include);
    return this;
  }

  /**
   * edmx:IncludeAnnotations elements that specify the annotations to include from the target document.
   *
   * @return List of {@link EdmxReferenceIncludeAnnotation} or null if none specified
   */
  public List<EdmxReferenceIncludeAnnotation> getIncludeAnnotations() {
    return Collections.unmodifiableList(edmxIncludeAnnotations);
  }

  /**
   * Add include annotation element to current list.
   *
   * @param includeAnnotation to be added
   * @return this EdmxReference object
   */
  public EdmxReference addIncludeAnnotation(EdmxReferenceIncludeAnnotation includeAnnotation) {
    edmxIncludeAnnotations.add(includeAnnotation);
    return this;
  }
  
  @Override
  public List<CsdlAnnotation> getAnnotations() {
    return annotations;
  }

// --Commented out by Inspection START (''21-03-11 19:51):
//  /**
//   * Sets annotations.
//   *
//   * @param annotations the annotations
//   * @return the annotations
//   */
//  public EdmxReference setAnnotations(List<CsdlAnnotation> annotations) {
//    this.annotations = annotations;
//    return this;
//  }
// --Commented out by Inspection STOP (''21-03-11 19:51)

}