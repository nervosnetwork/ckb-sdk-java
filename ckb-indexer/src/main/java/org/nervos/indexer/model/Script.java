package org.nervos.indexer.model;

import com.google.gson.annotations.SerializedName;

public class Script {
  @SerializedName("code_hash")
  public String codeHash;

  @SerializedName("hash_type")
  public String hashType;

  public String args;

  public Script(String codeHash, String hashType, String args) {
    this.codeHash = codeHash;
    this.args = args;
    this.hashType = hashType;
  }
}
