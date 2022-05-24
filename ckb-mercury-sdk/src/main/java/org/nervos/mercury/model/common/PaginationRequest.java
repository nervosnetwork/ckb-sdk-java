package org.nervos.mercury.model.common;

import com.google.gson.annotations.SerializedName;

public class PaginationRequest {
  public byte[] cursor;
  public Order order;
  public int limit;
  public boolean returnCount;

  public enum Order {
    @SerializedName("asc")
    ASC,
    @SerializedName("desc")
    DESC
  }
}
