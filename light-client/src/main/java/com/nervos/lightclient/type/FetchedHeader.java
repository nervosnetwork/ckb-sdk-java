package com.nervos.lightclient.type;

import org.nervos.ckb.type.Header;

public class FetchedHeader {
  public FetchStatus status;
  public Header data;
  public long firstSent;
  public long timestamp;
}
