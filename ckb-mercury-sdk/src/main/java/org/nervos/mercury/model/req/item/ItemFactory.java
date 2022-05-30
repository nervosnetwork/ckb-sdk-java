package org.nervos.mercury.model.req.item;

import org.bouncycastle.util.encoders.Hex;
import org.nervos.ckb.type.OutPoint;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.utils.Numeric;

import java.util.Arrays;

import static org.nervos.ckb.type.Script.SECP256_BLAKE160_SIGNHASH_ALL_CODE_HASH;

// https://github.com/XuJiandong/docs-bank/blob/master/omni_lock.md#auth
public class ItemFactory {

  public static Item newAddressItem(String address) {
    return new Item(Item.Type.ADDRESS, address);
  }

  public static Item newIdentityItemByCkb(byte[] pubKeyHash) {
    if (pubKeyHash.length != 20) {
      throw new IllegalArgumentException("pubKeyHash length is not 20");
    }
    byte[] content = new byte[21];
    content[0] = 0x00;
    System.arraycopy(pubKeyHash, 0, content, 1, 20);
    return new Item(Item.Type.IDENTITY, Hex.toHexString(content));
  }

  public static Item newIdentityItemByCkb(String pubKeyHash) {
    return newIdentityItemByCkb(Numeric.hexStringToByteArray(pubKeyHash));
  }

  public static Item newIdentityItemBySecp256k1Blake160SignhashAllAddress(String address) {
    org.nervos.ckb.utils.address.Address addr = org.nervos.ckb.utils.address.Address.decode(address);
    Script script = addr.getScript();
    if (!Arrays.equals(SECP256_BLAKE160_SIGNHASH_ALL_CODE_HASH, script.codeHash)) {
      throw new IllegalArgumentException("address is not a SECP256_BLAKE160_SIGNHASH_ALL address");
    }
    if (script.hashType != Script.HashType.TYPE) {
      throw new IllegalArgumentException("address hash type should be TYPE");
    }
    return newIdentityItemByCkb(script.args);
  }

  public static Item newOutPointItem(byte[] txHash, int index) {
    return newOutPointItem(new OutPoint(txHash, index));
  }

  public static Item newOutPointItem(OutPoint outPoint) {
    return new Item(Item.Type.OUT_POINT, outPoint);
  }
}
