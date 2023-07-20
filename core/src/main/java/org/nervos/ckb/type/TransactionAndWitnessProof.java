package org.nervos.ckb.type;

import java.util.Arrays;
import java.util.Objects;

public class TransactionAndWitnessProof {
  public byte[] blockHash;
  public MerkleProof transactionsProof;
  public MerkleProof witnessesProof;

  public TransactionAndWitnessProof(byte[] blockHash, MerkleProof transactions_proof, MerkleProof witnessesProof) {
    this.transactionsProof = transactions_proof;
    this.blockHash = blockHash;
    this.witnessesProof = witnessesProof;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    TransactionAndWitnessProof that = (TransactionAndWitnessProof) o;
    return Arrays.equals(this.blockHash, that.blockHash) &&
        Objects.equals(this.transactionsProof, that.transactionsProof) &&
        Objects.equals(this.witnessesProof, that.witnessesProof);
  }

  @Override
  public int hashCode() {
    int result = Arrays.hashCode(this.blockHash);
    result = 31 * result + this.transactionsProof.hashCode();
    result = 31 * result + this.witnessesProof.hashCode();

    return result;
  }
}
