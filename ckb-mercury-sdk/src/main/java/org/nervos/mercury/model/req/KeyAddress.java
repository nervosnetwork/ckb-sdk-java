package org.nervos.mercury.model.req;

import com.google.gson.annotations.SerializedName;

/**
 * Only addresses in secp256k1 format are available, and the balance contains the balance of
 * addresses in other format. Copyright © 2019 Nervos Foundation. All rights reserved.
 */
public class KeyAddress extends QueryAddress {
  @SerializedName("KeyAddress")
  public String keyAddress;

  public KeyAddress(String keyAddress) {
    this.keyAddress = keyAddress;
  }

  @Override
  public String getAddress() {
    return this.keyAddress;
  }
}
