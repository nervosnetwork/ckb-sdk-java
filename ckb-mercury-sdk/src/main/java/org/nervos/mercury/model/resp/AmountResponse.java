package org.nervos.mercury.model.resp;

import com.google.gson.annotations.SerializedName;

/** @author zjh @Created Date: 2021/7/20 @Description: @Modify by: */
public class AmountResponse {
  public String value;

  @SerializedName("udt_hash")
  public String udtHash;

  public BalanceStatus status;
}
