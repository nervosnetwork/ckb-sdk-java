package org.nervos.ckb.type;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class MerkleProof {
  /**
   * Leaves indices in the CBMT that are proved present in the block.
   * These are indices in the CBMT tree not the transaction indices in the block.
   */
  public List<Integer> indices;
  /**
   * Hashes of all siblings along the paths to root.
   */
  public List<byte[]> lemmas;

  public MerkleProof(List<Integer> indices, List<byte[]> lemmas) {
    this.indices = indices;
    this.lemmas = lemmas;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    MerkleProof that = (MerkleProof) o;
    if (!Objects.equals(this.indices, that.indices)) {return false;}

    if (this.lemmas == that.lemmas) return true;
    if (this.lemmas == null || this.lemmas.size() != that.lemmas.size()) {return false;}

    for (Iterator<byte[]> this_it = this.lemmas.iterator(), that_it = that.lemmas.iterator(); this_it.hasNext(); ) {
      if (!Arrays.equals(this_it.next(), that_it.next())) {
        return false;
      }
    }
    return true;
  }

  @Override
  public int hashCode() {
    int lemmas_hashcode = 0;
    if (lemmas != null) {
      for (byte[] bytes : lemmas) {
        lemmas_hashcode = lemmas_hashcode * 31 + Arrays.hashCode(bytes);
      }
    }
    int indices_hashcode = indices == null ? 0 : indices.hashCode();

    return 31 * indices_hashcode + lemmas_hashcode;
  }
}
