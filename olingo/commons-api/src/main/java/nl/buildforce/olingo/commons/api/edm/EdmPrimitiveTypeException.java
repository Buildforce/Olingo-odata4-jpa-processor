/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm;

import nl.buildforce.olingo.commons.api.ex.ODataException;

public class EdmPrimitiveTypeException extends ODataException {

  // private static final long serialVersionUID = -93578822384514620L;

  public EdmPrimitiveTypeException(String msg) {
    super(msg);
  }

  public EdmPrimitiveTypeException(String msg, Exception cause) {
    super(msg, cause);
  }

}