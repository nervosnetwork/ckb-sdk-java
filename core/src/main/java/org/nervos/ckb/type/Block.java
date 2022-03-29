package org.nervos.ckb.type;

import java.util.List;
import org.nervos.ckb.type.transaction.Transaction;

/** Copyright © 2018 Nervos Foundation. All rights reserved. */
public class Block {
  public Header header;

  public List<Transaction> transactions;

  public List<byte[]> proposals;

  public List<Uncle> uncles;

  public static class Uncle {

    public Header header;

    public List<byte[]> proposals;
  }
}
