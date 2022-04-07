package org.nervos.mercury.model.resp;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;

/** @author zjh @Created Date: 2021/7/20 @Description: @Modify by: */
public class BurnInfo {
  @SerializedName("udt_hash")
  public byte[] udtHash;

  public BigInteger amount;
}
