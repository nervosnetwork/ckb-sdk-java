package model;

import com.google.gson.annotations.SerializedName;

public class WalletInfo {

  @SerializedName("udt_hash")
  public String udtHash;

  @SerializedName("min_ckb")
  public Integer minCkb = 3;

  @SerializedName("min_udt")
  public Integer minUdt;

  public WalletInfo(String udtHash, Integer minUdt) {
    this.udtHash = udtHash;
    this.minUdt = minUdt;
  }
}
