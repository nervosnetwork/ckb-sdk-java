package model;

import com.google.gson.annotations.SerializedName;

/** @author zjh @Created Date: 2021/7/17 @Description: @Modify by: */
public class KeyAddress extends QueryAddress {
  @SerializedName("KeyAddress")
  public String keyAddress;

  public KeyAddress(String keyAddress) {
    this.keyAddress = keyAddress;
  }

  @Override
  public String getAddress() {
    return this.keyAddress;
  }
}
