package nl.buildforce.sequoia.jpa.processor.core.api.example;

import nl.buildforce.sequoia.jpa.processor.core.api.JPAODataPage;
import nl.buildforce.sequoia.jpa.processor.core.api.JPAODataPagingProvider;
import nl.buildforce.sequoia.jpa.processor.core.query.JPACountQuery;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriInfo;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourceEntitySet;

import jakarta.persistence.EntityManager;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;

public class JPAExamplePagingProvider implements JPAODataPagingProvider {

  private static final int DEFAULT_BUFFER_SIZE = 100;
  private final Map<String, Integer> maxPageSizes;
  private final Map<String, CacheEntry> pageCache;
  private final int cacheSize;
  private final Queue<String> index;

  public JPAExamplePagingProvider(Map<String, Integer> pageSizes) {
    this(pageSizes, DEFAULT_BUFFER_SIZE);
  }

  public JPAExamplePagingProvider(Map<String, Integer> pageSizes, final int bufferSize) {
    maxPageSizes = pageSizes;
    pageCache = new HashMap<>(bufferSize);
    cacheSize = bufferSize;
    index = new LinkedList<>();
  }

  @Override
  public JPAODataPage getNextPage(final String skiptoken) {
    final CacheEntry previousPage = pageCache.get(skiptoken.replaceAll("'", ""));
    if (previousPage != null) {
      // Calculate next page
      final int skip = previousPage.getPage().getSkip() + previousPage.getPage().getTop();
      // Create a new skiptoken if next page is not the last one
      String nextToken = null;
      if (skip + previousPage.getPage().getTop() < previousPage.getMaxTop())
        nextToken = UUID.randomUUID().toString();
      final int top = (int) ((skip + previousPage.getPage().getTop()) < previousPage.getMaxTop() ? previousPage
          .getPage().getTop() : previousPage.getMaxTop() - skip);
      final JPAODataPage page = new JPAODataPage(previousPage.getPage().getUriInfo(),
          skip, top, nextToken);
      if (nextToken != null)
        addToChach(page, previousPage.getMaxTop());
      return page;
    }
    // skip token not found => let JPA Processor handle this
    return null;
  }

  @Override
  public JPAODataPage getFirstPage(final UriInfo uriInfo, final Integer preferredPageSize,
      final JPACountQuery countQuery, final EntityManager em) throws ODataApplicationException {

    final UriResource root = uriInfo.getUriResourceParts().get(0);
    // Paging will only be done for Entity Sets
    if (root instanceof UriResourceEntitySet) {
      // Check if Entity Set shall be packaged
      final Integer maxSize = maxPageSizes.get(((UriResourceEntitySet) root).getEntitySet().getName());
      if (maxSize != null) {
        // Read $top and $skip
        final int skipValue = uriInfo.getSkipOption() != null ? uriInfo.getSkipOption().getValue() : 0;
        final Integer topValue = uriInfo.getTopOption() != null ? uriInfo.getTopOption().getValue() : null;
        // Determine end of list
        final Long count = topValue != null ? (topValue + skipValue) : countQuery.countResults();
        // Determine page size
        final Integer size = preferredPageSize != null && preferredPageSize < maxSize ? preferredPageSize : maxSize;
        // Create a unique skiptoken if needed
        String skiptoken = null;
        if (size < count)
          skiptoken = UUID.randomUUID().toString();
        // Create page information
        final JPAODataPage page = new JPAODataPage(uriInfo, skipValue, topValue != null && topValue < size ? topValue
            : size, skiptoken);
        // Cache page to be able to fulfill next link based request
        if (skiptoken != null)
          addToChach(page, count);
        return page;
      }
    }
    return null;
  }

  private void addToChach(final JPAODataPage page, final Long count) {
    if (pageCache.size() == cacheSize)
      pageCache.remove(index.poll());

    pageCache.put((String) page.getSkipToken(), new CacheEntry(count, page));
    index.add((String) page.getSkipToken());
  }

  private static class CacheEntry {
    private final Long maxTop;
    private final JPAODataPage page;

    CacheEntry(Long count, JPAODataPage page) {
      this.maxTop = count;
      this.page = page;
    }

    public Long getMaxTop() {
      return maxTop;
    }

    public JPAODataPage getPage() {
      return page;
    }
  }

}