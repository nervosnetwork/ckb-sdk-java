package org.nervos.ckb.methods.type.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.nervos.ckb.Encoder;
import org.nervos.ckb.crypto.Blake2b;
import org.nervos.ckb.crypto.secp256k1.ECKeyPair;
import org.nervos.ckb.crypto.secp256k1.Sign;
import org.nervos.ckb.exceptions.InvalidNumberOfWitnessesException;
import org.nervos.ckb.methods.type.Witness;
import org.nervos.ckb.methods.type.cell.CellDep;
import org.nervos.ckb.methods.type.cell.CellInput;
import org.nervos.ckb.methods.type.cell.CellOutput;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.Serializer;

/** Copyright © 2018 Nervos Foundation. All rights reserved. */
public class Transaction {

  public String version;
  public String hash;

  @JsonProperty("cell_deps")
  public List<CellDep> cellDeps;

  @JsonProperty("header_deps")
  public List<String> headerDeps;

  public List<CellInput> inputs;
  public List<CellOutput> outputs;

  @JsonProperty("outputs_data")
  public List<String> outputsData;

  public List<Witness> witnesses;

  public Transaction() {}

  public Transaction(
      String version,
      List<CellDep> cellDeps,
      List<String> headerDeps,
      List<CellInput> cellInputs,
      List<CellOutput> cellOutputs,
      List<String> outputsData,
      List<Witness> witnesses) {
    this.version = version;
    this.cellDeps = cellDeps;
    this.headerDeps = headerDeps;
    this.inputs = cellInputs;
    this.outputs = cellOutputs;
    this.outputsData = outputsData;
    this.witnesses = witnesses;
  }

  public Transaction(
      String version,
      String hash,
      List<CellDep> cellDeps,
      List<String> headerDeps,
      List<CellInput> cellInputs,
      List<CellOutput> cellOutputs,
      List<String> outputsData,
      List<Witness> witnesses) {
    this.version = version;
    this.hash = hash;
    this.cellDeps = cellDeps;
    this.headerDeps = headerDeps;
    this.inputs = cellInputs;
    this.outputs = cellOutputs;
    this.outputsData = outputsData;
    this.witnesses = witnesses;
  }

  public String computeHash() {
    Blake2b blake2b = new Blake2b();
    blake2b.update(Encoder.encode(Serializer.serializeTransaction(this)));
    return blake2b.doFinalString();
  }

  public Transaction sign(BigInteger privateKey) {
    if (witnesses.size() < inputs.size()) {
      throw new InvalidNumberOfWitnessesException("Invalid number of witnesses");
    }
    String txHash = computeHash();
    ECKeyPair ecKeyPair = ECKeyPair.createWithPrivateKey(privateKey, false);
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
    return new Transaction(
        version, txHash, cellDeps, headerDeps, inputs, outputs, outputsData, signedWitnesses);
  }
}
