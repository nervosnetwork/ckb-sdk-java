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
public class Secp256k1MultisigAllBuilder {

  private Transaction transaction;
  private byte[] multiSigSerialize;

  public Secp256k1MultisigAllBuilder(Transaction transaction, byte[] multiSigSerialize) {
    this.transaction = transaction;
    this.multiSigSerialize = multiSigSerialize;
  }

  public void sign(ScriptGroup scriptGroup, List<String> privateKeys) throws IOException {
    List groupWitnesses = new ArrayList();
    if (transaction.witnesses.size() < transaction.inputs.size()) {
      throw new IOException("Transaction witnesses count must not be smaller than inputs count");
    }
    if (scriptGroup.inputIndexes.size() < 1) {
      throw new RuntimeException("Need at least one witness!");
    }
    for (Integer index : scriptGroup.inputIndexes) {
      groupWitnesses.add(transaction.witnesses.get(index));
    }
    for (int i = transaction.inputs.size(); i < transaction.witnesses.size(); i++) {
      groupWitnesses.add(transaction.witnesses.get(i));
    }
    if (groupWitnesses.get(0).getClass() != Witness.class) {
      throw new RuntimeException("First witness must be of Witness type!");
    }

    byte[] txHash = transaction.computeHash();
    byte[] emptySignature = new byte[0];
//    StringBuilder emptySignature = new StringBuilder();
    for (int i = 0; i < privateKeys.size(); i++) {
      emptySignature = Numeric.concatBytes(emptySignature, Witness.SIGNATURE_PLACEHOLDER);
    }
    Witness emptiedWitness = (Witness) groupWitnesses.get(0);
    emptiedWitness.lock = Numeric.concatBytes(multiSigSerialize, emptySignature);
    Table witnessTable = Serializer.serializeWitnessArgs(emptiedWitness);
    Blake2b blake2b = new Blake2b();
    blake2b.update(txHash);
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

    StringBuilder concatenatedSignatures = new StringBuilder();
    for (String privateKey : privateKeys) {
      ECKeyPair ecKeyPair = ECKeyPair.createWithPrivateKey(privateKey, false);
      concatenatedSignatures.append(
          Numeric.toHexStringNoPrefix(
              Sign.signMessage(Numeric.hexStringToByteArray(message), ecKeyPair).getSignature()));
    }

    Witness signedWitness = (Witness) groupWitnesses.get(0);
    signedWitness.lock = Numeric.concatBytes(multiSigSerialize, Numeric.hexStringToByteArray(concatenatedSignatures.toString()));

    transaction.witnesses.set(
        scriptGroup.inputIndexes.get(0),
        Numeric.toHexString(Serializer.serializeWitnessArgs(signedWitness).toBytes()));
  }

  public Transaction buildTx() {
    return transaction;
  }
}
