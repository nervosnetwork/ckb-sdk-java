package org.nervos.ckb.type;

import com.google.gson.annotations.SerializedName;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class AlertMessage {
  public int id;
  public int priority;

  @SerializedName("notice_until")
  public long noticeUntil;

  public String message;
}
