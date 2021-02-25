/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.etag;

import java.util.Collection;
import java.util.Collections;

import nl.buildforce.olingo.server.api.etag.ETagHelper;
import nl.buildforce.olingo.server.api.etag.PreconditionException;

public class ETagHelperImpl implements ETagHelper {

  @Override
  public boolean checkReadPreconditions(String eTag,
                                        Collection<String> ifMatchHeaders, Collection<String> ifNoneMatchHeaders)
          throws PreconditionException {
    if (eTag != null) {
      ETagInformation ifMatch = createETagInformation(ifMatchHeaders);
      if (!ifMatch.isMatchedBy(eTag) && !ifMatch.getETags().isEmpty()) {
        throw new PreconditionException("The If-Match precondition is not fulfilled.",
            PreconditionException.MessageKeys.FAILED);
      }
      return createETagInformation(ifNoneMatchHeaders).isMatchedBy(eTag);
    }
    return false;
  }

  @Override
  public void checkChangePreconditions(String eTag,
                                       Collection<String> ifMatchHeaders, Collection<String> ifNoneMatchHeaders)
          throws PreconditionException {
    if (eTag != null) {
      ETagInformation ifMatch = createETagInformation(ifMatchHeaders);
      ETagInformation ifNoneMatch = createETagInformation(ifNoneMatchHeaders);
      if (!ifMatch.isMatchedBy(eTag) && !ifMatch.getETags().isEmpty()
          || ifNoneMatch.isMatchedBy(eTag)) {
        throw new PreconditionException("The preconditions are not fulfilled.",
            PreconditionException.MessageKeys.FAILED);
      }
    }
  }

  /**
   * Creates ETag information from the values of a HTTP header
   * containing a list of entity tags or a single star character, i.e.,
   * <code>If-Match</code> and <code>If-None-Match</code>.
   * @param values the collection of header values
   * @return an {@link ETagInformation} instance
   */
  protected ETagInformation createETagInformation(Collection<String> values) {
    Collection<String> eTags = ETagParser.parse(values);
    boolean isAll = eTags.size() == 1 && "*".equals(eTags.iterator().next());
    return new ETagInformation(isAll,
        isAll ? Collections.emptySet() : Collections.unmodifiableCollection(eTags));
  }
}
