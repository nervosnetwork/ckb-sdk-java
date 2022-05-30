package org.nervos.ckb.service;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;
import org.nervos.ckb.service.adapter.*;

import java.math.BigInteger;

public class GsonFactory {
  public static Gson create() {

    return new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
        .registerTypeAdapterFactory(new CheckedEnumTypeAdapterFactory())
        .registerTypeAdapter(byte[].class, new ByteArrayTypeAdapter())
        .registerTypeAdapter(int.class, new IntegerTypeAdapter())
        .registerTypeAdapter(Integer.class, new IntegerTypeAdapter())
        .registerTypeAdapter(long.class, new LongTypeAdapter())
        .registerTypeAdapter(Long.class, new LongTypeAdapter())
        .registerTypeAdapter(BigInteger.class, new BigIntegerTypeAdapter())
        .create();
  }
}
