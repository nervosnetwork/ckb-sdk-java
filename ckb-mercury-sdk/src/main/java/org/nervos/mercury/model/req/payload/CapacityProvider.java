package org.nervos.mercury.model.req.payload;

import com.google.gson.annotations.SerializedName;

public enum CapacityProvider {
  @SerializedName("From")
  FROM,
  @SerializedName("To")
  TO
}
