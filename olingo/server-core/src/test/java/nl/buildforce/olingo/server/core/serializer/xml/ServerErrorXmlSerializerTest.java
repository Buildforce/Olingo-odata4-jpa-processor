/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.serializer.xml;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.util.Collections;

import nl.buildforce.olingo.server.api.OData;
import nl.buildforce.olingo.server.api.ODataServerError;
import nl.buildforce.olingo.server.api.serializer.ODataSerializer;
import org.apache.commons.io.IOUtils;
import nl.buildforce.olingo.commons.api.ex.ODataErrorDetail;
import nl.buildforce.olingo.commons.api.format.ContentType;
import nl.buildforce.olingo.server.api.serializer.SerializerException;
import org.junit.Test;

public class ServerErrorXmlSerializerTest {

  final ODataSerializer ser;

  public ServerErrorXmlSerializerTest() throws SerializerException {
    ser = OData.newInstance().createSerializer(ContentType.APPLICATION_XML);
  }

  @Test
  public void basicODataErrorWithCode() throws Exception {
    ODataServerError error = new ODataServerError();
    error.setCode("Code").setMessage("ErrorMessage");
    InputStream stream = ser.error(error).getContent();
    String jsonString = IOUtils.toString(stream);
    assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
        + "<error xmlns=\"http://docs.oasis-open.org/odata/ns/metadata\">"
        + "<code>Code</code>"
        + "<message>ErrorMessage</message>"
        + "</error>",
        jsonString);
  }

  @Test(expected = SerializerException.class)
  public void nullErrorResultsInException() throws Exception {
    ser.error(null);
  }

  @Test
  public void singleDetailNothingSet() throws Exception {
    ODataServerError error = new ODataServerError()
    .setCode("code")
    .setMessage("err message")
    .setTarget("target")
    .setDetails(Collections.singletonList(
        new ODataErrorDetail()
        .setCode("detail code")
        .setMessage("detail message")));

    InputStream stream = ser.error(error).getContent();
    String jsonString = IOUtils.toString(stream);
    assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
        + "<error xmlns=\"http://docs.oasis-open.org/odata/ns/metadata\">"
        + "<code>code</code>"
        + "<message>err message</message>"
        + "<target>target</target>"
        + "<details>"
        + "<code>detail code</code>"
        + "<message>detail message</message>"
        + "</details>"
        + "</error>",
        jsonString);
  }

}