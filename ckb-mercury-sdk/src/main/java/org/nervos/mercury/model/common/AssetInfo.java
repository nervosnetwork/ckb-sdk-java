package org.nervos.mercury.model.common;

public class AssetInfo {
  public AssetType assetType;
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
