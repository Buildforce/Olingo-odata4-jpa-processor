package nl.buildforce.sequoia.processor.core.testobjects;

import jakarta.persistence.EntityManager;
import nl.buildforce.sequoia.metadata.core.edm.annotation.EdmFunction;
import nl.buildforce.sequoia.metadata.core.edm.annotation.EdmFunction.ReturnType;
import nl.buildforce.sequoia.metadata.core.edm.annotation.EdmParameter;
import nl.buildforce.sequoia.metadata.core.edm.mapper.extension.ODataFunction;
import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DitumFunction implements ODataFunction {
  public static EntityManager em;

  public DitumFunction(EntityManager em) {
    DitumFunction.em = em;
  }

  @EdmFunction(name = "DitumFunction", returnType = @ReturnType)
  public Integer calculateSum(@EdmParameter(name = "A") short a, @EdmParameter(name = "B") short b) {
    if (a == 0 || b == 0)
      return null;
    return a + b;
  }

  @EdmFunction(name = "GetUTCDateTime", returnType = @ReturnType)
  public String getUTCDateTime(@EdmParameter(name = "ICCID") String iCCID, @EdmParameter(name = "SerialNr") String serialNr) {
    if (iCCID == null || serialNr == null)
      return "";
    String uTCDateTime = LocalDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm"));
    return uTCDateTime;
  }

  @EdmFunction(name = "GetUTCDateTimeReturnDateTime", returnType = @ReturnType)
  public LocalDateTime getUTCDateTimeReturnDateTime(@EdmParameter(name = "ICCID") String iCCID, @EdmParameter(name = "SerialNr") String serialNr) {
    if (iCCID == null || serialNr == null)
      return null;
    LocalDateTime localDateTime = LocalDateTime.now(ZoneOffset.UTC);
    return localDateTime;
  }

  @EdmFunction(name = "CardValidation", returnType = @ReturnType)
  public String cardValidation(@EdmParameter(name = "CardID") String cardID, @EdmParameter(name = "ICCID") String iCCID, @EdmParameter(name = "SerialNr") String serialNr) {
    if (cardID == null || iCCID == null || serialNr == null)
      return "";

    JSONObject jsonpObject = new JSONObject();
    int evaluationCode = 1;
    String expirationDate = new SimpleDateFormat("YYYY-MM-dd").format(DateUtils.addYears(new Date(),1));
    String encryptedPassword = "PW";
    String name = "John Doe";
    jsonpObject.put("evaluationCode",evaluationCode);
    jsonpObject.put("expirationDate",expirationDate);
    jsonpObject.put("encryptedPassword",encryptedPassword);
    jsonpObject.put("name",name);

    return jsonpObject.toString();
  }

  @EdmFunction(name = "CardValidationWCredintentials", returnType = @ReturnType)
  public String cardValidationWCredintentials(@EdmParameter(name = "ICCID") String iCCID, @EdmParameter(name = "SerialNr") String serialNr)  {
    if (iCCID == null || serialNr == null)
      return "";

    String token = "qwtrruyiuyoiu5467tre232311@#87";
    return token;
  }

  @EdmFunction(name = "CardValidationWCredintentialsReturnLong", returnType = @ReturnType)
  public Long cardValidationWCredintentialsReturnLong(@EdmParameter(name = "ICCID") String iCCID, @EdmParameter(name = "SerialNr") String serialNr)  {
    if (iCCID == null || serialNr == null)
      return null;

    Long token = Long.parseLong("12345677890");
    return token;
  }

  @EdmFunction(name = "WorkedHoursDeclaration", returnType = @ReturnType)
  public String workedHoursDeclaration(@EdmParameter(name = "CardID") String cardID, @EdmParameter(name = "ICCID") String iCCID, @EdmParameter(name = "SerialNr") String serialNr, @EdmParameter(name = "WorkedMinutes") short workedMinutes) {
    if (cardID == null || iCCID == null || serialNr == null  || workedMinutes == 0 )
      return "";

    return new SimpleDateFormat("YYYY-MM-dd").format(DateUtils.addYears(new Date(),1));
  }

  @EdmFunction(name = "WorkedHoursDeclarationReturnDate", returnType = @ReturnType)
  public Date workedHoursDeclarationReturnDate(@EdmParameter(name = "CardID") String cardID, @EdmParameter(name = "ICCID") String iCCID, @EdmParameter(name = "SerialNr") String serialNr, @EdmParameter(name = "WorkedMinutes") short workedMinutes) {
    if (cardID == null || iCCID == null || serialNr == null  || workedMinutes == 0 )
      return null;

    return DateUtils.addYears(new Date(),1);
  }

}
