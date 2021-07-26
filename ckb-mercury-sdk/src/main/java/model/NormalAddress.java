package model;

import com.google.gson.annotations.SerializedName;

/** @author zjh @Created Date: 2021/7/17 @Description: @Modify by: */
public class NormalAddress extends QueryAddress {
  @SerializedName("NormalAddress")
  public String normalAddress;

  public NormalAddress(String normalAddress) {
    this.normalAddress = normalAddress;
  }

  @Override
  public String getAddress() {
    return this.normalAddress;
  }
}
