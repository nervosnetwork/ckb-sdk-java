package org.nervos.ckb.type;

import java.util.List;

public class TransactionProof {
  public Proof proof;
  public byte[] blockHash;
  public byte[] witnessesRoot;

  public TransactionProof(Proof proof, byte[] blockHash, byte[] witnessesRoot) {
    this.proof = proof;
    this.blockHash = blockHash;
    this.witnessesRoot = witnessesRoot;
  }

  public static class Proof {
    public List<Integer> indices;
    public List<byte[]> lemmas;

    public Proof(List<Integer> indices, List<byte[]> lemmas) {
      this.indices = indices;
      this.lemmas = lemmas;
    }
  }
}
