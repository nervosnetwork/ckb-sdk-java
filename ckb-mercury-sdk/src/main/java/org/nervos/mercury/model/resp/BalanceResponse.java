package org.nervos.mercury.model.resp;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;

/** @author zjh @Created Date: 2021/7/16 @Description: @Modify by: */
public class BalanceResponse {
  public String address;

  public AssetInfo assetInfo;

  public BigInteger free;

  public BigInteger claimable;

  public BigInteger freezed;

  public static class RpcBalanceResponse {
    @SerializedName("key_address")
    public String keyAddress;

    @SerializedName("udt_hash")
    public String udtHash;

    public BigInteger unconstrained;

    public BigInteger fleeting;

    public BigInteger locked;
  }
}
