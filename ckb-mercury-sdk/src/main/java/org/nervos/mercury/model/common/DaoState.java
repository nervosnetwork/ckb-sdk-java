package org.nervos.mercury.model.common;

import com.google.gson.annotations.SerializedName;

public enum DaoState {
  @SerializedName("Deposit")
  DEPOSIT,
  @SerializedName("Withdraw")
  WITHDRAW,
}
