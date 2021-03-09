/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.deserializer.batch;

import nl.buildforce.olingo.server.api.ODataRequest;
import nl.buildforce.olingo.server.api.deserializer.FixedFormatDeserializer;

/**
 * Options for the batch deserializer.
 * @see FixedFormatDeserializer #parseBatchRequest(java.io.InputStream,
 * String, BatchOptions)
 */
public final class BatchOptions {
  private boolean isStrict = true;
  private String rawBaseUri = "";
  private String rawServiceResolutionUri = "";

  private BatchOptions() {}

  /**
   * Returns if the batch parsing is strict.
   * Default is true.
   * @return true if parsing is strict
   */
  public boolean isStrict() {
    return isStrict;
  }

  /**
   * Gets raw base URI.
   * @see ODataRequest#getRawBaseUri()
   */
  public String getRawBaseUri() {
    return rawBaseUri;
  }

  /**
   * Gets raw service resolution URI.
   * @see ODataRequest#getRawServiceResolutionUri()
   */
  public String getRawServiceResolutionUri() {
    return rawServiceResolutionUri;
  }

  /**
   * Creates a new BatchOptions builder.
   * @return new BatchOptions builder instance
   */
  public static Builder with() {
    return new Builder();
  }

  /**
   * BatchOptions builder
   */
  public static class Builder {
    private final BatchOptions options;

    /** Initializes the options builder. */
    private Builder() {
      options = new BatchOptions();
    }

    /**
     * @see BatchOptions#isStrict()
     */
    public Builder isStrict(boolean isStrict) {
      options.isStrict = isStrict;
      return this;
    }

    /**
     * @see ODataRequest#getRawBaseUri()
     */
    public Builder rawBaseUri(String baseUri) {
      options.rawBaseUri = baseUri;
      return this;
    }

    /**
     * @see ODataRequest#getRawServiceResolutionUri()
     */
    public Builder rawServiceResolutionUri(String serviceResolutionUri) {
      options.rawServiceResolutionUri = serviceResolutionUri;
      return this;
    }

    /**
     * Creates a new BatchOptions instance.
     * @return new BatchOptions instance
     */
    public BatchOptions build() {
      return options;
    }
  }

}