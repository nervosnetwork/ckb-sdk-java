package model.resp;

import com.google.gson.annotations.SerializedName;

/** @author zjh @Created Date: 2021/7/20 @Description: @Modify by: */
public class OperationResponse {

  public Integer id;

  @SerializedName("key_address")
  public String keyAddress;

  @SerializedName("normal_address")
  public String normalAddress;

  public AmountResponse amount;
}
