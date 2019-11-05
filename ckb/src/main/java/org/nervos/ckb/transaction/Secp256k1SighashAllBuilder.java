package org.nervos.ckb.transaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.nervos.ckb.crypto.Blake2b;
import org.nervos.ckb.crypto.secp256k1.ECKeyPair;
import org.nervos.ckb.crypto.secp256k1.Sign;
import org.nervos.ckb.type.Witness;
import org.nervos.ckb.type.dynamic.Table;
import org.nervos.ckb.type.fixed.UInt64;
import org.nervos.ckb.type.transaction.Transaction;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.Serializer;

/** Copyright © 2019 Nervos Foundation. All rights reserved. */
public class Secp256k1SighashAllBuilder implements DefaultSigHashAllBuilder {

  private Transaction transaction;
  private List<String> signedWitnesses = new ArrayList<>();

  public Secp256k1SighashAllBuilder(Transaction transaction) {
    this.transaction = transaction;
  }

  public void sign(ScriptGroup scriptGroup, String privateKey) throws IOException {
    List groupWitnesses = new ArrayList();
    if (transaction.witnesses.size() < transaction.inputs.size()) {
      throw new IOException("Transaction witnesses count must be bigger than inputs count");
    }
    if (scriptGroup.inputIndexes.size() < 1) {
      throw new RuntimeException("Need at least one witness!");
    }
    for (Integer i : scriptGroup.inputIndexes) {
      groupWitnesses.add(transaction.witnesses.get(i));
    }
    for (int i = transaction.inputs.size(); i < transaction.witnesses.size(); i++) {
      groupWitnesses.add(transaction.witnesses.get(i));
    }
    if (groupWitnesses.get(0).getClass() != Witness.class) {
      throw new RuntimeException("First witness must be of Witness type!");
    }
    String txHash = transaction.computeHash();
    Witness emptiedWitness = (Witness) groupWitnesses.get(0);
    emptiedWitness.lock = Witness.EMPTY_LOCK;
    Table witnessTable = Serializer.serializeWitnessArgs(emptiedWitness);
    Blake2b blake2b = new Blake2b();
    blake2b.update(Numeric.hexStringToByteArray(txHash));
    blake2b.update(new UInt64(witnessTable.getLength()).toBytes());
    blake2b.update(witnessTable.toBytes());
    for (int i = 1; i < groupWitnesses.size(); i++) {
      byte[] bytes;
      if (groupWitnesses.get(i).getClass() == Witness.class) {
        bytes = Serializer.serializeWitnessArgs((Witness) groupWitnesses.get(i)).toBytes();
      } else {
        bytes = Numeric.hexStringToByteArray((String) groupWitnesses.get(i));
      }
      blake2b.update(new UInt64(bytes.length).toBytes());
      blake2b.update(bytes);
    }
    String message = blake2b.doFinalString();
    ECKeyPair ecKeyPair = ECKeyPair.createWithPrivateKey(privateKey, false);
    ((Witness) groupWitnesses.get(0)).lock =
        Numeric.toHexString(
            Sign.signMessage(Numeric.hexStringToByteArray(message), ecKeyPair).getSignature());

    List<String> signedGroupWitness = new ArrayList<>();
    for (Object witness : groupWitnesses) {
      if (witness.getClass() == Witness.class) {
        signedGroupWitness.add(
            Numeric.toHexString(Serializer.serializeWitnessArgs((Witness) witness).toBytes()));
      } else {
        signedGroupWitness.add((String) witness);
      }
    }
    signedWitnesses.addAll(signedGroupWitness);
  }

  public Transaction buildTx() {
    transaction.witnesses = signedWitnesses;
    return transaction;
  }
}
