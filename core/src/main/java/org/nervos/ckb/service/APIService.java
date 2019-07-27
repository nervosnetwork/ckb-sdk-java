package org.nervos.ckb.service;

import java.io.IOException;
import java.util.concurrent.Future;
import org.nervos.ckb.methods.Request;
import org.nervos.ckb.methods.Response;

/** Copyright © 2018 Nervos Foundation. All rights reserved. */
public interface APIService {

  /**
   * Perform a synchronous JSON-RPC request.
   *
   * @param request request to perform
   * @param responseType class of a data item returned by the request
   * @param <T> type of a data item returned by the request
   * @return deserialized JSON-RPC response
   * @throws IOException thrown if failed to perform a request
   */
  <T extends Response> T send(Request request, Class<T> responseType) throws IOException;

  /**
   * Performs an asynchronous JSON-RPC request.
   *
   * @param request request to perform
   * @param responseType class of a data item returned by the request
   * @param <T> type of a data item returned by the request
   * @return CompletableFuture that will be completed when a result is returned or if a request has
   *     failed
   */
  <T extends Response> Future<T> sendAsync(Request request, Class<T> responseType);
}
