package org.nervos.ckb.type;

import java.util.List;

public class BlockchainInfo {
  public boolean isInitialBlockDownload;
  public long epoch;
  public byte[] difficulty;
  public long medianTime;
  public String chain;
  public List<AlertMessage> alerts;
}
