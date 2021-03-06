/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.serializer.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.buildforce.olingo.server.api.OData;
import nl.buildforce.olingo.server.api.ODataServerError;
import nl.buildforce.olingo.server.api.serializer.ODataSerializer;
import org.apache.commons.io.IOUtils;
import nl.buildforce.olingo.commons.api.ex.ODataErrorDetail;
import nl.buildforce.olingo.commons.api.format.ContentType;
import nl.buildforce.olingo.server.api.serializer.SerializerException;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;

public class ServerErrorSerializerTest {

  final ODataSerializer ser;

  public ServerErrorSerializerTest() throws SerializerException {
    ser = OData.newInstance().createSerializer(ContentType.CT_JSON);
  }

  @Test
  public void basicODataErrorNoCode() throws Exception {
    ODataServerError error = new ODataServerError();
    error.setMessage("ErrorMessage");
    InputStream stream = ser.error(error).getContent();
    String jsonString = IOUtils.toString(stream);
    assertEquals("{\"error\":{\"code\":null,\"message\":\"ErrorMessage\"}}", jsonString);
  }

  @Test
  public void basicODataErrorWithCode() throws Exception {
    ODataServerError error = new ODataServerError();
    error.setCode("Code").setMessage("ErrorMessage");
    InputStream stream = ser.error(error).getContent();
    String jsonString = IOUtils.toString(stream);
    assertEquals("{\"error\":{\"code\":\"Code\",\"message\":\"ErrorMessage\"}}", jsonString);
  }

  @Test
  public void basicODataErrorWithCodeAndTarget() throws Exception {
    ODataServerError error = new ODataServerError();
    error.setCode("Code").setMessage("ErrorMessage").setTarget("Target");
    InputStream stream = ser.error(error).getContent();
    String jsonString = IOUtils.toString(stream);
    assertEquals("{\"error\":{\"code\":\"Code\",\"message\":\"ErrorMessage\",\"target\":\"Target\"}}", jsonString);
  }

  @Test(expected = SerializerException.class)
  public void nullErrorResultsInException() throws Exception {
    ser.error(null);
  }

  @Test
  public void emptyDetailsList() throws Exception {
    ODataServerError error = new ODataServerError();
    error.setMessage("ErrorMessage").setDetails(new ArrayList<ODataErrorDetail>());
    InputStream stream = ser.error(error).getContent();
    String jsonString = IOUtils.toString(stream);
    assertEquals("{\"error\":{\"code\":null,\"message\":\"ErrorMessage\",\"details\":[]}}", jsonString);
  }

  @Test
  public void nothingSetAtODataErrorObject() throws Exception {
    ODataServerError error = new ODataServerError();
    InputStream stream = ser.error(error).getContent();
    String jsonString = IOUtils.toString(stream);
    assertEquals("{\"error\":{\"code\":null,\"message\":null}}", jsonString);
  }

  @Test
  public void singleDetailNothingSet() throws Exception {
    List<ODataErrorDetail> details = new ArrayList<ODataErrorDetail>();
    details.add(new ODataErrorDetail());
    ODataServerError error = new ODataServerError().setDetails(details);
    InputStream stream = ser.error(error).getContent();
    String jsonString = IOUtils.toString(stream);
    assertEquals("{\"error\":{\"code\":null,\"message\":null,\"details\":[{\"code\":null,\"message\":null}]}}",
        jsonString);
  }

  @Test
  public void verifiedWithJacksonParser() throws Exception {
    ODataServerError error =
        new ODataServerError().setCode("Code").setMessage("Message").setTarget("Target")
        .setDetails(Collections.singletonList(
            new ODataErrorDetail().setCode("detailCode").setMessage("detailMessage").setTarget("detailTarget")));
    InputStream stream = ser.error(error).getContent();
    JsonNode tree = new ObjectMapper().readTree(stream);
    assertNotNull(tree);
    tree = tree.get("error");
    assertNotNull(tree);
    assertEquals("Code", tree.get("code").textValue());
    assertEquals("Message", tree.get("message").textValue());
    assertEquals("Target", tree.get("target").textValue());

    tree = tree.get("details");
    assertNotNull(tree);
    assertEquals(JsonNodeType.ARRAY, tree.getNodeType());

    tree = tree.get(0);
    assertNotNull(tree);
    assertEquals("detailCode", tree.get("code").textValue());
    assertEquals("detailMessage", tree.get("message").textValue());
    assertEquals("detailTarget", tree.get("target").textValue());
  }
  
  @Test
  public void testErrorObjectWithAdditionalProperties() throws Exception {
	  Map<String, Object> innerError = new HashMap<>();
	  List<Map<String, Object>> list = new ArrayList<>();
	  Map<String, Object> map = new HashMap<>();
	  map.put("targetDetail", "targetDetail");
	  map.put("@Common.numericSeverity", 4);
	  list.add(map);
	  innerError.put("@Common.additionalTargets", list);
	  innerError.put("@Common.longtextUrl", "url");
    ODataServerError error =
        new ODataServerError().setCode("Code").setMessage("Message").setTarget("Target")
        .setAdditionalProperties(innerError)
        .setDetails(Collections.singletonList(
            new ODataErrorDetail().setCode("detailCode").setMessage("detailMessage")
            .setTarget("detailTarget").setAdditionalProperties(innerError)));
    InputStream stream = ser.error(error).getContent();
    JsonNode tree = new ObjectMapper().readTree(stream);
    assertNotNull(tree);
    tree = tree.get("error");
    assertNotNull(tree);
    assertEquals("Code", tree.get("code").textValue());
    assertEquals("Message", tree.get("message").textValue());
    assertEquals("Target", tree.get("target").textValue());
    assertEquals(1, tree.get("@Common.additionalTargets").size());
    assertEquals("targetDetail", tree.get("@Common.additionalTargets").get(0).get("targetDetail").textValue());
    assertEquals(4, tree.get("@Common.additionalTargets").get(0).get("@Common.numericSeverity").asInt());
    assertEquals("url", tree.get("@Common.longtextUrl").textValue());

    tree = tree.get("details");
    assertNotNull(tree);
    assertEquals(JsonNodeType.ARRAY, tree.getNodeType());

    tree = tree.get(0);
    assertNotNull(tree);
    assertEquals("detailCode", tree.get("code").textValue());
    assertEquals("detailMessage", tree.get("message").textValue());
    assertEquals("detailTarget", tree.get("target").textValue());
    assertEquals(1, tree.get("@Common.additionalTargets").size());
    assertEquals("targetDetail", tree.get("@Common.additionalTargets").get(0).get("targetDetail").textValue());
    assertEquals("url", tree.get("@Common.longtextUrl").textValue());
  }

}