package model.resp;

import lombok.Data;

import java.math.BigInteger;

@Data
public class GetBalanceResponse {
    private String owned;
    private String claimable;
    private String in_lock;
}
