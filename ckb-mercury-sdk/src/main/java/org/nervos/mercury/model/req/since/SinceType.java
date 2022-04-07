package org.nervos.mercury.model.req.since;

import com.google.gson.annotations.SerializedName;

public enum SinceType {
  @SerializedName("BlockNumber")
  BLOCK_NUMBER,
  @SerializedName("EpochNumber")
  EPOCH_NUMBER,
  @SerializedName("Timestamp")
  TIMESTAMP,
}
