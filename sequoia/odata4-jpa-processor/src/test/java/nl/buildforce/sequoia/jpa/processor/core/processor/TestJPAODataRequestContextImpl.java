package nl.buildforce.sequoia.jpa.processor.core.processor;

import nl.buildforce.sequoia.jpa.processor.core.api.JPAODataClaimProvider;
import nl.buildforce.sequoia.jpa.processor.core.api.JPAODataClaimsProvider;
import nl.buildforce.sequoia.jpa.processor.core.api.JPAODataDefaultTransactionFactory;
import nl.buildforce.sequoia.jpa.processor.core.api.JPAODataGroupProvider;
import nl.buildforce.sequoia.jpa.processor.core.api.JPAODataGroupsProvider;
import nl.buildforce.sequoia.jpa.processor.core.api.JPAODataPage;
import nl.buildforce.sequoia.jpa.processor.core.api.JPAODataTransactionFactory;
import nl.buildforce.sequoia.jpa.processor.core.exception.JPAIllegalAccessException;
import nl.buildforce.sequoia.jpa.processor.core.serializer.JPASerializer;
import org.apache.olingo.server.api.uri.UriInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class TestJPAODataRequestContextImpl {
  private JPAODataRequestContextImpl cut;

  @BeforeEach
  public void setup() {
    cut = new JPAODataRequestContextImpl();
  }

  @Test
  public void testInitialEmptyClaimsProvider() {
    assertFalse(cut.getClaimsProvider().isPresent());
  }

  @Test
  public void testInitialEmptyGroupsProvider() {
    assertFalse(cut.getGroupsProvider().isPresent());
  }

  @Test
  public void testReturnsSetClaimsProvider() {
    final JPAODataClaimProvider exp = new JPAODataClaimsProvider();
    cut.setClaimsProvider(exp);
    assertEquals(exp, cut.getClaimsProvider().get());
  }

  @Test
  public void testReturnsSetGroupsProvider() {
    final JPAODataGroupProvider exp = new JPAODataGroupsProvider();
    cut.setGroupsProvider(exp);
    assertEquals(exp, cut.getGroupsProvider().get());
  }

  @Test
  public void testReturnsSetEntityManager() {
    final EntityManager exp = mock(EntityManager.class);
    cut.setEntityManager(exp);
    assertEquals(exp, cut.getEntityManager());
  }

  @Test
  public void testThrowsExceptionOnEntityManagerIsNull() {
    assertThrows(NullPointerException.class, () -> cut.setEntityManager(null));
  }

  @Test
  public void testReturnsSetPage() throws JPAIllegalAccessException {
    final UriInfo uriInfo = mock(UriInfo.class);
    final JPAODataPage exp = new JPAODataPage(uriInfo, 0, 10, "12354");
    cut.setJPAODataPage(exp);
    assertEquals(exp, cut.getPage());
    assertEquals(uriInfo, cut.getUriInfo());
  }

  @Test
  public void testReturnsSetUriInfo() throws JPAIllegalAccessException {
    final UriInfo exp = mock(UriInfo.class);
    cut.setUriInfo(exp);
    assertEquals(exp, cut.getUriInfo());
  }

  @Test
  public void testReturnsSetJPASerializer() {
    final JPASerializer exp = mock(JPASerializer.class);
    cut.setJPASerializer(exp);
    assertEquals(exp, cut.getSerializer());
  }

  @Test
  public void testThrowsExceptionOnSetPageIfUriInfoExists() throws JPAIllegalAccessException {
    final UriInfo uriInfo = mock(UriInfo.class);
    final JPAODataPage page = new JPAODataPage(uriInfo, 0, 10, "12354");
    cut.setUriInfo(uriInfo);
    assertThrows(JPAIllegalAccessException.class, () -> cut.setJPAODataPage(page));
  }

  @Test
  public void testThrowsExceptionOnPageIsNull() {
    assertThrows(NullPointerException.class, () -> cut.setJPAODataPage(null));
  }

  @Test
  public void testThrowsExceptionOnSetUriInfoIfUriInfoExists() throws JPAIllegalAccessException {
    final UriInfo uriInfo = mock(UriInfo.class);
    final JPAODataPage page = new JPAODataPage(uriInfo, 0, 10, "12354");
    cut.setJPAODataPage(page);
    assertThrows(JPAIllegalAccessException.class, () -> cut.setUriInfo(uriInfo));
  }

  @Test
  public void testThrowsExceptionOnUriInfoIsNull() {
    assertThrows(NullPointerException.class, () -> cut.setUriInfo(null));
  }

  @Test
  public void testThrowsExceptionOnSerializerIsNull() {
    assertThrows(NullPointerException.class, () -> cut.setJPASerializer(null));
  }

  @Test
  public void testCopyConstructorCopysExternalAndAddsUriInfo() throws JPAIllegalAccessException {
    fillContextForCopyConstructor();
    final JPASerializer serializer = mock(JPASerializer.class);
    final UriInfo uriInfo = mock(UriInfo.class);
    final JPAODataPage page = new JPAODataPage(uriInfo, 0, 10, "12354");
    JPAODataRequestContextImpl act = new JPAODataRequestContextImpl(page, serializer, cut);

    assertEquals(uriInfo, act.getUriInfo());
    assertEquals(page, act.getPage());
    assertEquals(serializer, act.getSerializer());
    assertCopied(act);
  }

  @Test
  public void testCopyConstructorCopysExternalAndAddsPageSerializer() {
    fillContextForCopyConstructor();
    final UriInfo uriInfo = mock(UriInfo.class);
    JPAODataRequestContextImpl act = new JPAODataRequestContextImpl(uriInfo, cut);

    assertEquals(uriInfo, act.getUriInfo());
    assertCopied(act);
  }

  @Test
  public void testCopyConstructorCopysExternalAndAddsUriInfoSerializer() {
    fillContextForCopyConstructor();
    final UriInfo uriInfo = mock(UriInfo.class);
    final JPASerializer serializer = mock(JPASerializer.class);
    JPAODataRequestContextImpl act = new JPAODataRequestContextImpl(uriInfo, serializer, cut);

    assertEquals(uriInfo, act.getUriInfo());
    assertEquals(serializer, act.getSerializer());
    assertCopied(act);
  }

  @Test
  public void testCopyConstructorCopysExternalAndAddsUriInfoSerializerNull() {
    fillContextForCopyConstructor();
    final UriInfo uriInfo = mock(UriInfo.class);
    JPAODataRequestContextImpl act = new JPAODataRequestContextImpl(uriInfo, null, cut);

    assertEquals(uriInfo, act.getUriInfo());
      assertNull(act.getSerializer());
    assertCopied(act);
  }

  @Test
  public void testReturnsDefaultTransactionFactory() {
    final EntityManager em = mock(EntityManager.class);
    cut.setEntityManager(em);
    assertTrue(cut.getTransactionFactory() instanceof JPAODataDefaultTransactionFactory);
  }

  @Test
  public void testReturnsProvidedTransactionFactory() {
    final JPAODataTransactionFactory exp = mock(JPAODataTransactionFactory.class);
    cut.setTransactionFactory(exp);
    assertEquals(exp, cut.getTransactionFactory());
  }

  private void assertCopied(JPAODataRequestContextImpl act) {
    assertEquals(cut.getEntityManager(), act.getEntityManager());
    assertEquals(cut.getClaimsProvider().get(), act.getClaimsProvider().get());
    assertEquals(cut.getGroupsProvider().get(), act.getGroupsProvider().get());
  }

  private void fillContextForCopyConstructor() {
    final EntityManager expEm = mock(EntityManager.class);
    final JPAODataClaimProvider expCp = new JPAODataClaimsProvider();
    final JPAODataGroupProvider expGp = new JPAODataGroupsProvider();
    cut.setEntityManager(expEm);
    cut.setClaimsProvider(expCp);
    cut.setGroupsProvider(expGp);
  }

}