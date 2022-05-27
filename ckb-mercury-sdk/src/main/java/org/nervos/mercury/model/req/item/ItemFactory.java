package org.nervos.mercury.model.req.item;

import org.bouncycastle.util.encoders.Hex;
import org.nervos.ckb.type.OutPoint;
import org.nervos.ckb.utils.Numeric;

// https://github.com/XuJiandong/docs-bank/blob/master/omni_lock.md#auth
public class ItemFactory {

  public static Item newAddressItem(String address) {
    return new Item(Item.Type.ADDRESS, address);
  }

  public static Item newIdentityItemByCkb(byte[] pubKey) {
    byte[] content = new byte[21];
    content[0] = 0x00;
    System.arraycopy(pubKey, 0, content, 1, 20);
    return new Item(Item.Type.IDENTITY, Hex.toHexString(content));
  }

  public static Item newIdentityItemByCkb(String pubKey) {
    return newIdentityItemByCkb(Numeric.hexStringToByteArray(pubKey));
  }

  public static Item newIdentityItemBySecp256k1Blake160SignhashAllAddress(String address) {
    org.nervos.ckb.utils.address.Address addr = org.nervos.ckb.utils.address.Address.decode(address);
    byte[] content = new byte[21];
    content[0] = 0x00;
    System.arraycopy(addr.getScript(), 1, content, 1, 20);
    return new Item(Item.Type.IDENTITY, Hex.toHexString(content));
  }

  public static Item newOutPointItem(byte[] txHash, int index) {
    return newOutPointItem(new OutPoint(txHash, index));
  }

  public static Item newOutPointItem(OutPoint outPoint) {
    return new Item(Item.Type.OUT_POINT, outPoint);
  }
}
