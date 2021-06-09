package model;

import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

public class WalletInfo {

    @SerializedName("udt_hash")
    public String udtHash;

    @SerializedName("min_ckb")
    public BigInteger minCkb;

    @SerializedName("min_udt")
    public BigInteger minUdt;

    public WalletInfo(String udtHash, BigInteger minCkb, BigInteger minUdt) {
        this.udtHash = udtHash;
        this.minCkb = minCkb;
        this.minUdt = minUdt;
    }
}
