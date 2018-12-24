package org.nervos.ckb.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.nervos.ckb.request.Request;
import org.nervos.ckb.response.Response;
import org.nervos.ckb.utils.Async;
import org.nervos.ckb.utils.ObjectMapperFactory;

import java.io.IOException;
import java.io.Reader;
import java.util.concurrent.Future;

/**
 * Created by duanyytop on 2018-12-20.
 * <p>
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 *
 * Base api service implementation.
 */
public abstract class APIServiceImpl implements APIService {

    protected final ObjectMapper objectMapper;

    protected abstract Reader performIO(String payload) throws IOException;

    public APIServiceImpl() {
        objectMapper = ObjectMapperFactory.getObjectMapper();
    }

    @Override
    public <T extends Response> T send(
            Request request, Class<T> responseType) throws IOException {
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
        return Async.run(() ->
                APIServiceImpl.this.send(jsonRpc20Request, responseType));
    }

}
