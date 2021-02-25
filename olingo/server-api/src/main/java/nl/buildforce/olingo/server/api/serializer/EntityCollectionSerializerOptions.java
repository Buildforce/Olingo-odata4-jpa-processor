/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.serializer;

import nl.buildforce.olingo.commons.api.data.ContextURL;
import nl.buildforce.olingo.server.api.ODataContentWriteErrorCallback;
import nl.buildforce.olingo.server.api.uri.queryoption.CountOption;
import nl.buildforce.olingo.server.api.uri.queryoption.ExpandOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SelectOption;

/** Options for the OData serializer. */
public class EntityCollectionSerializerOptions {

  private ContextURL contextURL;
  private CountOption count;
  private ExpandOption expand;
  private SelectOption select;
  private boolean writeOnlyReferences;
  private String id;
  private ODataContentWriteErrorCallback odataContentWriteErrorCallback;
  private String xml10InvalidCharReplacement;
  private boolean isFullRepresentation;

  /** Gets the {@link ContextURL}. */
  public ContextURL getContextURL() {
    return contextURL;
  }

  /** Gets the $count system query option. */
  public CountOption getCount() {
    return count;
  }

  /** Gets the $expand system query option. */
  public ExpandOption getExpand() {
    return expand;
  }

  /** Gets the $select system query option. */
  public SelectOption getSelect() {
    return select;
  }

  /** only writes the references of the entities */
  public boolean getWriteOnlyReferences() {
    return writeOnlyReferences;
  }

  /** Gets the id of the entity collection */
  public String getId() {
    return id;
  }

  /**
   * Gets the callback which is used in case of an exception during
   * write of the content (in case the content will be written/streamed
   * in the future)
   * @return callback which is used in case of an exception during
   * write of the content
   *
   */
  public ODataContentWriteErrorCallback getODataContentWriteErrorCallback() {
    return odataContentWriteErrorCallback;
  }
  /** Gets the replacement string for unicode characters, that is not allowed in XML 1.0 */
  public String xml10InvalidCharReplacement() {
    return xml10InvalidCharReplacement;
  }  
  
  /** Inline entries will not have @delta if representation is full **/ 
  public boolean isFullRepresentation() {
    return isFullRepresentation;
  }

  /** Initializes the options builder. */
  public static Builder with() {
    return new Builder();
  }

  /** Builder of OData serializer options. */
  public static final class Builder {

    private final EntityCollectionSerializerOptions options;

    private Builder() {
      options = new EntityCollectionSerializerOptions();
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

    /** Sets to serialize only references */
    public Builder writeOnlyReferences(boolean ref) {
      options.writeOnlyReferences = ref;
      return this;
    }

    /** Sets id of the collection */
    public Builder id(String id) {
      options.id = id;
      return this;
    }

    /**
     * Set the callback which is used in case of an exception during
     * write of the content.
     *
     * @param ODataContentWriteErrorCallback the callback
     * @return the builder
     */
    public Builder writeContentErrorCallback(ODataContentWriteErrorCallback ODataContentWriteErrorCallback) {
      options.odataContentWriteErrorCallback = ODataContentWriteErrorCallback;
      return this;
    }

    /** set the replacement String for xml 1.0 unicode controlled characters that are not allowed */
    public Builder xml10InvalidCharReplacement(String replacement) {
      options.xml10InvalidCharReplacement = replacement;
      return this;
    } 
    
    /** sets isFullRepresentation to represent inline entries**/
    public Builder isFullRepresentation(boolean isFullRepresentation) {
      options.isFullRepresentation = isFullRepresentation;
      return this;
    }
    
    /** Builds the OData serializer options. */
    public EntityCollectionSerializerOptions build() {
      return options;
    }
  }
}
