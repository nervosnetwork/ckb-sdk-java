package org.nervos.mercury.model.common;

import com.google.gson.annotations.SerializedName;

public class AssetInfo {
  @SerializedName("asset_type")
  public AssetType assetType;

  @SerializedName("udt_hash")
  public String udtHash;

  private AssetInfo(AssetType assetType, String udtHash) {
    this.assetType = assetType;
    this.udtHash = udtHash;
  }

  public static AssetInfo newCkbAsset() {
    return new AssetInfo(
        AssetType.CKB, "0x0000000000000000000000000000000000000000000000000000000000000000");
  }

  public static AssetInfo newUdtAsset(String udtHash) {
    return new AssetInfo(AssetType.UDT, udtHash);
  }
}
