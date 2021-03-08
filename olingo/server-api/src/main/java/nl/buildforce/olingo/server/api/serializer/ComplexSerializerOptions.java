/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.serializer;

import nl.buildforce.olingo.commons.api.data.ContextURL;
import nl.buildforce.olingo.server.api.uri.queryoption.ExpandOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SelectOption;

/** Options for the OData serializer. */
public class ComplexSerializerOptions {

  private ContextURL contextURL;
  private ExpandOption expand;
  private SelectOption select;
  private String xml10InvalidCharReplacement;

  /** Gets the {@link ContextURL}. */
  public ContextURL getContextURL() {
    return contextURL;
  }

  /** Gets the $expand system query option. */
  public ExpandOption getExpand() {
    return expand;
  }

  /** Gets the $select system query option. */
  public SelectOption getSelect() {
    return select;
  }

  /** Gets the replacement string for unicode characters, that is not allowed in XML 1.0 */
  public String xml10InvalidCharReplacement() {
    return xml10InvalidCharReplacement;
  }  

  private ComplexSerializerOptions() {}

  /** Initializes the options builder. */
  public static Builder with() {
    return new Builder();
  }

  /** Builder of OData serializer options. */
  public static final class Builder {

    private final ComplexSerializerOptions options;

    private Builder() {
      options = new ComplexSerializerOptions();
    }

    /** Sets the {@link ContextURL}. */
    public Builder contextURL(ContextURL contextURL) {
      options.contextURL = contextURL;
      return this;
    }

    /** Sets the $expand system query option. */
    public Builder expand(ExpandOption expand) {
      options.expand = expand;
      return this;
    }

    /** Sets the $select system query option. */
    public Builder select(SelectOption select) {
      options.select = select;
      return this;
    }
    
    /** set the replacement string for xml 1.0 unicode controlled characters that are not allowed */
    public Builder xml10InvalidCharReplacement(String replacement) {
      options.xml10InvalidCharReplacement = replacement;
      return this;
    }

    /** Builds the OData serializer options. */
    public ComplexSerializerOptions build() {
      return options;
    }
  }

}