/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import nl.buildforce.olingo.commons.api.edm.EdmAction;
import nl.buildforce.olingo.commons.api.edm.EdmEntityType;
import nl.buildforce.olingo.commons.api.ex.ODataRuntimeException;
import nl.buildforce.olingo.server.api.uri.UriResourceAction;
import nl.buildforce.olingo.server.api.uri.UriResourceEntitySet;
import nl.buildforce.olingo.server.api.uri.UriInfo;
import nl.buildforce.olingo.server.api.uri.UriInfoKind;
import nl.buildforce.olingo.server.api.uri.queryoption.AliasQueryOption;
import nl.buildforce.olingo.server.api.uri.queryoption.QueryOption;
import nl.buildforce.olingo.server.core.uri.queryoption.AliasQueryOptionImpl;
import nl.buildforce.olingo.server.core.uri.queryoption.ApplyOptionImpl;
import nl.buildforce.olingo.server.core.uri.queryoption.CountOptionImpl;
import nl.buildforce.olingo.server.core.uri.queryoption.CustomQueryOptionImpl;
import nl.buildforce.olingo.server.core.uri.queryoption.DeltaTokenOptionImpl;
import nl.buildforce.olingo.server.core.uri.queryoption.ExpandOptionImpl;
import nl.buildforce.olingo.server.core.uri.queryoption.FilterOptionImpl;
import nl.buildforce.olingo.server.core.uri.queryoption.FormatOptionImpl;
import nl.buildforce.olingo.server.core.uri.queryoption.IdOptionImpl;
import nl.buildforce.olingo.server.core.uri.queryoption.LevelsOptionImpl;
import nl.buildforce.olingo.server.core.uri.queryoption.OrderByOptionImpl;
import nl.buildforce.olingo.server.core.uri.queryoption.SearchOptionImpl;
import nl.buildforce.olingo.server.core.uri.queryoption.SelectOptionImpl;
import nl.buildforce.olingo.server.core.uri.queryoption.SkipOptionImpl;
import nl.buildforce.olingo.server.core.uri.queryoption.SkipTokenOptionImpl;
import nl.buildforce.olingo.server.core.uri.queryoption.TopOptionImpl;
import org.junit.Test;
import org.mockito.Mockito;

public class UriInfoImplTest {

  @Test
  public void kind() {
    UriInfo uriInfo = new UriInfoImpl().setKind(UriInfoKind.all);
    assertEquals(UriInfoKind.all, uriInfo.getKind());
  }

  @Test
  public void casts() {
    UriInfo uriInfo = new UriInfoImpl();

    assertEquals(uriInfo, uriInfo.asUriInfoAll());
    assertEquals(uriInfo, uriInfo.asUriInfoBatch());
    assertEquals(uriInfo, uriInfo.asUriInfoCrossjoin());
    assertEquals(uriInfo, uriInfo.asUriInfoEntityId());
    assertEquals(uriInfo, uriInfo.asUriInfoMetadata());
    assertEquals(uriInfo, uriInfo.asUriInfoResource());
    assertEquals(uriInfo, uriInfo.asUriInfoService());
  }

  @Test
  public void entityNames() {
    UriInfo uriInfo = new UriInfoImpl()
        .addEntitySetName("A")
        .addEntitySetName("B");
    assertArrayEquals(new String[] { "A", "B" }, uriInfo.getEntitySetNames().toArray());
  }

  @Test
  public void resourceParts() {
    UriInfoImpl uriInfo = new UriInfoImpl();

    UriResourceAction action = new UriResourceActionImpl((EdmAction) null);
    UriResourceEntitySet entitySet0 = new UriResourceEntitySetImpl(null);
    UriResourceEntitySet entitySet1 = new UriResourceEntitySetImpl(null);

    uriInfo.addResourcePart(action);
    uriInfo.addResourcePart(entitySet0);

    assertEquals(action, uriInfo.getUriResourceParts().get(0));
    assertEquals(entitySet0, uriInfo.getUriResourceParts().get(1));

    assertEquals(entitySet0, uriInfo.getLastResourcePart());

    uriInfo.addResourcePart(entitySet1);
    assertEquals(entitySet1, uriInfo.getLastResourcePart());
  }

  @Test(expected = ODataRuntimeException.class)
  public void doubleSystemQueryOptions() {
    new UriInfoImpl()
        .setSystemQueryOption(new FormatOptionImpl())
        .setSystemQueryOption(new FormatOptionImpl());
  }

  @Test
  public void customQueryOption() {
    QueryOption apply       = new ApplyOptionImpl().setName("");
    QueryOption expand      = new ExpandOptionImpl().setName("");
    QueryOption filter      = new FilterOptionImpl().setName("");
    QueryOption format      = new FormatOptionImpl().setName("");
    QueryOption id          = new IdOptionImpl().setName("");
    QueryOption inlinecount = new CountOptionImpl().setName("");
    QueryOption orderby     = new OrderByOptionImpl().setName("");
    QueryOption search      = new SearchOptionImpl().setName("");
    QueryOption select      = new SelectOptionImpl().setName("");
    QueryOption skip        = new SkipOptionImpl().setName("");
    QueryOption skipToken   = new SkipTokenOptionImpl().setName("");
    QueryOption top         = new TopOptionImpl().setName("");
    QueryOption levels      = new LevelsOptionImpl().setName("");
    QueryOption deltaToken  = new DeltaTokenOptionImpl().setName("");
    
    QueryOption customOption0 = new CustomQueryOptionImpl().setName("0").setText("A");
    QueryOption customOption1 = new CustomQueryOptionImpl().setName("1").setText("B");

    QueryOption initialQueryOption = new CustomQueryOptionImpl();

    QueryOption alias = new AliasQueryOptionImpl().setName("alias").setText("C");

    UriInfo uriInfo = new UriInfoImpl()
        .setQueryOption(apply)
        .setQueryOption(expand)
        .setQueryOption(filter)
        .setQueryOption(format)
        .setQueryOption(id)
        .setQueryOption(inlinecount)
        .setQueryOption(orderby)
        .setQueryOption(search)
        .setQueryOption(select)
        .setQueryOption(skip)
        .setQueryOption(skipToken)
        .setQueryOption(top)
        .setQueryOption(customOption0)
        .setQueryOption(customOption1)
        .setQueryOption(levels)
        .setQueryOption(initialQueryOption)
        .setQueryOption(alias)
        .setQueryOption(deltaToken);

    assertEquals(14, uriInfo.getSystemQueryOptions().size());
    assertEquals(apply, uriInfo.getApplyOption());
    assertEquals(expand, uriInfo.getExpandOption());
    assertEquals(filter, uriInfo.getFilterOption());
    assertEquals(format, uriInfo.getFormatOption());
    assertEquals(id, uriInfo.getIdOption());
    assertEquals(inlinecount, uriInfo.getCountOption());
    assertEquals(orderby, uriInfo.getOrderByOption());
    assertEquals(search, uriInfo.getSearchOption());
    assertEquals(select, uriInfo.getSelectOption());
    assertEquals(skip, uriInfo.getSkipOption());
    assertEquals(skipToken, uriInfo.getSkipTokenOption());
    assertEquals(top, uriInfo.getTopOption());
    assertEquals(deltaToken, uriInfo.getDeltaTokenOption());

    assertArrayEquals(new QueryOption[] { alias }, uriInfo.getAliases().toArray());
    assertEquals("C", uriInfo.getValueForAlias("alias"));

    assertArrayEquals(new QueryOption[] { customOption0, customOption1 },
        uriInfo.getCustomQueryOptions().toArray());
  }

  @Test
  public void fragment() {
    UriInfo uriInfo = new UriInfoImpl().setFragment("F");
    assertEquals("F", uriInfo.getFragment());
  }

  @Test
  public void entityTypeCast() {
    EdmEntityType entityType = Mockito.mock(EdmEntityType.class);
    UriInfo uriInfo = new UriInfoImpl()
        .setEntityTypeCast(entityType);
    assertEquals(entityType, uriInfo.getEntityTypeCast());
  }

  @Test
  public void alias() {
    UriInfo uriInfo = new UriInfoImpl()
        .addAlias((AliasQueryOption) new AliasQueryOptionImpl().setName("A").setText("X"))
        .addAlias((AliasQueryOption) new AliasQueryOptionImpl().setName("B").setText("Y"))
        .addAlias((AliasQueryOption) new AliasQueryOptionImpl().setName("C").setText("Z"));

    assertEquals(3, uriInfo.getAliases().size());
    assertEquals("X", uriInfo.getValueForAlias("A"));
    assertEquals("Y", uriInfo.getValueForAlias("B"));
    assertEquals("Z", uriInfo.getValueForAlias("C"));
    assertNull(uriInfo.getValueForAlias("D"));

    assertTrue(uriInfo.getSystemQueryOptions().isEmpty());
    assertTrue(uriInfo.getCustomQueryOptions().isEmpty());
  }

  @Test(expected = ODataRuntimeException.class)
  public void doubleAlias() {
    AliasQueryOption alias = (AliasQueryOption) new AliasQueryOptionImpl().setName("A");
    new UriInfoImpl()
        .addAlias(alias)
        .addAlias(alias);
  }
}
