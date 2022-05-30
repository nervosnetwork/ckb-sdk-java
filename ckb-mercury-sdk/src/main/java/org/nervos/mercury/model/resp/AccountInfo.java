package org.nervos.mercury.model.resp;

import com.google.gson.annotations.SerializedName;

public class AccountInfo {
  public int accountNumber;
  public String accountAddress;
  public Type accountType;

  public enum Type {
    @SerializedName("Acp")
    ACP,
    @SerializedName("PwLock")
    PW_LOCK
  }
}
