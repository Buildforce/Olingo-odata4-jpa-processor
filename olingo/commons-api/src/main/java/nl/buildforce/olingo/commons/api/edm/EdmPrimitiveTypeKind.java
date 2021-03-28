/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration of all primitive type kinds.
 */
public enum EdmPrimitiveTypeKind {
  Binary,
  Boolean,
  Byte,
  SByte,
  Date,
  DateTimeOffset,
  TimeOfDay,
  Duration,
  Decimal,
  Single,
  Double,
  Guid,
  Int16,
  Int32,
  Int64,
  String,
  Stream;

  private static final Map<String, EdmPrimitiveTypeKind> VALUES_BY_NAME;

  static {
    Map<String, EdmPrimitiveTypeKind> valuesByName = new HashMap<>();
    for (EdmPrimitiveTypeKind value : values()) {
      valuesByName.put(value.name(), value);
    }
    VALUES_BY_NAME = Collections.unmodifiableMap(valuesByName);
  }

  /**
   * Get a type kind by name.
   *
   * @param name The name.
   * @return The type kind or <tt>null</tt> if it does not exist.
   */
  public static EdmPrimitiveTypeKind getByName(String name) {
    return VALUES_BY_NAME.get(name);
  }

  /**
   * Returns the {@link FullQualifiedName} for this type kind.
   *
   * @return {@link FullQualifiedName}
   */
  public FullQualifiedName getFullQualifiedName() {
    return new FullQualifiedName(EdmPrimitiveType.EDM_NAMESPACE, toString());
  }

  /**
   * Gets the {@link EdmPrimitiveTypeKind} from a full-qualified type name.
   * @param fqn full-qualified type name
   * @return {@link EdmPrimitiveTypeKind} object
   */
  public static EdmPrimitiveTypeKind valueOfFQN(FullQualifiedName fqn) {
    if (EdmPrimitiveType.EDM_NAMESPACE.equals(fqn.getNamespace())) {
      return valueOf(fqn.getName());
    } else {
      throw new IllegalArgumentException(fqn + " does not look like an EDM primitive type.");
    }
  }

  /**
   * Gets the {@link EdmPrimitiveTypeKind} from a full type expression (like <code>Edm.Int32</code>).
   * @param fqn String containing a full-qualified type name
   * @return {@link EdmPrimitiveTypeKind} object
   */
  public static EdmPrimitiveTypeKind valueOfFQN(String fqn) {
    if (!fqn.startsWith(EdmPrimitiveType.EDM_NAMESPACE + ".")) {
      throw new IllegalArgumentException(fqn + " does not look like an Edm primitive type");
    }

    return valueOf(fqn.substring(4));
  }

}