package org.nervos.ckb.methods.type.transaction;

import com.fasterxml.jackson.annotation.JsonInclude;
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

  @JsonInclude(JsonInclude.Include.NON_NULL)
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

  /**
   * Sign transaction with single private key
   *
   * @param privateKey the private key used to sign transaction
   * @return signed transaction
   */
  public Transaction sign(BigInteger privateKey) {
    if (witnesses.size() < inputs.size()) {
      throw new InvalidNumberOfWitnessesException("Invalid number of witnesses");
    }
    String txHash = computeHash();
    List<CellWithPrivateKey> cellWithPrivateKeys = new ArrayList<>();
    for (CellInput input : inputs) {
      cellWithPrivateKeys.add(new CellWithPrivateKey(input, privateKey.toString(16)));
    }
    List<Witness> signedWitnesses = singWitnesses(witnesses, cellWithPrivateKeys);
    return new Transaction(
        version, txHash, cellDeps, headerDeps, inputs, outputs, outputsData, signedWitnesses);
  }

  /**
   * Sign transaction with multiply private keys
   *
   * @param cellWithPrivateKeys the list of CellInput and corresponding private key
   * @return signed transaction
   */
  public Transaction sign(List<CellWithPrivateKey> cellWithPrivateKeys) {
    if (witnesses.size() < inputs.size()) {
      throw new InvalidNumberOfWitnessesException("Invalid number of witnesses");
    }
    String txHash = computeHash();
    List<Witness> signedWitnesses = singWitnesses(witnesses, cellWithPrivateKeys);
    return new Transaction(
        version, txHash, cellDeps, headerDeps, inputs, outputs, outputsData, signedWitnesses);
  }

  private List<Witness> singWitnesses(
      List<Witness> witnesses, List<CellWithPrivateKey> cellWithPrivateKeys) {
    List<Witness> signedWitnesses = new ArrayList<>();
    String txHash = computeHash();
    for (int i = 0; i < witnesses.size(); i++) {
      List<String> oldData = witnesses.get(i).data;
      Blake2b blake2b = new Blake2b();
      blake2b.update(Numeric.hexStringToByteArray(txHash));
      for (String datum : witnesses.get(i).data) {
        blake2b.update(Numeric.hexStringToByteArray(datum));
      }
      String message = blake2b.doFinalString();

      ECKeyPair ecKeyPair =
          ECKeyPair.createWithPrivateKey(cellWithPrivateKeys.get(i).privateKey, false);
      String signature =
          Numeric.toHexString(
              Sign.signMessage(Numeric.hexStringToByteArray(message), ecKeyPair).getSignature());
      witnesses.get(i).data = new ArrayList<>();
      witnesses.get(i).data.add(signature);
      witnesses.get(i).data.addAll(oldData);
      signedWitnesses.add(witnesses.get(i));
    }
    return signedWitnesses;
  }

  public static class CellWithPrivateKey {
    CellInput input;
    String privateKey;

    public CellWithPrivateKey(CellInput input, String privateKey) {
      this.input = input;
      this.privateKey = privateKey;
    }
  }
}
