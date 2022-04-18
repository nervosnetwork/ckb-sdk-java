package org.nervos.mercury.model.req.payload;

import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

import org.nervos.mercury.model.req.From;

public class DaoDepositPayload {

  public From from;

  public String to;

  public long amount;

  @SerializedName("fee_rate")
  public BigInteger feeRate;

  protected DaoDepositPayload() {
  }
}
