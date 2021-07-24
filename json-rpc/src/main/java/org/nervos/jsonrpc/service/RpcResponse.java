package org.nervos.jsonrpc.service;

/** Copyright © 2020 Nervos Foundation. All rights reserved. */
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
