package model;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import java.util.List;

public class CreateWalletPayload {

  public String ident;

  public List<WalletInfo> info;

  @SerializedName("fee_rate")
  public BigInteger feeRate = new BigInteger("1000");

  public CreateWalletPayload(String ident, List<WalletInfo> info, BigInteger feeRate) {
    this.ident = ident;
    this.info = info;
    this.feeRate = feeRate;
  }
}
