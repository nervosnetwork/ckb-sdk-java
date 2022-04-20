package org.nervos.mercury.model.req.since;

import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

public class SinceConfig {
  public SinceFlag flag;
  @SerializedName("type_")
  public SinceType type;
  public BigInteger value;
}
