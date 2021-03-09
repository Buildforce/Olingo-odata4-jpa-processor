/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.serializer;

import nl.buildforce.olingo.commons.api.data.ContextURL;

/**
 * Options to pass as additional information to the reference serializer.
 */
public final class ReferenceSerializerOptions {
  private ContextURL contextURL;

  /** Gets the {@link ContextURL}. */
  public ContextURL getContextURL() {
    return contextURL;
  }

  private ReferenceSerializerOptions() {}

  /** Initializes the options builder. */
  public static Builder with() {
    return new Builder();
  }

  /** Builder of OData serializer options. */
  public static final class Builder {
    private final ReferenceSerializerOptions options;

    private Builder() {
      options = new ReferenceSerializerOptions();
    }

    /** Sets the {@link ContextURL}. */
    public Builder contextURL(ContextURL contextURL) {
      options.contextURL = contextURL;
      return this;
    }

    /** Builds the OData serializer options. */
    public ReferenceSerializerOptions build() {
      return options;
    }
  }

}