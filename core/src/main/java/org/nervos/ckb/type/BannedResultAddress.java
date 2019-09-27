package org.nervos.ckb.type;

import com.google.gson.annotations.SerializedName;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class BannedResultAddress {

  public String address;

  @SerializedName("ban_reason")
  public String banReason;

  @SerializedName("ban_until")
  public String banUntil;

  @SerializedName("created_at")
  public String createdAt;
}
