package model;

import com.google.gson.annotations.SerializedName;
import java.util.Objects;

/** @author zjh @Created Date: 2021/7/22 @Description: @Modify by: */
public class ToKeyAddress implements ToAddress {
  @SerializedName("key_address")
  public KeyAddress keyAddress;

  public ToKeyAddress(String keyAddress, Action action) {
    this.keyAddress = new KeyAddress(keyAddress, action);
  }

  @Override
  public Boolean isPayByFrom() {
    return Objects.equals(Action.pay_by_from, this.keyAddress.action);
  }

  private static class KeyAddress {
    @SerializedName("key_address")
    public String keyAddress;

    public Action action;

    public KeyAddress(String keyAddress, Action action) {
      this.keyAddress = keyAddress;
      this.action = action;
    }
  }
}
