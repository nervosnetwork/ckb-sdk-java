package org.nervos.mercury.model.resp;

import com.google.gson.annotations.SerializedName;

/** @author zjh @Created Date: 2021/7/16 @Description: @Modify by: */
public class BalanceResponse {

  @SerializedName("key_address")
  public String keyAddress;

  @SerializedName("udt_hash")
  public String udtHash;

  public String unconstrained;

  public String fleeting;

  public String locked;
}
