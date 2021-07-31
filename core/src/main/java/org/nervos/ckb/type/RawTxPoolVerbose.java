package org.nervos.ckb.type;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

/** Copyright © 2021 Nervos Foundation. All rights reserved. */
public class RawTxPoolVerbose {
  public Map<String, VerboseDetail> pending;
  public Map<String, VerboseDetail> proposed;

  public static class VerboseDetail {
    public String cycles;
    public String size;
    public String fee;

    @SerializedName("ancestors_size")
    public String ancestorsSize;

    @SerializedName("ancestors_cycles")
    public String ancestorsCycles;

    @SerializedName("ancestors_count")
    public String ancestorsCount;
  }
}
