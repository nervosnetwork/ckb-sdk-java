package org.nervos.ckb.type;

import org.nervos.ckb.crypto.Blake2b;
import org.nervos.ckb.utils.Numeric;

public class PackedHeader {
  /**
   * 0x-prefixed hex string
   */
  public String header;

  public byte[] getHeaderBytes() {
    return Numeric.hexStringToByteArray(this.header);
  }

  public byte[] calculateHash() {
    return Blake2b.digest(this.getHeaderBytes());
  }
}
