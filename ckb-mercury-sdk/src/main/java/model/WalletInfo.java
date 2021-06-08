package model;

import lombok.Data;

import java.math.BigInteger;

@Data
public class WalletInfo {
    private String udt_hash;
    private BigInteger min_ckb;
    private BigInteger min_udt;

    public WalletInfo(String udt_hash, BigInteger min_ckb, BigInteger min_udt) {
        this.udt_hash = udt_hash;
        this.min_ckb = min_ckb;
        this.min_udt = min_udt;
    }
}
