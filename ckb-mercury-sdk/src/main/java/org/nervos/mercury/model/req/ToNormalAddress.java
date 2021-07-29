package org.nervos.mercury.model.req;

import com.google.gson.annotations.SerializedName;

/** @author zjh @Created Date: 2021/7/23 @Description: @Modify by: */
public class ToNormalAddress implements ToAddress {
  @SerializedName("normal_address")
  public String normalAddress;

  public ToNormalAddress(String normalAddress) {
    this.normalAddress = normalAddress;
  }

  @Override
  public Boolean isPayByFrom() {
    return Boolean.FALSE;
  }
}
