package model;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class TransferPayloadBuilder {

    private String udtHash;
    private FromAccount from;
    private List<TransferItem> items = new ArrayList<>(8);
    private String change;
    private BigInteger fee;

    public void addFrom(FromAccount from) {
        this.from = from;
    }

    public void addUdtHash(String udtHash) {
        this.udtHash = udtHash;
    }

    public void addItem(ToAccount to, BigInteger amount) {
        this.items.add(new TransferItem(to, amount));
    }

    public void addChange(String change) {
        this.change = change;
    }

    public void addFee(BigInteger fee) {
        this.fee = fee;
    }

    public TransferPayload build() {
        assert !(this.from == null) : "from not null";
        assert !(this.items.size() <= 0) : "items not empty";

        TransferPayload payload = new TransferPayload(this.udtHash, this.from, this.items, this.change, this.fee);
        return payload;

    }
}
