package model.resp;

import com.google.gson.annotations.SerializedName;

public class SignatureEntry {

    public String type;

    public Integer index;

    @SerializedName("pub_key")
    public String pubKey;

    public Byte[] message;
}
