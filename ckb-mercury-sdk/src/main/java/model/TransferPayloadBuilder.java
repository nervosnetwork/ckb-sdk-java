package model;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class TransferPayloadBuilder {

  private String udtHash;

  private FromAccount from;

  private List<TransferItem> items = new ArrayList<>(8);

  private String change;

  private BigInteger feeRate = new BigInteger("1000");

  public void from(FromAccount from) {
    this.from = from;
  }

  public void udtHash(String udtHash) {
    this.udtHash = udtHash;
  }

  public void addItem(ToAccount to, BigInteger amount) {
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

    TransferPayload payload =
        new TransferPayload(this.udtHash, this.from, this.items, this.change, this.feeRate);
    return payload;
  }
}
