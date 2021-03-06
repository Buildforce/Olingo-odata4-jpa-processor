/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.serializer;

import nl.buildforce.olingo.commons.api.data.ContextURL;

/** Options for the OData serializer. */
public final class PrimitiveSerializerOptions {

  private ContextURL contextURL;
  private Boolean isNullable;
  private Integer maxLength;
  private Integer precision;
  private Integer scale;
  private Boolean isUnicode;
  private String xml10InvalidCharReplacement;

  /** Gets the {@link ContextURL}. */
  public ContextURL getContextURL() {
    return contextURL;
  }

  /** Gets the nullable facet. */
  public Boolean isNullable() {
    return isNullable;
  }

  /** Gets the maxLength facet. */
  public Integer getMaxLength() {
    return maxLength;
  }

  /** Gets the precision facet. */
  public Integer getPrecision() {
    return precision;
  }

  /** Gets the scale facet. */
  public Integer getScale() {
    return scale;
  }

  /** Gets the unicode facet. */
  public Boolean isUnicode() {
    return isUnicode;
  }
  
  /** Gets the replacement string for unicode characters, that is not allowed in XML 1.0 */
  public String xml10InvalidCharReplacement() {
    return xml10InvalidCharReplacement;
  }  
  

  private PrimitiveSerializerOptions() {}

  /** Initializes the options builder. */
  public static Builder with() {
    return new Builder();
  }

  /** Builder of OData serializer options. */
  public static final class Builder {

    private final PrimitiveSerializerOptions options;

    private Builder() {
      options = new PrimitiveSerializerOptions();
    }

    /** Sets the {@link ContextURL}. */
    public Builder contextURL(ContextURL contextURL) {
      options.contextURL = contextURL;
      return this;
    }

// --Commented out by Inspection START (''21-03-09 22:42):
//    /** Sets the nullable facet. */
//    public Builder nullable(Boolean isNullable) {
//      options.isNullable = isNullable;
//      return this;
//    }
// --Commented out by Inspection STOP (''21-03-09 22:42)

// --Commented out by Inspection START (''21-03-09 22:42):
//// --Commented out by Inspection START (''21-03-09 22:42):
////    /** Sets the maxLength facet. */
// --Commented out by Inspection START (''21-03-09 22:42):
//////    public Builder maxLength(Integer maxLength) {
// --Commented out by Inspection START (''21-03-09 22:42):
////////      options.maxLength = maxLength;
////// --Commented out by Inspection STOP (''21-03-09 22:42)
//// --Commented out by Inspection STOP (''21-03-09 22:42)
////      return this;
// --Commented out by Inspection STOP (''21-03-09 22:42)
//    }
// --Commented out by Inspection STOP (''21-03-09 22:42)

    /** Sets the precision facet. */
/*
    public Builder precision(Integer precision) {
      options.precision = precision;
      return this;
    }
*/

// --Commented out by Inspection START (''21-03-09 22:42):
//    /** Sets the scale facet. */
//    public Builder scale(Integer scale) {
//      options.scale = scale;
//      return this;
//    }
//
//    /** Sets the unicode facet. */
//    public Builder unicode(Boolean isUnicode) {
//      options.isUnicode = isUnicode;
//      return this;
//    }
//
//// --Commented out by Inspection START (''21-03-09 22:42):
// --Commented out by Inspection STOP (''21-03-09 22:42)
//    /** Sets all facets from an EDM property. */
//    public Builder facetsFrom(EdmProperty property) {
//      options.isNullable = property.isNullable();
//      options.maxLength = property.getMaxLength();
//      options.precision = property.getPrecision();
//      options.scale = property.getScale();
//      options.isUnicode = property.isUnicode();
//      return this;
//    }
// --Commented out by Inspection STOP (''21-03-09 22:42)

/*
    */
/** set the replacement string for xml 1.0 unicode controlled characters that are not allowed *//*

    public Builder xml10InvalidCharReplacement(String replacement) {
      options.xml10InvalidCharReplacement = replacement;
      return this;
    } 
*/

    /** Builds the OData serializer options. */
    public PrimitiveSerializerOptions build() {
      return options;
    }
  }

}