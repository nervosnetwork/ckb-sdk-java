package org.nervos.ckb.type;

import java.util.List;

public class PeerNodeInfo {
  public String nodeId;
  public long connectedDuration;
  public boolean isOutbound;
  public long lastPingDuration;
  public String version;
  public List<Address> addresses;
  public List<Protocol> protocols;
  public SyncState syncState;

  public static class Address {
    public String address;
    public long score;
  }

  public static class Protocol {
    public long id;
    public String version;
  }

  public static class SyncState {
    public byte[] bestKnownHeaderHash;
    public long bestKnownHeaderNumber;
    public byte[] lastCommonHeaderHash;
    public long lastCommonHeaderNumber;
    @Deprecated
    public long unknownHeaderListSize;
    public long inflightCount;
    public long canFetchCount;
  }
}
