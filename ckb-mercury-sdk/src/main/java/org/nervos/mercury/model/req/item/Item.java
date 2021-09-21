package org.nervos.mercury.model.req.item;

import org.nervos.ckb.type.OutPoint;
import org.nervos.ckb.type.Script;

public interface Item {
  static Item newAddressItem(String address) {
    return new Address(address);
  }

  static Item newIdentityItemByCkb(String pubKey) {
    return new Identity(Identity.IDENTITY_FLAGS_CKB, pubKey);
  }

  static Item newIdentityItemByAddress(String address) {
    return Identity.toIdentityByAddress(address);
  }

  /**
   * This method is used for tests and examples, if you need to use record, please use the record
   * returned by mercury
   *
   * @param point
   * @param script
   * @return
   */
  static Item newRecordItemByScript(OutPoint point, Script script) {
    return new Record(point, script);
  }

  /**
   * This method is used for tests and examples, if you need to use record, please use the record
   * returned by mercury
   *
   * @param point
   * @param address
   * @return
   */
  static Item newRecordItemByAddress(OutPoint point, String address) {
    return new Record(point, address);
  }
}
