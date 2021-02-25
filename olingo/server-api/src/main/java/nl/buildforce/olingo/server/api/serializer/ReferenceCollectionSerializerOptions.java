/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.serializer;

import nl.buildforce.olingo.commons.api.data.ContextURL;
import nl.buildforce.olingo.server.api.uri.queryoption.CountOption;

/** Options to pass as additional information to the reference-collection serializer. */
public final class ReferenceCollectionSerializerOptions {
  private ContextURL contextURL;
  private CountOption count;

  /** Gets the {@link ContextURL}. */
  public ContextURL getContextURL() {
    return contextURL;
  }

  /** Gets the $count system query option. */
  public CountOption getCount() {
    return count;
  }

  private ReferenceCollectionSerializerOptions() {}

  /** Initializes the options builder. */
  public static Builder with() {
    return new Builder();
  }

  /** Builder of OData serializer options. */
  public static final class Builder {
    private final ReferenceCollectionSerializerOptions options;

    public Builder() {
      options = new ReferenceCollectionSerializerOptions();
    }

    /** Sets the {@link ContextURL}. */
    public Builder contextURL(ContextURL contextURL) {
      options.contextURL = contextURL;
      return this;
    }

    /** Sets the $count system query option. */
    public Builder count(CountOption count) {
      options.count = count;
      return this;
    }

    /** Builds the OData serializer options. */
    public ReferenceCollectionSerializerOptions build() {
      return options;
    }
  }
}
