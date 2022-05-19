package org.nervos.ckb.sign;

public interface WitnessPlaceholder {
  default byte[] getWitnessPlaceHolder(byte[] originalWitness, Object context) {
    if (originalWitness == null) {
      return new byte[0];
    } else {
      return originalWitness;
    }
  }
}
