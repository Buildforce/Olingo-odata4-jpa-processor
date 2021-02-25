/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm.primitivetype;

import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveType;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeKind;

public final class EdmPrimitiveTypeFactory {

  /**
   * Returns an instance for the provided {@link EdmPrimitiveTypeKind} in the form of {@link EdmPrimitiveType}.
   *
   * @param kind EdmPrimitiveTypeKind
   * @return {@link EdmPrimitiveType} instance
   */
  public static EdmPrimitiveType getInstance(EdmPrimitiveTypeKind kind) {
    switch (kind) {
    case Binary:
      return EdmBinary.getInstance();
    case Boolean:
      return EdmBoolean.getInstance();
    case Byte:
      return EdmByte.getInstance();
    case SByte:
      return EdmSByte.getInstance();
    case Date:
      return EdmDate.getInstance();
    case DateTimeOffset:
      return EdmDateTimeOffset.getInstance();
    case TimeOfDay:
      return EdmTimeOfDay.getInstance();
    case Duration:
      return EdmDuration.getInstance();
    case Decimal:
      return EdmDecimal.getInstance();
    case Single:
      return EdmSingle.getInstance();
    case Double:
      return EdmDouble.getInstance();
    case Guid:
      return EdmGuid.getInstance();
    case Int16:
      return EdmInt16.getInstance();
    case Int32:
      return EdmInt32.getInstance();
    case Int64:
      return EdmInt64.getInstance();
    case String:
      return EdmString.getInstance();
    case Stream:
      return EdmStream.getInstance();

    /*case Geography:
      return EdmGeography.getInstance();
    case GeographyPoint:
      return EdmGeographyPoint.getInstance();
    case GeographyLineString:
      return EdmGeographyLineString.getInstance();
    case GeographyPolygon:
      return EdmGeographyPolygon.getInstance();
    case GeographyMultiPoint:
      return EdmGeographyMultiPoint.getInstance();
    case GeographyMultiLineString:
      return EdmGeographyMultiLineString.getInstance();
    case GeographyMultiPolygon:
      return EdmGeographyMultiPolygon.getInstance();
    case GeographyCollection:
      return EdmGeographyCollection.getInstance();
    case Geometry:
      return EdmGeometry.getInstance();
    case GeometryPoint:
      return EdmGeometryPoint.getInstance();
    case GeometryLineString:
      return EdmGeometryLineString.getInstance();
    case GeometryPolygon:
      return EdmGeometryPolygon.getInstance();
    case GeometryMultiPoint:
      return EdmGeometryMultiPoint.getInstance();
    case GeometryMultiLineString:
      return EdmGeometryMultiLineString.getInstance();
    case GeometryMultiPolygon:
      return EdmGeometryMultiPolygon.getInstance();
    case GeometryCollection:
      return EdmGeometryCollection.getInstance();*/

    default:
      throw new IllegalArgumentException("Wrong type: " + kind);
    }
  }

  private EdmPrimitiveTypeFactory() {
    // empty constructor for static utility class
  }

}