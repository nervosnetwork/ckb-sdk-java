package org.nervos.ckb.unlocker;

public class Context {
  private String ecPrivateKey;
  private Object payload;

  public Context(String privateKey) {
    this(privateKey, null);
  }

  public Context(String privateKey, Object payload) {
    this.ecPrivateKey = privateKey;
    this.payload = payload;
  }

  public String getPrivateKey() {
    return ecPrivateKey;
  }

  public void setPrivateKey(String ecPrivateKey) {
    this.ecPrivateKey = ecPrivateKey;
  }

  public Object getPayload() {
    return payload;
  }

  public void setPayload(Object payload) {
    this.payload = payload;
  }
}
