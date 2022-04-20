package org.nervos.ckb.service;

public class RpcResponse<T> {
  public long id;
  public String jsonrpc;
  public T result;
  public Error error;

  static class Error {
    public int code;
    public String message;
  }
}
