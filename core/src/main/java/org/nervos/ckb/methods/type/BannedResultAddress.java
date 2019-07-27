package org.nervos.ckb.methods.type;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class BannedResultAddress {

  public String address;

  @JsonProperty("ban_reason")
  public String banReason;

  @JsonProperty("ban_until")
  public String banUntil;

  @JsonProperty("created_at")
  public String createdAt;
}
