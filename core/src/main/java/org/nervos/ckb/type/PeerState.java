package org.nervos.ckb.type;

import com.google.gson.annotations.SerializedName;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class PeerState {

  @SerializedName("last_updated")
  public String lastUpdated;

  @SerializedName("blocks_in_flight")
  public String blocksInFlight;

  public String peer;
}
