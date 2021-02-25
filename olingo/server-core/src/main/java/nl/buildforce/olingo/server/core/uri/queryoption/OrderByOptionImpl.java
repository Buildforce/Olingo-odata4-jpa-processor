/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.queryoption;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.buildforce.olingo.server.api.uri.queryoption.OrderByItem;
import nl.buildforce.olingo.server.api.uri.queryoption.OrderByOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SystemQueryOptionKind;

public class OrderByOptionImpl extends SystemQueryOptionImpl implements OrderByOption {

  private final List<OrderByItem> orders = new ArrayList<>();

  public OrderByOptionImpl() {
    setKind(SystemQueryOptionKind.ORDERBY);
  }

  @Override
  public List<OrderByItem> getOrders() {
    return Collections.unmodifiableList(orders);
  }

  public OrderByOptionImpl addOrder(OrderByItem order) {
    orders.add(order);
    return this;
  }

}