/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api;

import java.nio.channels.WritableByteChannel;

/**
 * Contains the response content for the OData request.
 * <p/>
 * Because the content is potential streamable an error can occur when the
 * <code>write</code> methods are used.
 * If this happens <b>NO</b> exception will be thrown but if registered the
 * org.apache.olingo.server.api.ODataContentWriteErrorCallback is called.
 */
public interface ODataContent {
  /**
   * Write the available content into the given <code>WritableByteChannel</code>.
   *
   * If during write of the content an exception is thrown this exception will be catched
   * and the org.apache.olingo.server.api.ODataContentWriteErrorCallback is called (if registered).
   *
   * @param channel channel in which the content is written.
   */
  void write(WritableByteChannel channel);

// --Commented out by Inspection START (''21-03-09 16:25):
//  /**
//   * Write the available content into the given <code>OutputStream</code>.
//   *
//   * If during write of the content an exception is thrown this exception will be catched
//   * and the org.apache.olingo.server.api.ODataContentWriteErrorCallback is called (if registered).
//   *
//   * @param stream stream in which the content is written.
//   */
//  void write(OutputStream stream);
// --Commented out by Inspection STOP (''21-03-09 16:25)

}