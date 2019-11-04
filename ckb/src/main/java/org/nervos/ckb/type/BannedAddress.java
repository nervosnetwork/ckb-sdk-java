package org.nervos.ckb.type;

import com.google.gson.annotations.SerializedName;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class BannedAddress {
  public String address;
  public String command;

  @SerializedName("ban_time")
  public String banTime;

  public boolean absolute;
  public String reason;

  public BannedAddress() {}

  public BannedAddress(
      String address, String command, String banTime, boolean absolute, String reason) {
    this.address = address;
    this.command = command;
    this.banTime = banTime;
    this.absolute = absolute;
    this.reason = reason;
  }
}
