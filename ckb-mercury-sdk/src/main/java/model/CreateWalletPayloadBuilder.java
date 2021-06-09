package model;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class CreateWalletPayloadBuilder {

    public String address;
    public List<WalletInfo> info = new ArrayList<>(2);
    public BigInteger fee;

    public void address(String address) {
        this.address = address;
    }

    public void addWalletInfo(WalletInfo info) {
        this.info.add(info);
    }

    public void fee(BigInteger fee) {
        this.fee = fee;
    }

    public CreateWalletPayload build() {
        return new CreateWalletPayload(address, info, fee);
    }


}
