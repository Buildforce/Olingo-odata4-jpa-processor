/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.serializer.utils;

import nl.buildforce.olingo.commons.api.format.ContentType;

public class ContentTypeHelper {

  public static boolean isODataMetadataNone(ContentType contentType) {
    return contentType.isCompatible(ContentType.APPLICATION_JSON)
        && ContentType.VALUE_ODATA_METADATA_NONE.equalsIgnoreCase(
            contentType.getParameter(ContentType.PARAMETER_ODATA_METADATA));
  }

  public static boolean isODataMetadataFull(ContentType contentType) {
    return contentType.isCompatible(ContentType.APPLICATION_JSON)
        && ContentType.VALUE_ODATA_METADATA_FULL.equalsIgnoreCase(
            contentType.getParameter(ContentType.PARAMETER_ODATA_METADATA));
  }

  public static boolean isODataIEEE754Compatible(ContentType contentType) {
    return Boolean.TRUE.toString().equalsIgnoreCase(
        contentType.getParameter(ContentType.PARAMETER_IEEE754_COMPATIBLE));
  }

}