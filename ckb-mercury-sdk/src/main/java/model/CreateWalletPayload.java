package model;

import lombok.Data;

import java.math.BigInteger;
import java.util.List;

@Data
public class CreateWalletPayload {
    private String address;
    private List<WalletInfo> info;
    private BigInteger fee;

    public CreateWalletPayload(String address, List<WalletInfo> info, BigInteger fee) {
        this.address = address;
        this.info = info;
        this.fee = fee;
    }
}
