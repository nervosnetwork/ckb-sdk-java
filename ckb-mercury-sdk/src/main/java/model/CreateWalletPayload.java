package model;

import java.math.BigInteger;
import java.util.List;

public class CreateWalletPayload {

  public String ident;

  public List<WalletInfo> info;

  public BigInteger fee;

  public CreateWalletPayload(String ident, List<WalletInfo> info, BigInteger fee) {
    this.ident = ident;
    this.info = info;
    this.fee = fee;
  }
}
