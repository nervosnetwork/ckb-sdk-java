package org.nervos.mercury.model.resp;

public class AssetInfo {
  public AssetType assetType;
  public String udtHash;

  enum AssetType {
    CKB,
    UDT
  }
}
