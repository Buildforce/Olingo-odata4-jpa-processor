package nl.buildforce.sequoia.processor.core.testobjects;

import com.fasterxml.jackson.databind.util.JSONPObject;
import jakarta.persistence.EntityManager;
import nl.buildforce.sequoia.metadata.core.edm.annotation.EdmFunction;
import nl.buildforce.sequoia.metadata.core.edm.annotation.EdmFunction.ReturnType;
import nl.buildforce.sequoia.metadata.core.edm.annotation.EdmParameter;
import nl.buildforce.sequoia.metadata.core.edm.mapper.extension.ODataFunction;
import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONObject;

import java.time.Instant;
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
    return String.valueOf(Instant.now());
  }

  @EdmFunction(name = "CardValidation", returnType = @ReturnType)
  public String cardValidation(@EdmParameter(name = "cardID") String cardID, @EdmParameter(name = "ICCID") String iCCID, @EdmParameter(name = "SerialNr") String serialNr) {
    if (cardID == null || iCCID == null || serialNr == null)
      return "";

    JSONObject jsonpObject = new JSONObject();
    int evaluationCode = 1;
    Date expirationDate = DateUtils.addYears(new Date(),1);
    String encryptedPassword = "PW";
    String name = "John Doe";
    jsonpObject.put("evaluationCode",evaluationCode);
    jsonpObject.put("expirationDate",expirationDate);
    jsonpObject.put("encryptedPassword",encryptedPassword);
    jsonpObject.put("name",name);

    return jsonpObject.toString();
  }

  @EdmFunction(name = "CardValidationWCredintentials ", returnType = @ReturnType)
  public String cardValidationWCredintentials(@EdmParameter(name = "ICCID") String iCCID, @EdmParameter(name = "SerialNr") String serialNr)  {
    if (iCCID == null || serialNr == null)
      return "";

    String token = "qwtrruyiuyoiu5467tre232311@#87";
    return token;
  }

  @EdmFunction(name = "WorkedHoursDeclaration", returnType = @ReturnType)
  public String workedHoursDeclaration(@EdmParameter(name = "cardID") String cardID, @EdmParameter(name = "ICCID") String iCCID, @EdmParameter(name = "SerialNr") String serialNr) {
    if (cardID == null || iCCID == null || serialNr == null)
      return "";

    Date expirationDate = DateUtils.addYears(new Date(),1);
    return String.valueOf(expirationDate);
  }

}
