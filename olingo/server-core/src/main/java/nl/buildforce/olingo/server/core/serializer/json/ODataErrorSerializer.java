/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.serializer.json;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nl.buildforce.olingo.commons.api.Constants;
import nl.buildforce.olingo.commons.api.ex.ODataError;
import nl.buildforce.olingo.commons.api.ex.ODataErrorDetail;
import nl.buildforce.olingo.server.api.serializer.SerializerException;

import com.fasterxml.jackson.core.JsonGenerator;

public class ODataErrorSerializer {

  public void writeErrorDocument(JsonGenerator json, ODataError error)
      throws IOException, SerializerException {
    if (error == null) {
      throw new SerializerException("ODataError object MUST NOT be null!",
          SerializerException.MessageKeys.NULL_INPUT);
    }
    json.writeStartObject();
    json.writeFieldName(Constants.JSON_ERROR);

    json.writeStartObject();
    writeODataError(json, error.getCode(), error.getMessage(), error.getTarget());
    writeODataAdditionalProperties(json, error.getAdditionalProperties());

    if (error.getDetails() != null) {
      json.writeArrayFieldStart(Constants.ERROR_DETAILS);
      for (ODataErrorDetail detail : error.getDetails()) {
        json.writeStartObject();
        writeODataError(json, detail.getCode(), detail.getMessage(), detail.getTarget());
        writeODataAdditionalProperties(json, detail.getAdditionalProperties());
        json.writeEndObject();
      }
      json.writeEndArray();
    }

    json.writeEndObject();
    json.writeEndObject();
  }

  @SuppressWarnings("unchecked")
  private void writeODataAdditionalProperties(JsonGenerator json, 
		  Map<String, Object> additionalProperties) throws IOException {
	  if (additionalProperties != null) {
		  for (Entry<String, Object> additionalProperty : additionalProperties.entrySet()) {
			  Object value = additionalProperty.getValue();
			  if (value instanceof List) {
				  List<Map<String, Object>> list = (List<Map<String, Object>>) value;
				  json.writeArrayFieldStart(additionalProperty.getKey());
				  for (Map<String, Object> entry : list) {
					  json.writeStartObject();
					  writeODataAdditionalProperties(json, entry);
					  json.writeEndObject();
				  }
				  json.writeEndArray();
			  } else if (value instanceof Map) {
				  writeODataAdditionalProperties(json, (Map<String, Object>) value);
			  } else {
				  json.writeObjectField(additionalProperty.getKey(), value);
			  }
		  }
	  }
}

private void writeODataError(JsonGenerator json, String code, String message, String target)
      throws IOException {
    json.writeFieldName(Constants.ERROR_CODE);
    if (code == null) {
      json.writeNull();
    } else {
      json.writeString(code);
    }

    json.writeFieldName(Constants.ERROR_MESSAGE);
    if (message == null) {
      json.writeNull();
    } else {
      json.writeString(message);
    }

    if (target != null) {
      json.writeStringField(Constants.ERROR_TARGET, target);
    }
  }
}
