package org.nervos.ckb.methods.type.transaction;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.nervos.ckb.crypto.Blake2b;
import org.nervos.ckb.crypto.secp256k1.ECKeyPair;
import org.nervos.ckb.crypto.secp256k1.Sign;
import org.nervos.ckb.exceptions.InvalidNumberOfWitnessesException;
import org.nervos.ckb.methods.type.OutPoint;
import org.nervos.ckb.methods.type.Witness;
import org.nervos.ckb.methods.type.cell.CellInput;
import org.nervos.ckb.methods.type.cell.CellOutput;
import org.nervos.ckb.utils.Numeric;

/** Created by duanyytop on 2018-12-21. Copyright © 2018 Nervos Foundation. All rights reserved. */
public class Transaction {

  public String version;
  public String hash;
  public List<OutPoint> deps;
  public List<CellInput> inputs;
  public List<CellOutput> outputs;
  public List<Witness> witnesses;

  public Transaction() {}

  public Transaction(
      String version,
      List<OutPoint> deps,
      List<CellInput> cellInputs,
      List<CellOutput> cellOutputs,
      List<Witness> witnesses) {
    this.version = version;
    this.deps = deps;
    this.inputs = cellInputs;
    this.outputs = cellOutputs;
    this.witnesses = witnesses;
  }

  public Transaction(
      String version,
      String hash,
      List<OutPoint> deps,
      List<CellInput> cellInputs,
      List<CellOutput> cellOutputs,
      List<Witness> witnesses) {
    this.version = version;
    this.hash = hash;
    this.deps = deps;
    this.inputs = cellInputs;
    this.outputs = cellOutputs;
    this.witnesses = witnesses;
  }

  public Transaction sign(BigInteger privateKey, String txHash) {
    if (witnesses.size() < inputs.size()) {
      throw new InvalidNumberOfWitnessesException("Invalid number of witnesses");
    }
    ECKeyPair ecKeyPair = ECKeyPair.createWithPrivateKey(privateKey, false);
    String publicKey =
        Numeric.toHexStringWithPrefixZeroPadded(Sign.publicKeyFromPrivate(privateKey, true), 66);
    List<Witness> signedWitnesses = new ArrayList<>();
    for (Witness witness : witnesses) {
      List<String> oldData = witness.data;
      Blake2b blake2b = new Blake2b();
      blake2b.update(Numeric.hexStringToByteArray(txHash));
      for (String datum : witness.data) {
        blake2b.update(Numeric.hexStringToByteArray(datum));
      }
      String message = blake2b.doFinalString();

      String signature =
          Numeric.toHexString(
              Sign.signMessage(Numeric.hexStringToByteArray(message), ecKeyPair).getSignature());
      witness.data = new ArrayList<>();
      witness.data.add(signature);
      witness.data.addAll(oldData);
      signedWitnesses.add(witness);
    }
    return new Transaction(version, txHash, deps, inputs, outputs, signedWitnesses);
  }
}
