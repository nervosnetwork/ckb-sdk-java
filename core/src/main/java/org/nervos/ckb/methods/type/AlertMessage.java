package org.nervos.ckb.methods.type;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class AlertMessage {
  public String id;
  public String priority;

  @JsonProperty("notice_until")
  public String noticeUntil;

  public String message;
}
