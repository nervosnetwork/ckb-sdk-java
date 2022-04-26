package org.nervos.ckb.type;

import java.util.List;

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
