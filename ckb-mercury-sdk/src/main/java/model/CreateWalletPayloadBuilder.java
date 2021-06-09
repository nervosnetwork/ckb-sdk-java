package model;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class CreateWalletPayloadBuilder {

  public String ident;

  public List<WalletInfo> info = new ArrayList<>(2);

  public BigInteger fee;

  public void ident(String ident) {
    this.ident = ident;
  }

  public void addWalletInfo(WalletInfo info) {
    this.info.add(info);
  }

  public void fee(BigInteger fee) {
    this.fee = fee;
  }

  public CreateWalletPayload build() {
    return new CreateWalletPayload(ident, info, fee);
  }
}
