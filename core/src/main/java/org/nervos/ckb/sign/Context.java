package org.nervos.ckb.sign;

import org.nervos.ckb.crypto.secp256k1.ECKeyPair;

public class Context {
  private ECKeyPair keyPair;
  private Object payload;

  public Context(String privateKey) {
    this(ECKeyPair.create(privateKey), null);
  }

  public Context(byte[] privateKey) {
    this(ECKeyPair.create(privateKey), null);
  }

  public Context(ECKeyPair keyPair) {
    this(keyPair, null);
  }

  public Context(ECKeyPair keyPair, Object payload) {
    this.keyPair = keyPair;
    this.payload = payload;
  }

  public Context(String privateKey, Object payload) {
    this(privateKey);
    this.payload = payload;
  }

  public ECKeyPair getKeyPair() {
    return keyPair;
  }

  public void setKeyPair(ECKeyPair ecPrivateKey) {
    this.keyPair = ecPrivateKey;
  }

  public Object getPayload() {
    return payload;
  }

  public void setPayload(Object payload) {
    this.payload = payload;
  }
}
