package org.nervos.ckb.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.Reader;
import java.util.concurrent.Future;
import org.nervos.ckb.methods.Request;
import org.nervos.ckb.methods.Response;
import org.nervos.ckb.utils.Async;
import org.nervos.ckb.utils.ObjectMapperFactory;

/**
 * Created by duanyytop on 2018-12-20. Copyright © 2018 Nervos Foundation. All rights reserved.
 *
 * <p>Base api service implementation.
 */
public abstract class APIServiceImpl implements APIService {

  private final ObjectMapper objectMapper;

  protected abstract Reader performIO(String payload) throws IOException;

  APIServiceImpl() {
    objectMapper = ObjectMapperFactory.getObjectMapper();
  }

  @Override
  public <T extends Response> T send(Request request, Class<T> responseType) throws IOException {
    String payload = objectMapper.writeValueAsString(request);

    try (Reader result = performIO(payload)) {
      if (result != null) {
        return objectMapper.readValue(result, responseType);
      } else {
        return null;
      }
    }
  }

  @Override
  public <T extends Response> Future<T> sendAsync(
      final Request jsonRpc20Request, final Class<T> responseType) {
    return Async.run(() -> APIServiceImpl.this.send(jsonRpc20Request, responseType));
  }
}
