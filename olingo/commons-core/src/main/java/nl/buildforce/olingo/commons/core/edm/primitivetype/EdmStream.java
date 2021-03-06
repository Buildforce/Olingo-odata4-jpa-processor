/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm.primitivetype;

import java.net.URI;
import java.net.URISyntaxException;

import nl.buildforce.olingo.commons.api.data.Link;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeException;

/**
 * Implementation of the EDM primitive type Stream as URI.
 */
public final class EdmStream extends SingletonPrimitiveType {

  private static final EdmStream INSTANCE = new EdmStream();

  public static EdmStream getInstance() {
    return INSTANCE;
  }

  @Override
  public Class<?> getDefaultType() {
    return URI.class;
  }

  @Override
  public boolean validate(String value, Boolean isNullable, Integer maxLength,
                          Integer precision, Integer scale, Boolean isUnicode) {

    if (value == null) {
      return isNullable == null || isNullable;
    }

    try {
      new URI(value);
      return true;
    } catch (URISyntaxException e) {
      return false;
    }
  }

  @Override
  protected <T> T internalValueOfString(String value,
                                        Boolean isNullable, Integer maxLength, Integer precision,
                                        Integer scale, Boolean isUnicode, Class<T> returnType) throws EdmPrimitiveTypeException {

    try {
      URI stream = new URI(value);

    if (returnType.isAssignableFrom(URI.class)) {
      return returnType.cast(stream);
    } else if (returnType.isAssignableFrom(Link.class)) {
      Link link = new Link();
      link.setHref(value);
      return returnType.cast(link);
    } else {
      throw new EdmPrimitiveTypeException("The value type " + returnType + " is not supported.");
    }

  } catch (URISyntaxException e) {
    throw new EdmPrimitiveTypeException("The literal '" + value + "' has illegal content.", e);
  }


  }

  @Override
  protected <T> String internalValueToString(T value,
                                             Boolean isNullable, Integer maxLength, Integer precision,
                                             Integer scale, Boolean isUnicode) throws EdmPrimitiveTypeException {

    if (value instanceof URI) {
      return ((URI) value).toASCIIString();
    } else if (value instanceof Link) {
      return ((Link)value).getHref();
    } else {
      throw new EdmPrimitiveTypeException("The value type " + value.getClass() + " is not supported.");
    }
  }
}
