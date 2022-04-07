package org.nervos.ckb.type;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/** Copyright © 2018 Nervos Foundation. All rights reserved. */
public class PeerNodeInfo {

  @SerializedName("node_id")
  public String nodeId;

  @SerializedName("connected_duration")
  public long connectedDuration;

  @SerializedName("is_outbound")
  public boolean isOutbound;

  @SerializedName("last_ping_duration")
  public long lastPingDuration;

  public String version;
  public List<Address> addresses;
  public List<Protocol> protocols;

  @SerializedName("sync_state")
  public SyncState syncState;

  public static class Address {
    public String address;
    public int score;
  }

  public static class Protocol {
    public int id;
    public String version;
  }

  public static class SyncState {
    @SerializedName("best_known_header_hash")
    public String BEST_KNOWN_HEADER;

    @SerializedName("best_known_header_number")
    public String BEST_KNOWN_HEADER_NUMBER;

    @SerializedName("can_fetch_count")
    public String CAN_FETCH_COUNT;

    @SerializedName("inflight_count")
    public String INFLIGHT_COUNT;

    @SerializedName("last_common_header_hash")
    public String LAST_COMMON_HEADER_HASH;

    @SerializedName("last_common_header_number")
    public String LAST_COMMON_HEADER_NUMBER;

    @SerializedName("unknown_header_list_size")
    public String UNKNOWN_HEADER_LIST_SIZE;
  }
}
