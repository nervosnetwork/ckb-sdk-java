package model;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class TransferPayloadBuilder {

    private String udt_hash;
    private FromAccount from;
    private List<TransferItem> items = new ArrayList<>(8);
    private String change;
    private BigInteger fee;

    public void addFrom(FromAccount from) {
        this.from = from;
    }

    public void addUdtHash(String udtHash) {
        this.udt_hash = udtHash;
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
//        assert (!(this.udt_hash == null) || !(this.udt_hash == "")) : "udtHash not empty";
        assert !(this.from == null) : "from not null";
        assert !(this.items.size() <= 0) : "items not empty";
//        assert !(Integer.parseInt(this.fee) < 0) : "fee cannot be less than 0";

        TransferPayload payload = new TransferPayload(this.udt_hash, this.from, this.items, this.change, this.fee);
        return payload;

    }
}
