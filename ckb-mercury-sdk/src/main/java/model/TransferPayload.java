package model;

import lombok.Data;

import java.math.BigInteger;
import java.util.List;

@Data
public class TransferPayload {
    private String udt_hash;
    private FromAccount from;
    private List<TransferItem> items;
    private String change;
    private BigInteger fee;

    public TransferPayload(String udt_hash, FromAccount from, List<TransferItem> items, String change, BigInteger fee) {
        this.udt_hash = udt_hash;
        this.from = from;
        this.items = items;
        this.change = change;
        this.fee = fee;
    }
}
