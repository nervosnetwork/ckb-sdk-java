package org.nervos.mercury.model.req.item;

import org.nervos.ckb.type.OutPoint;
import org.nervos.ckb.type.Script;

public class ItemFactory {

  public static Item newAddressItem(String address) {
    return new Item(Item.Type.ADDRESS, new Address(address));
  }

  public static Item newIdentityItemByCkb(String pubKey) {
    return new Item(Item.Type.IDENTITY, new Identity(Identity.IDENTITY_FLAGS_CKB, pubKey));
  }

  public static Item newIdentityItemByAddress(String address) {
    return new Item(Item.Type.IDENTITY, Identity.toIdentityByAddress(address));
  }

  public static Item newRecordItemByScript(OutPoint point, Script script) {
    return new Item(Item.Type.RECORD, new Record(point, script));
  }

  public static Item newRecordItemByAddress(OutPoint point, String address) {
    return new Item(Item.Type.RECORD, new Record(point, address));
  }
}
