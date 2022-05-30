package org.nervos.mercury.model.req.since;

import com.google.gson.annotations.SerializedName;

public class SinceConfig {
  public SinceFlag flag;
  @SerializedName("type_")
  public SinceType type;
  public long value;
}
