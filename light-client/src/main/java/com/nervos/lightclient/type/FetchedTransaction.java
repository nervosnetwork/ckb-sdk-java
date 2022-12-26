package com.nervos.lightclient.type;

import org.nervos.ckb.type.Transaction;

public class FetchedTransaction {
  public FetchStatus status;
  public Transaction data;
  public long firstSent;
  public long timestamp;

}
