package nl.buildforce.sequoia.metadata.core.edm.mapper.annotation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlEnumMember;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Member extends CsdlEnumMember {

  @Override
  @JacksonXmlProperty(localName = "Name", isAttribute = true)
  public CsdlEnumMember setName(final String name) {
    return super.setName(name);
  }

  @Override
  @JacksonXmlProperty(localName = "Value", isAttribute = true)
  public CsdlEnumMember setValue(final String value) {
    return super.setValue(value);
  }

}