package org.nervos.indexer.model.resp;

public class TxWithCell {
  public int blockNumber;
  public int ioIndex;
  public IoType ioType;
  public byte[] txHash;
  public int txIndex;
}
