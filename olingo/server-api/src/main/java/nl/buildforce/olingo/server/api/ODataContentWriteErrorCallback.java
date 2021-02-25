/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api;

import java.io.OutputStream;
import java.nio.channels.WritableByteChannel;

/**
 * The ODataContentWriteErrorCallback is called when during the {@link ODataContent#write(OutputStream)}
 * or the {@link ODataContent#write(WritableByteChannel)} an error occurs.
 */
public interface ODataContentWriteErrorCallback {
  /**
   * Is called when during <i>write</i> in the ODataContent an error occurs.
   * The <code>context:ODataContentWriteErrorContext</code> contains all relevant information
   * and the <code>channel</code> is the channel (stream) in which before was written.
   * This channel is at this point not closed and can be used to write additional information.
   * <b>ATTENTION:</b> This channel MUST NOT be closed by the callback. It will be closed by the
   * layer responsible for the environment / data transfer (e.g. application server).
   *
   * @param context contains all relevant error information
   * @param channel is the channel (stream) in which before was written
   */
  void handleError(ODataContentWriteErrorContext context, WritableByteChannel channel);
}
