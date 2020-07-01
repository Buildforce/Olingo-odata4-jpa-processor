package nl.buildforce.sequoia.processor.core.api;

import nl.buildforce.sequoia.metadata.api.JPAEdmMetadataPostProcessor;
import nl.buildforce.sequoia.metadata.api.JPAEdmProvider;
import nl.buildforce.sequoia.metadata.core.edm.mapper.extension.IntermediateEntityTypeAccess;
import nl.buildforce.sequoia.metadata.core.edm.mapper.extension.IntermediateNavigationPropertyAccess;
import nl.buildforce.sequoia.metadata.core.edm.mapper.extension.IntermediatePropertyAccess;
import nl.buildforce.sequoia.metadata.core.edm.mapper.extension.IntermediateReferenceList;
import nl.buildforce.sequoia.metadata.core.edm.mapper.impl.JPADefaultEdmNameBuilder;
import nl.buildforce.sequoia.processor.core.testmodel.DataSourceHelper;
import nl.buildforce.olingo.commons.api.ex.ODataException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@Disabled
public class TestJPAODataServiceContextBuilder {
  private static final String PUNIT_NAME = "nl.buildforce.sequoia";
  private static final String[] enumPackages = {"nl.buildforce.sequoia.processor.core.testmodel"};

  private JPAODataCRUDContextAccess cut;
  private static DataSource ds;

  @BeforeAll
  public static void classSetup() {
    ds = DataSourceHelper.createDataSource(DataSourceHelper.DB_DERBY);
  }

/*
  @Test
  public void checkBuilderAvailable() {
    assertNotNull(JPAODataServiceContext.with());
  }
*/
  @Test
  public void checkSetDatasourceAndPUnit() throws ODataException {
    cut = new JPAODataServiceContext(PUNIT_NAME, ds);

    assertNotNull(cut.getEdmProvider());
  }

  @Test
  public void checkEmptyArrayOnNoPackagesProvided() throws ODataException {
    cut = new JPAODataServiceContext(PUNIT_NAME, ds);

    assertNotNull(cut.getPackageName());
    assertEquals(0, cut.getPackageName().length);
  }

  @Test
  public void checkArrayOnProvidedPackages() throws ODataException {
    cut = new JPAODataServiceContext(PUNIT_NAME, ds, "nl.buildforce.olingo.jpa.bl", "nl.buildforce.sequoia.processor.core.testmodel");

    assertNotNull(cut.getPackageName());
    assertEquals(2, cut.getPackageName().length);
  }
/*
  @Test
  public void checkReturnsProvidedPagingProvider() throws ODataException {
    final JPAODataPagingProvider provider = new JPAExamplePagingProvider(Collections.emptyMap());
    cut = JPAODataServiceContext.with()
            .setDataSource(ds)
            .setPUnit(PUNIT_NAME)
            .setPagingProvider(provider)
            .build();

    assertNotNull(cut.getPagingProvider());
    assertEquals(provider, cut.getPagingProvider());
  }
*/
  @Test
  public void checkEmptyListOnNoReferencesProvided() throws ODataException {

    cut = new JPAODataServiceContext(PUNIT_NAME, ds);

    assertNotNull(cut.getReferences());
    assertTrue(cut.getReferences().isEmpty());
  }
/*
  @Test
  public void checkReturnsReferencesProvider() throws ODataException, URISyntaxException {

    final List<EdmxReference> references = new ArrayList<>(1);
    references.add(new EdmxReference(new URI("http://exapmle.com/odata4/v1")));
    cut = JPAODataServiceContext.with()
            .setDataSource(ds)
            .setPUnit(PUNIT_NAME)
            .setReferences(references)
            .build();

    assertEquals(1, cut.getReferences().size());
  }

  @Test
  public void checkReturnsOperation() throws ODataException {

    final JPAODataDatabaseOperations operations = new JPADefaultDatabaseProcessor();
    cut = JPAODataServiceContext.with()
            .setDataSource(ds)
            .setPUnit(PUNIT_NAME)
            .setOperationConverter(operations)
            .build();

    assertNotNull(cut.getOperationConverter());
    assertEquals(operations, cut.getOperationConverter());
  }

  @Test
  public void checkReturnsDatabaseProcessor() throws ODataException {

    final JPAODataDatabaseProcessor processor = new JPADefaultDatabaseProcessor();
    cut = JPAODataServiceContext.with()
            .setDataSource(ds)
            .setPUnit(PUNIT_NAME)
            .setDatabaseProcessor(processor)
            .build();

    assertNotNull(cut.getDatabaseProcessor());
    assertEquals(processor, cut.getDatabaseProcessor());
  }

  @Test
  public void checkJPAEdmContainsPostprocessor() throws ODataException {

    final JPAEdmMetadataPostProcessor processor = new TestEdmPostProcessor();
    cut = JPAODataServiceContext.with()
            .setDataSource(ds)
            .setPUnit(PUNIT_NAME)
            .setTypePackage(enumPackages)
            .setMetadataPostProcessor(processor)
            .build();

    assertNotNull(cut.getEdmProvider());
    assertNotNull(cut.getEdmProvider().getEntityType(new FullQualifiedName(PUNIT_NAME, "Test")));
  }

  @Test
  public void checkReturnsErrorProcessor() throws ODataException {
    final ErrorProcessor processor = new JPADefaultErrorProcessor();
    cut = JPAODataServiceContext.with()
            .setDataSource(ds)
            .setPUnit(PUNIT_NAME)
            .setErrorProcessor(processor)
            .build();

    assertNotNull(cut.getErrorProcessor());
    assertEquals(processor, cut.getErrorProcessor());
  }
*/
  @Test
  public void checkThrowsExceptionOnDBConnectionProblem() throws SQLException {
    final DataSource dsSpy = spy(ds);
    when(dsSpy.getConnection()).thenThrow(SQLException.class);
    assertThrows(ODataException.class, () -> new JPAODataServiceContext(PUNIT_NAME, ds));

  }

  @Test
  public void checkJPAEdmContainsDefaultNameBuilder() throws ODataException {

    cut = new JPAODataServiceContext(PUNIT_NAME, ds, enumPackages);
    final JPAEdmProvider act = cut.getEdmProvider();
    assertNotNull(act);
    assertNotNull(act.getEdmNameBuilder());
    assertTrue(act.getEdmNameBuilder() instanceof JPADefaultEdmNameBuilder);
  }
/*
  @Test
  public void checkJPAEdmContainsCustomNameBuilder() throws ODataJPAException {

    final JPAEdmNameBuilder nameBuilder = new JPADefaultEdmNameBuilder("Bƒ");
    // when(nameBuilder.getNamespace()).thenReturn("unit.test");
    cut = JPAODataServiceContext.with()
            .setDataSource(ds)
            .setPUnit(PUNIT_NAME)
            .setTypePackage(enumPackages)
            .setEdmNameBuilder(nameBuilder)
            .build();
    final JPAEdmProvider act = cut.getEdmProvider();
    assertNotNull(act);
    assertNotNull(act.getEdmNameBuilder());
    assertEquals(nameBuilder.getNamespace(), act.getEdmNameBuilder().getNamespace());
  }

  @Test
  public void checkReturnsMappingPath() throws ODataException {
    final String exp = "test/v1";

    cut = JPAODataServiceContext.with()
            .setDataSource(ds)
            .setPUnit(PUNIT_NAME)
            .setRequestMappingPath(exp)
            .build();

    assertEquals(exp, cut.getMappingPath());
  }
*/

  private static class TestEdmPostProcessor extends JPAEdmMetadataPostProcessor {

    @Override
    public void processNavigationProperty(final IntermediateNavigationPropertyAccess property,
                                          final String jpaManagedTypeClassName) {
      // Default shall do nothing
    }

    @Override
    public void processProperty(final IntermediatePropertyAccess property, final String jpaManagedTypeClassName) {
      // Default shall do nothing
    }

    @Override
    public void provideReferences(final IntermediateReferenceList references) {
      // Default shall do nothing
    }

    @Override
    public void processEntityType(final IntermediateEntityTypeAccess entity) {
      if (entity.getExternalName().equals("BusinessPartner"))
        entity.setExternalName("Test");
    }
  }
}