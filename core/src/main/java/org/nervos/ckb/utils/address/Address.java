package org.nervos.ckb.utils.address;

import org.nervos.ckb.address.Network;
import org.nervos.ckb.type.Script;

import java.util.Objects;

public class Address {
  Script script;
  Network network;

  public Address() {
  }

  public Address(Script script, Network network) {
    this.script = script;
    this.network = network;
  }

  public Script getScript() {
    return script;
  }

  public Address setScript(Script script) {
    Objects.requireNonNull(script);
    this.script = script;
    return this;
  }

  public Network getNetwork() {
    return network;
  }

  public Address setNetwork(Network network) {
    Objects.requireNonNull(network);
    this.network = network;
    return this;

  }
}