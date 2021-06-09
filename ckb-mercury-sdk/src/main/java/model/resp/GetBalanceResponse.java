package model.resp;

import com.google.gson.annotations.SerializedName;


public class GetBalanceResponse {
    public String owned;
    public String claimable;

    @SerializedName("in_lock")
    private String inLock;
}
