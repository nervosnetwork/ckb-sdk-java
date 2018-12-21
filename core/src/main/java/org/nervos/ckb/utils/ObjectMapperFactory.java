package org.nervos.ckb.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;

/**
 * Factory for managing our ObjectMapper instances.
 */
public class ObjectMapperFactory {

    private static final ObjectMapper DEFAULT_OBJECT_MAPPER = new ObjectMapper();

    static {
        configureObjectMapper(DEFAULT_OBJECT_MAPPER);
    }

    public static ObjectMapper getObjectMapper() {
        return DEFAULT_OBJECT_MAPPER;

    }

    public static ObjectReader getObjectReader() {
        return DEFAULT_OBJECT_MAPPER.reader();
    }

    private static ObjectMapper configureObjectMapper(ObjectMapper objectMapper) {

        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return objectMapper;
    }
}
