package nl.buildforce.sequoia.metadata.core.edm.mapper.annotation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlComplexType;

import java.util.Arrays;

/**
 * Complex Type for vocabulary definitions<br>
 * Not supported: <code>NavigationProperty</code>, no use case known
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class ComplexType extends CsdlComplexType {

  @Override
  @JacksonXmlProperty(localName = "Name", isAttribute = true)
  public CsdlComplexType setName(final String name) {
    return super.setName(name);
  }

  @Override
  @JacksonXmlProperty(localName = "OpenType", isAttribute = true)
  public CsdlComplexType setOpenType(final boolean isOpenType) {
    return super.setOpenType(isOpenType);

  }

  @Override
  @JacksonXmlProperty(localName = "BaseType", isAttribute = true)
  public CsdlComplexType setBaseType(final String baseType) {
    return super.setBaseType(baseType);
  }

  @Override
  @JacksonXmlProperty(localName = "Abstract", isAttribute = true)
  public CsdlComplexType setAbstract(final boolean isAbstract) {
    return super.setAbstract(isAbstract);
  }

  @JacksonXmlProperty(localName = "Property")
  public void setProperties(Property[] properties) {
    this.properties.addAll(Arrays.asList(properties));
  }

}