package org.nervos.ckb.methods.type;

import java.util.List;
import org.nervos.ckb.methods.type.transaction.Transaction;

/** Created by duanyytop on 2018-12-20. Copyright © 2018 Nervos Foundation. All rights reserved. */
public class Block {
  public Header header;

  public List<Transaction> transactions;

  public List<String> proposals;

  public List<Uncle> uncles;

  public static class Uncle {

    public Header header;

    public List<String> proposals;
  }
}
