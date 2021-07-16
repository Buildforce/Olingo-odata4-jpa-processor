package nl.buildforce.sequoia.processor.core.query;

import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import nl.buildforce.olingo.commons.api.ex.ODataException;
import nl.buildforce.olingo.commons.api.http.HttpStatusCode;
import nl.buildforce.sequoia.processor.core.testmodel.DataSourceHelper;
import nl.buildforce.sequoia.processor.core.util.IntegrationTestHelper;
import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.eclipse.persistence.config.EntityManagerProperties.NON_JTA_DATASOURCE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestDitumFunction {
  protected static final String PUNIT_NAME = "nl.buildforce.sequoia";
  protected static EntityManagerFactory emf;
  protected static DataSource ds = DataSourceHelper.createDataSource(DataSourceHelper.DB_HSQLDB);

  @BeforeEach
  public void setup() throws ODataException {

    Map<String, Object> properties = new HashMap<>();
    properties.put(NON_JTA_DATASOURCE, ds);
    emf = Persistence.createEntityManagerFactory(PUNIT_NAME, properties);

  }

  @Test
  public void testDitumFunction() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(emf, ds, "DitumFunction(A=10,B=50)",
            "nl.buildforce.sequoia.processor.core.testobjects");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());
    ObjectNode r = helper.getValue();
    assertNotNull(r);
    assertNotNull(r.get("value"));
    assertEquals(60, r.get("value").asInt());
  }

  @Test
  public void testGetUTCDateTime() throws IOException, ODataException {

    String uTCDateTime = LocalDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm"));

    IntegrationTestHelper helper = new IntegrationTestHelper(emf, ds, "GetUTCDateTime(ICCID='DUMMY123',SerialNr='SERIAL123')",
            "nl.buildforce.sequoia.processor.core.testobjects");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());
    ObjectNode r = helper.getValue();
    assertNotNull(r);
    assertNotNull(r.get("value"));
    assertEquals(uTCDateTime, r.get("value").asText());
  }

  @Test
  public void testCardValidation() throws IOException, ODataException {

    JSONObject jsonpObject = new JSONObject();
    int evaluationCode = 1;
    String expirationDate = new SimpleDateFormat("YYYY-MM-dd").format(DateUtils.addYears(new Date(),1));
    String encryptedPassword = "PW";
    String name = "John Doe";
    jsonpObject.put("evaluationCode",evaluationCode);
    jsonpObject.put("expirationDate",expirationDate);
    jsonpObject.put("encryptedPassword",encryptedPassword);
    jsonpObject.put("name",name);

    IntegrationTestHelper helper = new IntegrationTestHelper(emf, ds, "CardValidation(CardID='CARD123',ICCID='DUMMY123',SerialNr='SERIAL123')",
            "nl.buildforce.sequoia.processor.core.testobjects");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());
    ObjectNode r = helper.getValue();
    assertNotNull(r);
    assertNotNull(r.get("value"));
    assertEquals(jsonpObject.toString(), r.get("value").asText());
  }

  @Test
  public void testCardValidationWCredintentials() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(emf, ds, "CardValidationWCredintentials(ICCID='DUMMY123',SerialNr='SERIAL123')",
            "nl.buildforce.sequoia.processor.core.testobjects");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());
    ObjectNode r = helper.getValue();
    assertNotNull(r);
    assertNotNull(r.get("value"));
    assertEquals("qwtrruyiuyoiu5467tre232311@#87", r.get("value").asText());
  }

  @Test
  public void testWorkedHoursDeclaration() throws IOException, ODataException {

    String expirationDate = new SimpleDateFormat("YYYY-MM-dd").format(DateUtils.addYears(new Date(),1));

    IntegrationTestHelper helper = new IntegrationTestHelper(emf, ds, "WorkedHoursDeclaration(CardID='CARD123',ICCID='DUMMY123',SerialNr='SERIAL123',WorkedMinutes=120)",
            "nl.buildforce.sequoia.processor.core.testobjects");
    helper.assertStatus(HttpStatusCode.OK.getStatusCode());
    ObjectNode r = helper.getValue();
    assertNotNull(r);
    assertNotNull(r.get("value"));
    assertEquals(expirationDate, r.get("value").asText());
  }

}
