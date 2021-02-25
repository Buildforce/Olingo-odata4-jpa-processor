/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm;

/**
 * An {@link EdmElement} can either be an {@link EdmNavigationProperty}, an {@link EdmProperty} or an
 * {@link EdmParameter}.
 */
public interface EdmElement extends EdmNamed, EdmTyped {
  // No additional methods needed for now.
}
