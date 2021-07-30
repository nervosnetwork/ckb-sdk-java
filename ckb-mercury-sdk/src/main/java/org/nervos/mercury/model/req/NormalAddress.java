package org.nervos.mercury.model.req;

import com.google.gson.annotations.SerializedName;

/**
 * Only the balance of the address in the corresponding format is available. For example, the
 * secp256k1 address will only query the balance of the secp256k1 format, and will not contain the
 * balance of the remaining formats. Copyright © 2019 Nervos Foundation. All rights reserved.
 */
public class NormalAddress extends QueryAddress {
  @SerializedName("NormalAddress")
  public String normalAddress;

  public NormalAddress(String normalAddress) {
    this.normalAddress = normalAddress;
  }

  @Override
  public String getAddress() {
    return this.normalAddress;
  }
}
