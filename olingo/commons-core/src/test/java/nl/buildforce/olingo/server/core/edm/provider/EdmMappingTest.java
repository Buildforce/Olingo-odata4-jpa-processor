/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.edm.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Date;

import nl.buildforce.olingo.commons.api.edm.EdmEntitySet;
import nl.buildforce.olingo.commons.api.edm.EdmParameter;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import nl.buildforce.olingo.commons.api.edm.EdmProperty;
import nl.buildforce.olingo.commons.api.edm.EdmSingleton;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlEntitySet;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlMapping;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlParameter;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlProperty;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlSingleton;
import nl.buildforce.olingo.commons.core.edm.EdmEntitySetImpl;
import nl.buildforce.olingo.commons.core.edm.EdmParameterImpl;
import nl.buildforce.olingo.commons.core.edm.EdmPropertyImpl;
import nl.buildforce.olingo.commons.core.edm.EdmSingletonImpl;
import org.junit.Test;

public class EdmMappingTest {

  @Test
  public void initialMappingMustBeNull() {
    CsdlProperty property = new CsdlProperty().setType(EdmPrimitiveTypeKind.DateTimeOffset.getFullQualifiedName());
    EdmProperty edmProperty = new EdmPropertyImpl(null, property);
    assertNull(edmProperty.getMapping());

    CsdlParameter parameter = new CsdlParameter().setType(EdmPrimitiveTypeKind.DateTimeOffset.getFullQualifiedName());
    EdmParameter edmParameter = new EdmParameterImpl(null, parameter);
    assertNull(edmParameter.getMapping());

    CsdlEntitySet es = new CsdlEntitySet().setName("test");
    EdmEntitySet edmES = new EdmEntitySetImpl(null, null, es);
    assertNull(edmES.getMapping());

    CsdlSingleton si = new CsdlSingleton().setName("test");
    EdmSingleton edmSi = new EdmSingletonImpl(null, null, si);
    assertNull(edmSi.getMapping());
  }

  public void getInternalNameViaMapping() {
    CsdlMapping mapping = new CsdlMapping().setInternalName("internalName");

    CsdlProperty property =
        new CsdlProperty().setType(EdmPrimitiveTypeKind.DateTimeOffset.getFullQualifiedName()).setMapping(mapping);
    EdmProperty edmProperty = new EdmPropertyImpl(null, property);
    assertNotNull(edmProperty.getMapping());
    assertEquals("internalName", edmProperty.getMapping().getInternalName());

    CsdlParameter parameter =
        new CsdlParameter().setType(EdmPrimitiveTypeKind.DateTimeOffset.getFullQualifiedName()).setMapping(mapping);
    EdmParameter edmParameter = new EdmParameterImpl(null, parameter);
    assertNotNull(edmParameter.getMapping());
    assertEquals("internalName", edmParameter.getMapping().getInternalName());

    CsdlEntitySet es = new CsdlEntitySet().setName("test").setMapping(mapping);
    EdmEntitySet edmES = new EdmEntitySetImpl(null, null, es);
    assertNotNull(edmES.getMapping());
    assertEquals("internalName", edmES.getMapping().getInternalName());

    CsdlSingleton si = new CsdlSingleton().setName("test").setMapping(mapping);
    EdmSingleton edmSi = new EdmSingletonImpl(null, null, si);
    assertNotNull(edmSi.getMapping());
    assertEquals("internalName", edmSi.getMapping().getInternalName());
  }

  @Test
  public void getDataClassForPrimTypeViaMapping() {
    CsdlMapping mapping = new CsdlMapping().setMappedJavaClass(Date.class);
    CsdlProperty property = new CsdlProperty()
        .setType(EdmPrimitiveTypeKind.DateTimeOffset.getFullQualifiedName())
        .setMapping(mapping);
    EdmProperty edmProperty = new EdmPropertyImpl(null, property);

    assertNotNull(edmProperty.getMapping());
    assertEquals(Date.class, edmProperty.getMapping().getMappedJavaClass());

    CsdlParameter parameter = new CsdlParameter()
        .setType(EdmPrimitiveTypeKind.DateTimeOffset.getFullQualifiedName())
        .setMapping(mapping);
    EdmParameter edmParameter = new EdmParameterImpl(null, parameter);

    assertNotNull(edmParameter.getMapping());
    assertEquals(Date.class, edmParameter.getMapping().getMappedJavaClass());
  }
}
