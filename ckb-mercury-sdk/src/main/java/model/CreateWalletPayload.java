package model;

import java.math.BigInteger;
import java.util.List;

public class CreateWalletPayload {
    public String address;
    public List<WalletInfo> info;
    public BigInteger fee;

    public CreateWalletPayload(String address, List<WalletInfo> info, BigInteger fee) {
        this.address = address;
        this.info = info;
        this.fee = fee;
    }
}
