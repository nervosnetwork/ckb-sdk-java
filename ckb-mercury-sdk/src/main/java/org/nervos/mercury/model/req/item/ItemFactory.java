package org.nervos.mercury.model.req.item;

import org.nervos.ckb.type.OutPoint;
import org.nervos.ckb.utils.Numeric;

public class ItemFactory {

  public static Item newAddressItem(String address) {
    return new Item(Item.Type.ADDRESS, new Address(address));
  }

  public static Item newIdentityItemByCkb(byte[] pubKey) {
    return new Item(Item.Type.IDENTITY, new Identity(Identity.IDENTITY_FLAGS_CKB, pubKey));
  }


  public static Item newIdentityItemByCkb(String pubKey) {
    return newIdentityItemByCkb(Numeric.hexStringToByteArray(pubKey));
  }

  public static Item newIdentityItemByAddress(String address) {
    return new Item(Item.Type.IDENTITY, Identity.toIdentityByAddress(address));
  }

  public static Item newOutPointItem(byte[] txHash, int index) {
    return newOutPointItem(new OutPoint(txHash, index));
  }

  public static Item newOutPointItem(OutPoint outPoint) {
    return new Item(Item.Type.OUT_POINT, outPoint);
  }
}
