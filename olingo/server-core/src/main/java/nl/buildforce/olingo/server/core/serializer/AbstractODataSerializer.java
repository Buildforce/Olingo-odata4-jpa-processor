/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.serializer;

import java.io.IOException;
import java.io.OutputStream;

import nl.buildforce.olingo.server.api.serializer.ODataSerializer;
import nl.buildforce.olingo.server.api.serializer.SerializerException;
import nl.buildforce.olingo.server.api.serializer.SerializerException.MessageKeys;

public abstract class AbstractODataSerializer implements ODataSerializer {

  protected static final String IO_EXCEPTION_TEXT = "An I/O exception occurred.";

  protected void closeCircleStreamBufferOutput(OutputStream outputStream, SerializerException cachedException)
      throws SerializerException {
    if (outputStream != null) {
      try {
        outputStream.close();
      } catch (IOException e) {
        if (cachedException == null) throw new SerializerException(IO_EXCEPTION_TEXT, e, MessageKeys.IO_EXCEPTION);
        else throw cachedException;
      }
    }
  }

}