package org.nervos.mercury.model.common;

import com.google.gson.annotations.SerializedName;

public class AssetInfo {
  @SerializedName("asset_type")
  public AssetType assetType;

  @SerializedName("udt_hash")
  public byte[] udtHash;

  private AssetInfo(AssetType assetType, byte[] udtHash) {
    this.assetType = assetType;
    this.udtHash = udtHash;
  }

  public static AssetInfo newCkbAsset() {
    return new AssetInfo(AssetType.CKB, new byte[32]);
  }

  public static AssetInfo newUdtAsset(byte[] udtHash) {
    return new AssetInfo(AssetType.UDT, udtHash);
  }
}
