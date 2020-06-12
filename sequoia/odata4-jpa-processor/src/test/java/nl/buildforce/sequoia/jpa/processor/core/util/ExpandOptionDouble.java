package nl.buildforce.sequoia.jpa.processor.core.util;

import org.apache.olingo.server.api.uri.queryoption.ExpandItem;
import org.apache.olingo.server.api.uri.queryoption.ExpandOption;
import org.apache.olingo.server.api.uri.queryoption.SystemQueryOptionKind;

import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

public class ExpandOptionDouble implements ExpandOption {
  private final String text;
  private final List<ExpandItem> items;

  public ExpandOptionDouble(final String text, final List<ExpandItem> items) {
    this.text = text;
    this.items = items;
  }

  @Override
  public SystemQueryOptionKind getKind() {
    fail();
    return null;
  }

  @Override
  public String getName() {
    fail();
    return null;
  }

  @Override
  public String getText() {
    return text;
  }

  @Override
  public List<ExpandItem> getExpandItems() {
    return items;
  }

}