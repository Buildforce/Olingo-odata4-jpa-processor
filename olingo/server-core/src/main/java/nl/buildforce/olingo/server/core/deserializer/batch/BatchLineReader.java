/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.deserializer.batch;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.net.HttpHeaders.CONTENT_TYPE;

import nl.buildforce.olingo.commons.api.format.ContentType;

public class BatchLineReader {
  private static final byte CR = '\r';
  private static final byte LF = '\n';
  private static final int EOF = -1;
  private static final int BUFFER_SIZE = 8192;
  private static final Charset DEFAULT_CHARSET = StandardCharsets.ISO_8859_1;
  public static final String BOUNDARY = "boundary";
  public static final String DOUBLE_DASH = "--";
  public static final String CRLF = "\r\n";
  private Charset currentCharset = DEFAULT_CHARSET;
  private String currentBoundary;
  private final ReadState readState = new ReadState();
  private final InputStream reader;
  private final byte[] buffer;
  private int offset;
  private int limit;

  public BatchLineReader(InputStream reader) {
    this(reader, BUFFER_SIZE);
  }

  public BatchLineReader(InputStream reader, int bufferSize) {
    if (bufferSize <= 0) {
      throw new IllegalArgumentException("Buffer size must be greater than zero.");
    }

    this.reader = reader;
    buffer = new byte[bufferSize];
  }

  public void close() throws IOException {
    reader.close();
  }

  public List<String> toList() throws IOException {
    List<String> result = new ArrayList<>();
    String currentLine = readLine();
    if (currentLine != null) {
      currentBoundary = currentLine.trim();
      result.add(currentLine);

      while ((currentLine = readLine()) != null) {
        result.add(currentLine);
      }
    }
    return result;
  }

  public List<Line> toLineList() throws IOException {
    List<Line> result = new ArrayList<>();
    String currentLine = readLine();
    if (currentLine != null) {
      currentBoundary = currentLine.trim();
      int counter = 1;
      result.add(new Line(currentLine, counter++));

      while ((currentLine = readLine()) != null) {
        result.add(new Line(currentLine, counter++));
      }
    }

    return result;
  }

  private void updateCurrentCharset(String currentLine) {
    if (currentLine != null) {
      if (currentLine.startsWith(CONTENT_TYPE)) {
        ContentType contentType = ContentType.parse(
            currentLine.substring(CONTENT_TYPE.length() + 1, currentLine.length() - 2).trim());
        if (contentType != null) {
          String charsetString = contentType.getParameter(ContentType.PARAMETER_CHARSET);
          currentCharset = charsetString == null ?
              contentType.isCompatible(ContentType.APPLICATION_JSON) || contentType.getSubtype().contains("xml") ?
                      StandardCharsets.UTF_8 :
                  DEFAULT_CHARSET :
              Charset.forName(charsetString);

          String boundary = contentType.getParameter(BOUNDARY);
          if (boundary != null) {
            currentBoundary = DOUBLE_DASH + boundary;
          }
        }
      } else if (CRLF.equals(currentLine)) {
        readState.foundLinebreak();
      } else if (isBoundary(currentLine)) {
        readState.foundBoundary();
      }
    }
  }

  private boolean isBoundary(String currentLine) {
    return (currentBoundary + CRLF).equals(currentLine)
        || (currentBoundary + DOUBLE_DASH + CRLF).equals(currentLine);
  }

  String readLine() throws IOException {
    if (limit == EOF) {
      return null;
    }

    ByteBuffer innerBuffer = ByteBuffer.allocate(BUFFER_SIZE);
    // EOF will be considered as line ending
    boolean foundLineEnd = false;

    while (!foundLineEnd) {
      // Is buffer refill required?
      if (limit == offset && fillBuffer() == EOF) {
        foundLineEnd = true;
      }

      if (!foundLineEnd) {
        byte currentChar = buffer[offset++];
        if (!innerBuffer.hasRemaining()) {
          innerBuffer.flip();
          ByteBuffer tmp = ByteBuffer.allocate(innerBuffer.limit() * 2);
          tmp.put(innerBuffer);
          innerBuffer = tmp;
        }
        innerBuffer.put(currentChar);

        if (currentChar == LF) {
          foundLineEnd = true;
        } else if (currentChar == CR) {
          foundLineEnd = true;

          // Check next byte. Consume \n if available
          // Is buffer refill required?
          if (limit == offset) {
            fillBuffer();
          }

          // Check if there is at least one character
          if (limit != EOF && buffer[offset] == LF) {
            innerBuffer.put(LF);
            offset++;
          }
        }
      }
    }

    if (innerBuffer.position() == 0) {
      return null;
    } else {
      String currentLine = new String(innerBuffer.array(), 0, innerBuffer.position(),
          readState.isReadBody() ? currentCharset : DEFAULT_CHARSET);
      updateCurrentCharset(currentLine);
      return currentLine;
    }
  }

  private int fillBuffer() throws IOException {
    limit = reader.read(buffer, 0, buffer.length);
    offset = 0;

    return limit;
  }

  /**
   * Read state indicator (whether currently the <code>body</code> or <code>header</code> part is read).
   */
  private static class ReadState {
    private int state;

    public void foundLinebreak() {
      state++;
    }

    public void foundBoundary() {
      state = 0;
    }

    public boolean isReadBody() {
      return state >= 2;
    }

    @Override
    public String toString() {
      return String.valueOf(state);
    }
  }

}