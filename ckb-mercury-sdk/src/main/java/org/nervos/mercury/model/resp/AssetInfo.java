package org.nervos.mercury.model.resp;

public class AssetInfo {
  public AssetType assetType;
  public String udtHash;

  public enum AssetType {
    CKB,
    UDT
  }

  public AssetInfo() {}

  public AssetInfo(AssetType assetType, String udtHash) {
    this.assetType = assetType;
    this.udtHash = udtHash;
  }

  public static AssetInfo newCkbAsset() {
    return new AssetInfo(AssetType.CKB, null);
  }

  public static AssetInfo newUdtAsset(String udtHash) {
    return new AssetInfo(AssetType.UDT, udtHash);
  }
}
