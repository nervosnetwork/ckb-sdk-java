package org.nervos.ckb.utils.address;

import org.nervos.ckb.address.Network;
import org.nervos.ckb.type.Script;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class AddressParseResult {
  public Network network;
  public Script script;

  public AddressParseResult(Network network, Script script) {
    this.network = network;
    this.script = script;
  }
}
