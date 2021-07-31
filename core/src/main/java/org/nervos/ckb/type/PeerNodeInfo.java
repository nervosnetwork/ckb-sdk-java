package org.nervos.ckb.type;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/** Copyright © 2018 Nervos Foundation. All rights reserved. */
public class PeerNodeInfo {

  @SerializedName("node_id")
  public String nodeId;

  @SerializedName("connected_duration")
  public String connectedDuration;

  @SerializedName("is_outbound")
  public Boolean isOutbound;

  @SerializedName("last_ping_duration")
  public String lastPingDuration;

  public String version;
  public List<Address> addresses;
  public List<Protocol> protocols;

  @SerializedName("sync_state")
  public SyncState syncState;

  public static class Address {

    public String address;
    public String score;
  }

  public static class Protocol {
    public String id;
    public String version;
  }

  public static class SyncState {
    @SerializedName("best_known_header_hash")
    public String bestKnownHeader;

    @SerializedName("best_known_header_number")
    public String bestKnownHeaderNumber;

    @SerializedName("can_fetch_count")
    public String canFetchCount;

    @SerializedName("inflight_count")
    public String inflightCount;

    @SerializedName("last_common_header_hash")
    public String lastCommonHeaderHash;

    @SerializedName("last_common_header_number")
    public String lastCommonHeaderNumber;

    @SerializedName("unknown_header_list_size")
    public String unknownHeaderListSize;
  }
}
