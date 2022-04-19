package org.nervos.ckb.type;

import java.util.Map;

public class RawTxPoolVerbose {
  public Map<byte[], VerboseDetail> pending;
  public Map<byte[], VerboseDetail> proposed;

  public static class VerboseDetail {
    public long cycles;
    public long size;
    public long fee;
    public long ancestorsSize;
    public long ancestorsCycles;
    public long ancestorsCount;
  }
}
