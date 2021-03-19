/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edmx;

/**
 * POJO for Edmx Reference Include Annotation.
 */
public class EdmxReferenceIncludeAnnotation {
  private final String termNamespace;
  private String qualifier;
  private String targetNamespace;

  /**
   * Create include annotation with given termNamespace and empty qualifier and targetNamespace.
   *
   * @param termNamespace of include annotation
   */
  public EdmxReferenceIncludeAnnotation(String termNamespace) {
    this(termNamespace, null, null);
  }

  /**
   * Create include annotation with given termNamespace, qualifier and targetNamespace.
   *
   * @param termNamespace of include annotation
   * @param qualifier of include annotation
   * @param targetNamespace of include annotation
   */
  public EdmxReferenceIncludeAnnotation(String termNamespace, String qualifier, String targetNamespace) {
    this.termNamespace = termNamespace;
    this.qualifier = qualifier;
    this.targetNamespace = targetNamespace;
  }

  /**
   * @return TermNamespace of the include annotation
   */
  public String getTermNamespace() {
    return termNamespace;
  }

  /**
   * @return Qualifier if one defined; null otherwise
   */
  public String getQualifier() {
    return qualifier;
  }

  /**
   * @return targetNamespace if defined; null otherwise
   */
  public String getTargetNamespace() {
    return targetNamespace;
  }

// --Commented out by Inspection START (''21-03-11 19:40):
//  /**
//   * Set qualifier for this include annotation.
//   *
//   * @param qualifier for include annotation
//   * @return this include annotation
//   */
//  public EdmxReferenceIncludeAnnotation setQualifier(String qualifier) {
//    this.qualifier = qualifier;
//    return this;
//  }
//
//  /**
//   * Set target namespace for this include annotation.
//   *
//   * @param targetNamespace for include annotation
//   * @return this include annotation
//   */
//  public EdmxReferenceIncludeAnnotation setTargetNamespace(String targetNamespace) {
//    this.targetNamespace = targetNamespace;
//    return this;
//  }
// --Commented out by Inspection STOP (''21-03-11 19:40)

}