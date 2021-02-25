/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.serializer;

import nl.buildforce.olingo.commons.api.data.ContextURL;

/** Options for the EDM-assisted serializer. */
public class EdmAssistedSerializerOptions {

  private ContextURL contextURL;

  /** Gets the {@link ContextURL}. */
  public ContextURL getContextURL() {
    return contextURL;
  }

  /** Initializes the options builder. */
  public static Builder with() {
    return new Builder();
  }

  /** Builder of serializer options. */
  public static final class Builder {

    private final EdmAssistedSerializerOptions options;

    private Builder() {
      options = new EdmAssistedSerializerOptions();
    }

    /** Sets the {@link ContextURL}. */
    public Builder contextURL(ContextURL contextURL) {
      options.contextURL = contextURL;
      return this;
    }

    /** Builds the OData serializer options. */
    public EdmAssistedSerializerOptions build() {
      return options;
    }
  }
}
