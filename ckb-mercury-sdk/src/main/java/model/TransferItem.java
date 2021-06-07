package model;

import lombok.Data;

import java.math.BigInteger;

@Data
public class TransferItem {
    private ToAccount to;
    private BigInteger amount;

    public TransferItem(ToAccount to, BigInteger amount) {
        this.to = to;
        this.amount = amount;
    }
}
