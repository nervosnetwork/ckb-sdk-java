package org.nervos.ckb.type;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TransactionProof {

  public Proof proof;

  @SerializedName("block_hash")
  public String blockHash;

  @SerializedName("witnesses_root")
  public String witnessesRoot;

  public TransactionProof(Proof proof, String blockHash, String witnessesRoot) {
    this.proof = proof;
    this.blockHash = blockHash;
    this.witnessesRoot = witnessesRoot;
  }

  public static class Proof {
    public List<String> indices;
    public List<String> lemmas;

    public Proof(List<String> indices, List<String> lemmas) {
      this.indices = indices;
      this.lemmas = lemmas;
    }
  }
}
