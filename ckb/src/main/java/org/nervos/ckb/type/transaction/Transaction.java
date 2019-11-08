package org.nervos.ckb.type.transaction;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.nervos.ckb.Encoder;
import org.nervos.ckb.crypto.Blake2b;
import org.nervos.ckb.crypto.secp256k1.ECKeyPair;
import org.nervos.ckb.crypto.secp256k1.Sign;
import org.nervos.ckb.type.Witness;
import org.nervos.ckb.type.cell.CellDep;
import org.nervos.ckb.type.cell.CellInput;
import org.nervos.ckb.type.cell.CellOutput;
import org.nervos.ckb.type.dynamic.Table;
import org.nervos.ckb.type.fixed.UInt64;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.Serializer;

/** Copyright © 2018 Nervos Foundation. All rights reserved. */
public class Transaction {

  public String version;

  public String hash;

  @SerializedName("cell_deps")
  public List<CellDep> cellDeps;

  @SerializedName("header_deps")
  public List<String> headerDeps;

  public List<CellInput> inputs;
  public List<CellOutput> outputs;

  @SerializedName("outputs_data")
  public List<String> outputsData;

  public List witnesses;

  public Transaction() {}

  public Transaction(
      String version,
      List<CellDep> cellDeps,
      List<String> headerDeps,
      List<CellInput> cellInputs,
      List<CellOutput> cellOutputs,
      List<String> outputsData) {
    this.version = version;
    this.cellDeps = cellDeps;
    this.headerDeps = headerDeps;
    this.inputs = cellInputs;
    this.outputs = cellOutputs;
    this.outputsData = outputsData;
  }

  public Transaction(
      String version,
      List<CellDep> cellDeps,
      List<String> headerDeps,
      List<CellInput> cellInputs,
      List<CellOutput> cellOutputs,
      List<String> outputsData,
      List witnesses) {
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
      List witnesses) {
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
    blake2b.update(Encoder.encode(Serializer.serializeRawTransaction(this)));
    return blake2b.doFinalString();
  }

  public Transaction sign(BigInteger privateKey) {
    if (witnesses.size() < 1) {
      throw new RuntimeException("Need at least one witness!");
    }
    if (witnesses.get(0).getClass() != Witness.class) {
      throw new RuntimeException("First witness must be of Witness type!");
    }
    String txHash = computeHash();
    Witness emptiedWitness = (Witness) witnesses.get(0);
    emptiedWitness.lock = Witness.SIGNATURE_PLACEHOLDER;
    Table witnessTable = Serializer.serializeWitnessArgs(emptiedWitness);
    Blake2b blake2b = new Blake2b();
    blake2b.update(Numeric.hexStringToByteArray(txHash));
    blake2b.update(new UInt64(witnessTable.getLength()).toBytes());
    blake2b.update(witnessTable.toBytes());
    for (int i = 1; i < witnesses.size(); i++) {
      byte[] bytes;
      if (witnesses.get(i).getClass() == Witness.class) {
        bytes = Serializer.serializeWitnessArgs((Witness) witnesses.get(i)).toBytes();
      } else {
        bytes = Numeric.hexStringToByteArray((String) witnesses.get(i));
      }
      blake2b.update(new UInt64(bytes.length).toBytes());
      blake2b.update(bytes);
    }
    String message = blake2b.doFinalString();
    ECKeyPair ecKeyPair = ECKeyPair.createWithPrivateKey(privateKey, false);
    ((Witness) witnesses.get(0)).lock =
        Numeric.toHexString(
            Sign.signMessage(Numeric.hexStringToByteArray(message), ecKeyPair).getSignature());

    List<String> signedWitness = new ArrayList<>();
    for (Object witness : witnesses) {
      if (witness.getClass() == Witness.class) {
        signedWitness.add(
            Numeric.toHexString(Serializer.serializeWitnessArgs((Witness) witness).toBytes()));
      } else {
        signedWitness.add((String) witness);
      }
    }

    return new Transaction(
        version, cellDeps, headerDeps, inputs, outputs, outputsData, signedWitness);
  }
}
