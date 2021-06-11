package model;

import com.google.gson.annotations.SerializedName;

public class WalletInfo {

  @SerializedName("udt_hash")
  public String udtHash;

  public WalletInfo(String udtHash) {
    this.udtHash = udtHash;
  }
}
