/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.queryoption.apply;

import java.util.ArrayList;
import java.util.List;

import nl.buildforce.olingo.server.api.uri.queryoption.ApplyItem;
import nl.buildforce.olingo.server.api.uri.queryoption.ApplyOption;

/**
 * Represents the concat transformation.
 */
public class ConcatImpl implements ApplyItem {

  private final List<ApplyOption> options = new ArrayList<>();

  @Override
  public Kind getKind() {
    return Kind.CONCAT;
  }

/*
  @Override
  public List<ApplyOption> getApplyOptions() {
    return options;
  }
*/

  public ConcatImpl addApplyOption(ApplyOption option) {
    options.add(option);
    return this;
  }

}