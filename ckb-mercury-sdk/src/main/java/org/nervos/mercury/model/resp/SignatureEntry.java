package org.nervos.mercury.model.resp;

import com.google.gson.annotations.SerializedName;

public class SignatureEntry {

  public String type;

  public Integer index;

  @SerializedName("group_len")
  public Integer groupLen;

  @SerializedName("pub_key")
  public String pubKey;
}
