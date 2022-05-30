package org.nervos.mercury.model.common;

import com.google.gson.annotations.SerializedName;

public class PaginationRequest {
  public Long cursor;
  public Order order;
  public Long limit;
  public boolean returnCount;

  public enum Order {
    @SerializedName(value = "asc", alternate = "Asc")
    ASC,
    @SerializedName(value = "desc", alternate = "Desc")
    DESC
  }
}
