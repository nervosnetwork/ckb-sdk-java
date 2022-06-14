package org.nervos.mercury.model.req.item;

import org.bouncycastle.util.encoders.Hex;
import org.nervos.ckb.Network;
import org.nervos.ckb.type.OutPoint;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.address.Address;

import java.util.Arrays;

import static org.nervos.ckb.type.Script.*;

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

  /**
   * Create a new identity item from secp256_blake160_signhash_all or ACP address
   *
   * @param address a secp256_blake160_signhash_all or ACP address
   * @return a new identity item
   */
  public static Item newIdentityItemByAddress(String address) {
    Address a = Address.decode(address);
    Script script = a.getScript();
    if (!isValidAddress(a)) {
      throw new IllegalArgumentException("not a valid secp256_blake160_signhash_all or ACP address");
    }
    return newIdentityItemByCkb(script.args);
  }

  /**
   * Check if the address is a valid secp256_blake160_signhash_all or ACP address.
   */
  private static boolean isValidAddress(Address address) {
    Script script = address.getScript();
    Network network = address.getNetwork();
    if (script.hashType != Script.HashType.TYPE) {
      return false;
    }
    if (Arrays.equals(SECP256_BLAKE160_SIGNHASH_ALL_CODE_HASH, script.codeHash)
        || (Network.TESTNET == network && Arrays.equals(ANY_CAN_PAY_CODE_HASH_TESTNET, script.codeHash)
        || (Network.MAINNET == network && Arrays.equals(ANY_CAN_PAY_CODE_HASH_MAINNET, script.codeHash)))) {
      return true;
    }
    return false;
  }

  public static Item newOutPointItem(byte[] txHash, int index) {
    return newOutPointItem(new OutPoint(txHash, index));
  }

  public static Item newOutPointItem(OutPoint outPoint) {
    return new Item(Item.Type.OUT_POINT, outPoint);
  }
}
