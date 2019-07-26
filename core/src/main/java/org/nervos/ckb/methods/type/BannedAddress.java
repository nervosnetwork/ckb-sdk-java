package org.nervos.ckb.methods.type;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Created by duanyytop on 2019-07-26. Copyright © 2019 Nervos Foundation. All rights reserved. */
public class BannedAddress {
  public String address;
  public String command;

  @JsonProperty("ban_time")
  public String banTime;

  public String absolute;
  public String reason;
}
