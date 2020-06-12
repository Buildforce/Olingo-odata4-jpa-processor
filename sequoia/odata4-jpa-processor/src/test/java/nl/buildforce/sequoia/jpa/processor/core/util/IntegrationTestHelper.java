package nl.buildforce.sequoia.jpa.processor.core.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import nl.buildforce.sequoia.jpa.metadata.api.JPAEdmProvider;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAException;
import nl.buildforce.sequoia.jpa.processor.core.api.JPAODataBatchProcessor;
import nl.buildforce.sequoia.jpa.processor.core.api.JPAODataCRUDContextAccess;
import nl.buildforce.sequoia.jpa.processor.core.api.JPAODataClaimsProvider;
import nl.buildforce.sequoia.jpa.processor.core.api.JPAODataContextAccessDouble;
import nl.buildforce.sequoia.jpa.processor.core.api.JPAODataGroupProvider;
import nl.buildforce.sequoia.jpa.processor.core.api.JPAODataPagingProvider;
import nl.buildforce.sequoia.jpa.processor.core.api.JPAODataRequestProcessor;
import nl.buildforce.sequoia.jpa.processor.core.processor.JPAODataRequestContextImpl;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.olingo.commons.api.ex.ODataException;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataHttpHandler;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static nl.buildforce.sequoia.jpa.processor.core.util.TestBase.PUNIT_NAME;
import static nl.buildforce.sequoia.jpa.processor.core.util.TestBase.uriPrefix;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class IntegrationTestHelper {
  private final HttpServletResponseDouble resp;

  public IntegrationTestHelper(final EntityManagerFactory localEmf, final String urlPath) throws ODataJPAException {
    this(localEmf, null, urlPath, null, null, null);
  }

  public IntegrationTestHelper(final EntityManagerFactory localEmf, final String urlPath,
      final Map<String, List<String>> headers) throws ODataJPAException {
    this(localEmf, null, urlPath, null, null, null, headers, null, null);
  }

  public IntegrationTestHelper(final EntityManagerFactory localEmf, final String urlPath,
      final JPAODataGroupProvider groups) throws ODataJPAException {
    this(localEmf, null, urlPath, null, null, null, null, null, groups);
  }

  public IntegrationTestHelper(EntityManagerFactory localEmf, DataSource ds, String urlPath) throws
          ODataException {
    this(localEmf, ds, urlPath, null, null, null);
  }

  public IntegrationTestHelper(EntityManagerFactory localEmf, String urlPath, StringBuffer requestBody) throws ODataJPAException {
    this(localEmf, null, urlPath, requestBody, null, null);
  }

  public IntegrationTestHelper(EntityManagerFactory localEmf, DataSource ds, String urlPath, String functionPackage) throws ODataJPAException {
    this(localEmf, ds, urlPath, null, functionPackage, null);
  }

  public IntegrationTestHelper(final EntityManagerFactory localEmf, final String urlPath, final JPAODataPagingProvider provider) throws ODataJPAException {
    this(localEmf, null, urlPath, null, null, provider);
  }

  public IntegrationTestHelper(EntityManagerFactory localEmf, final String urlPath, JPAODataClaimsProvider claims) throws ODataJPAException {
    this(localEmf, null, urlPath, null, null, null, null, claims, null);
  }

  public IntegrationTestHelper(EntityManagerFactory localEmf, final String urlPath,
      final JPAODataPagingProvider provider, JPAODataClaimsProvider claims) throws ODataJPAException {
    this(localEmf, null, urlPath, null, null, provider, null, claims, null);
  }

  public IntegrationTestHelper(final EntityManagerFactory emf, final String urlPath,
      final JPAODataPagingProvider provider, final Map<String, List<String>> headers) throws ODataJPAException {
    this(emf, null, urlPath, null, null, provider, headers, null, null);
  }

  public IntegrationTestHelper(EntityManagerFactory localEmf,
                               DataSource ds,
                               String urlPath,
                               StringBuffer requestBody,
                               String functionPackage,
                               JPAODataPagingProvider provider) throws ODataJPAException {
    this(localEmf, ds, urlPath, requestBody, functionPackage, provider, null, null, null);
  }

  public IntegrationTestHelper(final EntityManagerFactory localEmf,
                               final DataSource ds,
                               final String urlPath,
                               final StringBuffer requestBody,
                               final String functionPackage,
                               final JPAODataPagingProvider provider,
                               final Map<String, List<String>> headers,
                               final JPAODataClaimsProvider claims,
                               final JPAODataGroupProvider groups) throws ODataJPAException {
    final EntityManager em = localEmf.createEntityManager();
    final OData odata = OData.newInstance();
    String[] packages = TestBase.enumPackages;
    final JPAODataRequestContextImpl requestContext = new JPAODataRequestContextImpl();
    final HttpServletRequestDouble req = new HttpServletRequestDouble(uriPrefix + urlPath, requestBody, headers);
    resp = new HttpServletResponseDouble();
    if (functionPackage != null) packages = ArrayUtils.add(packages, functionPackage);

    final JPAODataCRUDContextAccess sessionContext = new JPAODataContextAccessDouble(
            new JPAEdmProvider(PUNIT_NAME, localEmf, null, packages),
            ds, provider, functionPackage);

    final ODataHttpHandler handler = odata.createHandler(
            odata.createServiceMetadata(sessionContext.getEdmProvider(), new ArrayList<>()));
    requestContext.setClaimsProvider(claims);
    requestContext.setGroupsProvider(groups);
    requestContext.setEntityManager(em);
    handler.register(new JPAODataRequestProcessor(sessionContext, requestContext));
    handler.register(new JPAODataBatchProcessor(requestContext));
    handler.process(req, resp);

  }

  public int getStatus() {
    return resp.getStatus();
  }

  public String getRawResult() throws IOException {
    InputStream in = resp.getInputStream();
    StringBuilder sb = new StringBuilder();
    BufferedReader br = new BufferedReader(new InputStreamReader(in));
    String read;

    while ((read = br.readLine()) != null) {
      sb.append(read);
    }
    br.close();
    return sb.toString();
  }

  public List<String> getRawBatchResult() throws IOException {
    List<String> result = new ArrayList<>();

    InputStream in = resp.getInputStream();
    BufferedReader br = new BufferedReader(new InputStreamReader(in));
    String read;

    while ((read = br.readLine()) != null) {
      result.add(read);
    }
    br.close();
    return result;
  }

  public ArrayNode getValues() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    JsonNode node = mapper.readTree(getRawResult());
    if (!(node.get("value") instanceof ArrayNode))
      fail("Wrong result type; ArrayNode expected");
    return (ArrayNode) node.get("value");
  }

  public ObjectNode getValue() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    JsonNode value = mapper.readTree(getRawResult());
    if (!(value instanceof ObjectNode))
      fail("Wrong result type; ObjectNode expected");
    return (ObjectNode) value;
  }

  public ValueNode getSingleValue() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    JsonNode value = mapper.readTree(getRawResult());
    if (!(value instanceof ValueNode))
      fail("Wrong result type; ValueNode expected");
    return (ValueNode) value;
  }

  public ValueNode getSingleValue(final String nodeName) throws IOException {
    final ObjectMapper mapper = new ObjectMapper();
    final JsonNode node = mapper.readTree(getRawResult());
    if (!(node.get(nodeName) instanceof ValueNode))
      fail("Wrong result type; ArrayNode expected");
    return (ValueNode) node.get(nodeName);
  }

  public void assertStatus(int exp) throws IOException {
    assertEquals(exp, getStatus(), getRawResult());
  }

  public int getBatchResultStatus(int i) throws IOException {
    List<String> result = getRawBatchResult();
    int count = 0;
    for (String resultLine : result) {
      if (resultLine.contains("HTTP/1.1")) {
        count += 1;
        if (count == i) {
          String[] statusElements = resultLine.split(" ");
          return Integer.parseInt(statusElements[1]);
        }
      }
    }
    return 0;
  }

  public JsonNode getBatchResult(int i) throws IOException {
    List<String> result = getRawBatchResult();
    int count = 0;
    boolean found = false;

    for (String resultLine : result) {
      if (resultLine.contains("HTTP/1.1")) {
        count += 1;
        if (count == i) {
          found = true;
        }
      }
      if (found && resultLine.startsWith("{")) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(resultLine);
      }
    }
    return null;
  }

  public byte[] getBinaryResult() throws IOException {
    byte[] result = new byte[resp.getBufferSize()];
    InputStream in = resp.getInputStream();
    in.read(result);
    return result;
  }

}