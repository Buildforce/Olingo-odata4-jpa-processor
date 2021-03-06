package nl.buildforce.sequoia.metadata.core.edm.mapper.annotation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlReturnType;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReturnType extends CsdlReturnType {

  @Override
  @JacksonXmlProperty(localName = "Type")
  public CsdlReturnType setType(final String type) {
    Objects.requireNonNull(type, "Type is a required attribute of return type");
    if (type.startsWith("Collection")) {
      setCollection(true);
      return super.setType(new FullQualifiedName(type.split("[()]")[1]));
    }
    return super.setType(new FullQualifiedName(type));
  }

  @Override
  @JacksonXmlProperty(localName = "Nullable")
  public CsdlReturnType setNullable(final boolean nullable) {
    return super.setNullable(nullable);
  }

  @Override
  @JacksonXmlProperty(localName = "MaxLength", isAttribute = true)
  public CsdlReturnType setMaxLength(final Integer maxLength) {
    return super.setMaxLength(maxLength);
  }

  @Override
  @JacksonXmlProperty(localName = "Precision", isAttribute = true)
  public CsdlReturnType setPrecision(final Integer precision) {
    return super.setPrecision(precision);
  }

  @Override
  @JacksonXmlProperty(localName = "Scale", isAttribute = true)
  public CsdlReturnType setScale(final Integer scale) {
    return super.setScale(scale);
  }

}