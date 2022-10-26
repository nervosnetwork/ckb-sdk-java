package com.nervos.lightclient.type;

import org.nervos.ckb.type.Transaction;
import org.nervos.indexer.model.resp.IoType;

import java.util.List;

public class TxsWithCell {
  public byte[] lastCursor;
  public List<TxWithCell> objects;

  public static class TxWithCell {
    public int blockNumber;
    public int ioIndex;
    public IoType ioType;
    public Transaction transaction;
    public int txIndex;
  }
}
