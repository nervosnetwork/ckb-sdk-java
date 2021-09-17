package org.nervos.mercury.model.req.item;

import com.google.common.primitives.Bytes;
import com.google.gson.annotations.SerializedName;
import org.nervos.ckb.utils.Numeric;

public class Identity implements Item {
  public static final String IDENTITY_FLAGS_CKB = "0x00";

  @SerializedName("Identity")
  public String identity;

  public transient String flag;

  public transient String pubKey;

  public Identity() {}

  public Identity(String flag, String pubKey) {
    this.flag = flag;
    this.pubKey = pubKey;
    this.identity = this.toIdentity();
  }

  public String toIdentity() {
    return Numeric.toHexString(
        Bytes.concat(Numeric.hexStringToByteArray(flag), Numeric.hexStringToByteArray(pubKey)));
  }
}
