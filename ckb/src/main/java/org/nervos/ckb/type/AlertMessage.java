package org.nervos.ckb.type;

import com.google.gson.annotations.SerializedName;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class AlertMessage {
  public String id;
  public String priority;

  @SerializedName("notice_until")
  public String noticeUntil;

  public String message;
}
