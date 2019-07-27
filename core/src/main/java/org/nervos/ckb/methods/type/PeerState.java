package org.nervos.ckb.methods.type;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class PeerState {

  @JsonProperty("last_updated")
  public String lastUpdated;

  @JsonProperty("blocks_in_flight")
  public String blocksInFlight;

  public String peer;
}
