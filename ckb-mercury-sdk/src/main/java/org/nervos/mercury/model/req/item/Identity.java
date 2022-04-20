package org.nervos.mercury.model.req.item;

import com.google.common.primitives.Bytes;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.address.AddressTools;

import java.util.Objects;

public class Identity {
  public static final String IDENTITY_FLAGS_CKB = "0x00";

  public String identity;

  public transient String flag;

  public transient byte[] pubKey;

  public Identity(String flag, byte[] pubKey) {
    this.flag = flag;
    this.pubKey = pubKey;
    this.identity = this.toIdentity();
  }

  public String toIdentity() {
    if (Objects.isNull(this.identity)) {
      return Numeric.toHexString(Bytes.concat(Numeric.hexStringToByteArray(flag), pubKey));
    } else {
      return this.identity;
    }
  }

  public static Identity toIdentityByAddress(String address) {
    Script script = AddressTools.parse(address).script;
    return new Identity(Identity.IDENTITY_FLAGS_CKB, script.args);
  }
}
