/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.provider;

import java.util.List;

/**
 * Super type of all annotatable CSDL items
 */
public interface CsdlAnnotatable {

  /**
   * Returns a list of annotations
   * @return list of annotations
   */
  List<CsdlAnnotation> getAnnotations();
}
