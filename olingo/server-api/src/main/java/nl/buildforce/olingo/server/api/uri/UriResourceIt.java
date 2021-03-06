/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri;

import nl.buildforce.olingo.commons.api.edm.EdmType;

/**
 * Class indicating the $it reference. $it may be used within expression to
 * refer to the last EDM object referenced in the resource path.
 */
public interface UriResourceIt extends UriResourcePartTyped {

// --Commented out by Inspection START (''21-03-06 11:58):
//  /**
//   * @return Type filter if $it refers to a collection
//   */
//  EdmType getTypeFilterOnCollection();
//  /**
//   * @return Type filter if $it refers to a single entry
//   */
//  EdmType getTypeFilterOnEntry();
// --Commented out by Inspection STOP (''21-03-06 11:58)

}