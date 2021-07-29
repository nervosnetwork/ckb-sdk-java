package org.nervos.mercury.model;

import java.math.BigInteger;
import java.util.ArrayList;
import org.nervos.mercury.FeeConstant;
import org.nervos.mercury.model.req.FromAddresses;
import org.nervos.mercury.model.req.ToAddress;
import org.nervos.mercury.model.req.TransferItem;
import org.nervos.mercury.model.req.TransferPayload;

public class TransferPayloadBuilder extends TransferPayload {

  public TransferPayloadBuilder() {
    this.feeRate = FeeConstant.DEFAULT_FEE_RATE;
    this.items = new ArrayList<>(2);
  }

  public void from(FromAddresses from) {
    this.from = from;
  }

  public void udtHash(String udtHash) {
    this.udtHash = udtHash;
  }

  public void addItem(ToAddress to, BigInteger amount) {
    this.items.add(new TransferItem(to, amount));
  }

  public void change(String change) {
    this.change = change;
  }

  public void feeRate(BigInteger feeRate) {
    this.feeRate = feeRate;
  }

  public TransferPayload build() {
    assert !(this.from == null) : "from not null";
    assert !(this.items.size() <= 0) : "items not empty";

    return this;
  }
}
