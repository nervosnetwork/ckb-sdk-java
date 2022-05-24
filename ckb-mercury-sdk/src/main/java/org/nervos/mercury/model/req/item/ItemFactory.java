package org.nervos.mercury.model.req.item;

import org.bouncycastle.util.encoders.Hex;
import org.nervos.ckb.Network;
import org.nervos.ckb.sign.SystemContract;
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

  public static Item newIdentityItemByAddress(String address) {
    org.nervos.ckb.utils.address.Address addr = org.nervos.ckb.utils.address.Address.decode(address);
    Network network = addr.getNetwork();
    SystemContract.Type type = network.getSystemContractType(addr.getScript());
    byte[] content = new byte[21];
    if (type == SystemContract.Type.SECP256K1_BLAKE160_SIGHASH_ALL) {
      content[0] = 0x00;
      System.arraycopy(addr.getScript(), 1, content, 1, 20);
    } else if (type == SystemContract.Type.SECP256K1_BLAKE160_MULTISIG_ALL) {
      content[0] = 0x06;
      System.arraycopy(addr.getScript(), 1, content, 1, 20);
    } else {
      throw new IllegalArgumentException("Unsupported address type");
    }
    return new Item(Item.Type.IDENTITY, Hex.toHexString(content));
  }

  public static Item newOutPointItem(byte[] txHash, int index) {
    return newOutPointItem(new OutPoint(txHash, index));
  }

  public static Item newOutPointItem(OutPoint outPoint) {
    return new Item(Item.Type.OUT_POINT, outPoint);
  }
}
