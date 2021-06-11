package model.resp;

import com.google.gson.annotations.SerializedName;

public class GetBalanceResponse {

  @SerializedName("owned")
  public String unconstrained;

  @SerializedName("claimable")
  public String fleeting;

  @SerializedName("locked")
  private String locked;
}
