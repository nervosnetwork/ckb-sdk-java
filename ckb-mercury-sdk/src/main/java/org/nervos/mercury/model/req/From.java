package org.nervos.mercury.model.req;

import org.nervos.mercury.model.req.item.Item;

import java.util.List;

public class From {

  public List<Item> items;

  public Source source;

  public From(List<Item> items, Source source) {
    this.items = items;
    this.source = source;
  }

  public static From newFrom(List<Item> items, Source source) {
    return new From(items, source);
  }
}
