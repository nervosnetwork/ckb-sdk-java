package org.nervos.mercury.model.common;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import java.util.List;

public class PaginationRequest {
  public List<Integer> cursor;
  public Order order;
  public int limit;
  // TODO: 2021/8/26 zhengjianhui
  public int skip;

  @SerializedName("return_count")
  public boolean returnCount;

  public enum Order {
    @SerializedName("ASC")
    ASC,
    @SerializedName("Desc")
    DESC;
  }
}
