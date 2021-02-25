/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.etag;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;

import nl.buildforce.olingo.server.api.OData;
import nl.buildforce.olingo.server.api.etag.ETagHelper;
import nl.buildforce.olingo.server.api.etag.PreconditionException;
import org.junit.Test;

public class ETagHelperTest {

  private static final ETagHelper eTagHelper = OData.newInstance().createETagHelper();

  @Test
  public void readPrecondition() throws Exception {
    assertFalse(eTagHelper.checkReadPreconditions(null, null, null));
    assertFalse(eTagHelper.checkReadPreconditions("\"ETag\"", null, null));
    assertFalse(eTagHelper.checkReadPreconditions(null, Collections.singleton("\"ETag\""), null));
    assertFalse(eTagHelper.checkReadPreconditions(null, null, Collections.singleton("\"ETag\"")));
    assertFalse(eTagHelper.checkReadPreconditions("\"ETag\"", Collections.singleton("\"ETag\""), null));
    assertFalse(eTagHelper.checkReadPreconditions("\"ETag\"", Collections.singleton("*"), null));
    assertTrue(eTagHelper.checkReadPreconditions("\"ETag\"", null, Collections.singleton("\"ETag\"")));
    assertTrue(eTagHelper.checkReadPreconditions("\"ETag\"", null, Collections.singleton("*")));
    assertFalse(eTagHelper.checkReadPreconditions("\"ETag\"", null, Collections.singleton("\"ETag2\"")));
  }

  @Test(expected = PreconditionException.class)
  public void readPreconditionFail() throws Exception {
    eTagHelper.checkReadPreconditions("\"ETag\"", Collections.singleton("\"ETag2\""), null);
  }

  @Test
  public void changePrecondition() throws Exception {
    eTagHelper.checkChangePreconditions(null, null, null);
    eTagHelper.checkChangePreconditions("\"ETag\"", null, null);
    eTagHelper.checkChangePreconditions(null, Collections.singleton("\"ETag\""), null);
    eTagHelper.checkChangePreconditions(null, Collections.singleton("*"), null);
    eTagHelper.checkChangePreconditions(null, null, Collections.singleton("*"));
    eTagHelper.checkChangePreconditions("\"ETag\"", Collections.singleton("\"ETag\""), null);
    eTagHelper.checkChangePreconditions("\"ETag\"", Collections.singleton("*"), null);
    eTagHelper.checkChangePreconditions("\"ETag\"", null, Collections.singleton("\"ETag2\""));
  }

  @Test(expected = PreconditionException.class)
  public void changePreconditionFailIfMatch() throws Exception {
    eTagHelper.checkChangePreconditions("\"ETag\"", Collections.singleton("\"ETag2\""), null);
  }

  @Test(expected = PreconditionException.class)
  public void changePreconditionFailIfNoneMatch() throws Exception {
    eTagHelper.checkChangePreconditions("\"ETag\"", null, Collections.singleton("\"ETag\""));
  }

  @Test(expected = PreconditionException.class)
  public void changePreconditionFailIfNoneMatchAll() throws Exception {
    eTagHelper.checkChangePreconditions("\"ETag\"", null, Collections.singleton("*"));
  }
}
